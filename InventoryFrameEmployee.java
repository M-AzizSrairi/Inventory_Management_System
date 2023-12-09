package com.inventory.JavaProject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.border.EmptyBorder;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;


public class InventoryFrameEmployee extends javax.swing.JFrame {
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JLabel totalCategoriesLabel;
    private JLabel totalProductsLabel;
    private JLabel lowStockLabel;
    private Map<String, Integer> categoryCounts;
    
    private JPanel sidebarPanel;
    
    public InventoryFrameEmployee() {
        initializeUI();
        displayCSVData();
        updateKPIs();
    }
    private void initializeUI() {
        setLayout(new BorderLayout());
        // Set the default font for the UIManager
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            Font font = new Font("Poppins", Font.PLAIN, 14);
            UIManager.put("Button.font", font);
            UIManager.put("Label.font", font);
            UIManager.put("TextField.font", font);
            UIManager.put("Table.font", font);
            UIManager.put("TableHeader.font", font);
            // Add more components as needed

        } catch (Exception e) {
            e.printStackTrace();
        }
        setTitle("Inventory Management System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);


        // Create the product table
        String[] columnNames = {"Product ID", "Product Name", "Category", "Buying Price", "Selling Price", "Quantity"};
        tableModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(tableModel);
        productTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
        JScrollPane scrollPane = new JScrollPane(productTable);

        // Create a panel for the sidebar
        JPanel sidebarPanel = createSidebarPanel();
        add(sidebarPanel, BorderLayout.WEST);
        JPanel contentPanel = new JPanel();
        JPanel kpiPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        // Add the table to the center of the frame
        add(scrollPane, BorderLayout.CENTER);
        // Add a button to add a new product
        JButton addProductButton = new JButton("Add Product");
        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddProductDialog();
            }
        });

        // Add a button to export to CSV
        JButton exportCsvButton = new JButton("Refresh Data");
        exportCsvButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshData();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addProductButton);
        buttonPanel.add(exportCsvButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add labels for KPIs
        totalCategoriesLabel = new JLabel("Total Categories: 0");
        totalProductsLabel = new JLabel("Total Products: 0");
        lowStockLabel = new JLabel("Low Stock: 0");
        
        kpiPanel.add(totalCategoriesLabel);
        kpiPanel.add(totalProductsLabel);
        kpiPanel.add(lowStockLabel);
        add(kpiPanel, BorderLayout.NORTH);
        // Initialize category counts
        categoryCounts = new HashMap<>();
        // Display the frame
        setVisible(true);
    }
    
    
    private class CustomTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Set background color based on row (alternating colors for better visibility)
            if (row % 2 == 0) {
                c.setBackground(Color.decode("#E3E4E8")); // Light grey for even rows
            } else {
                c.setBackground(Color.WHITE);
            }

            // Set foreground color for text
            if (row == -1) {
                c.setForeground(Color.decode("#667085")); // Header row text color
            } else {
                c.setForeground(Color.decode("#48505E")); // Other rows text color
            }

            // Set grid color between rows
            table.setGridColor(Color.decode("#D0D3D9")); // Grey grid color

            return c;
        }
    }

    private void showAddProductDialog() {
    ProductAdditionInterface productAdditionInterface = new ProductAdditionInterface();

    JButton addButton = new JButton("Add");
    addButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Handle adding the product to the table
            String productId = productAdditionInterface.getProductId();
            String productName = productAdditionInterface.getProductName();
            String category = productAdditionInterface.getCategory();
            String buyingPrice = productAdditionInterface.getBuyingPrice();
            String sellingPrice = productAdditionInterface.getSellingPrice();
            String quantity = productAdditionInterface.getQuantity();

            
            // Update KPIs
            updateKPIs();
            productAdditionInterface.dispose();
            displayCSVData();
        }
    });
    
    productAdditionInterface.setVisible(true);
    }
    
    private void displayCSVData() {
        String csvFile = "C:\\Users\\USER\\Documents\\NetBeansProjects\\Inventory\\src\\main\\java\\com\\inventory\\JavaProject\\inventory.csv";
        String line;
        String cvsSplitBy = ",";
        boolean firstLine = true;

       try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            DefaultTableModel model = (DefaultTableModel) productTable.getModel();

            // Clear existing data from the table
            model.setRowCount(0);

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    // Skip the first line (headers)
                    firstLine = false;
                    continue;
                }

                // use comma as separator
                String[] data = line.split(cvsSplitBy);
                model.addRow(data);
            }

            // Set row height for better readability
            productTable.setRowHeight(30);

            // Set padding between cell content and cell border
            productTable.setIntercellSpacing(new Dimension(10, 10));

            // Apply custom cell renderer
            productTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    
    private void refreshData() {
        displayCSVData();
        updateKPIs();
        JOptionPane.showMessageDialog(this, "Table refreshed successfully!", "Refresh Successful", JOptionPane.INFORMATION_MESSAGE);
    }

    
    private void updateKPIs() {
        String csvFile = "C:\\Users\\USER\\Documents\\NetBeansProjects\\Inventory\\src\\main\\java\\com\\inventory\\JavaProject\\inventory.csv";
        String line;
        String cvsSplitBy = ",";
        Set<String> categorySet = new HashSet<>();
        int totalProducts = 0;
        int lowStockCount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Skip the headers
            br.readLine();

            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] data = line.split(cvsSplitBy);

                // Update total categories 
                categorySet.add(data[2]);

                // Update total products
                totalProducts++;

                int quantity = Integer.parseInt(data[5]);
                if (quantity < 10) {
                    lowStockCount++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Update total categories label
        int totalCategories = categorySet.size();
        totalCategoriesLabel.setText("Total Categories: " + totalCategories);

        // Update total products label
        totalProductsLabel.setText("Total Products: " + totalProducts);

        // Update low stock label
        lowStockLabel.setText("Low Stock: " + lowStockCount);
    }
    
    // -------------------- SideBarPanel Here -------------------- //
    private JPanel createSidebarPanel() {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setBackground(new Color(240, 241, 243)); // #F0F1F3
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBorder(new EmptyBorder(20, 10, 20, 10)); // Add padding

        JLabel marketLabel = new JLabel("UNO Market");
        marketLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        marketLabel.setFont(new Font("Poppins", Font.BOLD, 16)); // Use Poppins font
        sidebarPanel.add(marketLabel);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Add vertical spacing

        String[] pageNames = {"Inventory", "Sales Invoice", "Orders"};
        for (String pageName : pageNames) {
            JButton pageButton = new JButton(pageName);
            pageButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            pageButton.setFont(new Font("Poppins", Font.PLAIN, 14)); // Use Poppins font
            pageButton.setBackground(new Color(240, 241, 243)); // #F0F1F3
            pageButton.setBorderPainted(false); // No border
            pageButton.setFocusPainted(false); // No focus border
            pageButton.setContentAreaFilled(false); // No background
            pageButton.setForeground(new Color(93, 102, 121)); // Font color: #5D6679

            pageButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    handlePageNavigation(pageName);
                }
            });

            // Highlight the current page button
            if (getCurrentPage().equalsIgnoreCase(pageName)) {
                pageButton.setForeground(new Color(21, 112, 239)); // Font color: #1570EF
            }

            sidebarPanel.add(pageButton);
            sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add vertical spacing
        }

        return sidebarPanel;
    }
    
    private String getCurrentPage() {
        // Get the simple name of the class of the active frame
        String className = getClass().getSimpleName();
        // Map the class name to the corresponding page name
        switch (className) {
            case "InventoryFrameEmployee":
                return "Inventory";
            case "SaleGUI":
                return "Sales Invoice";
            case "OrdersFrame":
                return "Orders";
            default:
                return "UNKOWN"; // Handle unknown class names or return a default value
        }
    }


    private void handlePageNavigation(String pageName) {
        // Dispose of the current frame
        dispose();

        // Implement logic to switch between pages based on the button clicked
        switch (pageName) {
            case "Inventory":
                new InventoryFrameEmployee().setVisible(true);
                break;
            case "Sales Invoice" :
                new SaleGUI("SalesReport").setVisible(true);
                break;
            case "Orders":
                // Open the OrdersFrame
                //new OrdersFrame().setVisible(true);
                break;
            default:
                // Handle unknown page names
                System.out.println("Unknown page: " + pageName);
                break;
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>                        

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> new InventoryFrameEmployee());
    }

    // Variables declaration - do not modify                     
    // End of variables declaration                   
}
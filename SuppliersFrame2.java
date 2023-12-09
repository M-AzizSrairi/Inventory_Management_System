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


public class SuppliersFrame2 extends javax.swing.JFrame {
    private JTable suppliersTable;
    // Table model for the suppliers table
    private DefaultTableModel tableModel;

    // Components for updating KPIs
    private JLabel totalSuppliersLabel;
    
    private JPanel sidebarPanel;
    
    public SuppliersFrame2() {
        initializeUI();
        displayCSVData();
        updateKPIs();
        setLocationRelativeTo(null);
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


        String[] columnNames = {"Supplier Name", "Product", "Category", "Contact Number", "Email"};
        tableModel = new DefaultTableModel(columnNames, 0);
        suppliersTable = new JTable(tableModel);
        // Set custom cell renderer to add space between cells
        suppliersTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
        JScrollPane scrollPane = new JScrollPane(suppliersTable);

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
        JButton addProductButton = new JButton("Add Supplier");
        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddSupplierDialog();
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

        totalSuppliersLabel = new JLabel("Total Suppliers: 0");
        kpiPanel.add(totalSuppliersLabel);
        add(kpiPanel, BorderLayout.NORTH);

        setLocationRelativeTo(null);
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

    private void showAddSupplierDialog() {
    SupplierAdditionInterface supplierAdditionInterface = new SupplierAdditionInterface();

    JButton addButton = new JButton("Add");
    addButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Handle adding the product to the table
            String supplierName = supplierAdditionInterface.getSupplierName();
            String productName = supplierAdditionInterface.getProductName();
            String category = supplierAdditionInterface.getCategory();
            String number = supplierAdditionInterface.getcontact();
            String email = supplierAdditionInterface.getemail();
            
            // Update KPIs
            updateKPIs();
            supplierAdditionInterface.dispose();
            displayCSVData();
        }
    });
    
    supplierAdditionInterface.setVisible(true);
    }
    
    private void displayCSVData() {
        String csvFile = "C:\\Users\\USER\\Documents\\NetBeansProjects\\Inventory\\src\\main\\java\\com\\inventory\\JavaProject\\suppliers.csv";
        String line;
        String cvsSplitBy = ",";
        boolean firstLine = true;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            DefaultTableModel model = (DefaultTableModel) suppliersTable.getModel();

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
            suppliersTable.setRowHeight(30);

            // Set padding between cell content and cell border
            suppliersTable.setIntercellSpacing(new Dimension(10, 10));

            // Apply custom cell renderer
            suppliersTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
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
        // Update total suppliers label
        int totalSuppliers = tableModel.getRowCount();
        totalSuppliersLabel.setText("Total Suppliers: " + totalSuppliers);
    }
    
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

        String[] pageNames = {"Dashboard", "Inventory", "Reports", "Suppliers", "Orders"};
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
            case "DashboardFrame":
                return "Dashboard";
            case "InventoryFrame":
                return "Inventory";
            case "ReportsFrame":
                return "Reports";
            case "SuppliersFrame2":
                return "Suppliers";
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
        case "Dashboard":
            // Open the DashboardFrame
            new DashboardFrame().setVisible(true);
            break;
        case "Inventory":
            // Open the InventoryFrame
            new InventoryFrame().setVisible(true);
            break;
        case "Reports":
            // Open the ReportsFrame
            //new ReportsFrame().setVisible(true);
            break;
        case "Suppliers":
            // Open the SuppliersFrame
            new SuppliersFrame2().setVisible(true);
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
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
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SuppliersFrame2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SuppliersFrame2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SuppliersFrame2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SuppliersFrame2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SuppliersFrame2().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

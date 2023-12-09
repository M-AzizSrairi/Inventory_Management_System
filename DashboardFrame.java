package com.inventory.JavaProject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author USER
 */
public class DashboardFrame extends javax.swing.JFrame {

    /**
     * Creates new form DashboardFrame
     */
    public DashboardFrame() {
        initComponents();
        initiliazeUI();
        setLocationRelativeTo(null);
    }
    private void initiliazeUI() {
        
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
        // Create a panel for the sidebar
        JPanel sidebarPanel = createSidebarPanel();
        add(sidebarPanel, BorderLayout.WEST);
        
        // Create a panel for the dashboard
        JPanel dashboardPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        dashboardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Sales Overview Summary Panel
        JPanel salesSummaryPanel = createSalesSummaryPanel();
        dashboardPanel.add(salesSummaryPanel);
        
        // Daiy Sales Overview Summary Panel
        JPanel DailysalesSummaryPanel = createDailySalesSummaryPanel();
        dashboardPanel.add(DailysalesSummaryPanel);

        // Inventory Summary Panel
        JPanel inventorySummaryPanel = createInventorySummaryPanel();
        dashboardPanel.add(inventorySummaryPanel);

        // Suppliers Summary Panel
        JPanel suppliersSummaryPanel = createSuppliersSummaryPanel();
        dashboardPanel.add(suppliersSummaryPanel);

        // Top Selling Products Panel
        JPanel topSellingProductsPanel = createTopSellingProductsPanel();
        dashboardPanel.add(topSellingProductsPanel);

        // Low Quantity Stock Panel
        JPanel lowQuantityStockPanel = createLowQuantityStockPanel();
        dashboardPanel.add(lowQuantityStockPanel);

        // Add the dashboard panel to the frame
        add(dashboardPanel);

        // Set the frame visible
        setVisible(true);
    }
    
    private JPanel createSalesSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 241, 243)); // #F0F1F3
        panel.setBorder(createRoundedBorder());

        // Read data from the report.csv file
        // report.csv format is: date, totalProfit, totalCost, netRevenue
        // Example data: 3/8/2023, 110, 55, 55
        Map<String, String[]> salesData = readCSV("/C:/Users/USER/Documents/NetBeansProjects/Inventory/src/main/java/com/inventory/JavaProject/report.csv");

        // prodreport.csv format is: date, name, category, count
        // Example data: 12/6/2023, Milk_Delice, Dairy, 14
        Map<String, Integer> itemsSoldByDay = readItemsSoldByDay("/C:/Users/USER/Documents/NetBeansProjects/Inventory/src/main/java/com/inventory/JavaProject/prodReport.csv");

        // Calculate KPIs
        int numberOfItemsSold = 0; //prodReport
        double totalProfit = 0;
        double totalCost = 0;
        double netRevenue = 0;

        for (String date : salesData.keySet()) {
            String[] values = salesData.get(date);
            numberOfItemsSold += itemsSoldByDay.getOrDefault(date, 0);
            totalProfit += Double.parseDouble(values[1]);
            totalCost += Double.parseDouble(values[2]);
            netRevenue += Double.parseDouble(values[3]);
        }

        // Create labels and values for the summary
        JLabel titleLabel = new JLabel("Sales Overview");
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel summaryContentPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        summaryContentPanel.setBackground(new Color(240, 241, 243));

        addSummaryItem(summaryContentPanel, "Number of Items Sold:", String.valueOf(numberOfItemsSold));
        addSummaryItem(summaryContentPanel, "Total Profit:", "$" + String.format("%.2f", totalProfit));
        addSummaryItem(summaryContentPanel, "Total Cost:", "$" + String.format("%.2f", totalCost));
        addSummaryItem(summaryContentPanel, "Net Revenue:", "$" + String.format("%.2f", netRevenue));

        panel.add(summaryContentPanel, BorderLayout.CENTER);

        return panel;
    }
    
    private JPanel createDailySalesSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 241, 243)); // #F0F1F3
        panel.setBorder(createRoundedBorder());

        // Read data from the report.csv file
        Map<String, String[]> salesData = readCSV("/C:/Users/USER/Documents/NetBeansProjects/Inventory/src/main/java/com/inventory/JavaProject/report.csv");

        Map<String, Integer> itemsSoldByDay = readItemsSoldByDay("/C:/Users/USER/Documents/NetBeansProjects/Inventory/src/main/java/com/inventory/JavaProject/prodReport.csv");

        // Get today's date 
        String todayDate = getTodayDate().trim(); 

        // Calculate KPIs for today
        int numberOfItemsSoldToday = itemsSoldByDay.getOrDefault(todayDate, 0);
        double totalProfitToday = 0;
        double totalCostToday = 0;
        double netRevenueToday = 0;

        if (salesData.containsKey(todayDate)) {
            String[] values = salesData.get(todayDate);
            totalProfitToday = Double.parseDouble(values[1]);
            totalCostToday = Double.parseDouble(values[2]);
            netRevenueToday = Double.parseDouble(values[3]);
        }

        // Create labels and values for the summary
        JLabel titleLabel = new JLabel("Today's Sales Overview");
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel summaryContentPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        summaryContentPanel.setBackground(new Color(240, 241, 243));

        addSummaryItem(summaryContentPanel, "Number of Items Sold Today:", String.valueOf(numberOfItemsSoldToday));
        addSummaryItem(summaryContentPanel, "Total Profit Today:", "$" + String.format("%.2f", totalProfitToday));
        addSummaryItem(summaryContentPanel, "Total Cost Today:", "$" + String.format("%.2f", totalCostToday));
        addSummaryItem(summaryContentPanel, "Net Revenue Today:", "$" + String.format("%.2f", netRevenueToday));

        panel.add(summaryContentPanel, BorderLayout.CENTER);

        return panel;
    }
    
    private String getTodayDate() {
        // Get today's date
        LocalDate today = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return today.format(formatter);
    }
    
    private JPanel createInventorySummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 241, 243)); // #F0F1F3
        panel.setBorder(createRoundedBorder());

        // Specify the path to your inventory.csv file
        String csvFilePath = "/C:/Users/USER/Documents/NetBeansProjects/Inventory/src/main/java/com/inventory/JavaProject/inventory.csv";

        // Create labels for total categories and total products
        JLabel totalCategoriesLabel = new JLabel("Total Categories: ");
        JLabel totalProductsLabel = new JLabel("Total Products: ");

        // Calculate KPIs
        Set<String> categorySet = new HashSet<>();
        int totalProducts = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            // Skip the headers
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] data = line.split(",");

                // Update total categories
                categorySet.add(data[2]);

                // Update total products
                totalProducts++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Update total categories label
        int totalCategories = categorySet.size();
        totalCategoriesLabel.setText("Total Categories: " + totalCategories);

        // Update total products label
        totalProductsLabel.setText("Total Products: " + totalProducts);

        // Create labels and values for the summary
        JLabel titleLabel = new JLabel("Inventory Summary");
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel summaryContentPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        summaryContentPanel.setBackground(new Color(240, 241, 243));

        // Add total categories label to the summary content panel
        summaryContentPanel.add(totalCategoriesLabel);

        // Add total products label to the summary content panel
        summaryContentPanel.add(totalProductsLabel);

        panel.add(summaryContentPanel, BorderLayout.CENTER);

        return panel;
    }
    private JPanel createSuppliersSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 241, 243)); // #F0F1F3
        panel.setBorder(createRoundedBorder());
        Map<String, Integer> suppliersData = readSuppliers("/C:/Users/USER/Documents/NetBeansProjects/Inventory/src/main/java/com/inventory/JavaProject/suppliers.csv");

        // Calculate KPIs
        int totalSuppliers = suppliersData.size();
        String topSupplier = findTopSupplier(suppliersData);

        // Create labels and values for the summary
        JLabel titleLabel = new JLabel("Suppliers Summary");
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel summaryContentPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        summaryContentPanel.setBackground(new Color(240, 241, 243));

        addSummaryItem(summaryContentPanel, "Total Suppliers:", String.valueOf(totalSuppliers));
        addSummaryItem(summaryContentPanel, "Top Supplier:", topSupplier);

        panel.add(summaryContentPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTopSellingProductsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 241, 243)); // #F0F1F3
        panel.setBorder(createRoundedBorder());
        
        Map<String, String[]> salesData = readCSV("/C:/Users/USER/Documents/NetBeansProjects/Inventory/src/main/java/com/inventory/JavaProject/prodReport.csv");

        // Calculate the total quantity sold for each product
        Map<String, Integer> totalQuantitySold = new HashMap<>();
        for (String date : salesData.keySet()) {
            Map<String, Integer> itemsSoldByProduct = readItemsSoldByProduct(date);
            for (String productName : itemsSoldByProduct.keySet()) {
                int quantitySold = itemsSoldByProduct.get(productName);
                totalQuantitySold.put(productName, totalQuantitySold.getOrDefault(productName, 0) + quantitySold);
            }
        }
        
        // Sort the totalQuantitySold map by value in descending order
        totalQuantitySold = totalQuantitySold.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        // Create labels and values for the summary
        JLabel titleLabel = new JLabel("Top Selling Products");
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel summaryContentPanel = new JPanel(new GridLayout(1, 1, 10, 10));
        summaryContentPanel.setBackground(new Color(240, 241, 243));

        // Show only the top 5 products
        Map<String, Integer> top5Products = totalQuantitySold.entrySet().stream()
                .limit(5)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        JTable productTable = createSellingTable(top5Products);
        // Apply UI customization for the productTable
        productTable.setRowHeight(30);
        productTable.setIntercellSpacing(new Dimension(10, 10));
        productTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());

        JScrollPane productScrollPane = new JScrollPane(productTable);
        summaryContentPanel.add(productScrollPane);

        panel.add(summaryContentPanel, BorderLayout.CENTER);

        return panel;
    }
    
    private JTable createSellingTable(Map<String, Integer> data) {
        String[] columnNames = {"Product", "Quantity Sold"};
        Object[][] rowData = data.entrySet().stream()
                .map(entry -> new Object[]{entry.getKey(), entry.getValue()})
                .toArray(Object[][]::new);

        return new JTable(rowData, columnNames);
    }
    private JTable createTable(Map<String, Integer> data) {
        String[] columnNames = {"Product", "Quantity Remaining"};
        Object[][] rowData = data.entrySet().stream()
                .map(entry -> new Object[]{entry.getKey(), entry.getValue()})
                .toArray(Object[][]::new);

        return new JTable(rowData, columnNames);
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

    private JPanel createLowQuantityStockPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 241, 243)); // #F0F1F3
        panel.setBorder(createRoundedBorder());

        // Read data from the inventory.csv file
        // Assuming the inventory.csv format is: name, category, quantity
        // You may need to adjust this based on the actual format of your file
        Map<String, Integer> inventoryData = readInventory("/C:/Users/USER/Documents/NetBeansProjects/Inventory/src/main/java/com/inventory/JavaProject/inventory.csv");

        // Create labels and values for the summary
        JLabel titleLabel = new JLabel("Low Quantity Stock");
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel summaryContentPanel = new JPanel(new GridLayout(1, 1, 10, 10));
        summaryContentPanel.setBackground(new Color(240, 241, 243));

        // Find low quantity stock
        Map<String, Integer> lowQuantityStock = findLowQuantityStock(inventoryData);

        // Sort the lowQuantityStock map by value in descending order
        lowQuantityStock = lowQuantityStock.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        // Create table for low quantity stock
        JTable lowQuantityTable = createTable(lowQuantityStock);

        // Apply UI customization for the lowQuantityTable
        lowQuantityTable.setRowHeight(30);
        lowQuantityTable.setIntercellSpacing(new Dimension(10, 10));
        lowQuantityTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());

        JScrollPane lowQuantityScrollPane = new JScrollPane(lowQuantityTable);
        summaryContentPanel.add(lowQuantityScrollPane);

        panel.add(summaryContentPanel, BorderLayout.CENTER);

        return panel;
    }

    private Map<String, String[]> readCSV(String filePath) {
        Map<String, String[]> data = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Skip the header line
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                data.put(values[0], values);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error reading CSV file: " + e.getMessage());
        }

        return data;
    }

    private Map<String, Integer> readItemsSoldByDay(String filePath) {
        Map<String, Integer> data = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Skip the header line
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                String date = values[0];
                int count = Integer.parseInt(values[3]);
                data.put(date, data.getOrDefault(date, 0) + count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    public static String findTopSupplier(Map<String, Integer> suppliersData) {
        // Find the supplier with the most items purchased
        String topSupplier = null;
        int maxItemsPurchased = 0;
        for (String supplier : suppliersData.keySet()) {
            int itemsPurchased = suppliersData.get(supplier);
            if (itemsPurchased > maxItemsPurchased) {
                maxItemsPurchased = itemsPurchased;
                topSupplier = supplier;
            }
        }
        return topSupplier;
    }

    
    // Add this method to create a rounded border for panels
    private Border createRoundedBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );
    }

    // Add this method to create a table with column headers and data
    private void addTableToPanel(JPanel panel, Map<String, Integer> data) {
        String[] columnNames = {"Product Name", "Quantity Remaining"};
        Object[][] tableData = new Object[data.size()][2];
        int index = 0;
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            tableData[index][0] = entry.getKey();
            tableData[index][1] = entry.getValue();
            index++;
        }

        JTable table = new JTable(tableData, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane);
    }
    
        // Method to read items sold by product from a given date
    private static Map<String, Integer> readItemsSoldByProduct(String date) {
        Map<String, Integer> data = new HashMap<>();

        // Assuming the items sold by product file format is: date, name, category, count
        // You may need to adjust this based on the actual format of your file
        // Example data: 12/6/2023, Milk_Delice, Dairy, 14

        try (BufferedReader reader = new BufferedReader(new FileReader("/C:/Users/USER/Documents/NetBeansProjects/Inventory/src/main/java/com/inventory/JavaProject/prodReport.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (date.equals(values[0])) {
                    String productName = values[1];
                    int count = Integer.parseInt(values[3]);
                    data.put(productName, data.getOrDefault(productName, 0) + count);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    // Method to find low quantity stock
    private static Map<String, Integer> findLowQuantityStock(Map<String, Integer> inventoryData) {
        // Implement logic to find products with low quantity
        // Placeholder data
        Map<String, Integer> lowQuantityStock = new HashMap<>();
        for (Map.Entry<String, Integer> entry : inventoryData.entrySet()) {
            if (entry.getValue() < 10) { // Adjust the threshold as needed
                lowQuantityStock.put(entry.getKey(), entry.getValue());
            }
        }

        // Sort the lowQuantityStock map by value in descending order
        lowQuantityStock = lowQuantityStock.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return lowQuantityStock;
    }
    
    
    public static void addSummaryItem(JPanel panel, String label, String value) {
        JLabel labelComponent = new JLabel(label);
        JLabel valueComponent = new JLabel(value);

        labelComponent.setFont(new Font("Poppins", Font.PLAIN, 14));
        valueComponent.setFont(new Font("Poppins", Font.BOLD, 14));

        panel.add(labelComponent);
        panel.add(valueComponent);
    }

    // Method to read suppliers data from a file
    public static Map<String, Integer> readSuppliers(String filePath) {
        Map<String, Integer> data = new HashMap<>();

        // Assuming the suppliers.csv format is: Supplier Name, Product, Category, Contact Number, Email
        // You may need to adjust this based on the actual format of your file

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                String supplierName = values[0].trim(); // Trim to remove leading/trailing whitespaces
                data.put(supplierName, data.getOrDefault(supplierName, 0) + 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    // Method to read inventory data from a file
    public static Map<String, Integer> readInventory(String filePath) {
        Map<String, Integer> data = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Skip the header line
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                String productName = values[1];
                int quantity = Integer.parseInt(values[5]);
                data.put(productName, quantity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
    
    
    // ---------- SideBarPanel Here ---------- //
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

        String[] pageNames = {"Dashboard", "Inventory", "Reports", "Suppliers", "Orders", "Logout"};
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
            case "LoginFrame":
                return "Logout";
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
            //new DashboardFrame().setVisible(true);
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
        case "Logout":
            // Open the logutFrame
            new LoginFrame().setVisible(true);
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
            java.util.logging.Logger.getLogger(DashboardFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DashboardFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DashboardFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DashboardFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DashboardFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

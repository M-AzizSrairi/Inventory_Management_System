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

public class InventoryFrame extends javax.swing.JFrame {
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JLabel totalCategoriesLabel;
    private JLabel totalProductsLabel;
    private JLabel lowStockLabel;
    private Map<String, Integer> categoryCounts;
    
    public InventoryFrame() {
        initializeUI();
    }
    private void initializeUI() {
        setTitle("Inventory Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        // Create the product table
        String[] columnNames = {"Product", "Product ID", "Category", "Buying Price", "Quantity", "Availability"};
        tableModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);
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
        JButton exportCsvButton = new JButton("Export to CSV");
        exportCsvButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportToCSV();
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

        JPanel kpiPanel = new JPanel();
        kpiPanel.add(totalCategoriesLabel);
        kpiPanel.add(totalProductsLabel);
        kpiPanel.add(lowStockLabel);
        add(kpiPanel, BorderLayout.NORTH);
        // Initialize category counts
        categoryCounts = new HashMap<>();
        // Display the frame
        setVisible(true);
    }
    
    private void showAddProductDialog() {
        JDialog addProductDialog = new JDialog(this, "Add Product", true);
        addProductDialog.setSize(400, 300);
        addProductDialog.setLayout(new GridLayout(6, 2));

        addProductDialog.add(new JLabel("Product Name:"));
        JTextField productNameField = new JTextField();
        addProductDialog.add(productNameField);

        addProductDialog.add(new JLabel("Product ID:"));
        JTextField productIdField = new JTextField();
        addProductDialog.add(productIdField);

        addProductDialog.add(new JLabel("Category:"));
        JComboBox<String> categoryComboBox = new JComboBox<>(getSampleCategories());
        addProductDialog.add(categoryComboBox);

        addProductDialog.add(new JLabel("Buying Price:"));
        JTextField buyingPriceField = new JTextField();
        addProductDialog.add(buyingPriceField);

        addProductDialog.add(new JLabel("Quantity:"));
        JTextField quantityField = new JTextField();
        addProductDialog.add(quantityField);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle adding the product to the table
                String productName = productNameField.getText();
                String productId = productIdField.getText();
                String category = (String) categoryComboBox.getSelectedItem();
                String buyingPrice = buyingPriceField.getText();
                String quantity = quantityField.getText();

                // Add validation and error handling as needed
                addProductToTable(productName, productId, category, buyingPrice, quantity);
                // Update KPIs
                updateKPIs();

                addProductDialog.dispose();
            }
        });

        addProductDialog.add(addButton);

        addProductDialog.setVisible(true);
    }
    
    private void exportToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export to CSV");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave + ".csv"))) {
                // Write column headers
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    writer.write(tableModel.getColumnName(i));
                    if (i < tableModel.getColumnCount() - 1) {
                        writer.write(",");
                    }
                }
                writer.newLine();

                // Write data
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        writer.write(tableModel.getValueAt(i, j).toString());
                        if (j < tableModel.getColumnCount() - 1) {
                            writer.write(",");
                        }
                    }
                    writer.newLine();
                }

                JOptionPane.showMessageDialog(this, "Table exported to CSV successfully!", "Export Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error exporting to CSV: " + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addProductToTable(String productName, String productId, String category, String buyingPrice, String quantity) {
        // Add the product details to the table
        Vector<String> row = new Vector<>();
        row.add(productName);
        row.add(productId);
        row.add(category);
        row.add(buyingPrice);
        row.add(quantity);
        row.add("Availability"); // Placeholder for availability
        tableModel.addRow(row);

        // Update category counts
        categoryCounts.put(category, categoryCounts.getOrDefault(category, 0) + 1);

        // You may also want to perform data validation and error handling
    }
    private void updateKPIs() {
        // Update total categories label
        int totalCategories = categoryCounts.size();
        totalCategoriesLabel.setText("Total Categories: " + totalCategories);

        // Update total products label
        int totalProducts = tableModel.getRowCount();
        totalProductsLabel.setText("Total Products: " + totalProducts);

        // Update low stock label (assuming low stock when quantity is less than 10, adjust as needed)
        int lowStockCount = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int quantity = Integer.parseInt((String) tableModel.getValueAt(i, 4));
            if (quantity < 10) {
                lowStockCount++;
            }
        }
        lowStockLabel.setText("Low Stock: " + lowStockCount);
    }
    private String[] getSampleCategories() {
        return new String[]{"Espresso", "Latte", "Cappuccino", "Mocha", "Americano"};
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
        SwingUtilities.invokeLater(() -> new InventoryFrame());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import Classes.Product;
import Classes.Sale;
import Classes.SaleItem;


class SaleGUI extends JFrame {

    private Sale sale;
    private JTextField productIdField;
    private JTextField quantityField;
    private JTextArea reportArea;

    public SaleGUI(String title) {
        super(title);
        sale = new Sale();

        // Create components
        productIdField = new JTextField(10);
        quantityField = new JTextField(5);
        reportArea = new JTextArea(15, 30);
        reportArea.setEditable(false);

        JButton addProductButton = new JButton("Add Product");
        JButton validateButton = new JButton("Validate");

        // Create layout
        setLayout(new BorderLayout());

        JPanel entryPanel = new JPanel(new FlowLayout());
        entryPanel.add(new JLabel("Product ID:"));
        entryPanel.add(productIdField);
        entryPanel.add(new JLabel("Quantity:"));
        entryPanel.add(quantityField);
        entryPanel.add(addProductButton);

        JScrollPane reportScrollPane = new JScrollPane(reportArea);

        // Add components to the frame
        add(entryPanel, BorderLayout.NORTH);
        add(reportScrollPane, BorderLayout.CENTER);
        add(validateButton, BorderLayout.SOUTH);

        // Add action listeners
        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });

        validateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });
    }

    private void addProduct() {
        try {
            int productId = Integer.parseInt(productIdField.getText());
            int quantity = Integer.parseInt(quantityField.getText());

            // Assuming you have a method to get a Product object by ID
            Product product = getProductById(productId);

            if (product != null) {
                sale.addSaleItem(product, quantity);
                updateReportArea();
            } else {
                JOptionPane.showMessageDialog(this, "Product not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }

            // Clear fields
            productIdField.setText("");
            quantityField.setText("");
            productIdField.requestFocus();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input! Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generateReport() {
        sale.displaySaleInfo();
        reportArea.append("\n\n");
        updateReportArea(); // Update report area with the latest information
    }

    private void updateReportArea() {
        reportArea.setText(""); // Clear the text area
        reportArea.append("Sale Report:\n");
        reportArea.append("SaleID: " + sale.getSaleID() + "\n");
        reportArea.append("SaleDate: " + sale.getSaleDate() + "\n");
        reportArea.append("SaleTime: " + sale.getSaleTime() + "\n");
        reportArea.append("SaleItems:\n");

        for (SaleItem item : sale.getSaleItems()) {
            reportArea.append("  - ProductID: " + item.getProductID() + "\n");
            reportArea.append("    ProductName: " + item.getName() + "\n");
            reportArea.append("    Category: " + item.getCategory() + "\n");
            reportArea.append("    SellingPrice: " + item.getSellingPrice() + "\n");
            reportArea.append("    Quantity: " + item.getQuantity() + "\n");
        }

        reportArea.append("TotalPrice: " + sale.getTotalPrice() + "\n");
    }

    // Assuming you have a method to retrieve a Product by ID
    private Product getProductById(int productId) {
        // Implement this method to get the product by ID from your data source
        // For now, returning a dummy product
        return new Product(10.0, 20.0, "Dummy Product", "Dummy Category");
    }
}
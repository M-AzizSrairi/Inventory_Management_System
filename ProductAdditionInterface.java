package com.inventory.JavaProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ProductAdditionInterface extends JFrame {

    private JTextField productIdField;
    private JTextField productNameField;
    private JTextField categoryField;
    private JTextField buyingPriceField;
    private JTextField sellingPriceField;
    private JTextField quantityField;

    public ProductAdditionInterface() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Product Addition Interface");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);

        JPanel mainPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainPanel.add(new JLabel("Product ID:"));
        productIdField = new JTextField();
        mainPanel.add(productIdField);

        mainPanel.add(new JLabel("Product Name:"));
        productNameField = new JTextField();
        mainPanel.add(productNameField);

        mainPanel.add(new JLabel("Category:"));
        categoryField = new JTextField();
        mainPanel.add(categoryField);

        mainPanel.add(new JLabel("Buying Price:"));
        sellingPriceField = new JTextField();
        mainPanel.add(sellingPriceField);
        
        mainPanel.add(new JLabel("Selling Price:"));
        buyingPriceField = new JTextField();
        mainPanel.add(buyingPriceField);

        mainPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        mainPanel.add(quantityField);

        JButton addButton = new JButton("Add Product");
        addButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            addProductToCSV();
        }
    });
    mainPanel.add(addButton);
        JButton searchButton = new JButton("Search Product");
    searchButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            searchProductByName();
        }
    });
    mainPanel.add(searchButton);

    add(mainPanel);
    }

    private void addProductToCSV() {
        String productId = productIdField.getText();
        String productName = productNameField.getText();
        String category = categoryField.getText();
        String buyingPrice = buyingPriceField.getText();
        String sellingPrice = sellingPriceField.getText();
        String quantity = quantityField.getText();

        // Validate input fields
        if (productId.isEmpty() || productName.isEmpty() || category.isEmpty() || buyingPrice.isEmpty() || sellingPrice.isEmpty() || quantity.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String csvFile = "C:\\Users\\USER\\Documents\\NetBeansProjects\\Inventory\\src\\main\\java\\com\\inventory\\JavaProject\\inventory.csv";
        String line;
        String cvsSplitBy = ",";
        boolean productFound = false;
        List<String> updatedLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Skip the headers
            updatedLines.add(br.readLine());

            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] data = line.split(cvsSplitBy);

                // Check if the product ID matches
                if (productId.equalsIgnoreCase(data[0])) {
                    // Product found, update the quantity
                    int existingQuantity = Integer.parseInt(data[5]);
                    int newQuantity = Integer.parseInt(quantity);
                    int totalQuantity = existingQuantity + newQuantity;
                    data[5] = String.valueOf(totalQuantity);
                    productFound = true;
                }

                // Add the line to the updatedLines list
                updatedLines.add(String.join(",", data));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // If the product was not found, add a new line
        if (!productFound) {
            updatedLines.add(productId + "," + productName + "," + category + "," + buyingPrice + "," + sellingPrice + "," + quantity);
        }

        // Write the updatedLines back to the CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile, false))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error updating product information: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Product information updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Clear the fields after updating the product information
        productIdField.setText("");
        productNameField.setText("");
        categoryField.setText("");
        buyingPriceField.setText("");
        sellingPriceField.setText("");
        quantityField.setText("");

        // Close the dialog
        dispose();
    }

    private void searchProductByName() {
        String productName = productNameField.getText().trim();

        if (productName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a product name to search.", "Search Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String csvFile = "C:\\Users\\USER\\Documents\\NetBeansProjects\\Inventory\\src\\main\\java\\com\\inventory\\JavaProject\\inventory.csv";
        String line;
        String cvsSplitBy = ",";
        boolean productFound = false;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Skip the headers
            br.readLine();

            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] data = line.split(cvsSplitBy);

                // Check if the product name matches
                if (productName.equalsIgnoreCase(data[1])) {
                    // Product found, fill the form with existing data (except quantity)
                    productIdField.setText(data[0]);
                    categoryField.setText(data[2]);
                    buyingPriceField.setText(data[3]);
                    sellingPriceField.setText(data[4]);
                    quantityField.setText("");  // Leave quantity empty for the user to fill

                    productFound = true;
                    break;  // No need to continue searching
                }
            }

            if (!productFound) {
                JOptionPane.showMessageDialog(this, "Product not found.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String getProductId() {
    return productIdField.getText();
}

    public String getProductName() {
        return productNameField.getText();
    }

    public String getCategory() {
        return categoryField.getText();
    }

    public String getBuyingPrice() {
        return buyingPriceField.getText();
    }
    
    public String getSellingPrice() {
        return buyingPriceField.getText();
    }

    public String getQuantity() {
        return quantityField.getText();
    }
    public void setAddButtonAction(ActionListener actionListener) {
    JButton addButton = (JButton) getContentPane().getComponent(getContentPane().getComponentCount() - 1);
    addButton.addActionListener(actionListener);
    }

    public void dispose() {
        super.dispose();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProductAdditionInterface().setVisible(true));
    }
}

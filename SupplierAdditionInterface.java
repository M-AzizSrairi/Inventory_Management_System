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


public class SupplierAdditionInterface extends JFrame {

    private JTextField supplierNameField;
    private JTextField productField;
    private JTextField categoryField;
    private JTextField contactNumberField;
    private JTextField emailField;

    public SupplierAdditionInterface() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Supplier Addition Interface");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);

        JPanel mainPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainPanel.add(new JLabel("Supplier Name:"));
        supplierNameField = new JTextField();
        mainPanel.add(supplierNameField);

        mainPanel.add(new JLabel("Product Name:"));
        productField = new JTextField();
        mainPanel.add(productField);

        mainPanel.add(new JLabel("Category:"));
        categoryField = new JTextField();
        mainPanel.add(categoryField);

        mainPanel.add(new JLabel("Contact Number:"));
        contactNumberField = new JTextField();
        mainPanel.add(contactNumberField);
        
        mainPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        mainPanel.add(emailField);

        JButton addButton = new JButton("Add Supplier");
        addButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            addSupplierToCSV();
        }
    });
    mainPanel.add(addButton);
        JButton searchButton = new JButton("Search Supplier");
    searchButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            searchSupplierByName();
        }
    });
    mainPanel.add(searchButton);

    add(mainPanel);
    }

    private void addSupplierToCSV() {
        String SupplierName = supplierNameField.getText();
        String product = productField.getText();
        String category = categoryField.getText();
        String contact = contactNumberField.getText();
        String email = emailField.getText();

        // Validate input fields
        if (SupplierName.isEmpty() || product.isEmpty() || category.isEmpty() || contact.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String csvFile = "C:\\Users\\USER\\Documents\\NetBeansProjects\\Inventory\\src\\main\\java\\com\\inventory\\JavaProject\\suppliers.csv";
        String line;
        String cvsSplitBy = ",";
        List<String> updatedLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Skip the headers
            updatedLines.add(br.readLine());

            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] data = line.split(cvsSplitBy);
                
                

                // Add the line to the updatedLines list
                updatedLines.add(String.join(",", data));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        updatedLines.add(SupplierName + "," + product + "," + category + "," + contact + "," + email);
        

        // Write the updatedLines back to the CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile, false))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error updating supplier information: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Supplier information updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Clear the fields after updating the product information
        supplierNameField.setText("");
        productField.setText("");
        categoryField.setText("");
        contactNumberField.setText("");
        emailField.setText("");
        // Close the dialog
        dispose();
    }

    private void searchSupplierByName() {
        String SupplierName = supplierNameField.getText().trim();

        if (SupplierName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a product name to search.", "Search Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String csvFile = "C:\\Users\\USER\\Documents\\NetBeansProjects\\Inventory\\src\\main\\java\\com\\inventory\\JavaProject\\suppliers.csv";
        String line;
        String cvsSplitBy = ",";
        boolean supplierFound = false;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Skip the headers
            br.readLine();

            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] data = line.split(cvsSplitBy);

                // Check if the product name matches
                if (SupplierName.equalsIgnoreCase(data[0])) {
                    // Product found, fill the form with existing data (except quantity)
                    productField.setText(""); // Leave product empty for the user to fill
                    categoryField.setText(data[2]);
                    contactNumberField.setText(data[3]);
                    emailField.setText(data[4]);

                    supplierFound = true;
                    break;  // No need to continue searching
                }
            }

            if (!supplierFound) {
                JOptionPane.showMessageDialog(this, "Product not found.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String getProductName() {
    return productField.getText();
}

    public String getSupplierName() {
        return supplierNameField.getText();
    }

    public String getCategory() {
        return categoryField.getText();
    }

    public String getcontact() {
        return contactNumberField.getText();
    }
    
    public String getemail() {
        return emailField.getText();
    }

    public void setAddButtonAction(ActionListener actionListener) {
    JButton addButton = (JButton) getContentPane().getComponent(getContentPane().getComponentCount() - 1);
    addButton.addActionListener(actionListener);
    }

    public void dispose() {
        super.dispose();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SupplierAdditionInterface().setVisible(true));
    }
}

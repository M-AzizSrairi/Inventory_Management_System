import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ProductAdditionInterface extends JFrame {

    private JTextField productIdField;
    private JTextField productNameField;
    private JTextField categoryField;
    private JTextField buyingPriceField;
    private JTextField quantityField;

    public ProductAdditionInterface() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Product Addition Interface");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        JPanel mainPanel = new JPanel(new GridLayout(6, 2, 10, 10));
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

        add(mainPanel);
    }

    private void addProductToCSV() {
        String productId = productIdField.getText();
        String productName = productNameField.getText();
        String category = categoryField.getText();
        String buyingPrice = buyingPriceField.getText();
        String quantity = quantityField.getText();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("inventory.csv", true))) {
            // Append the new product information to the CSV file
            writer.write(productId + "," + productName + "," + category + "," + buyingPrice + "," + quantity);
            writer.newLine();

            JOptionPane.showMessageDialog(this, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error adding product to CSV: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Clear the fields after adding the product
        productIdField.setText("");
        productNameField.setText("");
        categoryField.setText("");
        buyingPriceField.setText("");
        quantityField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProductAdditionInterface().setVisible(true));
    }
}

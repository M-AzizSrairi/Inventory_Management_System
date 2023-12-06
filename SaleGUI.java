import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Scanner;

import Classes.Sale;
import Classes.SaleItem;
import Classes.Product;

public class SaleGUI extends JFrame {

    private Sale sale;
    private JTextField productIdField;
    private JTextField quantityField;
    private JTextArea reportArea;

    public SaleGUI(String title) {
        super(title);
        setSize(900, 600);

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
                createTextFile();
            }
        });
    }

    private void addProduct() {
        int productId = Integer.parseInt(productIdField.getText());
        int quantity = Integer.parseInt(quantityField.getText());

        // Retrieve product and available quantity based on the entered productId
        Object[] result = getProductById(productId, quantity);

        if (result != null) {
            Product product = (Product) result[0];
            int availableQuantity = (int) result[1];

            // Check if there is enough quantity in the inventory
            if (quantity <= availableQuantity) {
                // Subtract the quantity from the availableQuantity in the inventory
                updateInventory(productId, quantity, availableQuantity);

                // Add the product to the sale
                sale.addSaleItem(product, quantity);

                // Update the product report
                updateProductReportCSV();

                // Update the sales report
                updateReportArea();
            } else {
                JOptionPane.showMessageDialog(this, "Not enough quantity available in the inventory!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Product not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Clear fields
        productIdField.setText("");
        quantityField.setText("");
        productIdField.requestFocus();
    }

    private Object[] getProductById(int productId, int quantity) {
        try (Scanner scanner = new Scanner(new File("inventory.csv"))) {
            scanner.useLocale(new java.util.Locale("en", "US"));
            scanner.useDelimiter(",|\\R");

            // Skip the header line
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            while (scanner.hasNext()) {
                int id = Integer.parseInt(scanner.next());
                String name = scanner.next();
                String category = scanner.next();
                double buyingPrice = Double.parseDouble(scanner.next());
                double sellingPrice = Double.parseDouble(scanner.next());
                int availableQuantity = Integer.parseInt(scanner.next());

                if (id == productId) {
                    if (quantity <= availableQuantity) {
                        Product product = new Product(name, category, buyingPrice, sellingPrice);
                        return new Object[]{product, availableQuantity};
                    } else {
                        JOptionPane.showMessageDialog(this, "Not enough quantity available!", "Error", JOptionPane.ERROR_MESSAGE);
                        return null;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void updateInventory(int productId, int quantity, int availableQuantity) {
        try (Scanner scanner = new Scanner(new File("inventory.csv"))) {
            scanner.useLocale(new java.util.Locale("en", "US"));
            scanner.useDelimiter(",|\\R");

            // Create a StringBuilder to store the updated inventory content
            StringBuilder updatedInventory = new StringBuilder();

            // Process header line
            if (scanner.hasNextLine()) {
                updatedInventory.append(scanner.nextLine()).append("\n");
            }

            while (scanner.hasNext()) {
                int id = Integer.parseInt(scanner.next());
                String name = scanner.next();
                String category = scanner.next();
                double buyingPrice = Double.parseDouble(scanner.next());
                double sellingPrice = Double.parseDouble(scanner.next());
                int currentAvailableQuantity = Integer.parseInt(scanner.next());

                if (id == productId) {
                    // Subtract the quantity from the availableQuantity in the inventory
                    currentAvailableQuantity -= quantity;
                }

                // Append the updated line to the StringBuilder
                updatedInventory.append(id).append(",").append(name).append(",").append(category).append(",")
                        .append(buyingPrice).append(",").append(sellingPrice).append(",").append(currentAvailableQuantity)
                        .append("\n");
            }

            // Write the updated content back to the inventory.csv file
            try (PrintWriter writer = new PrintWriter("inventory.csv")) {
                writer.print(updatedInventory.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateProductReportCSV() {
        String date = sale.getSaleDate().toString();

        for (SaleItem item : sale.getSaleItems()) {
            try (Scanner scanner = new Scanner(new File("prodreport.csv"))) {
                scanner.useLocale(new java.util.Locale("en", "US"));
                scanner.useDelimiter(",|\\R");

                StringBuilder updatedProductReport = new StringBuilder();
                boolean dateExists = false;

                // Process header line
                if (scanner.hasNextLine()) {
                    String headerLine = scanner.nextLine();
                    updatedProductReport.append(headerLine).append("\n");
                }

                // Check if the date exists in the prodreport.csv
                while (scanner.hasNext()) {
                    String currentDate = scanner.next();
                    String productName = scanner.next();
                    String productCategory = scanner.next();
                    int count = Integer.parseInt(scanner.next());

                    if (currentDate.equals(date) && productName.equals(item.getName()) && productCategory.equals(item.getCategory())) {
                        // Date and product exist, update the count
                        dateExists = true;
                        count += item.getQuantity();
                    }

                    // Append the line to the StringBuilder
                    updatedProductReport.append(currentDate).append(",").append(productName).append(",")
                            .append(productCategory).append(",").append(count).append("\n");
                }

                // If the date doesn't exist, add a new line for the current product purchase
                if (!dateExists) {
                    updatedProductReport.append(date).append(",").append(item.getName()).append(",")
                            .append(item.getCategory()).append(",").append(item.getQuantity()).append("\n");
                }

                // Write the updated content back to the prodreport.csv file
                try (PrintWriter writer = new PrintWriter("prodreport.csv")) {
                    writer.print(updatedProductReport.toString());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void generateReport() {
        sale.displaySaleInfo();
        reportArea.append("\nWelcome, see you soon\n");
        updateReportArea(); // Update report area with the latest information
        updateReportCSV();
    }

    private void updateReportArea() {
        reportArea.setText(""); // Clear the text area
        reportArea.append("your Receipt:\n");
        reportArea.append("SaleID: " + sale.getSaleID() + "\n");
        reportArea.append("Date of today: " + sale.getSaleDate() + "\n");
        reportArea.append("Time of today: " + sale.getSaleTime() + "\n");
        reportArea.append("\nGroceries list:\n");

        for (SaleItem item : sale.getSaleItems()) {
            reportArea.append(item.getName() + ", " + item.getQuantity() + ", " +
                    item.getSellingPrice() + ", " + item.calculateTotalPrice() + "\n");
        }

        reportArea.append("\nTotal Price: " + sale.getTotalPrice() + "\n");
        reportArea.append("See you soon");
    }

    private void updateReportCSV() {
        String date = sale.getSaleDate().toString();
        double totalProfit = sale.getTotalPrice();
        double totalCost = sale.getTotalCost();
        double netRevenue = sale.calculateNetRevenue();

        try (Scanner scanner = new Scanner(new File("report.csv"))) {
            scanner.useLocale(new java.util.Locale("en", "US"));
            scanner.useDelimiter(",|\\R");

            StringBuilder updatedReport = new StringBuilder();
            boolean dateExists = false;

            // Process header line
            if (scanner.hasNextLine()) {
                String headerLine = scanner.nextLine();
                updatedReport.append(headerLine).append("\n");
            }

            // Check if the date exists in the report.csv
            while (scanner.hasNext()) {
                String currentDate = scanner.next();
                double currentTotalProfit = Double.parseDouble(scanner.next());
                double currentTotalCost = Double.parseDouble(scanner.next());
                double currentNetRevenue = Double.parseDouble(scanner.next());
                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                symbols.setDecimalSeparator('.');
                DecimalFormat decimalFormat = new DecimalFormat("#.##", symbols);
                decimalFormat.format(currentNetRevenue);

                if (currentDate.equals(date)) {
                    // Date exists, update the values
                    dateExists = true;
                    currentTotalProfit += totalProfit;
                    currentTotalCost += totalCost;
                    currentNetRevenue += netRevenue;
                }

                // Append the line to the StringBuilder
                updatedReport.append(currentDate).append(",").append(currentTotalProfit).append(",")
                        .append(currentTotalCost).append(",").append(decimalFormat.format(currentNetRevenue)).append("\n");
            }

            // If the date doesn't exist, add a new line for the current sale
            if (!dateExists) {
                updatedReport.append(date).append(",").append(totalProfit).append(",")
                        .append(totalCost).append(",").append(netRevenue).append("\n");
            }

            // Write the updated content back to the report.csv file
            try (PrintWriter writer = new PrintWriter("report.csv")) {
                writer.print(updatedReport.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createTextFile() {
        try (PrintWriter writer = new PrintWriter("receipt" + sale.getSaleID() + " - " + sale.getSaleDate() + ".txt")) {
            writer.println("Your Receipt:");
            writer.println("SaleID: " + sale.getSaleID());
            writer.println("Date of today: " + sale.getSaleDate());
            writer.println("Date of today: " + sale.getSaleTime());
            writer.println("\nGroceries list:");
            writer.println("Product, Quantity, Unit Price, Total");
            for (SaleItem item : sale.getSaleItems()) {
                writer.println(item.getName() + ", " + item.getQuantity() + ", " +
                        item.getSellingPrice() + ", " + item.calculateTotalPrice());
            }

            writer.println("\nTotal Price: " + sale.getTotalPrice());
            writer.println("See you soon");

            JOptionPane.showMessageDialog(this, "Text file generated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating text file!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SaleGUI("Sale Interface").setVisible(true));
    }
}

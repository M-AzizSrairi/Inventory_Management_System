package Classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Sale {
    private static int lastSaleID = 0;

    private int saleID;
    private LocalDate saleDate;
    private LocalTime saleTime;
    private List<SaleItem> saleItems;
    private double totalPrice;

    public Sale() {
        this.saleID = generateSaleID();
        this.saleDate = LocalDate.now();
        this.saleTime = LocalTime.now();
        this.saleItems = new ArrayList<>();
        this.totalPrice = 0.0;
    }

    private int generateSaleID() {
        if (lastSaleID == 0) {
            lastSaleID = getLastAssignedSaleIDFromStorage();
        }

        lastSaleID++;
        return lastSaleID;
    }

    private void storeLastAssignedSaleID() {
        try (PrintWriter writer = new PrintWriter("lastSaleID.txt")) {
            writer.println(lastSaleID);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private int getLastAssignedSaleIDFromStorage() {
        try (Scanner scanner = new Scanner(new File("lastSaleID.txt"))) {
            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            }
        } catch (FileNotFoundException e) {
            // Handle file not found or other exceptions
        }
        return 0; // Return 0 if file doesn't exist or is empty
    }


    public int getSaleID() {
        return saleID;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public LocalTime getSaleTime() {
        return saleTime;
    }

    public List<SaleItem> getSaleItems() {
        return saleItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void addSaleItem(Product product, int quantity) {
        SaleItem item = new SaleItem(product, quantity);
        saleItems.add(item);
        totalPrice += item.calculateTotalPrice();
    }

    private void calculateTotalPrice() {
        totalPrice = saleItems.stream()
                .mapToDouble(SaleItem::calculateTotalPrice)
                .sum();
    }
    
    public void resetFacture() {
        totalPrice = 0;
        saleItems.clear();
    }

    public double getTotalCost() {
        double totalCost = 0.0;
        for (SaleItem item : saleItems) {
            totalCost += item.getBuyingPrice() * item.getQuantity();
        }
        return totalCost;
    }

    public double calculateNetRevenue() {
        return getTotalPrice() - getTotalCost();
    }

    public void displaySaleInfo() {
        System.out.println("SaleID: " + saleID);
        System.out.println("SaleDate: " + saleDate);
        System.out.println("SaleTime: " + saleTime);
        System.out.println("SaleItems:");

        for (SaleItem item : saleItems) {
            System.out.println("  - ProductID: " + item.getProductID());
            System.out.println("    ProductName: " + item.getName());
            System.out.println("    Category: " + item.getCategory());
            System.out.println("    SellingPrice: " + item.getSellingPrice());
            System.out.println("    Quantity: " + item.getQuantity());
        }

        calculateTotalPrice();
        System.out.println("TotalPrice: " + totalPrice);

        // Store the last assigned saleID after displaying sale info
        storeLastAssignedSaleID();
    }
    
}

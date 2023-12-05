package Classes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Sale {
    private static int nextSaleID = 1;
    private static String currentDateString = getCurrentDateString();

    private int saleID;
    private Date saleDate;
    private Date saleTime;
    private List<SaleItem> saleItems;
    private double totalPrice;

    public Sale() {
        this.saleID = generateSaleID();
        this.saleDate = new Date(); 
        this.saleTime = new Date();
        this.saleItems = new ArrayList<>();
        this.totalPrice = 0.0;
    }

    private static String getCurrentDateString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }

    private int generateSaleID() {
        String currentDate = getCurrentDateString();
        if (currentDate.equals(currentDateString)) {
            return nextSaleID++;
        } else {
            currentDateString = currentDate;
            nextSaleID = 1;
            return nextSaleID++;
        }
    }

    public int getSaleID() {
        return saleID;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public Date getSaleTime() {
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
    }
}

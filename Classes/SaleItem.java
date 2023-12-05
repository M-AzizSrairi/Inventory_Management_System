package Classes;

public class SaleItem {
    private int productID;
    private String name;
    private String category;
    private double sellingPrice;
    private int quantity;

    public SaleItem(Product product, int quantity) {
        this.productID = product.getProductID();
        this.name = product.getName();
        this.category = product.getCategory();
        this.sellingPrice = product.getSellingPrice();
        this.quantity = quantity;
    }

    public int getProductID() {
        return productID;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public double calculateTotalPrice() {
        return sellingPrice * quantity;
    }
}


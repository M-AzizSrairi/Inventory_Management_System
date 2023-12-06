package Classes;

public class Product {
    private static int nextProductID = 1;

    private int productID;
    private String name;
    private String category;
    private double buyingPrice;
    private double sellingPrice;

    public Product(String name, String category, double buyingPrice, double sellingPrice) {
        this.productID = nextProductID++;
        this.name = name;
        this.category = category;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
    }

    public int getProductID() {
        return productID;
    }

    public double getBuyingPrice() {
        return buyingPrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }
}



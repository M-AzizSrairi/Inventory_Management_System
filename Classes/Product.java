package Classes;

public class Product {
    private static int nextProductID = 1;

    private int productID;
    private double buyingPrice;
    private double sellingPrice;
    private String name;
    private String category;

    public Product(double buyingPrice, double sellingPrice, String name, String category) {
        this.productID = nextProductID++;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
        this.name = name;
        this.category = category;
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

    // You can add more methods or attributes based on your specific requirements
}



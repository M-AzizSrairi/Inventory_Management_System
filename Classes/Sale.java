public class Sale {
    private int saleId;
    private Product product;
    private int quantity;
    private double sellingPrice; // Add selling price

    public Sale(int saleId, Product product, int quantity, double sellingPrice) {
        this.saleId = saleId;
        this.product = product;
        this.quantity = quantity;
        this.sellingPrice = sellingPrice;
    }

    public int getSaleId() {
        return saleId;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public double getSaleTotalPrice() {
        return sellingPrice * quantity;
    }

    // Additional methods can be added as needed
}

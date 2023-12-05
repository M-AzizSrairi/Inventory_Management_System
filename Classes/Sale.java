public class Sale {
    private int saleId;
    private Product product;
    private int quantity;
    private double totalPrice;

    public Sale(int saleId, Product product, int quantity) {
        this.saleId = saleId;
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = calculateTotalPrice();
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

    public double getTotalPrice() {
        return totalPrice;
    }

    private double calculateTotalPrice() {
        return product.getSellingPrice() * quantity;
    }

    // Additional methods can be added as needed
}

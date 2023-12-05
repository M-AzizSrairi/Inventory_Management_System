public class OrderItem {
    private Product product;
    private int quantity;
    private double buyingPrice; // Add buying price

    public OrderItem(Product product, int quantity, double buyingPrice) {
        this.product = product;
        this.quantity = quantity;
        this.buyingPrice = buyingPrice;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getBuyingPrice() {
        return buyingPrice;
    }

    public double getOrderItemTotalPrice() {
        return buyingPrice * quantity;
    }


}

package Classes;
public class OrderItem {
    private Product product;
    private int quantity;
    private double buyingPrice;
    private Supplier supplier;

    public OrderItem(Product product, int quantity, double buyingPrice, Supplier supplier) {
        this.product = product;
        this.quantity = quantity;
        this.buyingPrice = buyingPrice;
        this.supplier = supplier;
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

    public Supplier getSupplier(){
        return this.supplier;
    }

    //print invoice
    public void issueInvoice(){
        //to be added;
    }


}

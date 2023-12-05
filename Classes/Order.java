package Classes;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    private int orderId;
    private Date orderDate;
    private List<OrderItem> orderItems;

    public Order(int orderId, Date orderDate) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderItems = new ArrayList<>();
    }

    public int getOrderId() {
        return orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public List<OrderItem> getOrderItems() {
        return new ArrayList<>(orderItems);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
    }

    public double getOrderTotalPrice() {
        double total = 0.0;
        for (OrderItem orderItem : orderItems) {
            total += orderItem.getOrderItemTotalPrice();
        }
        return total;
    }

    // Additional methods can be added as needed
}

package Classes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory {
    private Map<Product, Integer> products;

    public Inventory() {
        this.products = new HashMap<>();
    }

    public void addProduct(Product product, int quantity) {
        if (products.containsKey(product)) {
            int currentQuantity = products.get(product);
            products.put(product, currentQuantity + quantity);
        } else {
            products.put(product, quantity);
        }
    }

    public void updateProduct(Product product, int quantity) {
        if (products.containsKey(product)) {
            products.put(product, quantity);
        } else {
            System.out.println("Product not found in inventory.");
        }
    }

    public void removeProduct(Product product) {
        if (products.containsKey(product)) {
            products.remove(product);
        } else {
            System.out.println("Product not found in inventory.");
        }
    }

    public int getProductQuantity(Product product) {
        return products.getOrDefault(product, 0);
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products.keySet());
    }

    public boolean checkStockAvailability(Product product, int quantity) {
        return products.getOrDefault(product, 0) >= quantity;
    }
}

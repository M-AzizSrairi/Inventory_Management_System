package Classes;

import java.util.List;

public class Supplier extends Person {

    //Attributes
    private static final String ROLE = "Supplier";
    private String email;
    private String phone;
    private List<String> productList;
    private String location;

    // Constructor 
    public Supplier(String name, String birthdate, String email, String phone, List<String> productList, String location) {
        super(name, birthdate); 

        if (isValidSupplierPhone(phone)) {
            this.phone = phone;
        } else {
            throw new IllegalArgumentException("Invalid phone format for Supplier. Must start with +216 and have 8 digits.");
        }

        if (isValidSupplierEmail(email)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Invalid email format for Supplier.");
        }

        this.productList = productList;
        this.location = location;
    }


    // Helper method to validate phone format for Supplier
    private boolean isValidSupplierPhone(String phone) {
        return phone != null && phone.matches("\\+216\\d{8}");
    }   

    // Helper method to validate phone format for Supplier
    private boolean isValidSupplierEmail(String email) {
        return email != null && email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }

    //setters
    public void setProductList(List<String> productList) {
        this.productList = productList;
    }

    public void setSupplierEmail(String email){
        if (isValidSupplierEmail(email)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Invalid email format for Supplier.");
        }        
    }

    public void setSupplierPhone(String phone){
        if (isValidSupplierPhone(phone)) {
            this.phone = phone;
        } else {
            throw new IllegalArgumentException("Invalid phone format for Supplier. Must start with +216 and have 8 digits.");
        }
    }

    public void setSupplierLocation(String location){
        this.location = location;
    }

    // Getters
    public String getRole() {
        return ROLE;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhone() {
        return this.phone;
    }

    public List<String> getProductList() {
        return this.productList;
    }

    public String getLocation() {
        return this.location;
    }

    @Override
    public void displayInfo() {
        System.out.println(
            "SupplierID: " + getPersonID() + "\n" +
            "Supplier's name: " + getName() + "\n" +
            "Supplier's age: " + getAge() + "\n" + 
            "Supplier's role: " + getRole() + "\n" + 
            "Supplier's email: " + this.email + "\n" +
            "Supplier's phone: " + this.phone + "\n" +
            "Supplier's product list: " + this.productList + "\n" +
            "Supplier's location: " + this.location + "\n"
        );
    }
}

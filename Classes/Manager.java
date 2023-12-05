package Classes;

public class Manager  extends Person{

    private double monthlySalary;
    private static final String ROLE = "Manager";

    //Constructor
    public Manager(String name, String birthdate, double monthlySalary) {
        super(name, birthdate);
        if (monthlySalary >= 0){
            this.monthlySalary = monthlySalary;
        }
    }

    //Setters
    public void setMonthlySalary(int monthlySalary){
        if(monthlySalary >= 0){
            this.monthlySalary = monthlySalary;
        }
    }

    //Getters 
    public String getRole() {
        return ROLE;
    }

    public double getMonthlySalary() {
        return this.monthlySalary;
    }


    @Override
    public void displayInfo() {
        System.out.println(
            "ManagerID: " + getPersonID() + "\n" +
            "Manager's name: " + getName() + "\n" +
            "Manager's age: " + getAge() + "\n" + 
            "Manager's role: " + getRole() + "\n" + 
            "Manager's monthly salary: " + this.monthlySalary + "\n"
        );
    }
    
}

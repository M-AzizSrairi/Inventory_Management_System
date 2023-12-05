package Classes;

public class Worker extends Person {

    private int workingHoursPerDay;
    private double salaryPerHour;
    private static final String ROLE = "Worker";
    

    // Constructor 
    public Worker(String name, String birthdate, int workingHoursPerDay, double salaryPerHour) {
        super(name, birthdate);

        if (workingHoursPerDay >= 0) {
            this.workingHoursPerDay = workingHoursPerDay;
        }

        if (salaryPerHour >= 0) {
            this.salaryPerHour = salaryPerHour;
        }
    }

    // Setters
    public void setWorkingHoursPerDay(int workingHoursPerDay) {
        if (workingHoursPerDay >= 0) {
            this.workingHoursPerDay = workingHoursPerDay;
        }
    }

    public void setSalaryPerHour(double salaryPerHour) {
        if (salaryPerHour >= 0) {
            this.salaryPerHour = salaryPerHour;
        }
    }

    // Getters
    public String getRole() {
        return ROLE;
    }
    
    public int getWorkingHoursPerDay() {
        return this.workingHoursPerDay;
    }

    public double getSalaryPerHour() {
        return this.salaryPerHour;
    }

    public double monthlySalary(int workingDays) {
        return this.workingHoursPerDay * this.salaryPerHour * workingDays;
    }

    @Override
    public void displayInfo() {
        System.out.println(
            "WorkerID: " + getPersonID() + "\n" +
            "Worker's name: " + getName() + "\n" +
            "Worker's role: " + this.ROLE + "\n" +
            "Worker's age: " + getAge() + "\n" +
            "Worker's working hours per day: " + this.workingHoursPerDay + "\n" +
            "Worker's salary per hour: " + this.salaryPerHour + "\n"
        );
    }
}

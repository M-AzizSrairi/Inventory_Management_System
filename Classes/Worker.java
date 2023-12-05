package Classes;

public class Worker extends Person {

    private int workingHoursPerDay;
    private double salaryPerHour;

    //Constructor 
    public Worker(String name, int age, int workingHoursPerDay, double salaryPerHour){
        super(name,age);
        if (workingHoursPerDay >= 0) {
            this.workingHoursPerDay = workingHoursPerDay;
        }

        if (salaryPerHour >= 0) {
            this.salaryPerHour = salaryPerHour;
        }
    }

    //Setters
    public void setWorkingHoursPerDay(int workingHoursPerDay){
        if (workingHoursPerDay >= 0) {
            this.workingHoursPerDay = workingHoursPerDay;
        }
    }

    public void setSalaryperHour(int salaryPerHour){
        if (salaryPerHour >= 0) {
            this.salaryPerHour = salaryPerHour;
        }
    }

    //Getters
    public int getWorkingHoursPerDay(){
        return this.workingHoursPerDay;
    }

    public double getSalaryPerHour() {
        return this.workingHoursPerDay;
    }

    public double monthlySalary(int workingDays){
        return this.workingHoursPerDay * this.salaryPerHour * workingDays ;
    }


    @Override
    public void displayInfo(){
        System.out.println(
            "WorkerID : " + getPersonID() + "\n" + 
            "Worker's name : " + getName() +  "\n" + 
            "Worker's age : " + getAge() + "\n" + 
            "Worker's working hours per day : " + this.workingHoursPerDay + "\n" + 
            "Worker's salary per hour :  " + this.salaryPerHour + "\n"
        );
    }

    
}
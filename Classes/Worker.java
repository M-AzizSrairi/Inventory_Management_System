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
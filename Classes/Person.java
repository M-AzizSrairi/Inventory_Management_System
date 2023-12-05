package Classes;

public abstract class Person {
    private static int nextPersonID = 1000;
    protected int personID;
    protected String name;
    private int age;

    //Constructor 
    public Person(String name, int age){

        this.personID = nextPersonID++;

        if (name != null || !name.trim().isEmpty()){
            this.name = name;
        } else {
            throw new IllegalArgumentException("Name must not be null or empty");
        }

        if (age >= 16 && age <= 90) {
            this.age = age;
        } else {
            throw new IllegalArgumentException("Age must be within the range (16,90) inclusive ");
        }

    }

    //abstract method - display Info
    public abstract void displayInfo();

    //Getter methods
    public int getPersonID(){
        return this.personID;
    }

    public String getName(){
        return this.name;
    }
    
}
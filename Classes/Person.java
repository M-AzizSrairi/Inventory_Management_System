package Classes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public abstract class Person {
    private static int nextPersonID = 1000;
    protected int personID;
    protected String name;
    private Date birthdate;
    private int age;

    // Constructor
    public Person(String name, String birthdate) {
        this.personID = nextPersonID++;

        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Name must not be null or empty");
        }

        // Validate and set birthdate
        if (isValidDate(birthdate)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                this.birthdate = sdf.parse(birthdate);

                // Calculate age based on current date
                calculateAge();
            } catch (ParseException e) {
                throw new IllegalArgumentException("Invalid date format. Use yyyy-MM-dd.");
            }
        } else {
            throw new IllegalArgumentException("Invalid date format. Use yyyy-MM-dd.");
        }
    }

    // Abstract method - display Info
    public abstract void displayInfo();

    // Getter methods
    public int getPersonID() {
        return this.personID;
    }

    public String getName() {
        return this.name;
    }

    public Date getBirthdate() {
        return this.birthdate;
    }

    // Updated getter method for age
    public int getAge() {
        return this.age;
    }

    // Calculate age based on current date
    private void calculateAge() {
        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime(this.birthdate);

        Calendar currentCalendar = Calendar.getInstance();

        int years = currentCalendar.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);

        // Adjust age if the birthday hasn't occurred yet this year
        if (currentCalendar.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
            years--;
        }

        this.age = years;
    }

    // Validate date format
    private boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}

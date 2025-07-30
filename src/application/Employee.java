package application;

import java.util.HashSet;
import java.util.Random;

public class Employee {
    private String FirstName;
    private String LastName;
    private String UserName;
    private String Password;
    private String Department;
    private Double PayRate;
    private Double TaxRate;
    private int ID;

    private PTO pto;
    private AddHours addHours;

    private static final HashSet<Integer> uniqueIds = new HashSet<>();
    private static final Random random = new Random();

    public Employee(String FirstName, String LastName, String UserName, String Password, String Department) {
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.UserName = UserName;
        this.Password = Password;
        this.Department = Department;
        this.ID = generateUniqueId();
        this.pto = new PTO(40);         
        this.addHours = new AddHours();
        this.PayRate = 15.0;
        this.TaxRate = 0.4;
    }

    private int generateUniqueId() {
        int id;
        do {
            id = 1000 + random.nextInt(9000);
        } while (!uniqueIds.add(id));
        return id;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartmentToManager() {
        this.Department = "Manager";
    }

    public String getFirstName() {
        return this.FirstName;
    }

    public String getLastName() {
        return this.LastName;
    }

    public int getID() {
        return this.ID;
    }
    
    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return this.FirstName + " " + this.LastName;
    }

    public String getUsername() {
        return UserName;
    }
    public String getPassword() {
    	return Password;
    }

    public PTO getPTO() {
        return this.pto;
    }

    public AddHours getAddHours() {
        return this.addHours;
    }
    
    public void setAddHours(AddHours AH) {
    	this.addHours = AH;
    }
    
    public double getPayRate() {
    	return this.PayRate;
    }
    
    public double getTaxRate() {
    	return this.TaxRate;
    }
    

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName() + " (ID: " + getID() + ")";
    }
}

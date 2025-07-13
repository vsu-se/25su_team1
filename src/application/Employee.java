package application;

import java.util.HashSet;
import java.util.Random;

class Employee {
	private String FirstName;
    private String LastName;
	private String UserName;
    private String Password;
    private String Department;
    private Double PayRate;
    private Double TaxRate;
    private int PTO;
    private int ID; //4 digits
    

    private static final HashSet<Integer> uniqueIds = new HashSet<>();
    private static final Random random = new Random();
    
    public Employee(String FirstName, String LastName, String UserName, String Password, String Department) {
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.UserName = UserName;
        this.Password = Password;
        this.Department = Department;
        Double PayRate;
        Double TaxRate;
        int PTO;
        this.ID = generateUniqueId();
    }	
    
    public int generateUniqueId() {
        int id;
        do {
            id = 1000 + random.nextInt(9000); 
        } while (!uniqueIds.add(id)); //
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
    public String getUsername() {
    	return this.UserName;
    }
    public int getID() {
    	return this.ID;
    }
    public String getName() {
    	return this.FirstName + " " + this.LastName;
    }
}

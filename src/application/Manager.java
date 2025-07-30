package application;

public class Manager extends Employee {

    public Manager(String FirstName, String LastName, String UserName, String Password) {
        super(FirstName, LastName, UserName, Password, "Manager"); // Call Employee constructor
    }
    
}

package application;
import java.util.ArrayList;
import java.util.List;
public class CreateEmployee {
    private List<Employee> currentEmployeeList = new ArrayList<>();
    private List<Manager> currentManagersList = new ArrayList<>();
    public void createEmp(String firstname, String lastname, String UserName, String Password, String position){

        switch (position) {
        	case "Manager":
        		//set this way so that they share IDS.
        		Manager manager = new Manager(firstname, lastname, UserName, Password);
        		currentManagersList.add(manager);
        		currentEmployeeList.add(manager);
        		
                break;
            case "Staff":
                currentEmployeeList.add(new Employee(firstname, lastname, UserName, Password, "Staff"));
                break;
            default:
            	System.out.println("Invalid Answer");
                break;
                }
     

    }
    
    public List<Employee> getEmployees() {
        return new ArrayList<>(currentEmployeeList); // Return a copy to prevent external modification
    }

    public List<Manager> getManagers() {
        return new ArrayList<>(currentManagersList); // Return a copy to prevent external modification
<<<<<<< Updated upstream
=======
    }
    
    public void clearEmployees() {
        currentEmployeeList.clear();
        currentManagersList.clear();
    }
    
    public void addEmployee(Employee employee) {
        currentEmployeeList.add(employee);
        if (employee instanceof Manager) {
            currentManagersList.add((Manager) employee);
        }
>>>>>>> Stashed changes
    }
}
    


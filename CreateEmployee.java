package application;

import java.util.ArrayList;
import java.util.List;

public class CreateEmployee {
    private List<Employee> currentEmployeeList = new ArrayList<>();
    private List<Manager> currentManagersList = new ArrayList<>();

    public void yo(String firstname, String lastname, String UserName, String Password, String position) {
        switch (position) {
            case "Manager":
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

        System.out.println("--- Staff Employees ---");
        if (!currentEmployeeList.isEmpty()) {
            for (Employee e : currentEmployeeList) {
                System.out.println("Employee Name: " + e.getName());
                System.out.println("Position: " + e.getDepartment());
            }
        } else {
            System.out.println("No staff employees.");
        }

        System.out.println("--- Managers ---");
        if (!currentManagersList.isEmpty()) {
            for (Manager m : currentManagersList) {
                System.out.println("Employee Name: " + m.getName());
            }
        } else {
            System.out.println("No managers.");
        }
    }

    public List<Manager> getManagers() {
        return new ArrayList<>(currentManagersList);
    }

    public List<Employee> getEmployees() {
        return new ArrayList<>(currentEmployeeList);
    }
}

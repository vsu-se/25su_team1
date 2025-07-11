import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class Main {
    public static void main(String[] args) {
        List<Employee> currentEmployeeList = new ArrayList<>();
        List<Manager> currentManagersList = new ArrayList<>();
        String firstName, lastName, position;

        System.out.println("New Employee? y/n");
        Scanner input = new Scanner(System.in);
        String answer = input.nextLine();
        if(answer.equalsIgnoreCase("y")){
            System.out.println("first name");
            firstName = input.nextLine();
            System.out.println("last name");
            lastName = input.nextLine();
            System.out.println("position? (Manager or Staff)");
            position = input.nextLine();
            switch (position) {
                case "Manager":
                    currentManagersList.add(new Manager(firstName + " " +lastName));
                    break;
                case "Staff":
                    currentEmployeeList.add(new Employee(firstName + " " +lastName, "Staff"));
                default:
                    System.out.println("Invalid Answer");
                    break;
            }
        }

        System.out.println("--- Staff Employees ---");
        if (!currentEmployeeList.isEmpty()) {
            for (Employee e : currentEmployeeList) {
                System.out.println("Employee Name: " + e.getName());
                System.out.println("Position: " + e.getPosition());
            }
        } else {
            System.out.println("No staff employees.");
        }

        System.out.println("--- Managers ---");
        if (!currentManagersList.isEmpty()) {
            for (Manager m : currentManagersList) {
                System.out.println("Employee Name: " + m.getName());
                System.out.println("Position: " + m.getPosition());
            }
        } else {
            System.out.println("No managers.");
        }

        input.close();
    }
}
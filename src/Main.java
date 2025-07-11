import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        String firstName, lastName, position;
        Employee a = null;
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
            a = new Employee(firstName + " " + lastName, position);
        }

        if (a != null) {
            System.out.println("Employee Name: " + a.getName());
            System.out.println("Position: " + a.getPosition());
        } else {
            System.out.println("No employee was created.");
        }

        input.close(); // Good practice to close Scanner
    }
}
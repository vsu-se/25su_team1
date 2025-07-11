import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        String firstName, lastName, position;
        Employee a = null;
        Manager aa = null;
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
                    aa = new Manager(firstName + " "+ lastName);
                    break;
                case "Staff":
                    a = new Employee(firstName + " " + lastName, position);
                default:
                    System.out.println("Invalid Answer");
                    break;
            }
        }

        if (a != null) {
            System.out.println("Employee Name: " + a.getName());
            System.out.println("Position: " + a.getPosition());
        } else if (aa != null)  {
            System.out.println("Employee Name: " + aa.getName());
            System.out.println("Position: " + aa.getPosition());
        } else {
            System.out.println("No employeees");
        }

        input.close(); // Good practice to close Scanner
    }
}
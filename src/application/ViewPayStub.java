package application;

import java.io.IOException;
import java.io.PrintWriter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewPayStub extends Application {

    private Employee employee;

    public ViewPayStub(Employee e) {
        this.employee = e;
    }

    public ViewPayStub() {
        this.employee = new Employee("Christian", "Misner", "ChristianMisner", "Password1", "Staff");
        employee.getAddHours().setHours(0, 8.0);
        employee.getAddHours().setHours(1, 12.3);
        employee.getAddHours().setHours(2, 10.0);
        employee.getAddHours().setHours(3, 12.3);
        employee.getAddHours().setPTO(4, employee.getPTO());
        employee.getAddHours().setHours(5, 0.0);
        employee.getAddHours().setHours(6, 2.0);
    }

    public String isPTO(int n, AddHours addHours) {
        return addHours.isPTO(n) ? " (PTO)" : "";
    }

    public double getPay(Employee e, AddHours ah) {
        double payRate = e.getPayRate();
        double weekday = ah.getWeekdayHours();
        double weekend = ah.getWeekendHours();
        double totalPay = 0;

        if (e.getDepartment().equals("Manager")) {
            return 40 * payRate;
        }

        if (weekday > 40) {
            totalPay += 40 * payRate + (weekday - 40) * payRate * 1.5;
        } else {
            totalPay += weekday * payRate;
        }

        totalPay += weekend * payRate * 1.5;
        return totalPay;
    }

    public String[] PayStub(Employee e, AddHours ah, PTO pto) {
        double gross = getPay(e, ah);
        double tax = gross * 0.45;
        double net = gross - tax;

        return new String[]{
            "Name: " + e.getName() + " (ID: " + e.getID() + ")",
            "Hours Worked:",
            "Monday: " + ah.getHours(0) + isPTO(0, ah) +
            "  Tuesday: " + ah.getHours(1) + isPTO(1, ah) +
            "  Wednesday: " + ah.getHours(2) + isPTO(2, ah) +
            "  Thursday: " + ah.getHours(3) + isPTO(3, ah) +
            "  Friday: " + ah.getHours(4) + isPTO(4, ah),
            "Saturday: " + ah.getHours(5) + isPTO(5, ah) +
            "  Sunday: " + ah.getHours(6) + isPTO(6, ah),
            "Weekday: " + ah.getWeekdayHours() +
            "  Weekend: " + ah.getWeekendHours() +
            "  Total: " + ah.getTotalHours(),
            "Gross Pay: $" + String.format("%.2f", gross) +
            "  Tax Withholdings: $" + String.format("%.2f", tax) +
            "  Net Pay: $" + String.format("%.2f", net) +
            "  Remaining PTO: " + pto.getRemainingPTOHours()
        };
    }

    @Override
    public void start(Stage primaryStage) {
        AddHours ah = employee.getAddHours();
        PTO pto = employee.getPTO();

        String[] payStub = PayStub(employee, ah, pto);

        VBox layout = new VBox(10);
        for (String line : payStub) {
            layout.getChildren().add(new Label(line));
        }

        Button saveButton = new Button("Save PayStub");
        saveButton.setOnAction(e -> {
            try (PrintWriter writer = new PrintWriter(employee.getName() + " Pay Stub.txt")) {
                for (String line : payStub) {
                    writer.println(line);
                    writer.println();
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        });

        layout.getChildren().add(saveButton);
        Scene scene = new Scene(layout, 500, 300);
        primaryStage.setTitle("View Pay Stub");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void launchWithEmployee(Employee employee) {
        ViewPayStub gui = new ViewPayStub(employee);
        gui.start(new Stage());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListEmployeeHours extends Application {

    private static List<Employee> employeeList;

    public static void setEmployeeList(List<Employee> employees) {
        employeeList = employees;
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Employee Weekly Hours");

        ListView<String> listView = new ListView<>();

        if (employeeList == null) {
            listView.getItems().add("No employee data available.");
        } else {

            Collections.sort(employeeList, Comparator
                    .comparing(Employee::getLastName)
                    .thenComparing(Employee::getFirstName)
                    .thenComparing(Employee::getDepartment)
                    .thenComparingInt(Employee::getID));

            for (Employee emp : employeeList) {
                double totalHours = emp.getAddHours().getTotalHours();
                String display = String.format("%s, %s (%s) - ID: %d - Total Hours: %.2f",
                        emp.getLastName(),
                        emp.getFirstName(),
                        emp.getDepartment(),
                        emp.getID(),
                        totalHours);
                listView.getItems().add(display);
            }
        }

        VBox layout = new VBox(10);
        layout.getChildren().addAll(new Label("Weekly Hours Report:"), listView);

        Scene scene = new Scene(layout, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

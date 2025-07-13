package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class AddHoursGui extends Application {

    private AddHours addHours = new AddHours();
    private PTO pto;
    private Employee selectedEmployee;

    public AddHoursGui(Employee employee) {
        this.selectedEmployee = employee;
        this.pto = employee.getPTO();
    }

    public AddHoursGui() {
        this.selectedEmployee = new Employee("jason", "pearson", "jp", "uno", "Staff");
        this.pto = selectedEmployee.getPTO();
    }

    @Override
    public void start(Stage primaryStage) {
        Label titleLabel = new Label("Add Hours for Employee");
        Label nameLabel = new Label("Employee: " + selectedEmployee.getName() + " (ID: " + selectedEmployee.getID() + ")");

        ComboBox<String> daySelector = new ComboBox<>();
        daySelector.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");

        TextField hoursField = new TextField();
        hoursField.setPromptText("Enter number of hours");

        CheckBox ptoCheck = new CheckBox("Is this PTO?");

        Button submitButton = new Button("Submit Hours");
        Button summaryButton = new Button("Show Weekly Summary");

        Label messageLabel = new Label();
        Label ptoBalanceLabel = new Label("PTO Balance: " + pto.getRemainingPTOHours() + " hours");

        submitButton.setOnAction(e -> {
            int dayIndex = daySelector.getSelectionModel().getSelectedIndex();
            String hoursText = hoursField.getText();

            if (dayIndex == -1 || hoursText.isEmpty()) {
                messageLabel.setText("Please select a day and enter hours.");
                return;
            }

            if (addHours.getHours(dayIndex) > 0 || addHours.isPTO(dayIndex)) {
                messageLabel.setText("Hours already recorded for this day.");
                return;
            }

            try {
                double hours = Double.parseDouble(hoursText);

                if (ptoCheck.isSelected()) {
                    if (hours != 8) {
                        messageLabel.setText("Only allowed 8 hours of PTO for the day.");
                        return;
                    }
                    boolean ptoSet = addHours.setPTO(dayIndex, pto);
                    messageLabel.setText(ptoSet ? "PTO recorded." : "PTO not allowed or already used.");
                } else {
                    addHours.setHours(dayIndex, hours);
                    messageLabel.setText("Regular hours recorded.");
                }

                ptoBalanceLabel.setText("PTO Balance: " + pto.getRemainingPTOHours() + " hours");

            } catch (NumberFormatException ex) {
                messageLabel.setText("Invalid number format for hours.");
            }
        });

        summaryButton.setOnAction(e -> {
            StringBuilder summary = new StringBuilder();
            summary.append("Total Hours: ").append(addHours.getTotalHours()).append("\n");
            summary.append("Weekday Hours: ").append(addHours.getWeekdayHours()).append("\n");
            summary.append("Weekend Hours: ").append(addHours.getWeekendHours()).append("\n");
            summary.append("Days Worked: ").append(addHours.getNumDaysWorked()).append("\n");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Weekly Summary");
            alert.setHeaderText("Employee Time Summary");
            alert.setContentText(summary.toString());
            alert.showAndWait();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(
            titleLabel,
            nameLabel,
            daySelector,
            hoursField,
            ptoCheck,
            submitButton,
            summaryButton,
            messageLabel,
            ptoBalanceLabel
        );

        Scene scene = new Scene(layout, 400, 500);
        primaryStage.setTitle("Add Weekly Hours");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void launchWithEmployee(Employee employee) {
        AddHoursGui gui = new AddHoursGui(employee);
        gui.start(new Stage());
    }

    public static void main(String[] args) {
        launch(args);
    }
}

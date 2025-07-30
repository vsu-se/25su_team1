package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Map;
import java.util.HashMap;

public class EmployeeLogin extends Application {
    private static Map<String, String> e_user = new HashMap<>();
    private static Map<String, Employee> e_map = new HashMap<>();

    @Override
    public void start(Stage stage) {
        initializeEmployeeUsers();

        Label userLabel = new Label("Employee Username:");
        TextField userField = new TextField();
        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();
        Button loginButton = new Button("Login");
        Button backButton = new Button("Back");
        Label messageLabel = new Label();

        VBox layout = new VBox(10, userLabel, userField, passLabel, passField, loginButton, backButton, messageLabel);
        Scene loginScene = new Scene(layout, 400, 250);
        loginScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setTitle("Employee Login");
        stage.setScene(loginScene);
        stage.show();

        loginButton.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();

            if (e_user.containsKey(username) && e_user.get(username).equals(password)) {
                Employee emp = e_map.get(username);
                showEmployeeDashboard(stage, emp);
            } else {
                messageLabel.setText("Invalid employee credentials.");
            }
        });

        backButton.setOnAction(e -> stage.close());
    }

    private void showEmployeeDashboard(Stage stage, Employee emp) {
        VBox layout = new VBox(10);
        Label welcome = new Label("Welcome, " + emp.getName());
        ComboBox<String> daySelector = new ComboBox<>();
        daySelector.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");

        TextField hoursField = new TextField();
        hoursField.setPromptText("Enter number of hours");

        CheckBox ptoBox = new CheckBox("Use PTO");
        Label messageLabel = new Label();
        Label ptoBalance = new Label("PTO Balance: " + emp.getPTO().getRemainingPTOHours());

        Button submitButton = new Button("Submit Hours");
        Button summaryButton = new Button("View Weekly Summary");
        Button stubButton = new Button("View Pay Stub");
        Button changePasswordButton = new Button("Change Password");
        Button logoutButton = new Button("Logout");

        changePasswordButton.setOnAction(ev -> showChangePasswordScene(stage, emp));

        submitButton.setOnAction(e -> {
            int dayIndex = daySelector.getSelectionModel().getSelectedIndex();
            String input = hoursField.getText();
            if (dayIndex == -1 || input.isEmpty()) {
                messageLabel.setText("Select day and enter hours.");
                return;
            }

            if (emp.getAddHours().getHours(dayIndex) > 0 || emp.getAddHours().isPTO(dayIndex)) {
                messageLabel.setText("You already logged hours for this day.");
                return;
            }

            try {
                double hours = Double.parseDouble(input);
                if (ptoBox.isSelected()) {
                    if (hours != 8) {
                        messageLabel.setText("PTO must be exactly 8 hours.");
                        return;
                    }
                    boolean ptoSet = emp.getAddHours().setPTO(dayIndex, emp.getPTO());
                    messageLabel.setText(ptoSet ? "PTO recorded." : "PTO not allowed.");
                } else {
                    emp.getAddHours().setHours(dayIndex, hours);
                    messageLabel.setText("Hours submitted.");
                }
                ptoBalance.setText("PTO Balance: " + emp.getPTO().getRemainingPTOHours());
            } catch (NumberFormatException ex) {
                messageLabel.setText("Invalid number format.");
            }
        });

        summaryButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Weekly Summary");
            String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
            StringBuilder sb = new StringBuilder("Weekly Summary for " + emp.getName() + ":\n\nHours Logged:\n");
            for (int i = 0; i < 7; i++) {
                double h = emp.getAddHours().getHours(i);
                if (h > 0) {
                    sb.append(days[i]).append(": ").append(h);
                    if (emp.getAddHours().isPTO(i)) sb.append(" (PTO)");
                    sb.append("\n");
                }
            }
            sb.append("\nTotal Hours: ").append(emp.getAddHours().getTotalHours());
            alert.setContentText(sb.toString());
            alert.showAndWait();
        });

        stubButton.setOnAction(e -> ViewPayStub.launchWithEmployee(emp));

        logoutButton.setOnAction(e -> showLoginScreen(stage));

        layout.getChildren().addAll(
            welcome, daySelector, hoursField, ptoBox, submitButton, messageLabel,
            ptoBalance, summaryButton, stubButton, changePasswordButton, logoutButton
        );

        Scene empScene = new Scene(layout, 400, 450);
        empScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setScene(empScene);
    }

    private void showChangePasswordScene(Stage stage, Employee emp) {
        VBox layout = new VBox(10);
        Label currentLabel = new Label("Enter Current Password:");
        PasswordField currentField = new PasswordField();

        Label newLabel = new Label("Enter New Password:");
        PasswordField newField = new PasswordField();

        Label confirmLabel = new Label("Confirm New Password:");
        PasswordField confirmField = new PasswordField();

        Label message = new Label();

        Button submit = new Button("Submit");
        Button back = new Button("Back");

        submit.setOnAction(e -> {
            String current = currentField.getText();
            String newPass = newField.getText();
            String confirm = confirmField.getText();

            String stored = e_user.get(emp.getUsername());

            if (!stored.equals(current)) {
                message.setText("Current password is incorrect.");
                return;
            }

            if (!newPass.equals(confirm)) {
                message.setText("New passwords do not match.");
                return;
            }

            if (newPass.isEmpty()) {
                message.setText("New password cannot be empty.");
                return;
            }

            
            e_user.put(emp.getUsername(), newPass);
            message.setText("Password successfully changed.");
        });

        back.setOnAction(e -> showEmployeeDashboard(stage, emp));

        layout.getChildren().addAll(
            currentLabel, currentField,
            newLabel, newField,
            confirmLabel, confirmField,
            submit, back, message
        );

        Scene changePassScene = new Scene(layout, 400, 300);
        changePassScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setScene(changePassScene);
    }

    private void showLoginScreen(Stage stage) {
        start(stage);
    }

    private void initializeEmployeeUsers() {
        if (e_user.isEmpty()) { 
            Employee e1 = new Employee("Chris", "Ram", "chris", "1234", "Staff");
            e_user.put("chris", "password1234");
            e_map.put("chris", e1);

         
        }
    }
}
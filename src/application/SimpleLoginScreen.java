//AI Generated refactored code to improve readability and maintainability

package application;

import javafx.application.Application;
<<<<<<< Updated upstream
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
=======
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.*;
>>>>>>> Stashed changes
import java.util.*;

public class SimpleLoginScreen extends Application {

    private Stage primaryWindow;
    private Scene loginScene;
    private Scene mainMenuScene;
    private Scene addEmployeeScene;
    private Scene listEmployeeScene;
    private Scene logHoursScene;
    private Scene payStubScene;
    
    private CreateEmployee employeeManager;
    private ListView<String> employeeListView;
    private ViewPayStub payStubViewer;
    
    private Map<String, String> userCredentials;
    private Employee selectedEmployeeForHours;

    @Override
    public void start(Stage primaryStage) {
        this.primaryWindow = primaryStage;
        this.employeeManager = new CreateEmployee();
        this.payStubViewer = new ViewPayStub();
        this.userCredentials = new HashMap<>();
        
        initializeUserCredentials();
        setupLoginScene();
        setupMainMenuScene();
        setupAddEmployeeScene();
        setupListEmployeeScene();
        
        primaryWindow.setScene(loginScene);
        primaryWindow.setTitle("Manager Login");
        primaryWindow.show();
    }

    private void initializeUserCredentials() {
        userCredentials.put("jason", "password");
        userCredentials.put("man", "yo");
    }

    private void setupLoginScene() {
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
<<<<<<< Updated upstream
        Label messageLabel = new Label();

        loginButton.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();
            if (user.containsKey(username) && user.get(username).equals(password)) {
                messageLabel.setText("Login successful!");
                window.setScene(scene2);
            } else {
                messageLabel.setText("Invalid username or password.");
            }
        });

        VBox loginLayout = new VBox(10);
        loginLayout.getChildren().addAll(userLabel, userField, passLabel, passField, loginButton, messageLabel);
        scene1 = new Scene(loginLayout, 400, 300);
        window.setScene(scene1);
        window.setTitle("Manager Login");
        window.show();
=======
        Button employeeLoginButton = new Button("Employee Login");
        Label messageLabel = new Label();

        loginButton.setOnAction(event -> handleLogin(usernameField.getText(), passwordField.getText(), messageLabel));
        employeeLoginButton.setOnAction(event -> openEmployeeLogin());

        VBox loginLayout = new VBox(10);
        loginLayout.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, 
                                       loginButton, employeeLoginButton, messageLabel);
        
        loginScene = new Scene(loginLayout, 400, 300);
        loginScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    }
>>>>>>> Stashed changes

    private void handleLogin(String username, String password, Label messageLabel) {
        if (isValidCredentials(username, password)) {
            messageLabel.setText("Login successful!");
            primaryWindow.setScene(mainMenuScene);
        } else {
            messageLabel.setText("Invalid username or password.");
        }
    }

    private boolean isValidCredentials(String username, String password) {
        return userCredentials.containsKey(username) && userCredentials.get(username).equals(password);
    }

    private void openEmployeeLogin() {
        EmployeeLogin employeeLogin = new EmployeeLogin();
        employeeLogin.setEmployeeManager(employeeManager);
        employeeLogin.start(new Stage());
    }

    private void setupMainMenuScene() {
        Button addEmployeeButton = createAddEmployeeButton();
        Button listEmployeeButton = createListEmployeeButton();
        Button listEmployeeHoursButton = createListEmployeeHoursButton();
        Button importBulkHoursButton = createImportBulkHoursButton();
        Button logHoursButton = createLogHoursButton();
        Button viewPayStubButton = createViewPayStubButton();
        Button startNewWeekButton = createStartNewWeekButton();
        Button viewArchivedDataButton = createViewArchivedDataButton();
        Button saveSystemButton = createSaveSystemButton();
        Button restoreSaveStateButton = createRestoreSaveStateButton();
        Button editEmployeeButton = createEditEmployeeButton();
        Button deleteEmployeeButton = createDeleteEmployeeButton();
        Button logoutButton = createLogoutButton();

        // Create two columns for better organization
        VBox leftColumn = new VBox(10);
        VBox rightColumn = new VBox(10);
        
        // Left column buttons
        leftColumn.getChildren().addAll(
            addEmployeeButton, listEmployeeButton, listEmployeeHoursButton, importBulkHoursButton,
            logHoursButton, viewPayStubButton, startNewWeekButton
        );
        
        // Right column buttons
        rightColumn.getChildren().addAll(
            viewArchivedDataButton, saveSystemButton, restoreSaveStateButton, 
            editEmployeeButton, deleteEmployeeButton
        );
        
        // Create horizontal layout for the two columns
        HBox buttonLayout = new HBox(20);
        buttonLayout.getChildren().addAll(leftColumn, rightColumn);
        
        // Create main layout with logout button at bottom left
        VBox mainMenuLayout = new VBox(20);
        mainMenuLayout.getChildren().addAll(buttonLayout, logoutButton);
        
        mainMenuScene = new Scene(mainMenuLayout, 600, 400);
        mainMenuScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    }

    private Button createAddEmployeeButton() {
        Button button = new Button("Add Employee");
        button.setOnAction(event -> primaryWindow.setScene(addEmployeeScene));
        return button;
    }

    private Button createListEmployeeButton() {
        Button button = new Button("List Employee");
        button.setOnAction(event -> primaryWindow.setScene(listEmployeeScene));
        return button;
    }

    private Button createListEmployeeHoursButton() {
        Button button = new Button("List All Employee Hours");
        button.setOnAction(event -> {
            ListEmployeeHours.setEmployeeList(employeeManager.getEmployees());
            new ListEmployeeHours().start(new Stage());
        });
<<<<<<< Updated upstream

        Button logHoursButton = new Button("Log Hours for Employee");
        logHoursButton.setOnAction(e -> {
            List<Employee> employees = creator.getEmployees();
            if (employees.isEmpty()) {
                showAlert("No Employees", "No employees available to log hours.");
                return;
            }
=======
        return button;
    }
>>>>>>> Stashed changes

    private Button createImportBulkHoursButton() {
        Button button = new Button("Add Bulk Hours");
        button.setOnAction(event -> importBulkHours());
        return button;
    }

    private Button createLogHoursButton() {
        Button button = new Button("Log Hours for Employee");
        button.setOnAction(event -> openLogHoursDialog());
        return button;
    }

    private Button createViewPayStubButton() {
        Button button = new Button("View Employee Pay Stub");
        button.setOnAction(event -> openPayStubDialog());
        return button;
    }

    private Button createStartNewWeekButton() {
        Button button = new Button("Start New Week");
        button.setOnAction(event -> handleStartNewWeek());
        return button;
    }

    private Button createViewArchivedDataButton() {
        Button button = new Button("View Archived Data");
        button.setOnAction(event -> ArchivedDataViewer.viewArchivedFile(primaryWindow));
        return button;
    }

    private Button createSaveSystemButton() {
        Button button = new Button("Save System State");
        button.setOnAction(event -> handleSaveSystemState());
        return button;
    }

<<<<<<< Updated upstream
                submitButton.setOnAction(x -> {
                    int dayIndex = daySelector.getSelectionModel().getSelectedIndex();
                    String hoursText = hoursField.getText();

                    if (dayIndex == -1 || hoursText.isEmpty()) {
                        messageLabel5.setText("Please select a day and enter hours.");
                        return;
                    }

                    if (emp.getAddHours().getHours(dayIndex) > 0 || emp.getAddHours().isPTO(dayIndex)) {
                        messageLabel5.setText("Hours already recorded for this day.");
                        return;
                    }

                    try {
                        double hours = Double.parseDouble(hoursText);

                        if (ptoCheck.isSelected()) {
                            if (hours != 8) {
                                messageLabel5.setText("Only allowed 8 hours of PTO for the day.");
                                return;
                            }
                            boolean ptoSet = emp.getAddHours().setPTO(dayIndex, emp.getPTO());
                            messageLabel5.setText(ptoSet ? "PTO recorded." : "PTO not allowed or already used.");
                        } else {
                            emp.getAddHours().setHours(dayIndex, hours);
                            messageLabel5.setText("Regular hours recorded.");
                        }

                        ptoBalanceLabel.setText("PTO Balance: " + emp.getPTO().getRemainingPTOHours() + " hours");
                    } catch (NumberFormatException ex) {
                        messageLabel5.setText("Invalid number format for hours.");
                    }
                });

                summaryButton.setOnAction(x -> showSummary(emp));
                layout5.getChildren().addAll(titleLabel, daySelector, hoursField, ptoCheck, submitButton, summaryButton, messageLabel5, ptoBalanceLabel, backButton);
                scene5 = new Scene(layout5, 400, 400);
                window.setScene(scene5);
            });
=======
    private Button createRestoreSaveStateButton() {
        Button button = new Button("Restore Save State");
        button.setOnAction(event -> {
            restoreSystemState();
>>>>>>> Stashed changes
        });
        return button;
    }

<<<<<<< Updated upstream
        Button viewPayStubButton = new Button("View Employee Pay Stub");
        viewPayStubButton.setOnAction(e -> {
            VBox layout6 = new VBox(10);
            List<Employee> employees = creator.getEmployees();
            if (employees.isEmpty()) {
                showAlert("No Employees", "No employee pay stubs available.");
                return;
            }

            ChoiceDialog<Employee> dialog = new ChoiceDialog<>(employees.get(0), employees);
            dialog.setTitle("Select Employee");
            dialog.setHeaderText("Choose an employee to log hours for:");
            dialog.setContentText("Employee:");

            dialog.showAndWait().ifPresent(emp -> {
                String[] payStub = viewPayStub.PayStub(emp, emp.getAddHours(), emp.getPTO());
                Label nameLabel = new Label(payStub[0]);
                Label days = new Label(payStub[1]);
                Label mondayHours = new Label(payStub[2]);
                Label saturdayHours = new Label(payStub[3]);
                Label totalHours = new Label(payStub[4]);
                Label pay = new Label(payStub[5]);

                Button saveButton = new Button("Save PayStub");
                saveButton.setOnAction(em2 -> {
                    try (PrintWriter writer = new PrintWriter(emp.getName() + " Pay Stub.txt")) {
                        for (String line : payStub) {
                            writer.println(line);
                            writer.println();
                        }
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                });

                Button backButton = new Button("Back");
                backButton.setOnAction(x -> window.setScene(scene2));
                layout6.getChildren().addAll(nameLabel, days, mondayHours, saturdayHours, totalHours, pay, saveButton, backButton);
            });

            scene6 = new Scene(layout6, 500, 250);
            window.setScene(scene6);
        });

        VBox layout2 = new VBox(10);
        layout2.getChildren().addAll(addEmployeeScene, listEmployeeScene, listEmployeeHoursButton, logHoursButton, viewPayStubButton);
        scene2 = new Scene(layout2, 600, 300);

        Label firstName = new Label("Enter First Name");
        TextField firstNameField = new TextField();
        Label lastName = new Label("Enter Last Name:");
        TextField lastNameField = new TextField();
        ComboBox<String> position = new ComboBox<>();
        position.getItems().addAll("Manager", "Staff");
        position.setValue("Manager");

        Label username = new Label("Create a username");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Create a password");
        PasswordField passwordField = new PasswordField();
        Button addEmployee = new Button("Add Employee");

        addEmployee.setOnAction(e -> {
            String firstname = firstNameField.getText();
            String lastname = lastNameField.getText();
            String positionValue = position.getValue();
            String usrname = usernameField.getText();
            String password = passwordField.getText();

            if (!firstname.isEmpty() && !lastname.isEmpty() && !usrname.isEmpty() && !password.isEmpty()) {
                if (user.containsKey(usrname)) {
                    messageLabel.setText("Username already exists. Choose a different username.");
                } else {
                    user.put(usrname, password);
                    creator.createEmp(firstname, lastname, usrname, password, positionValue);
                    messageLabel.setText("Employee added successfully!");
                    window.setScene(scene2);
                }
            } else {
                messageLabel.setText("Failed to add employee.");
            }

            firstNameField.clear();
            lastNameField.clear();
            usernameField.clear();
            passwordField.clear();
        });

        VBox layout3 = new VBox(10);
        layout3.getChildren().addAll(firstName, firstNameField, lastName, lastNameField, position, username, usernameField, passwordLabel, passwordField, addEmployee);
        scene3 = new Scene(layout3, 600, 400);

        listView = new ListView<>();
        Label titleLabel = new Label("Employee and Manager Viewer");

        Button listEmployeesButton = new Button("List All Employees");
        listEmployeesButton.setOnAction(e -> {
            listView.getItems().clear();
            List<Employee> combined = creator.getEmployees();
            combined.sort(Comparator.comparing(Employee::getLastName).thenComparing(Employee::getFirstName).thenComparing(Employee::getDepartment).thenComparing(Employee::getID));
            for (Employee emp : combined) {
                String display = "Name: " + emp.getFirstName() + " " + emp.getLastName() + (emp instanceof Manager ? " (Manager)" : "") + ", Department: " + emp.getDepartment() + ", ID: " + emp.getID() + ", Username: " + emp.getUsername();
                listView.getItems().add(display);
            }
        });

        Button listManagersButton = new Button("List Managers Only");
        listManagersButton.setOnAction(e -> {
            listView.getItems().clear();
            List<Manager> managers = creator.getManagers();
            managers.sort(Comparator.comparing(Manager::getLastName).thenComparing(Manager::getFirstName).thenComparing(Manager::getDepartment).thenComparing(Manager::getID));
            for (Manager m : managers) {
                String display = "Name: " + m.getFirstName() + " " + m.getLastName() + " (Manager), Department: " + m.getDepartment() + ", ID: " + m.getID() + ", Username: " + m.getUsername();
                listView.getItems().add(display);
            }
        });

        Button listByDepartment = new Button("List By Department");
        listByDepartment.setOnAction(e -> {
            listView.getItems().clear();
            ListEmployeesByDepartment departmentLister = new ListEmployeesByDepartment();
            List<Employee> combined = creator.getEmployees();
            for (Employee emp : combined) departmentLister.newEmployee(emp);
            String[] sortedEmployees = departmentLister.getListEmployeesByDepartment();
            listView.getItems().addAll(sortedEmployees);
        });

        Button back = new Button("Back");
        back.setOnAction(e -> {
            window.setScene(scene2);
            listView.getItems().clear();
        });

        VBox layout4 = new VBox(10);
        layout4.getChildren().addAll(titleLabel, listEmployeesButton, listManagersButton, listByDepartment, listView, back);
        scene4 = new Scene(layout4, 600, 400);
=======
    private Button createEditEmployeeButton() {
        Button button = new Button("Edit Employee");
        button.setOnAction(event -> {
            List<Employee> employees = employeeManager.getEmployees();
            EmployeeManager.showEmployeeSelectionDialog(primaryWindow, employees, "Edit", employeeManager, 
                () -> refreshEmployeeList());
        });
        return button;
    }

    private Button createDeleteEmployeeButton() {
        Button button = new Button("Delete Employee");
        button.setOnAction(event -> {
            List<Employee> employees = employeeManager.getEmployees();
            EmployeeManager.showEmployeeSelectionDialog(primaryWindow, employees, "Delete", employeeManager, 
                () -> refreshEmployeeList());
        });
        return button;
    }



    private Button createLogoutButton() {
        Button button = new Button("Logout");
        button.setOnAction(event -> handleLogout());
        return button;
    }

    private void handleStartNewWeek() {
        List<Employee> employees = employeeManager.getEmployees();
        if (employees.isEmpty()) {
            showAlert("No Employees", "No employees available to start a new week for.");
            return;
        }
        
        boolean isSuccessful = NewWeek.archiveAndReset(employees, primaryWindow);
        if (isSuccessful) {
            showAlert("New Week Started", "All employee hours have been reset and the previous week data has been archived.");
        }
    }

    private void handleSaveSystemState() {
        List<Employee> employees = employeeManager.getEmployees();
        if (employees.isEmpty()) {
            showAlert("No Data", "No employees to save.");
            return;
        }
        SavingSystemData.saveOnDemand(employees);
        showAlert("Save Complete", "System state saved in text format to " + SavingSystemData.getSaveDirectory());
    }

    private void restoreSystemState() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Save State File to Restore");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );
        fileChooser.setInitialDirectory(new File(SavingSystemData.getSaveDirectory()));
        
        File selectedFile = fileChooser.showOpenDialog(primaryWindow);
        if (selectedFile != null) {
            try {
                List<Employee> restoredEmployees = loadEmployeesFromTextFile(selectedFile);
                if (restoredEmployees != null && !restoredEmployees.isEmpty()) {
                    // Clear current employees and load restored ones
                    employeeManager.clearEmployees();
                    for (Employee employee : restoredEmployees) {
                        employeeManager.addEmployee(employee);
                    }
                    showAlert("Restore Complete", "System state restored from " + selectedFile.getName());
                } else {
                    showAlert("Restore Failed", "No valid employee data found in the selected file.");
                }
            } catch (Exception e) {
                showAlert("Restore Error", "Failed to restore system state: " + e.getMessage());
            }
        }
    }

    private List<Employee> loadEmployeesFromTextFile(File file) {
        List<Employee> employees = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                // Skip header line
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                // Parse CSV line
                String[] fields = parseCsvLine(line);
                if (fields.length >= 27) { // Ensure we have all required fields
                    try {
                        // Extract basic employee information
                        int employeeId = Integer.parseInt(fields[0]);
                        String firstName = unescapeCsvField(fields[1]);
                        String lastName = unescapeCsvField(fields[2]);
                        String username = unescapeCsvField(fields[3]);
                        String password = unescapeCsvField(fields[4]); // Note: Password will be empty since we can't get it
                        String department = unescapeCsvField(fields[5]);
                        String position = unescapeCsvField(fields[6]);
                        double payRate = Double.parseDouble(fields[7]);
                        double taxRate = Double.parseDouble(fields[8]);
                        int ptoBalance = Integer.parseInt(fields[9]);
                        
                        // Create employee
                        Employee employee;
                        if ("Manager".equals(position)) {
                            employee = new Manager(firstName, lastName, username, password);
                        } else {
                            employee = new Employee(firstName, lastName, username, password, department);
                        }
                        
                        // Set additional properties
                        employee.setPayRate(payRate);
                        employee.setTaxRate(taxRate);
                        
                        // Restore hours for each day
                        AddHours addHours = employee.getAddHours();
                        
                        // Monday (index 0)
                        double mondayHours = Double.parseDouble(fields[10]);
                        boolean mondayPTO = "PTO".equals(fields[11]);
                        if (mondayHours > 0) {
                            addHours.setHours(0, mondayHours);
                            if (mondayPTO) {
                                addHours.setPTO(0, employee.getPTO());
                            }
                        }
                        
                        // Tuesday (index 1)
                        double tuesdayHours = Double.parseDouble(fields[12]);
                        boolean tuesdayPTO = "PTO".equals(fields[13]);
                        if (tuesdayHours > 0) {
                            addHours.setHours(1, tuesdayHours);
                            if (tuesdayPTO) {
                                addHours.setPTO(1, employee.getPTO());
                            }
                        }
                        
                        // Wednesday (index 2)
                        double wednesdayHours = Double.parseDouble(fields[14]);
                        boolean wednesdayPTO = "PTO".equals(fields[15]);
                        if (wednesdayHours > 0) {
                            addHours.setHours(2, wednesdayHours);
                            if (wednesdayPTO) {
                                addHours.setPTO(2, employee.getPTO());
                            }
                        }
                        
                        // Thursday (index 3)
                        double thursdayHours = Double.parseDouble(fields[16]);
                        boolean thursdayPTO = "PTO".equals(fields[17]);
                        if (thursdayHours > 0) {
                            addHours.setHours(3, thursdayHours);
                            if (thursdayPTO) {
                                addHours.setPTO(3, employee.getPTO());
                            }
                        }
                        
                        // Friday (index 4)
                        double fridayHours = Double.parseDouble(fields[18]);
                        boolean fridayPTO = "PTO".equals(fields[19]);
                        if (fridayHours > 0) {
                            addHours.setHours(4, fridayHours);
                            if (fridayPTO) {
                                addHours.setPTO(4, employee.getPTO());
                            }
                        }
                        
                        // Saturday (index 5)
                        double saturdayHours = Double.parseDouble(fields[20]);
                        boolean saturdayPTO = "PTO".equals(fields[21]);
                        if (saturdayHours > 0) {
                            addHours.setHours(5, saturdayHours);
                            if (saturdayPTO) {
                                addHours.setPTO(5, employee.getPTO());
                            }
                        }
                        
                        // Sunday (index 6)
                        double sundayHours = Double.parseDouble(fields[22]);
                        boolean sundayPTO = "PTO".equals(fields[23]);
                        if (sundayHours > 0) {
                            addHours.setHours(6, sundayHours);
                            if (sundayPTO) {
                                addHours.setPTO(6, employee.getPTO());
                            }
                        }
                        
                        employees.add(employee);
                        
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing employee data: " + e.getMessage());
                        continue;
                    }
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        
        return employees;
    }
    
    private String[] parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    // Escaped quote
                    currentField.append('"');
                    i++; // Skip the next quote
                } else {
                    // Toggle quote state
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                // End of field
                fields.add(currentField.toString());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }
        
        // Add the last field
        fields.add(currentField.toString());
        
        return fields.toArray(new String[0]);
    }
    
    private String unescapeCsvField(String field) {
        if (field == null || field.isEmpty()) return "";
        
        // Remove surrounding quotes if present
        if (field.startsWith("\"") && field.endsWith("\"")) {
            field = field.substring(1, field.length() - 1);
        }
        
        // Unescape double quotes
        return field.replace("\"\"", "\"");
    }

    private void handleLogout() {
        List<Employee> employees = employeeManager.getEmployees();
        if (!employees.isEmpty()) {
            SavingSystemData.saveOnLogout(employees);
        }
        primaryWindow.setScene(loginScene);
    }

    private void openLogHoursDialog() {
        List<Employee> employees = employeeManager.getEmployees();
        if (employees.isEmpty()) {
            showAlert("No Employees", "No employees available to log hours.");
            return;
        }

        ChoiceDialog<Employee> employeeDialog = new ChoiceDialog<>(employees.get(0), employees);
        employeeDialog.setTitle("Select Employee");
        employeeDialog.setHeaderText("Choose an employee to log hours for:");
        employeeDialog.setContentText("Employee:");

        employeeDialog.showAndWait().ifPresent(employee -> {
            selectedEmployeeForHours = employee;
            setupLogHoursScene();
            primaryWindow.setScene(logHoursScene);
        });
    }

    private void setupLogHoursScene() {
        VBox logHoursLayout = new VBox(10);
        Label titleLabel = new Label("Add Hours for " + selectedEmployeeForHours.getName());

        ComboBox<String> daySelector = new ComboBox<>();
        daySelector.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");

        TextField hoursField = new TextField();
        hoursField.setPromptText("Enter number of hours");

        CheckBox ptoCheckBox = new CheckBox("Is this PTO?");
        Button submitButton = new Button("Submit Hours");
        Label ptoBalanceLabel = new Label("PTO Balance: " + selectedEmployeeForHours.getPTO().getRemainingPTOHours() + " hours");
        Label messageLabel = new Label();

        Button summaryButton = new Button("Show Weekly Summary");
        Button backButton = new Button("Back");

        submitButton.setOnAction(event -> handleSubmitHours(daySelector, hoursField, ptoCheckBox, messageLabel, ptoBalanceLabel));
        summaryButton.setOnAction(event -> showWeeklySummary(selectedEmployeeForHours));
        backButton.setOnAction(event -> primaryWindow.setScene(mainMenuScene));

        logHoursLayout.getChildren().addAll(titleLabel, daySelector, hoursField, ptoCheckBox, submitButton, 
                                          summaryButton, messageLabel, ptoBalanceLabel, backButton);
        logHoursScene = new Scene(logHoursLayout, 400, 400);
    }

    private void handleSubmitHours(ComboBox<String> daySelector, TextField hoursField, CheckBox ptoCheckBox, 
                                 Label messageLabel, Label ptoBalanceLabel) {
        int selectedDayIndex = daySelector.getSelectionModel().getSelectedIndex();
        String hoursText = hoursField.getText();

        if (selectedDayIndex == -1 || hoursText.isEmpty()) {
            messageLabel.setText("Please select a day and enter hours.");
            return;
        }

        try {
            double hours = Double.parseDouble(hoursText);
            boolean isPtoRequested = ptoCheckBox.isSelected();

            if (isPtoRequested) {
                handlePtoSubmission(selectedDayIndex, hours, messageLabel, ptoBalanceLabel);
            } else {
                handleRegularHoursSubmission(selectedDayIndex, hours, messageLabel, ptoBalanceLabel);
            }
        } catch (NumberFormatException exception) {
            messageLabel.setText("Invalid number format for hours.");
        }
    }

    private void handlePtoSubmission(int dayIndex, double hours, Label messageLabel, Label ptoBalanceLabel) {
        if (hours != 8) {
            messageLabel.setText("Only allowed 8 hours of PTO for the day.");
            return;
        }
        
        boolean isPtoSet = selectedEmployeeForHours.getAddHours().setPTO(dayIndex, selectedEmployeeForHours.getPTO());
        messageLabel.setText(isPtoSet ? "PTO recorded." : "PTO not allowed or already used.");
        updatePtoBalanceLabel(ptoBalanceLabel);
    }

    private void handleRegularHoursSubmission(int dayIndex, double hours, Label messageLabel, Label ptoBalanceLabel) {
        selectedEmployeeForHours.getAddHours().setHours(dayIndex, hours);
        messageLabel.setText("Regular hours recorded.");
        updatePtoBalanceLabel(ptoBalanceLabel);
    }

    private void updatePtoBalanceLabel(Label ptoBalanceLabel) {
        ptoBalanceLabel.setText("PTO Balance: " + selectedEmployeeForHours.getPTO().getRemainingPTOHours() + " hours");
    }



    private void showWeeklySummary(Employee employee) {
        String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        StringBuilder summaryBuilder = new StringBuilder();
        
        for (int dayIndex = 0; dayIndex < 7; dayIndex++) {
            double hours = employee.getAddHours().getHours(dayIndex);
            if (hours > 0) {
                summaryBuilder.append(daysOfWeek[dayIndex]).append(": ").append(hours).append(" hours");
                if (employee.getAddHours().isPTO(dayIndex)) {
                    summaryBuilder.append(" (PTO)");
                }
                summaryBuilder.append("\n");
            }
        }
        summaryBuilder.append("Total: ").append(employee.getAddHours().getTotalHours()).append(" hours");
        
        Alert summaryAlert = new Alert(Alert.AlertType.INFORMATION);
        summaryAlert.setTitle("Weekly Summary");
        summaryAlert.setHeaderText("Hours Logged for " + employee.getName());
        summaryAlert.setContentText(summaryBuilder.toString());
        summaryAlert.showAndWait();
    }

    private void openPayStubDialog() {
        VBox payStubLayout = new VBox(10);
        List<Employee> employees = employeeManager.getEmployees();
        
        if (employees.isEmpty()) {
            showAlert("No Employees", "No employee pay stubs available.");
            return;
        }

        ChoiceDialog<Employee> employeeDialog = new ChoiceDialog<>(employees.get(0), employees);
        employeeDialog.setTitle("Select Employee");
        employeeDialog.setHeaderText("Choose an employee to view pay stub for:");
        employeeDialog.setContentText("Employee:");

        employeeDialog.showAndWait().ifPresent(employee -> {
            String[] payStubData = payStubViewer.PayStub(employee, employee.getAddHours(), employee.getPTO());
            setupPayStubScene(payStubLayout, payStubData, employee);
        });

        payStubScene = new Scene(payStubLayout, 500, 250);
        payStubScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryWindow.setScene(payStubScene);
    }

    private void setupPayStubScene(VBox payStubLayout, String[] payStubData, Employee employee) {
        Label nameLabel = new Label(payStubData[0]);
        Label daysLabel = new Label(payStubData[1]);
        Label mondayHoursLabel = new Label(payStubData[2]);
        Label saturdayHoursLabel = new Label(payStubData[3]);
        Label totalHoursLabel = new Label(payStubData[4]);
        Label payLabel = new Label(payStubData[5]);

        Button saveButton = new Button("Save PayStub");
        saveButton.setOnAction(event -> savePayStubToFile(employee, payStubData));

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> primaryWindow.setScene(mainMenuScene));
        
        payStubLayout.getChildren().addAll(nameLabel, daysLabel, mondayHoursLabel, saturdayHoursLabel, 
                                          totalHoursLabel, payLabel, saveButton, backButton);
    }

    private void savePayStubToFile(Employee employee, String[] payStubData) {
        try (PrintWriter writer = new PrintWriter(employee.getName() + " Pay Stub.txt")) {
            for (String line : payStubData) {
                writer.println(line);
                writer.println();
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void setupAddEmployeeScene() {
        Label firstNameLabel = new Label("Enter First Name");
        TextField firstNameField = new TextField();
        Label lastNameLabel = new Label("Enter Last Name:");
        TextField lastNameField = new TextField();
        ComboBox<String> positionComboBox = new ComboBox<>();
        positionComboBox.getItems().addAll("Manager", "Staff");
        positionComboBox.setValue("Manager");

        Label usernameLabel = new Label("Create a username");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Create a password");
        PasswordField passwordField = new PasswordField();
        Button addEmployeeButton = new Button("Add Employee");
        Label messageLabel = new Label();

        addEmployeeButton.setOnAction(event -> handleAddEmployee(firstNameField, lastNameField, positionComboBox, 
                                                              usernameField, passwordField, messageLabel));

        VBox addEmployeeLayout = new VBox(10);
        addEmployeeLayout.getChildren().addAll(firstNameLabel, firstNameField, lastNameLabel, lastNameField, 
                                             positionComboBox, usernameLabel, usernameField, passwordLabel, 
                                             passwordField, addEmployeeButton, messageLabel);
        
        addEmployeeScene = new Scene(addEmployeeLayout, 600, 400);
        addEmployeeScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    }

    private void handleAddEmployee(TextField firstNameField, TextField lastNameField, ComboBox<String> positionComboBox,
                                 TextField usernameField, PasswordField passwordField, Label messageLabel) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String position = positionComboBox.getValue();
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (isValidEmployeeData(firstName, lastName, username, password)) {
            if (userCredentials.containsKey(username)) {
                messageLabel.setText("Username already exists. Choose a different username.");
            } else {
                createNewEmployee(firstName, lastName, username, password, position, messageLabel);
                clearEmployeeForm(firstNameField, lastNameField, usernameField, passwordField);
            }
        } else {
            messageLabel.setText("Failed to add employee.");
        }
    }

    private boolean isValidEmployeeData(String firstName, String lastName, String username, String password) {
        return !firstName.isEmpty() && !lastName.isEmpty() && !username.isEmpty() && !password.isEmpty();
    }

    private void createNewEmployee(String firstName, String lastName, String username, String password, 
                                 String position, Label messageLabel) {
        userCredentials.put(username, password);
        employeeManager.createEmp(firstName, lastName, username, password, position);
        messageLabel.setText("Employee added successfully!");
        primaryWindow.setScene(mainMenuScene);
    }

    private void clearEmployeeForm(TextField firstNameField, TextField lastNameField, 
                                 TextField usernameField, PasswordField passwordField) {
        firstNameField.clear();
        lastNameField.clear();
        usernameField.clear();
        passwordField.clear();
    }

    private void setupListEmployeeScene() {
        employeeListView = new ListView<>();
        Label titleLabel = new Label("Employee and Manager Viewer");

        Button listAllEmployeesButton = new Button("List All Employees");
        listAllEmployeesButton.setOnAction(event -> displayAllEmployees());

        Button listManagersButton = new Button("List Managers Only");
        listManagersButton.setOnAction(event -> displayManagersOnly());

        Button listByDepartmentButton = new Button("List By Department");
        listByDepartmentButton.setOnAction(event -> displayByDepartment());

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> {
            primaryWindow.setScene(mainMenuScene);
            employeeListView.getItems().clear();
        });

        VBox listEmployeeLayout = new VBox(10);
        listEmployeeLayout.getChildren().addAll(titleLabel, listAllEmployeesButton, listManagersButton, 
                                              listByDepartmentButton, employeeListView, backButton);
        
        listEmployeeScene = new Scene(listEmployeeLayout, 600, 400);
        listEmployeeScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    }

    private void displayAllEmployees() {
        employeeListView.getItems().clear();
        List<Employee> allEmployees = employeeManager.getEmployees();
        allEmployees.sort(Comparator.comparing(Employee::getLastName)
                                  .thenComparing(Employee::getFirstName)
                                  .thenComparing(Employee::getDepartment)
                                  .thenComparing(Employee::getID));
        
        for (Employee employee : allEmployees) {
            String displayText = formatEmployeeDisplay(employee);
            employeeListView.getItems().add(displayText);
        }
    }

    private void displayManagersOnly() {
        employeeListView.getItems().clear();
        List<Manager> managers = employeeManager.getManagers();
        managers.sort(Comparator.comparing(Manager::getLastName)
                               .thenComparing(Manager::getFirstName)
                               .thenComparing(Manager::getDepartment)
                               .thenComparing(Manager::getID));
        
        for (Manager manager : managers) {
            String displayText = formatManagerDisplay(manager);
            employeeListView.getItems().add(displayText);
        }
    }

    private void displayByDepartment() {
        employeeListView.getItems().clear();
        ListEmployeesByDepartment departmentLister = new ListEmployeesByDepartment();
        List<Employee> allEmployees = employeeManager.getEmployees();
        
        for (Employee employee : allEmployees) {
            departmentLister.newEmployee(employee);
        }
        
        String[] sortedEmployees = departmentLister.getListEmployeesByDepartment();
        employeeListView.getItems().addAll(sortedEmployees);
    }

    private String formatEmployeeDisplay(Employee employee) {
        String managerIndicator = employee instanceof Manager ? " (Manager)" : "";
        return String.format("Name: %s %s%s, Department: %s, ID: %d, Username: %s",
                           employee.getFirstName(), employee.getLastName(), managerIndicator,
                           employee.getDepartment(), employee.getID(), employee.getUsername());
    }

    private String formatManagerDisplay(Manager manager) {
        return String.format("Name: %s %s (Manager), Department: %s, ID: %d, Username: %s",
                           manager.getFirstName(), manager.getLastName(),
                           manager.getDepartment(), manager.getID(), manager.getUsername());
    }

    private void importBulkHours() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV File");
        File selectedFile = fileChooser.showOpenDialog(primaryWindow);

        if (selectedFile != null) {
            processBulkHoursFile(selectedFile);
        }
    }

    private void processBulkHoursFile(File file) {
        int successfulImports = 0;
        int failedImports = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            List<Employee> allEmployees = employeeManager.getEmployees();
            Map<Integer, Employee> employeeMap = createEmployeeMap(allEmployees);
            String line;

            while ((line = reader.readLine()) != null) {
                if (processBulkHoursLine(line, employeeMap)) {
                    successfulImports++;
                } else {
                    failedImports++;
                }
            }

            showAlert("Import Complete", "Successfully logged hours: " + successfulImports + "\nFailed entries: " + failedImports);

        } catch (IOException exception) {
            showAlert("Error", "Failed to read file: " + exception.getMessage());
        }
    }

    private Map<Integer, Employee> createEmployeeMap(List<Employee> employees) {
        Map<Integer, Employee> employeeMap = new HashMap<>();
        for (Employee employee : employees) {
            employeeMap.put(employee.getID(), employee);
        }
        return employeeMap;
    }

    private boolean processBulkHoursLine(String line, Map<Integer, Employee> employeeMap) {
        String[] tokens = line.split(",");
        if (tokens.length < 4) {
            return false;
        }

        try {
            int employeeId = Integer.parseInt(tokens[0].trim());
            String dayName = tokens[1].trim();
            double hours = Double.parseDouble(tokens[2].trim());
            boolean isPto = Boolean.parseBoolean(tokens[3].trim());

            Employee employee = employeeMap.get(employeeId);
            if (employee == null) {
                return false;
            }

            int dayIndex = getDayIndex(dayName);
            if (dayIndex == -1) {
                return false;
            }

            if (employee.getAddHours().getHours(dayIndex) > 0 || employee.getAddHours().isPTO(dayIndex)) {
                return false;
            }

            return processBulkHoursEntry(employee, dayIndex, hours, isPto);

        } catch (Exception exception) {
            return false;
        }
    }

    private boolean processBulkHoursEntry(Employee employee, int dayIndex, double hours, boolean isPto) {
        if (isPto) {
            if (hours != 8) {
                return false;
            }
            return employee.getAddHours().setPTO(dayIndex, employee.getPTO());
        } else {
            employee.getAddHours().setHours(dayIndex, hours);
            return true;
        }
    }

    private int getDayIndex(String dayName) {
        switch (dayName.toLowerCase()) {
            case "monday": return 0;
            case "tuesday": return 1;
            case "wednesday": return 2;
            case "thursday": return 3;
            case "friday": return 4;
            case "saturday": return 5;
            case "sunday": return 6;
            default: return -1;
        }
>>>>>>> Stashed changes
    }

    private void refreshEmployeeList() {
        // Refresh the employee list if it's currently displayed
        if (employeeListView != null && !employeeListView.getItems().isEmpty()) {
            displayAllEmployees();
        }
    }

    private void showAlert(String title, String content) {
<<<<<<< Updated upstream
        Alert alert = new Alert(Alert.AlertType.WARNING);
=======
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
>>>>>>> Stashed changes
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import java.io.*;
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
        return button;
    }

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
        button.setOnAction(event -> {
            ArchivedDataViewer.viewArchivedFile(primaryWindow);
        });
        return button;
    }

    private Button createSaveSystemButton() {
        Button button = new Button("Save System State");
        button.setOnAction(event -> handleSaveSystemState());
        return button;
    }

    private Button createRestoreSaveStateButton() {
        Button button = new Button("Restore Save State");
        button.setOnAction(event -> {
            restoreSystemState();
        });
        return button;
    }

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
                                          messageLabel, ptoBalanceLabel, summaryButton, backButton);
        
        logHoursScene = new Scene(logHoursLayout, 400, 400);
        logHoursScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    }

    private void handleSubmitHours(ComboBox<String> daySelector, TextField hoursField, CheckBox ptoCheckBox, 
                                 Label messageLabel, Label ptoBalanceLabel) {
        int dayIndex = daySelector.getSelectionModel().getSelectedIndex();
        String hoursText = hoursField.getText();

        if (dayIndex == -1 || hoursText.isEmpty()) {
            messageLabel.setText("Please select a day and enter hours.");
            return;
        }

        if (selectedEmployeeForHours.getAddHours().getHours(dayIndex) > 0 || selectedEmployeeForHours.getAddHours().isPTO(dayIndex)) {
            messageLabel.setText("Hours already logged for this day.");
            return;
        }

        try {
            double hours = Double.parseDouble(hoursText);
            if (ptoCheckBox.isSelected()) {
                handlePtoSubmission(dayIndex, hours, messageLabel, ptoBalanceLabel);
            } else {
                handleRegularHoursSubmission(dayIndex, hours, messageLabel, ptoBalanceLabel);
            }
        } catch (NumberFormatException ex) {
            messageLabel.setText("Invalid number format.");
        }
    }

    private void handlePtoSubmission(int dayIndex, double hours, Label messageLabel, Label ptoBalanceLabel) {
        if (hours != 8) {
            messageLabel.setText("PTO must be exactly 8 hours.");
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Weekly Summary");
        alert.setHeaderText("Hours Summary for " + employee.getName());
        
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        StringBuilder summary = new StringBuilder();
        
        for (int i = 0; i < 7; i++) {
            double hours = employee.getAddHours().getHours(i);
            if (hours > 0) {
                summary.append(days[i]).append(": ").append(hours).append(" hours");
                if (employee.getAddHours().isPTO(i)) {
                    summary.append(" (PTO)");
                }
                summary.append("\n");
            }
        }
        
        summary.append("\nTotal Hours: ").append(employee.getAddHours().getTotalHours());
        alert.setContentText(summary.toString());
        alert.showAndWait();
    }

    private void openPayStubDialog() {
        List<Employee> employees = employeeManager.getEmployees();
        if (employees.isEmpty()) {
            showAlert("No Employees", "No employees available to view pay stubs.");
            return;
        }

        ChoiceDialog<Employee> employeeDialog = new ChoiceDialog<>(employees.get(0), employees);
        employeeDialog.setTitle("Select Employee");
        employeeDialog.setHeaderText("Choose an employee to view pay stub:");
        employeeDialog.setContentText("Employee:");

        employeeDialog.showAndWait().ifPresent(employee -> {
            String[] payStubData = payStubViewer.generatePayStub(employee);
            setupPayStubScene(payStubViewer.createPayStubLayout(payStubData, employee), payStubData, employee);
            primaryWindow.setScene(payStubScene);
        });
    }

    private void setupPayStubScene(VBox payStubLayout, String[] payStubData, Employee employee) {
        Button saveButton = new Button("Save Pay Stub");
        Button backButton = new Button("Back");
        
        saveButton.setOnAction(event -> savePayStubToFile(employee, payStubData));
        backButton.setOnAction(event -> primaryWindow.setScene(mainMenuScene));
        
        payStubLayout.getChildren().addAll(saveButton, backButton);
        payStubScene = new Scene(payStubLayout, 500, 600);
        payStubScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    }

    private void savePayStubToFile(Employee employee, String[] payStubData) {
        try {
            String filename = "PayStub_" + employee.getName().replace(" ", "_") + "_" + 
                            java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd")) + ".txt";
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
                for (String line : payStubData) {
                    writer.println(line);
                }
            }
            
            showAlert("Success", "Pay stub saved to " + filename);
        } catch (IOException e) {
            showAlert("Error", "Failed to save pay stub: " + e.getMessage());
        }
    }

    private void setupAddEmployeeScene() {
        Label titleLabel = new Label("Add New Employee");
        
        Label firstNameLabel = new Label("First Name:");
        TextField firstNameField = new TextField();
        
        Label lastNameLabel = new Label("Last Name:");
        TextField lastNameField = new TextField();
        
        Label positionLabel = new Label("Position:");
        ComboBox<String> positionComboBox = new ComboBox<>();
        positionComboBox.getItems().addAll("Staff", "Manager");
        positionComboBox.setValue("Staff");
        
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        
        Label messageLabel = new Label();
        
        Button addButton = new Button("Add Employee");
        Button backButton = new Button("Back");
        
        addButton.setOnAction(event -> handleAddEmployee(firstNameField, lastNameField, positionComboBox, 
                                                      usernameField, passwordField, messageLabel));
        backButton.setOnAction(event -> primaryWindow.setScene(mainMenuScene));
        
        VBox addEmployeeLayout = new VBox(10);
        addEmployeeLayout.getChildren().addAll(titleLabel, firstNameLabel, firstNameField, lastNameLabel, lastNameField,
                                             positionLabel, positionComboBox, usernameLabel, usernameField, 
                                             passwordLabel, passwordField, addButton, messageLabel, backButton);
        
        addEmployeeScene = new Scene(addEmployeeLayout, 400, 500);
        addEmployeeScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    }

    private void handleAddEmployee(TextField firstNameField, TextField lastNameField, ComboBox<String> positionComboBox,
                                 TextField usernameField, PasswordField passwordField, Label messageLabel) {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String position = positionComboBox.getValue();

        if (!isValidEmployeeData(firstName, lastName, username, password)) {
            messageLabel.setText("Please fill in all fields.");
            return;
        }

        createNewEmployee(firstName, lastName, username, password, position, messageLabel);
        clearEmployeeForm(firstNameField, lastNameField, usernameField, passwordField);
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
        fileChooser.setTitle("Select CSV File with Bulk Hours");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        
        File selectedFile = fileChooser.showOpenDialog(primaryWindow);
        if (selectedFile != null) {
            processBulkHoursFile(selectedFile);
        }
    }

    private void processBulkHoursFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            int processedCount = 0;
            int errorCount = 0;
            
            Map<Integer, Employee> employeeMap = createEmployeeMap(employeeManager.getEmployees());
            
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }
                
                if (processBulkHoursLine(line, employeeMap)) {
                    processedCount++;
                } else {
                    errorCount++;
                }
            }
            
            showAlert("Bulk Import Complete", 
                     String.format("Processed: %d entries\nErrors: %d entries", processedCount, errorCount));
            
        } catch (IOException e) {
            showAlert("Error", "Failed to read file: " + e.getMessage());
        }
    }

    private Map<Integer, Employee> createEmployeeMap(List<Employee> employees) {
        Map<Integer, Employee> map = new HashMap<>();
        for (Employee emp : employees) {
            map.put(emp.getID(), emp);
        }
        return map;
    }

    private boolean processBulkHoursLine(String line, Map<Integer, Employee> employeeMap) {
        try {
            String[] fields = line.split(",");
            if (fields.length < 4) return false;
            
            int employeeId = Integer.parseInt(fields[0].trim());
            String dayName = fields[1].trim();
            double hours = Double.parseDouble(fields[2].trim());
            boolean isPto = fields[3].trim().equalsIgnoreCase("PTO");
            
            Employee employee = employeeMap.get(employeeId);
            if (employee == null) return false;
            
            int dayIndex = getDayIndex(dayName);
            if (dayIndex == -1) return false;
            
            return processBulkHoursEntry(employee, dayIndex, hours, isPto);
            
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean processBulkHoursEntry(Employee employee, int dayIndex, double hours, boolean isPto) {
        if (employee.getAddHours().getHours(dayIndex) > 0 || employee.getAddHours().isPTO(dayIndex)) {
            return false; // Already has hours for this day
        }
        
        if (isPto) {
            if (hours != 8) return false; // PTO must be exactly 8 hours
            return employee.getAddHours().setPTO(dayIndex, employee.getPTO());
        } else {
            employee.getAddHours().setHours(dayIndex, hours);
            return true;
        }
    }

    private int getDayIndex(String dayName) {
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        for (int i = 0; i < days.length; i++) {
            if (days[i].equalsIgnoreCase(dayName)) {
                return i;
            }
        }
        return -1;
    }

    private void refreshEmployeeList() {
        // This method can be called to refresh the employee list after edits/deletes
        // For now, it's a placeholder for future functionality
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
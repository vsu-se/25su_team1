package application;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EmployeeManager {
    
    private static final String ARCHIVE_DIRECTORY = "deleted_employees/";
    
    static {
        // Create archive directory if it doesn't exist
        File directory = new File(ARCHIVE_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
    
    public static void showEditEmployeeDialog(Stage parentStage, Employee employee, CreateEmployee employeeManager, Runnable onEmployeeUpdated) {
        Stage editStage = new Stage();
        editStage.setTitle("Edit Employee - " + employee.getName());
        
        VBox layout = new VBox(10);
        
        // Create form fields
        TextField firstNameField = new TextField(employee.getFirstName());
        TextField lastNameField = new TextField(employee.getLastName());
        TextField usernameField = new TextField(employee.getUsername());
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter new password (leave blank to keep current)");
        
        ComboBox<String> departmentComboBox = new ComboBox<>();
        departmentComboBox.getItems().addAll("IT", "HR", "Finance", "Marketing", "Sales", "Operations", "Manager");
        departmentComboBox.setValue(employee.getDepartment());
        
        TextField payRateField = new TextField(String.valueOf(employee.getPayRate()));
        TextField taxRateField = new TextField(String.valueOf(employee.getTaxRate()));
        
        // Create labels
        Label firstNameLabel = new Label("First Name:");
        Label lastNameLabel = new Label("Last Name:");
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        Label departmentLabel = new Label("Department:");
        Label payRateLabel = new Label("Pay Rate ($/hour):");
        Label taxRateLabel = new Label("Tax Rate (%):");
        
        // Create buttons
        Button saveButton = new Button("Save Changes");
        Button cancelButton = new Button("Cancel");
        
        // Form layout
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        
        formGrid.add(firstNameLabel, 0, 0);
        formGrid.add(firstNameField, 1, 0);
        formGrid.add(lastNameLabel, 0, 1);
        formGrid.add(lastNameField, 1, 1);
        formGrid.add(usernameLabel, 0, 2);
        formGrid.add(usernameField, 1, 2);
        formGrid.add(passwordLabel, 0, 3);
        formGrid.add(passwordField, 1, 3);
        formGrid.add(departmentLabel, 0, 4);
        formGrid.add(departmentComboBox, 1, 4);
        formGrid.add(payRateLabel, 0, 5);
        formGrid.add(payRateField, 1, 5);
        formGrid.add(taxRateLabel, 0, 6);
        formGrid.add(taxRateField, 1, 6);
        
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(saveButton, cancelButton);
        
        layout.getChildren().addAll(formGrid, buttonBox);
        
        // Save button action
        saveButton.setOnAction(event -> {
            if (validateEditForm(firstNameField, lastNameField, usernameField, departmentComboBox, payRateField, taxRateField)) {
                updateEmployee(employee, firstNameField.getText(), lastNameField.getText(), 
                             usernameField.getText(), passwordField.getText(), 
                             departmentComboBox.getValue(), payRateField.getText(), taxRateField.getText());
                
                showAlert("Success", "Employee updated successfully!", Alert.AlertType.INFORMATION);
                editStage.close();
                if (onEmployeeUpdated != null) {
                    onEmployeeUpdated.run();
                }
            }
        });
        
        // Cancel button action
        cancelButton.setOnAction(event -> editStage.close());
        
        Scene scene = new Scene(layout, 400, 500);
        editStage.setScene(scene);
        editStage.show();
    }
    
    private static boolean validateEditForm(TextField firstNameField, TextField lastNameField, 
                                         TextField usernameField, ComboBox<String> departmentComboBox,
                                         TextField payRateField, TextField taxRateField) {
        
        if (firstNameField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "First name cannot be empty.", Alert.AlertType.ERROR);
            return false;
        }
        
        if (lastNameField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Last name cannot be empty.", Alert.AlertType.ERROR);
            return false;
        }
        
        if (usernameField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Username cannot be empty.", Alert.AlertType.ERROR);
            return false;
        }
        
        if (departmentComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select a department.", Alert.AlertType.ERROR);
            return false;
        }
        
        try {
            double payRate = Double.parseDouble(payRateField.getText());
            if (payRate < 0) {
                showAlert("Validation Error", "Pay rate cannot be negative.", Alert.AlertType.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Pay rate must be a valid number.", Alert.AlertType.ERROR);
            return false;
        }
        
        try {
            double taxRate = Double.parseDouble(taxRateField.getText());
            if (taxRate < 0 || taxRate > 100) {
                showAlert("Validation Error", "Tax rate must be between 0 and 100.", Alert.AlertType.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Tax rate must be a valid number.", Alert.AlertType.ERROR);
            return false;
        }
        
        return true;
    }
    
    private static void updateEmployee(Employee employee, String firstName, String lastName, 
                                    String username, String password, String department, 
                                    String payRateStr, String taxRateStr) {
        
        // Update basic information
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setUsername(username);
        employee.setDepartment(department);
        
        // Update password if provided
        if (!password.trim().isEmpty()) {
            employee.setPassword(password);
        }
        
        // Update pay rate and tax rate
        double payRate = Double.parseDouble(payRateStr);
        double taxRate = Double.parseDouble(taxRateStr);
        employee.setPayRate(payRate);
        employee.setTaxRate(taxRate);
    }
    
    public static void showDeleteEmployeeDialog(Stage parentStage, Employee employee, 
                                             CreateEmployee employeeManager, List<Employee> allEmployees,
                                             Runnable onEmployeeDeleted) {
        
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Delete Employee");
        confirmDialog.setHeaderText("Confirm Deletion");
        confirmDialog.setContentText("Are you sure you want to delete " + employee.getName() + "?\n\n" +
                                   "This action will:\n" +
                                   "• Remove the employee from the current system\n" +
                                   "• Archive all their data for future reference\n" +
                                   "• This action cannot be undone");
        
        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                archiveEmployeeData(employee);
                removeEmployeeFromSystem(employee, employeeManager, allEmployees);
                
                showAlert("Success", "Employee " + employee.getName() + " has been deleted.\n" +
                          "All data has been archived to: " + ARCHIVE_DIRECTORY, Alert.AlertType.INFORMATION);
                
                if (onEmployeeDeleted != null) {
                    onEmployeeDeleted.run();
                }
            }
        });
    }
    
    private static void archiveEmployeeData(Employee employee) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String filename = ARCHIVE_DIRECTORY + "deleted_employee_" + employee.getID() + "_" + timestamp + ".txt";
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("DELETED EMPLOYEE ARCHIVE - " + timestamp);
            writer.println("=====================================");
            writer.println("Employee ID: " + employee.getID());
            writer.println("Name: " + employee.getName());
            writer.println("Username: " + employee.getUsername());
            writer.println("Department: " + employee.getDepartment());
            writer.println("Position: " + (employee instanceof Manager ? "Manager" : "Staff"));
            writer.println("Pay Rate: $" + employee.getPayRate() + "/hour");
            writer.println("Tax Rate: " + employee.getTaxRate() + "%");
            writer.println("PTO Remaining: " + employee.getPTO().getRemainingPTOHours() + " hours");
            writer.println();
            writer.println("Weekly Hours at Time of Deletion:");
            
            String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
            for (int i = 0; i < 7; i++) {
                double hours = employee.getAddHours().getHours(i);
                if (hours > 0) {
                    writer.println("  " + days[i] + ": " + hours + " hours" +
                                 (employee.getAddHours().isPTO(i) ? " (PTO)" : ""));
                }
            }
            
            writer.println();
            writer.println("Summary:");
            writer.println("  Total Hours: " + employee.getAddHours().getTotalHours());
            writer.println("  Weekday Hours: " + employee.getAddHours().getWeekdayHours());
            writer.println("  Weekend Hours: " + employee.getAddHours().getWeekendHours());
            writer.println("  Days Worked: " + employee.getAddHours().getNumDaysWorked());
            writer.println();
            writer.println("Deletion Date: " + timestamp);
            writer.println("Reason: Employee deleted from system");
            
        } catch (IOException e) {
            System.err.println("Error archiving employee data: " + e.getMessage());
        }
    }
    
    private static void removeEmployeeFromSystem(Employee employee, CreateEmployee employeeManager, 
                                               List<Employee> allEmployees) {
        // Remove from the employee manager's list
        allEmployees.remove(employee);
        
        // Note: The actual removal from CreateEmployee would need to be implemented
        // in the CreateEmployee class. This is a placeholder for the concept.
        System.out.println("Employee " + employee.getName() + " removed from system.");
    }
    
    public static void showEmployeeSelectionDialog(Stage parentStage, List<Employee> employees, 
                                                String action, CreateEmployee employeeManager,
                                                Runnable onActionCompleted) {
        
        if (employees.isEmpty()) {
            showAlert("No Employees", "No employees available for " + action.toLowerCase() + ".", Alert.AlertType.INFORMATION);
            return;
        }
        
        ChoiceDialog<Employee> dialog = new ChoiceDialog<>(employees.get(0), employees);
        dialog.setTitle("Select Employee");
        dialog.setHeaderText("Choose an employee to " + action.toLowerCase() + ":");
        dialog.setContentText("Employee:");
        
        dialog.showAndWait().ifPresent(employee -> {
            if ("Edit".equals(action)) {
                showEditEmployeeDialog(parentStage, employee, employeeManager, onActionCompleted);
            } else if ("Delete".equals(action)) {
                showDeleteEmployeeDialog(parentStage, employee, employeeManager, employees, onActionCompleted);
            }
        });
    }
    
    public static void viewDeletedEmployeeArchives(Stage parentStage) {
        try {
            File archiveDir = new File(ARCHIVE_DIRECTORY);
            if (!archiveDir.exists() || archiveDir.listFiles() == null || archiveDir.listFiles().length == 0) {
                showAlert("No Archives", "No deleted employee archives found.", Alert.AlertType.INFORMATION);
                return;
            }
            
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Select Deleted Employee Archive");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            chooser.setInitialDirectory(archiveDir);
            
            File selectedFile = chooser.showOpenDialog(parentStage);
            if (selectedFile != null) {
                try {
                    String content = new String(java.nio.file.Files.readAllBytes(selectedFile.toPath()));
                    
                    Stage dialogStage = new Stage();
                    dialogStage.setTitle("Deleted Employee Archive - " + selectedFile.getName());
                    
                    TextArea textArea = new TextArea(content);
                    textArea.setEditable(false);
                    textArea.setWrapText(true);
                    textArea.setPrefRowCount(30);
                    textArea.setPrefColumnCount(100);
                    
                    Button closeButton = new Button("Close");
                    closeButton.setOnAction(e -> dialogStage.close());
                    
                    VBox layout = new VBox(10);
                    layout.getChildren().addAll(textArea, closeButton);
                    
                    Scene scene = new Scene(layout, 800, 600);
                    dialogStage.setScene(scene);
                    dialogStage.show();
                    
                } catch (Exception e) {
                    showAlert("Error", "Failed to load archive: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to access archive directory: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private static void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 
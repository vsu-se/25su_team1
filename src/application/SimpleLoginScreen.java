package application;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import java.io.*;
import java.util.*;

public class SimpleLoginScreen extends Application {

    Stage window;
    Scene scene1, scene2, scene3, scene4, scene5, scene6, scene7, scene8, scene9;
    CreateEmployee creator = new CreateEmployee();
    ListView<String> listView;
    AddHours currentAddHours = new AddHours();
    PTO currentPTO = new PTO(40);
    Employee selectedEmployee;
    ViewPayStub viewPayStub = new ViewPayStub();
    SavingSystemData sSD = new SavingSystemData();
    RestoreSystemState rSS = new RestoreSystemState();
    ArchivedDataViewer aDV = new ArchivedDataViewer();
    NewWeek nW = new NewWeek();
    EditDailyEntry EDE = new EditDailyEntry();
    private Map<String, String> user = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {
    	EDE.setAllRecords(EDE.restoreRecords());
    	List<Employee> Employees = rSS.restoreState();
    	for (Employee emp : Employees) {
    		creator.createEmp(emp.getFirstName(), emp.getLastName(), emp.getUsername(), emp.getPassword(), emp.getDepartment(), emp.getID());
    	}
        window = primaryStage;
        initializeUsers();

        Label userLabel = new Label("Username:");
        TextField userField = new TextField();
        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();
        Button loginButton = new Button("Login");
        Button employeeLoginButton = new Button("Employee Login");
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
        
        employeeLoginButton.setOnAction(e -> {
            new EmployeeLogin().start(new Stage());
        });

        VBox loginLayout = new VBox(10);
        loginLayout.getChildren().addAll(userLabel, userField, passLabel, passField, loginButton, employeeLoginButton, messageLabel);
        scene1 = new Scene(loginLayout, 400, 300);
        scene1.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        window.setScene(scene1);
        window.setTitle("Manager Login");
        window.show();

        Button addEmployeeScene = new Button("Add Employee");
        addEmployeeScene.setOnAction(e -> window.setScene(scene3));

        Button listEmployeeScene = new Button("List Employee");
        listEmployeeScene.setOnAction(e -> window.setScene(scene4));

        Button listEmployeeHoursButton = new Button("List All Employee Hours");
        listEmployeeHoursButton.setOnAction(e -> {
            ListEmployeeHours.setEmployeeList(creator.getEmployees());
            new ListEmployeeHours().start(new Stage());
        });
        
        Button importBulkHoursButton = new Button("Add Bulk Hours");
        importBulkHoursButton.setOnAction(e -> importBulkHours());

        Button logHoursButton = new Button("Log Hours for Employee");
        logHoursButton.setOnAction(e -> {
            List<Employee> employees = creator.getEmployees();
            if (employees.isEmpty()) {
                showAlert("No Employees", "No employees available to log hours.");
                return;
            }

            ChoiceDialog<Employee> dialog = new ChoiceDialog<>(employees.get(0), employees);
            dialog.setTitle("Select Employee");
            dialog.setHeaderText("Choose an employee to log hours for:");
            dialog.setContentText("Employee:");

            dialog.showAndWait().ifPresent(emp -> {
                VBox layout5 = new VBox(10);
                Label titleLabel = new Label("Add Hours for " + emp.getName());

                ComboBox<String> daySelector = new ComboBox<>();
                daySelector.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");

                TextField hoursField = new TextField();
                hoursField.setPromptText("Enter number of hours");

                CheckBox ptoCheck = new CheckBox("Is this PTO?");
                Button submitButton = new Button("Submit Hours");
                Label ptoBalanceLabel = new Label("PTO Balance: " + emp.getPTO().getRemainingPTOHours() + " hours");
                Label messageLabel5 = new Label();

                Button summaryButton = new Button("Show Weekly Summary");
                Button backButton = new Button("Back");
                backButton.setOnAction(x -> window.setScene(scene2));

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
        });

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
        
        
        
        
        Button SaveSystemData = new Button("Save System Data");
        SaveSystemData.setOnAction(x -> {
        	sSD.saveSystemState(creator.getEmployees());
        	EDE.saveRecords();
        	showAlert("System Data Saved","System Data Saved");
        	});
        
        Button newWeek = new Button("Start A New Week");
        newWeek.setOnAction(x -> {
        	VBox layout7 = new VBox(10);
        	Label label = new Label("Do You Want to Start a New Week?");
        	HBox yesNo = new HBox(2);
        	Button yes = new Button("Yes");
        	yes.setOnAction(xx -> {nW.archiveAndReset(creator.getEmployees()); window.setScene(scene2);});
        	Button no = new Button("No");
        	no.setOnAction(xx -> window.setScene(scene2));
        	yesNo.getChildren().addAll(yes, no);
        	layout7.getChildren().addAll(label, yesNo);
        	scene7 = new Scene(layout7, 300, 120);
        	window.setScene(scene7);
        	
        });
        
        Button EditDailyEntry = new Button("Change Hours for Employee");
        EditDailyEntry.setOnAction(e -> {
            List<Employee> employees = creator.getEmployees();
            int id;
            if (employees.isEmpty()) {
                showAlert("No Employees", "No employees' hours available to be changed.");
                return;
            }

            ChoiceDialog<Employee> dialog = new ChoiceDialog<>(employees.get(0), employees);
            dialog.setTitle("Select Employee");
            dialog.setHeaderText("Choose employee's hours to change:");
            dialog.setContentText("Employee:");

            dialog.showAndWait().ifPresent(emp -> {
                VBox layout8 = new VBox(10);
                Label titleLabel = new Label("Change Hours for " + emp.getName());
                ComboBox<String> daySelector = new ComboBox<>();
                daySelector.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
                TextField managerIDField = new TextField();
                managerIDField.setPromptText("Enter your ID");
                TextField hoursField = new TextField();
                hoursField.setPromptText("Enter number of hours");
                CheckBox ptoCheck = new CheckBox("Is this PTO?");
                Button submitButton = new Button("Change Hours");
                Label ptoBalanceLabel = new Label("PTO Balance: " + emp.getPTO().getRemainingPTOHours() + " hours");
                Label messageLabel5 = new Label();
                Button backButton = new Button("Back");
                backButton.setOnAction(x -> window.setScene(scene2));
                Button summaryButton = new Button("Show Weekly Summary");

                submitButton.setOnAction(x -> {
                    int dayIndex = daySelector.getSelectionModel().getSelectedIndex();
                    String hoursText = hoursField.getText();
                    int idText = Integer.parseInt(managerIDField.getText());
                    boolean IDexists = employees.stream().anyMatch(Employee -> Employee.getID() == idText);
                    
                    if (!IDexists) {
                    	messageLabel5.setText("Please enter your correct ID.");
                    	return;
                    }

                    if (dayIndex == -1 || hoursText.isEmpty()) {
                        messageLabel5.setText("Please select a day and enter hours.");
                        return;
                    }
                    
                    try {
                        double hours = Double.parseDouble(hoursText);

                        if (ptoCheck.isSelected()) {
                            if (hours != 8) {
                                messageLabel5.setText("Only allowed 8 hours of PTO for the day.");
                                return;
                            }
                            if (emp.getPTO().getRemainingPTOHours() < 8) {
                            messageLabel5.setText("Not Enough Hours: " + emp.getPTO() + " remaining.");
                            return;
                            }
                            
                            EDE.setAllRecords(EDE.editDailyEntry(idText, emp, dayIndex, hours, ptoCheck.isSelected()));
                            messageLabel5.setText("PTO hours updated");
                        } else {
                        	EDE.setAllRecords(EDE.editDailyEntry(idText, emp, dayIndex, hours, ptoCheck.isSelected()));
                            messageLabel5.setText("Regular hours updated.");
                        }

                        ptoBalanceLabel.setText("PTO Balance: " + emp.getPTO().getRemainingPTOHours() + " hours");
                    } catch (NumberFormatException ex) {
                        messageLabel5.setText("Invalid number format for hours.");
                    }
                });
                
                summaryButton.setOnAction(x -> {
                    showSummary(emp);});
                
                layout8.getChildren().addAll(titleLabel, managerIDField, daySelector, 
                							 hoursField, ptoCheck, submitButton, 
                							 messageLabel5, ptoBalanceLabel, summaryButton, backButton);
                scene8 = new Scene(layout8, 400, 400);
                window.setScene(scene8);
            });
        });
        
        Button viewArchivedData = new Button("View Archived Data");
        viewArchivedData.setOnAction(x -> aDV.viewArchivedFile(window));
        
        Button viewRecords = new Button("View All Records");
        viewRecords.setOnAction(x -> {
        	if (EDE.getAllRecords().size() < 1 || creator.getManagers().isEmpty()) {
        		showAlert("No Records", "No Records to view");
        		return;
        	}
        	VBox Layout9 = new VBox(10);
        	Label title = new Label("Records:");
        	List<Employee> employees = creator.getEmployees();
        	List<Manager> managers = creator.getManagers();
        	listView = new ListView<>();
        	ComboBox<Employee> eS = new ComboBox<>();
        	for (Employee e : employees) eS.getItems().add(e);
        	eS.setValue(employees.get(0));
        	ComboBox<Manager> mS = new ComboBox<>();
        	for (Manager m : managers) mS.getItems().add(m);
        	mS.setValue(managers.get(0));
        	Button auditEmployee = new Button("Audit Employee");
        	auditEmployee.setOnAction(ex -> {
        		listView.getItems().clear();
        		if (eS.getValue() != null) {
        		for (String s : EDE.auditEmployee((eS.getValue().getName())+"'s")) listView.getItems().add(s);
        		} else listView.getItems().add("Select an employee to audit.");
        	});
        	Button auditEditor = new Button("Audit Editor");
        	auditEditor.setOnAction(ex -> {
        		listView.getItems().clear();
        		if (eS.getValue() != null) {
        		for (String s : EDE.auditEditor(Integer.valueOf(mS.getValue().getID()))) listView.getItems().add(s);
        		} else listView.getItems().add("Select an editor to audit.");
        	});
        	Button allRecords = new Button("Show All Records");
        	allRecords.setOnAction(ex -> {
        		listView.getItems().clear();
        		for (String s : EDE.getAllRecords()) listView.getItems().add(s);
        	});
        	HBox employeeAudit = new HBox(2);
        	HBox editorAudit = new HBox(2);
        	employeeAudit.getChildren().addAll(auditEmployee, eS);
        	editorAudit.getChildren().addAll(auditEditor, mS);
        	Button back = new Button("Back");
            back.setOnAction(e -> {
                window.setScene(scene2);
                listView.getItems().clear();
            });
            Layout9.getChildren().addAll(title, employeeAudit, editorAudit, allRecords, listView, back);
            scene9 = new Scene(Layout9, 600, 400);
            window.setScene(scene9);
        });
        
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> window.setScene(scene1));

        VBox layout2 = new VBox(10);
        layout2.getChildren().addAll(
        	addEmployeeScene, 
        	listEmployeeScene, 
        	listEmployeeHoursButton, 
        	importBulkHoursButton,
        	logHoursButton, 
        	EditDailyEntry, 
        	viewPayStubButton, 
        	viewRecords, 
        	viewArchivedData, 
        	SaveSystemData, 
        	newWeek, 
        	logoutButton
        );
        scene2 = new Scene(layout2, 600, 425);
        scene2.getStylesheets().add(getClass().getResource("application.css").toExternalForm());


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
    }
    
    
    private void importBulkHours() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV File");
        File file = fileChooser.showOpenDialog(window);

        if (file != null) {
            int successCount = 0;
            int failCount = 0;

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                List<Employee> allEmployees = creator.getEmployees();
                Map<Integer, Employee> employeeMap = new HashMap<>();
                for (Employee emp : allEmployees) {
                    employeeMap.put(emp.getID(), emp);
                }

                while ((line = reader.readLine()) != null) {
                    String[] tokens = line.split(",");
                    if (tokens.length < 4) {
                        failCount++;
                        continue;
                    }

                    try {
                        int id = Integer.parseInt(tokens[0].trim());
                        String day = tokens[1].trim();
                        double hours = Double.parseDouble(tokens[2].trim());
                        boolean isPTO = Boolean.parseBoolean(tokens[3].trim());

                        Employee emp = employeeMap.get(id);
                        if (emp == null) {
                            failCount++;
                            continue;
                        }

                        int dayIndex = getDayIndex(day);
                        if (dayIndex == -1) {
                            failCount++;
                            continue;
                        }

                        if (emp.getAddHours().getHours(dayIndex) > 0 || emp.getAddHours().isPTO(dayIndex)) {
                            failCount++;
                            continue;
                        }

                        if (isPTO) {
                            if (hours != 8) {
                                failCount++;
                                continue;
                            }
                            boolean success = emp.getAddHours().setPTO(dayIndex, emp.getPTO());
                            if (success) successCount++;
                            else failCount++;
                        } else {
                            emp.getAddHours().setHours(dayIndex, hours);
                            successCount++;
                        }

                    } catch (Exception ex) {
                        failCount++;
                    }
                }

                showAlert("Import Complete", "Successfully logged hours: " + successCount + "\nFailed entries: " + failCount);

            } catch (IOException ex) {
                showAlert("Error", "Failed to read file: " + ex.getMessage());
            }
        }
    }

    private int getDayIndex(String day) {
        switch (day.toLowerCase()) {
            case "monday": return 0;
            case "tuesday": return 1;
            case "wednesday": return 2;
            case "thursday": return 3;
            case "friday": return 4;
            case "saturday": return 5;
            case "sunday": return 6;
            default: return -1;
        }
    }

    private void initializeUsers() {
        user.put("jason", "password");
        user.put("man", "yo");
        for (Employee e : creator.getEmployees()) {
        	user.put(e.getUsername(), e.getPassword());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showSummary(Employee e) {
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        StringBuilder summary = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            double h = e.getAddHours().getHours(i);
            if (h > 0) {
                summary.append(days[i]).append(": ").append(h).append(" hours");
                if (e.getAddHours().isPTO(i)) summary.append(" (PTO)");
                summary.append("\n");
            }
        }
        summary.append("Total: ").append(e.getAddHours().getTotalHours()).append(" hours");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Weekly Summary");
        alert.setHeaderText("Hours Logged for " + e.getName());
        alert.setContentText(summary.toString());
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

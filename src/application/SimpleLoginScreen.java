package application;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class SimpleLoginScreen extends Application {
	
	Stage window;
	Scene scene1, scene2, scene3,scene4;
	CreateEmployee creator = new CreateEmployee();
	ListView<String> listView;

    private Map<String, String> user = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {
    	window = primaryStage;
        initializeUsers();

        Label userLabel = new Label("Username:");
        TextField userField = new TextField();

        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();

        Button loginButton = new Button("Login");
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

        VBox layout = new VBox(10);
        layout.getChildren().addAll(userLabel, userField, passLabel, passField, loginButton, messageLabel);
        scene1 = new Scene(layout, 400, 300);
        window.setScene(scene1);
        window.setTitle("Manager Login");
        window.show();
        
        
        //Layout 2 Will be used later on for delete or what now.
        Button addEmployeeScene = new Button("Add employee");
        addEmployeeScene.setOnAction(e -> window.setScene(scene3));
        Button listEmployeeScene = new Button("List Employee");
        listEmployeeScene.setOnAction(e -> window.setScene(scene4));
        VBox layout2 = new VBox(10);
        layout2.getChildren().addAll(addEmployeeScene, listEmployeeScene);
        scene2 = new Scene(layout2, 600,300);
  
        
        
        //Layout 3
        
        Label firstName = new Label("Enter First Name");
        TextField firstNamefield = new TextField();

        Label lastName = new Label("Enter Last name:");
        TextField lastNamefield = new TextField();

        ComboBox<String> position = new ComboBox<>();
        position.getItems().addAll("Manager", "Staff");
        position.setValue("Manager");
        
        Label username = new Label("Create a username");
        TextField usernameField = new TextField();
        
        Label passwrd = new Label("Create a password");
        PasswordField passwrdField = new PasswordField();
        
        
        Button addEmployee = new Button("Add Employee");
        
        addEmployee.setOnAction(e -> {
        	String firstname = firstNamefield.getText();
        	String lastname = lastNamefield.getText();
        	String Position = position.getValue();
        	String usrname = usernameField.getText();
        	String password = passwrdField.getText();
        	if(firstname.length() > 0 && lastname.length() > 0 && usrname.length() > 0 && password.length() > 0) {
        		if (user.containsKey(usrname)) {
                    messageLabel.setText("Username already exists. Choose a different username.");
                } else {
                	user.put(usrname, password);
                    creator.yo(firstname, lastname, usrname, password, Position);
                    messageLabel.setText("Employee added successfully!");
                    window.setScene(scene2);
                }
        	} else {
                messageLabel.setText("Failed to add employee.");
            }
        	
        	
        });
        
        VBox layout3 = new VBox(10);
        layout3.getChildren().addAll(firstName, firstNamefield, lastName, lastNamefield, position, username, usernameField, passwrd, passwrdField, addEmployee);
        scene3 = new Scene(layout3, 600, 400);
        
        
        
        
        //layout 4
        listView = new ListView<>();

        Label titleLabel = new Label("Employee and Manager Viewer");

        Button listEmployeesButton = new Button("List All Employees");
        listEmployeesButton.setOnAction(e -> {
        	listView.getItems().clear();
        	List<Employee> combined = new ArrayList<>();
        	combined.addAll(creator.getEmployees());
        	//combined.addAll(creator.getManagers());
        	combined.sort(Comparator.comparing(Employee::getLastName)
        			.thenComparing(Employee::getFirstName)
        			.thenComparing(Employee::getDepartment)
        			.thenComparing(Employee::getID));
        	for (Employee emp : combined) {
        		String display = "Name: " + emp.getFirstName() + " " + emp.getLastName() +
        				(emp instanceof Manager ? " (Manager)" : "") +
        				", Department: " + emp.getDepartment() +
        				", ID: " + emp.getID() +
        				", Username: " + emp.getUsername();
        		listView.getItems().add(display);
				}
			});

        Button listManagersButton = new Button("List Managers Only");
        listManagersButton.setOnAction(e -> {
        	listView.getItems().clear();

        	List<Manager> managers = creator.getManagers();
        	managers.sort(Comparator.comparing(Manager::getLastName)
        			.thenComparing(Manager::getFirstName)
        			.thenComparing(Manager::getDepartment)
        			.thenComparing(Manager::getID));

        	for (Manager m : managers) {
        		String display = "Name: " + m.getFirstName() + " " + m.getLastName() +
        				" (Manager), Department: " + m.getDepartment() +
        				", ID: " + m.getID() +
        				", Username: " + m.getUsername();
        		listView.getItems().add(display);
				}
			});

        Button back = new Button("Back");
        back.setOnAction(e -> window.setScene(scene2));

	    VBox layout4 = new VBox(10);
	    layout4.getChildren().addAll(titleLabel, listEmployeesButton, listManagersButton, listView, back);
	    scene4 = new Scene(layout4, 600, 400);

        
        
    }

    private void initializeUsers() {
        user.put("jason", "password234");
        user.put("manager1", "letmein");
    }

    public static void main(String[] args) {
        launch(args);
    }
} 
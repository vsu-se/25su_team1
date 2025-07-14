package application;
	
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // UI Components
        Label userLabel = new Label("Username:");
        TextField userField = new TextField();

        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();

        Button loginButton = new Button("Login");
        Label messageLabel = new Label();

        // Button Action
        loginButton.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();

            if (username.equals("admin") && password.equals("password123")) {
                messageLabel.setText("Login successful!");
            } else {
                messageLabel.setText("Invalid username or password.");
            }
        });

        // Layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(userLabel, userField, passLabel, passField, loginButton, messageLabel);

        // Scene
        Scene scene = new Scene(layout, 250, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Manager Login");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
package application;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class SaveStateViewer {

    public static void viewSaveState(Stage parentStage) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select System State File");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));
        
        // Set initial directory to save directory
        chooser.setInitialDirectory(new File("system_saves"));

        File selectedFile = chooser.showOpenDialog(parentStage);
        if (selectedFile != null) {
            try {
                String content = new String(Files.readAllBytes(selectedFile.toPath()));
                
                // Create a custom dialog with scrollable text area
                Stage dialogStage = new Stage();
                dialogStage.setTitle("System State Viewer - " + selectedFile.getName());
                
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
                showError("Failed to load file: " + e.getMessage());
            }
        }
    }
    
    public static void listSaveStates(Stage parentStage) {
        try {
            Path saveDir = Paths.get("system_saves");
            StringBuilder availableFiles = new StringBuilder("Available System State Files:\n\n");
            
            if (!Files.exists(saveDir)) {
                availableFiles.append("No save directory found. System states will be saved to 'system_saves/' directory.\n");
            } else {
                try (Stream<Path> paths = Files.walk(saveDir, 1)) {
                    paths.filter(path -> path.toString().contains("system_state_") && 
                                      path.toString().endsWith(".txt"))
                         .sorted()
                         .forEach(path -> {
                             try {
                                 String fileName = path.getFileName().toString();
                                 String fileSize = String.format("%.1f KB", Files.size(path) / 1024.0);
                                 availableFiles.append(fileName).append(" (").append(fileSize).append(")\n");
                             } catch (Exception e) {
                                 availableFiles.append(path.getFileName().toString()).append("\n");
                             }
                         });
                }
                
                if (availableFiles.toString().equals("Available System State Files:\n\n")) {
                    availableFiles.append("No system state files found.\n");
                    availableFiles.append("Use 'Save System State' to create your first save.\n");
                }
            }
            
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Available System States");
            alert.setHeaderText("System State Files");
            alert.setContentText(availableFiles.toString());
            alert.getDialogPane().setPrefWidth(500);
            alert.showAndWait();
            
        } catch (Exception e) {
            showError("Failed to list save states: " + e.getMessage());
        }
    }

    private static void showError(String msg) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(msg);
        alert.showAndWait();
    }
} 
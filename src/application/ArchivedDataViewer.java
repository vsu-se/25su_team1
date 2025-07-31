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

public class ArchivedDataViewer {

    public static void viewArchivedFile(Stage parentStage) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Archived Week File");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));
        
        // Set initial directory to archived_weeks folder
        File archiveFolder = new File("archived_weeks");
        if (archiveFolder.exists()) {
            chooser.setInitialDirectory(archiveFolder);
        } else {
            chooser.setInitialDirectory(new File("."));
        }

        File selectedFile = chooser.showOpenDialog(parentStage);
        if (selectedFile != null) {
            try {
                String content = new String(Files.readAllBytes(selectedFile.toPath()));
                
                // Create a custom dialog with scrollable text area
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Archived Week Data - " + selectedFile.getName());
                
                TextArea textArea = new TextArea(content);
                textArea.setEditable(false);
                textArea.setWrapText(true);
                textArea.setPrefRowCount(25);
                textArea.setPrefColumnCount(80);
                
                Button closeButton = new Button("Close");
                closeButton.setOnAction(e -> dialogStage.close());
                
                VBox layout = new VBox(10);
                layout.getChildren().addAll(textArea, closeButton);
                
                Scene scene = new Scene(layout, 600, 500);
                dialogStage.setScene(scene);
                dialogStage.show();
                
            } catch (Exception e) {
                showError("Failed to load file: " + e.getMessage());
            }
        }
    }
    
    public static void listAvailableArchives(Stage parentStage) {
        try {
            Path archiveDir = Paths.get("archived_weeks");
            StringBuilder availableFiles = new StringBuilder("Available Archived Files:\n\n");
            
            if (!Files.exists(archiveDir)) {
                availableFiles.append("No archived_weeks folder found.\n");
                availableFiles.append("Archived files are created when you start a new week.\n");
            } else {
                try (Stream<Path> paths = Files.walk(archiveDir, 1)) {
                    paths.filter(path -> path.toString().contains("Archived_Week_") && path.toString().endsWith(".txt"))
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
            }
            
            if (availableFiles.toString().equals("Available Archived Files:\n\n")) {
                availableFiles.append("No archived files found in archived_weeks folder.\n");
                availableFiles.append("Archived files are created when you start a new week.\n");
            }
            
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Available Archives");
            alert.setHeaderText("Archived Week Files");
            alert.setContentText(availableFiles.toString());
            alert.getDialogPane().setPrefWidth(400);
            alert.showAndWait();
            
        } catch (Exception e) {
            showError("Failed to list archived files: " + e.getMessage());
        }
    }

    private static void showError(String msg) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}

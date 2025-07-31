package application;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class NewWeek {
    
    public static boolean archiveAndReset(List<Employee> employees, Stage parentStage) {
        // Check if there's any data to archive
        boolean hasData = false;
        for (Employee emp : employees) {
            if (emp.getAddHours().getTotalHours() > 0) {
                hasData = true;
                break;
            }
        }
        
        if (!hasData) {
            showAlert("No Data", "No hours have been logged for the current week. Starting a new week will reset all employee hours.", AlertType.INFORMATION);
        } else {
            // Confirm with user
            Alert confirmDialog = new Alert(AlertType.CONFIRMATION);
            confirmDialog.setTitle("Start New Week");
            confirmDialog.setHeaderText("Archive Current Week Data");
            confirmDialog.setContentText("This will archive all current week data and reset hours for all employees. Continue?");
            
            Optional<ButtonType> result = confirmDialog.showAndWait();
            if (result.isPresent() && result.get() != ButtonType.OK) {
                return false; // User cancelled
            }
        }
        
        // Create archive directory if it doesn't exist
        String archiveDirectory = "archived_weeks/";
        java.io.File archiveFolder = new java.io.File(archiveDirectory);
        if (!archiveFolder.exists()) {
            archiveFolder.mkdirs();
        }
        
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr = now.format(formatter);
        String filename = archiveDirectory + "Archived_Week_" + dateStr + ".txt";

        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("WEEK ARCHIVE - " + dateStr + "\n");
            writer.write("=====================================\n\n");
            
            int totalEmployees = 0;
            double totalCompanyHours = 0;
            
            for (Employee emp : employees) {
                double empTotalHours = emp.getAddHours().getTotalHours();
                if (empTotalHours > 0) {
                    totalEmployees++;
                    totalCompanyHours += empTotalHours;
                }
                
                writer.write("Employee: " + emp.getName() + " (ID: " + emp.getID() + ")\n");
                writer.write("Department: " + emp.getDepartment() + "\n");
                writer.write("Position: " + (emp instanceof Manager ? "Manager" : "Staff") + "\n");
                writer.write("Username: " + emp.getUsername() + "\n");
                writer.write("PTO Balance: " + emp.getPTO().getRemainingPTOHours() + " hours\n");
                writer.write("Weekly Hours:\n");
                
                String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
                boolean hasHours = false;
                
                for (int i = 0; i < 7; i++) {
                    double h = emp.getAddHours().getHours(i);
                    if (h > 0) {
                        hasHours = true;
                        writer.write("  " + days[i] + ": " + h + " hours");
                        if (emp.getAddHours().isPTO(i)) {
                            writer.write(" (PTO)");
                        }
                        writer.write("\n");
                    }
                }
                
                if (hasHours) {
                    writer.write("  Total Hours: " + empTotalHours + "\n");
                    writer.write("  Weekday Hours: " + emp.getAddHours().getWeekdayHours() + "\n");
                    writer.write("  Weekend Hours: " + emp.getAddHours().getWeekendHours() + "\n");
                    writer.write("  Days Worked: " + emp.getAddHours().getNumDaysWorked() + "\n");
                } else {
                    writer.write("  No hours logged this week\n");
                }
                
                writer.write("-------------------------------------\n\n");
                
                // Reset employee hours for new week
                emp.setAddHours(new AddHours());
            }
            
            // Write summary
            writer.write("=====================================\n");
            writer.write("WEEK SUMMARY\n");
            writer.write("=====================================\n");
            writer.write("Total Employees with Hours: " + totalEmployees + "\n");
            writer.write("Total Company Hours: " + totalCompanyHours + "\n");
            writer.write("Archive Date: " + dateStr + "\n");
            
            showAlert("Success", "Week archived successfully!\nFile: " + filename + "\n\nAll employee hours have been reset for the new week.", AlertType.INFORMATION);
            return true;
            
        } catch (IOException e) {
            showAlert("Error", "Failed to archive week data: " + e.getMessage(), AlertType.ERROR);
            return false;
        }
    }
    
    private static void showAlert(String title, String content, AlertType type) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }
}

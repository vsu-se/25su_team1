package application;

import java.io.*;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SavingSystemData {
    
    private static final String SAVE_DIRECTORY = "system_saves/";
    
    // Create save directory if it doesn't exist
    static {
        File directory = new File(SAVE_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
    
    /**
     * Save system state in text format
     */
    public static void saveSystemState(List<Employee> employees) {
        saveAsText(employees);
    }
    
    /**
     * Save as human-readable text format
     */
    public static void saveAsText(List<Employee> employees) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = SAVE_DIRECTORY + "system_state_" + timestamp + ".csv";
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Write CSV header
            writer.println("EmployeeID,FirstName,LastName,Username,Password,Department,Position,PayRate,TaxRate,PTOBalance," +
                         "MondayHours,MondayPTO,TuesdayHours,TuesdayPTO,WednesdayHours,WednesdayPTO,ThursdayHours,ThursdayPTO," +
                         "FridayHours,FridayPTO,SaturdayHours,SaturdayPTO,SundayHours,SundayPTO,TotalRegularHours,TotalPTOHours,TotalHours");
            
            // Write employee data
            for (Employee emp : employees) {
                AddHours addHours = emp.getAddHours();
                
                // Get hours and PTO status for each day
                double mondayHours = addHours.getHours(0);
                boolean mondayPTO = addHours.isPTO(0);
                double tuesdayHours = addHours.getHours(1);
                boolean tuesdayPTO = addHours.isPTO(1);
                double wednesdayHours = addHours.getHours(2);
                boolean wednesdayPTO = addHours.isPTO(2);
                double thursdayHours = addHours.getHours(3);
                boolean thursdayPTO = addHours.isPTO(3);
                double fridayHours = addHours.getHours(4);
                boolean fridayPTO = addHours.isPTO(4);
                double saturdayHours = addHours.getHours(5);
                boolean saturdayPTO = addHours.isPTO(5);
                double sundayHours = addHours.getHours(6);
                boolean sundayPTO = addHours.isPTO(6);
                
                // Calculate totals
                double totalRegularHours = 0;
                double totalPtoHours = 0;
                for (int i = 0; i < 7; i++) {
                    if (addHours.isPTO(i)) {
                        totalPtoHours += addHours.getHours(i);
                    } else {
                        totalRegularHours += addHours.getHours(i);
                    }
                }
                
                writer.printf("%d,%s,%s,%s,%s,%s,%s,%.2f,%.2f,%d," +
                            "%.1f,%s,%.1f,%s,%.1f,%s,%.1f,%s," +
                            "%.1f,%s,%.1f,%s,%.1f,%s,%.1f,%.1f,%.1f%n",
                            emp.getID(),
                            escapeCsvField(emp.getFirstName()),
                            escapeCsvField(emp.getLastName()),
                            escapeCsvField(emp.getUsername()),
                            escapeCsvField(""), // Password not accessible via getter
                            escapeCsvField(emp.getDepartment()),
                            escapeCsvField(emp instanceof Manager ? "Manager" : "Staff"),
                            emp.getPayRate(),
                            emp.getTaxRate(),
                            emp.getPTO().getRemainingPTOHours(),
                            mondayHours, mondayPTO ? "PTO" : "Regular",
                            tuesdayHours, tuesdayPTO ? "PTO" : "Regular",
                            wednesdayHours, wednesdayPTO ? "PTO" : "Regular",
                            thursdayHours, thursdayPTO ? "PTO" : "Regular",
                            fridayHours, fridayPTO ? "PTO" : "Regular",
                            saturdayHours, saturdayPTO ? "PTO" : "Regular",
                            sundayHours, sundayPTO ? "PTO" : "Regular",
                            totalRegularHours,
                            totalPtoHours,
                            addHours.getTotalHours());
            }
        } catch (IOException e) {
            System.err.println("Error saving system state as CSV: " + e.getMessage());
        }
    }
    
    private static String escapeCsvField(String field) {
        if (field == null) return "";
        // Escape quotes and wrap in quotes if contains comma, quote, or newline
        String escaped = field.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n")) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }
    

    

    /**
     * Save on demand (manual save)
     */
    public static void saveOnDemand(List<Employee> employees) {
        saveSystemState(employees);
        System.out.println("Manual save completed at " + 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
    
    /**
     * Save on logout
     */
    public static void saveOnLogout(List<Employee> employees) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        System.out.println("Saving system state on logout...");
        
        // Save in text format with logout indicator
        saveAsText(employees);
        
        System.out.println("Logout save completed at " + timestamp);
    }
    
    /**
     * Get save directory path
     */
    public static String getSaveDirectory() {
        return SAVE_DIRECTORY;
    }
    


}

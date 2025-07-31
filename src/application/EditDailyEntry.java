package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EditDailyEntry {
    private List<String> allRecords = new ArrayList<>();
    private static final String AUDIT_FILE = "audit_trail.txt";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public boolean editDailyEntry(int editorId, Employee employee, int dayIndex, double newHours, boolean isPTO) {
        try {
            // Get current values before making changes
            double oldHours = employee.getAddHours().getHours(dayIndex);
            boolean oldPTO = employee.getAddHours().isPTO(dayIndex);
            
            // Create audit record before making changes
            String auditRecord = createAuditRecord(editorId, employee, dayIndex, oldHours, newHours, oldPTO, isPTO);
            
            // Make the changes
            if (isPTO) {
                if (newHours != 8.0) {
                    System.out.println("PTO must be exactly 8 hours");
                    return false;
                }
                boolean ptoSet = employee.getAddHours().setPTO(dayIndex, employee.getPTO());
                if (!ptoSet) {
                    System.out.println("PTO not allowed or already used");
                    return false;
                }
            } else {
                employee.getAddHours().setHours(dayIndex, newHours);
                // Clear PTO if it was previously set and refund PTO hours
                if (oldPTO) {
                    employee.getAddHours().clearPTO(dayIndex);
                    employee.getPTO().refundPTOHours(8); // Refund 8 hours of PTO
                }
            }
            
            // Add audit record
            addRecord(auditRecord);
            saveRecords();
            
            return true;
        } catch (Exception e) {
            System.err.println("Error editing daily entry: " + e.getMessage());
            return false;
        }
    }
    
    private String createAuditRecord(int editorId, Employee employee, int dayIndex, 
                                   double oldHours, double newHours, boolean oldPTO, boolean newPTO) {
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        
        StringBuilder record = new StringBuilder();
        record.append("TIMESTAMP: ").append(timestamp).append(" | ");
        record.append("EDITOR_ID: ").append(editorId).append(" | ");
        record.append("EMPLOYEE: ").append(employee.getName()).append(" (ID: ").append(employee.getID()).append(") | ");
        record.append("DAY: ").append(days[dayIndex]).append(" | ");
        record.append("OLD_HOURS: ").append(oldHours).append(" | ");
        record.append("NEW_HOURS: ").append(newHours).append(" | ");
        record.append("OLD_PTO: ").append(oldPTO).append(" | ");
        record.append("NEW_PTO: ").append(newPTO);
        
        return record.toString();
    }
    
    public List<String> getAllRecords() {
        return new ArrayList<>(allRecords);
    }
    
    public void setAllRecords(List<String> records) {
        this.allRecords = new ArrayList<>(records);
    }
    
    public void addRecord(String record) {
        allRecords.add(record);
    }
    
    public List<String> getAuditByEditor(int editorId) {
        List<String> editorRecords = new ArrayList<>();
        for (String record : allRecords) {
            if (record.contains("EDITOR_ID: " + editorId)) {
                editorRecords.add(record);
            }
        }
        return editorRecords;
    }
    
    public List<String> getAuditByEmployee(String employeeName) {
        List<String> employeeRecords = new ArrayList<>();
        for (String record : allRecords) {
            if (record.contains("EMPLOYEE: " + employeeName)) {
                employeeRecords.add(record);
            }
        }
        return employeeRecords;
    }
    
    public List<String> getAuditByEmployeeId(int employeeId) {
        List<String> employeeRecords = new ArrayList<>();
        for (String record : allRecords) {
            if (record.contains("(ID: " + employeeId + ")")) {
                employeeRecords.add(record);
            }
        }
        return employeeRecords;
    }
    
    public List<String> getAuditByDateRange(String startDate, String endDate) {
        List<String> dateRangeRecords = new ArrayList<>();
        for (String record : allRecords) {
            String timestamp = extractTimestamp(record);
            if (timestamp != null && isInDateRange(timestamp, startDate, endDate)) {
                dateRangeRecords.add(record);
            }
        }
        return dateRangeRecords;
    }
    
    private String extractTimestamp(String record) {
        if (record.contains("TIMESTAMP: ")) {
            int start = record.indexOf("TIMESTAMP: ") + 11;
            int end = record.indexOf(" | ", start);
            if (end == -1) end = record.length();
            return record.substring(start, end);
        }
        return null;
    }
    
    private boolean isInDateRange(String timestamp, String startDate, String endDate) {
        return timestamp.compareTo(startDate) >= 0 && timestamp.compareTo(endDate) <= 0;
    }
    
    public void saveRecords() {
        try (PrintWriter writer = new PrintWriter(AUDIT_FILE)) {
            for (String record : allRecords) {
                writer.println(record);
            }
        } catch (IOException e) {
            System.err.println("Error saving audit records: " + e.getMessage());
        }
    }
    
    public void loadRecords() {
        File file = new File(AUDIT_FILE);
        if (!file.exists()) {
            return;
        }
        
        allRecords.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    allRecords.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading audit records: " + e.getMessage());
        }
    }
    
    public void clearRecords() {
        allRecords.clear();
        File file = new File(AUDIT_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
    
    public int getTotalRecords() {
        return allRecords.size();
    }
    
    public String getAuditSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Audit Trail Summary\n");
        summary.append("==================\n");
        summary.append("Total Records: ").append(getTotalRecords()).append("\n");
        summary.append("File Location: ").append(AUDIT_FILE).append("\n");
        summary.append("Last Updated: ").append(LocalDateTime.now().format(TIMESTAMP_FORMAT)).append("\n");
        return summary.toString();
    }
}

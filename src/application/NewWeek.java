package application;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class NewWeek {
    public static void archiveAndReset(List<Employee> employees) {
        LocalDate now = LocalDate.now();
        String filename = "Archived_Week_" + now + ".txt";

        try (FileWriter writer = new FileWriter(filename)) {
            for (Employee emp : employees) {
                writer.write("Employee: " + emp.getName() + "\n");
                for (int i = 0; i < 7; i++) {
                    double h = emp.getAddHours().getHours(i);
                    if (h > 0) {
                        writer.write("Day " + i + ": " + h + " hours");
                        if (emp.getAddHours().isPTO(i)) {
                            writer.write(" (PTO)");
                        }
                        writer.write("\n");
                    }
                }
                writer.write("Total Hours: " + emp.getAddHours().getTotalHours() + "\n");
                writer.write("-----\n");

                emp.setAddHours(new AddHours());
            }
        } catch (IOException e) {
            System.out.println("Failed to archive data: " + e.getMessage());
        }
    }
}
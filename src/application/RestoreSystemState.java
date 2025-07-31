package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class RestoreSystemState {
	List<Employee> restoreState() {
		EditDailyEntry EDE = new EditDailyEntry();
		File f = new File("SystemState");
		List<Employee> Es = new ArrayList<>();
    	int i = 0;
        String line, line2;
        String[]  lines, lines2, lines3, lines4, lines5;

        try (BufferedReader reader = new BufferedReader(new FileReader(f));) {
            while ((line = reader.readLine()) != null) {
            	if (line.equals("Employee Information:")) {
            		line = reader.readLine();
            		lines = line.split(" ");
            		line = reader.readLine();
            		lines2 = line.split(" ");
            		line = reader.readLine();
            		lines3 = line.split(" ");
            		line = reader.readLine();
            		lines4 = line.split(" ");
            		line = reader.readLine();
            		lines5 = line.split(" ");
            		
            		Es.add(new Employee(lines[1], lines[2], lines2[1], lines3[1], lines4[1]));
            		Es.get(i).setID(Integer.valueOf(lines5[1]));
            		line = reader.readLine();
            		line = reader.readLine();
            		line = reader.readLine();
            		lines = line.split(" ");
            		for (int j=0;j<7;j++) {
            				if (lines.length == 6) {
            					Es.get(i).getAddHours().setPTO(j, Es.get(i).getPTO());
            				}
            				else {
            					Es.get(i).getAddHours().setHours(j, Double.valueOf(lines[4]));
            				}
            		}
            		i++;
            	}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Es;
	}
	}


package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class EditDailyEntry {
	private List<String> AllRecords = new ArrayList<>(); 
	
	public List<String> editDailyEntry(int id, Employee e, int day, Double hours, boolean pto) {
		String[] days = new String[] {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
		double pd = e.getAddHours().getHours(day);
		e.getAddHours().setHours(day, hours);
		String ChangeRecords = e.getName()+"'s hours for "
		+days[day]+" was set to "+hours+" from " + pd;
		if (pto) {
			e.getAddHours().setIsPTO(day);
			e.getPTO().usePTO(8);
			ChangeRecords = ChangeRecords + " using PTO";
		}
		ChangeRecords = ChangeRecords + " by Employee ID: " + id + ".";
		addRecord(ChangeRecords);
		System.out.println(this.AllRecords.size());
		return AllRecords;
	}
	public List<String> getAllRecords() {
		return AllRecords;
	}
	public void setAllRecords(List<String> S) {
		AllRecords = S;
	}
	public void addRecord(String r) {
		AllRecords.add(r);
	}
	public List<String> auditEditor(int ID) {
		List<String> managerEdits = new ArrayList<>();
		for (String S : AllRecords) {
			String[] SS = S.split(" ");
			if ((Integer.toString(ID)+".").equals(SS[SS.length - 1])) {
				managerEdits.add(S);
			}
		}
		return managerEdits;
	}
	public List<String> auditEmployee(String name) {
		List<String> EmployeeHistory = new ArrayList<>();
		for (String S : AllRecords) {
			String[] SS = S.split(" ");
			if (name.equals(SS[0]+" "+SS[1])) {
				EmployeeHistory.add(S);
			}
		}
		return EmployeeHistory;
	}
	public void saveRecords() {
		try (PrintWriter writer = new PrintWriter("Records")) {
			for (String S : AllRecords) {
				writer.println("Record:");
				writer.println(S);
				writer.println("-----");
			}
		} catch (IOException e) {
            System.out.println("Error saving records: " + e.getMessage());
        }
	}
public List<String> restoreRecords() {
	String line;
	File f = new File("Records");
	try (BufferedReader reader = new BufferedReader(new FileReader(f));) {
        while ((line = reader.readLine()) != null) {
        	if (line.equals("Record:")) {
        		line = reader.readLine();
        		addRecord(line);
        	}
        }
		
	} catch (IOException e) {
        e.printStackTrace();
    }
	return AllRecords;
	}
}

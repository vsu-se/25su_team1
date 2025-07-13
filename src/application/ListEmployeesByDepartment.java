package application;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ListEmployeesByDepartment {
	
	
	private List<Employee> Employees = new ArrayList<>();
	public void EmployeeSort(List<Employee> Employees) {
		Employees.sort(Comparator.comparing(Employee::getDepartment)
				.thenComparing(Employee::getLastName)
				.thenComparing(Employee::getFirstName)
				.thenComparing(Employee::getID));
	}
	public String[] getListEmployeesByDepartment() {
    	EmployeeSort(Employees);
    	String[] s = new String[Employees.size()];
    	int i = 0;
    	for(Employee Employee : Employees) {
    		s[i]=(Employee.getDepartment()
    					+"  "+Employee.getLastName()
    					+"  "+Employee.getFirstName()
    					+"  "+Employee.getID());
    		i++;
    	}
    	return s;
	}
	public void newEmployee(Employee e) {
    	Employees.add(e);
    }
}



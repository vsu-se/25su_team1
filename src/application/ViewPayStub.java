package application;

import java.io.IOException;
import java.io.PrintWriter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class ViewPayStub extends Application {

    private Employee employee;

    public ViewPayStub(Employee e) {
        this.employee = e;
public class ViewPayStub extends Application{
	
    private Employee employee;
    private AddHours addHours = new AddHours();
    private PTO pto;
    
    
    public ViewPayStub(Employee e) {
        this.employee = e;
        this.pto = employee.getPTO();

    }

    public ViewPayStub() {
        this.employee = new Employee("Christian", "Misner", "ChristianMisner", "Password1", "Staff");
        employee.getAddHours().setHours(0, 8.0);
        employee.getAddHours().setHours(1, 12.3);
        employee.getAddHours().setHours(2, 10.0);
        employee.getAddHours().setHours(3, 12.3);
        employee.getAddHours().setPTO(4, employee.getPTO());
        employee.getAddHours().setHours(5, 0.0);
        employee.getAddHours().setHours(6, 2.0);
    }

    public String isPTO(int n, AddHours addHours) {
        return addHours.isPTO(n) ? " (PTO)" : "";
    }

    public double getPay(Employee e, AddHours ah) {
        double payRate = e.getPayRate();
        double weekday = ah.getWeekdayHours();
        double weekend = ah.getWeekendHours();
        double totalPay = 0;

        if (e.getDepartment().equals("Manager")) {
            return 40 * payRate;
        }

        if (weekday > 40) {
            totalPay += 40 * payRate + (weekday - 40) * payRate * 1.5;
        } else {
            totalPay += weekday * payRate;
        }

        totalPay += weekend * payRate * 1.5;
        return totalPay;
    }

    public String[] PayStub(Employee e, AddHours ah, PTO pto) {
        double gross = getPay(e, ah);
        double tax = gross * 0.45;
        double net = gross - tax;

        return new String[]{
            "Name: " + e.getName() + " (ID: " + e.getID() + ")",
            "Hours Worked:",
            "Monday: " + ah.getHours(0) + isPTO(0, ah) +
            "  Tuesday: " + ah.getHours(1) + isPTO(1, ah) +
            "  Wednesday: " + ah.getHours(2) + isPTO(2, ah) +
            "  Thursday: " + ah.getHours(3) + isPTO(3, ah) +
            "  Friday: " + ah.getHours(4) + isPTO(4, ah),
            "Saturday: " + ah.getHours(5) + isPTO(5, ah) +
            "  Sunday: " + ah.getHours(6) + isPTO(6, ah),
            "Weekday: " + ah.getWeekdayHours() +
            "  Weekend: " + ah.getWeekendHours() +
            "  Total: " + ah.getTotalHours(),
            "Gross Pay: $" + String.format("%.2f", gross) +
            "  Tax Withholdings: $" + String.format("%.2f", tax) +
            "  Net Pay: $" + String.format("%.2f", net) +
            "  Remaining PTO: " + pto.getRemainingPTOHours()
        };
    }

    @Override
    public void start(Stage primaryStage) {
        AddHours ah = employee.getAddHours();
        PTO pto = employee.getPTO();

        String[] payStub = PayStub(employee, ah, pto);

        VBox layout = new VBox(10);
        for (String line : payStub) {
            layout.getChildren().add(new Label(line));
        }

        Button saveButton = new Button("Save PayStub");
        saveButton.setOnAction(e -> {
            try (PrintWriter writer = new PrintWriter(employee.getName() + " Pay Stub.txt")) {
                for (String line : payStub) {
                    writer.println(line);
                    writer.println();
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        });

        layout.getChildren().add(saveButton);
        Scene scene = new Scene(layout, 500, 300);
        primaryStage.setTitle("View Pay Stub");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void launchWithEmployee(Employee employee) {
        ViewPayStub gui = new ViewPayStub(employee);
        gui.start(new Stage());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
        this.pto = employee.getPTO();
        setHours(8.0, 12.3, 10.0, 12.3, 1.0, 0.0, 2.0);
    }
    public Employee returnEmployee() {
    	return this.employee;
    }
    
    public void setHours(Double D1, Double D2, Double D3, Double D4, Double D5, Double D6, Double D7) {
		this.addHours.setHours(0, D1);
		this.addHours.setHours(1, D2);
		this.addHours.setHours(2, D3);
		this.addHours.setHours(3, D4);
		this.addHours.setPTO(4, pto);
		this.addHours.setHours(5, D6);
		this.addHours.setHours(6, D7);
	}
    
    public String isPTO(int n, AddHours addHours) {
		if (addHours.isPTO(n)) {
			return " (PTO)";
		}
		return "";
	}
	
	
	public Double getPay(Employee e, AddHours addHours) {

		Double Pay = 0.0;
		Double PayRate = 15.0;
		
		if (e.getDepartment() == "Manager") {
			return 40 * PayRate;
		}
		if (addHours.getWeekdayHours() > 40.0) {
			Pay += ((addHours.getWeekdayHours() - 40.0)*PayRate*1.5) + 40 * PayRate;
		}
		else {
			Pay += addHours.getWeekdayHours()*PayRate;
		}
		Pay += addHours.getWeekendHours()*PayRate*1.5;
		return Pay;
	}
	
	public String[] PayStub(Employee e, AddHours AH, PTO pto) {
		AddHours addHours = AH;
		Double taxWitholdings = getPay(e, AH) * 0.45;
		String[] s = new String[6];
		s[0] = "Name: " + e.getName() + " (ID: " + e.getID() + ")";
		s[1] = "Hours Worked: ";
		s[2] = "Monday: " + addHours.getHours(0) + isPTO(0,AH)
		     +"  Tuesday: " + addHours.getHours(1) + isPTO(1,AH)
		     + "  Wednesday: " + addHours.getHours(2) + isPTO(2,AH)
		     +"  Thursday: " + addHours.getHours(3) + isPTO(3,AH)
		     + "  Friday: " + addHours.getHours(4) + isPTO(4,AH);
		s[3] = "Saturday: " + addHours.getHours(5) + isPTO(5,AH)
			+"  Sunday: " + addHours.getHours(6) + isPTO(6,AH);
		s[4] = "Weekday: " + addHours.getWeekdayHours() 
			+ "  Weekend: " + addHours.getWeekendHours() 
			+ "  Total: " + addHours.getTotalHours();
		s[5] = "Gross Pay: " + getPay(e, AH) 
			+ "  Tax Withholdings: " + taxWitholdings 
			+ "  Net Pay: " + (getPay(e, AH) - taxWitholdings) 
			+ "  Remaining PTO: " + pto.getRemainingPTOHours();
		return s;
	}
	
    @Override
	public void start(Stage primaryStage) {
    	String[] PayStub = PayStub(employee, addHours, pto);
        Label nameLabel = new Label(PayStub[0]);
        Label Days = new Label(PayStub[1]);
        Label HoursWorkedMonday = new Label(PayStub[2]);
        Label HoursWorkedSaturday = new Label(PayStub[3]);
        Label TotalHoursWorked = new Label(PayStub[4]);
        Label Pay = new Label(PayStub[5]);
        
        Button saveButton = new Button("Save PayStub");
        saveButton.setOnAction(e -> {
        	 try (PrintWriter writer = new PrintWriter(employee.getName()+" Pay Stub.txt")) {
                 for (int i=0; i<PayStub.length;i++) {
                	 writer.println(PayStub[i]);
                	 writer.println("");
                 }
             } catch (IOException ex) {
                 System.out.println(ex.getMessage());
             }
        		});
		
		VBox layout = new VBox(10);
        layout.getChildren().addAll(
            nameLabel,
            Days,
            HoursWorkedMonday,
            HoursWorkedSaturday,
            TotalHoursWorked,
            Pay,
            saveButton
        );
		
		Scene scene = new Scene(layout, 500, 250);
        primaryStage.setTitle("View Pay Stub");
        primaryStage.setScene(scene);
        primaryStage.show();
	}
    public static void launchWithEmployee(Employee employee) {
    	ViewPayStub gui = new ViewPayStub(employee);
        gui.start(new Stage());
    }
    
	public static void main(String[] args) {
        launch(args);
    }
}

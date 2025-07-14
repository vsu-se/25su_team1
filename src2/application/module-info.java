module Team1 {
	requires javafx.controls;
	requires junit;
	
	opens application to javafx.graphics, javafx.fxml;
}

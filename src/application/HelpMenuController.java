package application;

import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class HelpMenuController {
	@FXML
	private AnchorPane help;
	public void initialize(){	
		help.getStyleClass().add("root");
	}
}

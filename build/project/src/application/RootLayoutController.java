package application;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class RootLayoutController{
	 // Reference to the main application
    @FXML
    private TextField inputId;
    @FXML
    private Pane pane;

	@FXML
	public void addKinonich(ActionEvent event) throws IOException{
//		Parent rootPageParent = FXMLLoader.load(getClass().getResource("MenuChooseId.fxml"));
//		Scene parentPageScene = new Scene(rootPageParent);
//		Node node= (Node)event.getSource();
//		Stage appStage = (Stage) pane.getScene().getWindow();
//		appStage.hide();
//		appStage.setScene(parentPageScene);
//		appStage.show();
		
	}

}

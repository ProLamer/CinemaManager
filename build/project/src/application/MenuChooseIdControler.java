package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MenuChooseIdControler implements Initializable{
	@FXML
	private Button buttonOk;
	@FXML
	private TextField getId;
	
	@FXML
	public void onButtonOkClick(ActionEvent event) throws IOException{
		Parent rootPageParent = FXMLLoader.load(getClass().getResource("PersonOverview.fxml"));
		Scene parentPageScene = new Scene(rootPageParent);
		Stage appStage = (Stage) (((Node) (event.getSource())).getScene().getWindow());
		appStage.setTitle("№ кіноночі");
		appStage.hide();
		appStage.setScene(parentPageScene);
		appStage.show();
		
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
}

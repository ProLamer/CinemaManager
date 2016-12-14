package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class MenuShowKinonichController {
	@FXML
	private Button buttonOkShow;
	@FXML
	private TextField getId;
	
	private RootLayoutController parent;
	public int idKinonich;
	private static final String SHOW_KINONICH_STATEMENT = "SELECT * FROM places WHERE idkinonich=(?);";
    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
//    public void setMain(Main main) {
//        this.main = main;
//    }
	
	@FXML
	public void OnButtonOkShow(ActionEvent event) throws IOException{
	    Stage appStage = (Stage) (((Node) (event.getSource())).getScene().getWindow());
	    appStage.hide();
		idKinonich = Integer.parseInt(getId.getText());
		Controller.setIdKinonich(idKinonich);
		showKinonich(idKinonich);
		
	}
	
	
	public static void showKinonich(int id) {
		Connection connection = ConnectionDataBase.getInstance();

		try {
			PreparedStatement prepareStatement = connection.prepareStatement(SHOW_KINONICH_STATEMENT);
			prepareStatement.setInt(1, id);
			ResultSet resultSet = prepareStatement.executeQuery();
			if(resultSet.next()||(Controller.getMaxKinonich()==id)){
				Controller.updateBookedPlaces(AllButtons.allButtons);
				Controller.updatePaidPlaces(AllButtons.allButtons);
			}else{
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("error");
				alert.setHeaderText("");
				alert.setContentText("Такого номеру кіноночі не існує!");
				alert.showAndWait();
			}
			prepareStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void setParent(RootLayoutController rootLayoutController) {
		this.parent = rootLayoutController;
		
	}

}

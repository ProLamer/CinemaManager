package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class MenuChooseIdControler implements Initializable{
	@FXML
	private Button buttonOk;
	@FXML
	private TextField getId;
	
	private RootLayoutController parent;
	public int idKinonich;
	public static String alertMessage;
	private static final String ADD_KINONICH_STATEMENT = "INSERT INTO kinonich (id) VALUES (?);";
	private static final String SHOW_KINONICH_STATEMENT = "SELECT * FROM kinonich;";
    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
//    public void setMain(Main main) {
//        this.main = main;
//    }
	
	@FXML
	public void onButtonOkClick(ActionEvent event) throws IOException{
		try {
	        // Load root layout from fxml file.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(Main.class
	                .getResource("MenuChooseId.fxml"));
	        Parent parent1 = loader.load();

	        // Show the scene containing the root layout.
	        Scene scene = new Scene(parent1);
	        Stage appStage = (Stage) (((Node) (event.getSource())).getScene().getWindow());
	        appStage.setScene(scene);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		idKinonich = Integer.parseInt(getId.getText());
		Controller.setIdKinonich(idKinonich);
		Boolean bool = addKinonich(idKinonich);
		if(bool==false){
			alertMessage = "Така кіноніч вже існує";
		}
		Controller.updateBookedPlaces(AllButtons.allButtons);
		Controller.updatePaidPlaces(AllButtons.allButtons);
		
		Alert alert = new Alert(AlertType.INFORMATION);
		//alert.initOwner(mainApp.getPrimaryStage());
		alert.setTitle("Інформація");
		
		alert.setHeaderText("");
		alert.setContentText(alertMessage);
		alert.showAndWait();
		
	}
	
	
	public static Boolean addKinonich(int id) {
		Connection connection = ConnectionDataBase.getInstance();
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(ADD_KINONICH_STATEMENT);
			PreparedStatement prepareStatement1 = connection.prepareStatement(SHOW_KINONICH_STATEMENT);
			ResultSet resultSet = prepareStatement1.executeQuery();
			while(resultSet.next()){
				int idFromBd = resultSet.getInt(1);
				if(id!=idFromBd){
					prepareStatement.setInt(1, id);
				}
				else{
					return false;
				}
			}
			prepareStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return true;
	}

	public void setParent(RootLayoutController rootLayoutController) {
		this.parent = rootLayoutController;
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}

package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ShowPhonesController {
	@FXML
	private static Button buttonSave;
	@FXML
	private ChoiceBox<String> zal;
	private static int idClient;
	public static Set <String> set = new TreeSet<>();
	private static final String UPDATE_PHONE_STATEMENT = "SELECT * FROM places WHERE idkinonich=(?) AND status=(?) AND zal=(?);";
	private static final String UPDATE_PHONE_STATEMENT_WITHOUTZAL = "SELECT * FROM places WHERE idkinonich=(?) AND status=(?);";
	private static final String UPDATE_PHONE_NUMBER_STATEMENT = "SELECT * FROM clients WHERE id=(?);";
	private ObservableList<String> zals = FXCollections.observableArrayList("Всі зали","Синій зал", "Червоний зал","Сихів","Копернік");
	@FXML
	public  void saveFile(){
			String phones = "";
			getIdClients();
			for (String string : set) {
				phones += "\r\n"+ string;
			}
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showSaveDialog(new Stage());
            
            if(file != null){
                save(phones, file);
            }
            set.clear();
            RootLayoutController.appStageForPhones.close();
        }
	 private static void save(String content, File file){
	        try {
	            FileWriter fileWriter = null;
	             
	            fileWriter = new FileWriter(file);
	            fileWriter.write(content.concat("\n"));
	            fileWriter.close();
	        } catch (IOException ex) {
	            Logger.getLogger(ShowPhonesController.class.getName()).log(Level.SEVERE, null, ex);
	        }
	         
	    }
	 @FXML
		public void initialize() {
			zal.setValue("Всі зали");
			zal.setItems(zals);	
		}
	 public void getIdClients(){
			Connection connection = ConnectionDataBase.getInstance();
			ResultSet rs = null;
			try {
				if(zal.getValue().equalsIgnoreCase("Всі зали")){
					PreparedStatement prepareStatementNew = connection.prepareStatement(UPDATE_PHONE_STATEMENT_WITHOUTZAL);
					prepareStatementNew.setInt(1, Controller.getIdKinonich());
					prepareStatementNew.setInt(2, 0);
					rs = prepareStatementNew.executeQuery();
				}else{
					PreparedStatement prepareStatement = connection.prepareStatement(UPDATE_PHONE_STATEMENT);
					prepareStatement.setInt(1, Controller.getIdKinonich());
					prepareStatement.setInt(2, 0);
					prepareStatement.setString(3, zal.getValue());					
					rs = prepareStatement.executeQuery();
				}
				while(rs.next()){
					idClient = rs.getInt(3);
					getIdClientsNumber();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public void getIdClientsNumber(){
			
			Connection connection = ConnectionDataBase.getInstance();
			try {
				PreparedStatement prepareStatement = connection.prepareStatement(UPDATE_PHONE_NUMBER_STATEMENT);
				prepareStatement.setInt (1, idClient);
				ResultSet rs = prepareStatement.executeQuery();
				while (rs.next()) {
					String phoneNumer = rs.getString(4);
					set.add(phoneNumer);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
}

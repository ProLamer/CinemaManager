package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;

public class ChangeDataController {
	@FXML
	private ChoiceBox<String> administrators;
	@FXML
	private TextField nameField;
	@FXML
	private TextField lNameField;
	@FXML
	private TextField idKinonichField;
	@FXML
	private TextField newNameField;
	@FXML
	private TextField newLastNameField;
	@FXML
	private TextField newPhoneField;
	@FXML
	private CheckBox isOnlinePaid;
	@FXML
	private TextField newPriceField;
	private ObservableList<String> admins = FXCollections.observableArrayList("Організатори","Вероніка Марченко","Ігор Сокальський","Назар Довженко","Валентин Довженко");
	private static final String FIND_CLIENT_STATEMENT = "SELECT * FROM clients WHERE name=(?) AND lName=(?);";
	private static final String FIND_PLACES_STATEMENT = "SELECT * FROM places WHERE idkinonich=(?) AND idclient=(?);";
	private static final String UPDATE_PLACE_STATEMENT = "UPDATE places SET price=(?),administrator=(?),onlinePaid=(?) WHERE idkinonich=(?) AND idclient=(?);";
	private static final String UPDATE_CLIENT_STATEMENT = "UPDATE clients SET name=(?), lName=(?),phoneNumber=(?) WHERE id=(?);";
	private static int idClient;
	
	@FXML 
	public void findClient(){
		Connection connection = ConnectionDataBase.getInstance();
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(FIND_CLIENT_STATEMENT);
			if(nameField.getText().equals("")||lNameField.getText().equals("")||idKinonichField.getText().equals("")){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("error");
				alert.setHeaderText("");
				alert.setContentText("Заповніть всі поля! (Номер телефону - не обов'язково)");
				alert.showAndWait();
			}else{
				prepareStatement.setString(1, nameField.getText());
				prepareStatement.setString(2, lNameField.getText());
				
				ResultSet resultSet = prepareStatement.executeQuery();
					while(resultSet.next()){
						if(resultSet.last()){
							newNameField.setText(resultSet.getString(2));
							newLastNameField.setText(resultSet.getString(3));
							newPhoneField.setText(resultSet.getString(4));
							idClient = resultSet.getInt(1);
						}
						}
					if(resultSet.previous()==false){
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("error");
						alert.setHeaderText("");
						alert.setContentText("Такого клієнта не знайдено!");
						alert.showAndWait();
						newLastNameField.setText("");
						newNameField.setText("");
						newPhoneField.setText("");
						newPriceField.setText("");
					}	
				prepareStatement.execute();
				findPlaces();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}			
	}
	public void findPlaces(){
		Connection connection = ConnectionDataBase.getInstance();
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(FIND_PLACES_STATEMENT);
			try{
				Integer.parseInt(idKinonichField.getText());
				prepareStatement.setInt(1, Integer.parseInt(idKinonichField.getText()));
				prepareStatement.setInt(2, idClient);
				ResultSet resultSet1 = prepareStatement.executeQuery();
				while(resultSet1.next()){	
					if(resultSet1.last()){
						newPriceField.setText(String.valueOf(resultSet1.getInt(5)));
						administrators.setValue(resultSet1.getString(6));
						isOnlinePaid.setSelected(Boolean.parseBoolean(resultSet1.getString(8)));
					}	
				}
				if(resultSet1.previous()==false){
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("error");
					alert.setHeaderText("");
					alert.setContentText("На вибрану кіноніч цей клієнт нічого не бронював!");
					alert.showAndWait();
					newLastNameField.setText("");
					newNameField.setText("");
					newPhoneField.setText("");
					newPriceField.setText("");
				}
				prepareStatement.execute();
			}catch(NumberFormatException e){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("error");
				alert.setHeaderText("");
				alert.setContentText("Введіть коректно номер кіноночі!");
				alert.showAndWait();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@FXML 
	public void updateData(){
		Connection connection = ConnectionDataBase.getInstance();
		int price = Integer.parseInt(newPriceField.getText());
		String admin = administrators.getValue();
		int id = Integer.parseInt(idKinonichField.getText());
		String onlinePaid = String.valueOf(isOnlinePaid.isSelected());
		try {
			if(newPriceField.getText().equals("")||idKinonichField.getText().equals("")){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("error");
				alert.setHeaderText("");
				alert.setContentText("Заповніть всі поля! (Номер телефону - не обов'язково)");
				alert.showAndWait();
			}else{	
				PreparedStatement prepareStatement = connection.prepareStatement(UPDATE_PLACE_STATEMENT);
				prepareStatement.setInt(1, price);
				prepareStatement.setString(2, admin);
				prepareStatement.setString(3, onlinePaid);
				prepareStatement.setInt(4, id);
				prepareStatement.setInt(5, idClient);
				prepareStatement.executeUpdate();
				updateClientData();
				newLastNameField.setText("");
				newNameField.setText("");
				newPhoneField.setText("");
				newPriceField.setText("");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@FXML
	public void initialize() {
		administrators.setValue("Організатори");
		administrators.setItems(admins);
			
	}
	public void updateClientData(){
		Connection connection = ConnectionDataBase.getInstance();
		try {
			PreparedStatement prepareStatement1 = connection.prepareStatement(UPDATE_CLIENT_STATEMENT);
			if(newNameField.getText().equals("")||newLastNameField.getText().equals("")){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("error");
				alert.setHeaderText("");
				alert.setContentText("Заповніть всі поля! (Номер телефону - не обов'язково)");
				alert.showAndWait();
			}else{				
				prepareStatement1.getMetaData();
				prepareStatement1.setString(1, newNameField.getText());
				prepareStatement1.setString(2, newLastNameField.getText());
				prepareStatement1.setString(3, newPhoneField.getText());
				prepareStatement1.setInt(4, idClient);
				prepareStatement1.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}

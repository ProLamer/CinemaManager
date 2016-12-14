package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class LoginFormController {
	@FXML
	TextField loginForm;
	@FXML
	PasswordField passwordForm;
	private String login;
	private String password;
	private static final String FIND_LOGIN_STATEMENT = "SELECT * FROM administrators WHERE login=(?) AND password=(?);";
	@FXML
	public void authentication(){
		Connection connection = ConnectionDataBase.getInstance();
		try{
			login = loginForm.getText();
			password = passwordForm.getText();
			PreparedStatement prepareStatement = connection.prepareStatement(FIND_LOGIN_STATEMENT);
			prepareStatement.setString(1, login);
			prepareStatement.setString(2, password);
			ResultSet rs = prepareStatement.executeQuery();
				if(rs.next()){
					Main main = new Main();
					main.initRootLayout();
					Main.showPersonOverview();		
				}else{
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("error");
					alert.setHeaderText("");
					alert.setContentText("Ћог≥н або пароль введено не в≥рно!");
					alert.showAndWait();
				}
		}catch (SQLException e) {
			e.printStackTrace();
		}	
	}
}

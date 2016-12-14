package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ConnectionDataBase {
	private static Connection instance;

	public static synchronized Connection getInstance() {
		if (instance == null) {
			try {
//				instance = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "root");
				instance = DriverManager.getConnection("jdbc connection to db");
			} catch (SQLException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("error");
				alert.setHeaderText("");
				alert.setContentText("Помилка підключення до бази даних.");
				alert.show();
				e.printStackTrace();
			}
		}
		return instance;
	}
}

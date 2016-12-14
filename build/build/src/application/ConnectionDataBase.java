package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDataBase {
	private static Connection instance;

	public static synchronized Connection getInstance() {
		if (instance == null) {
			try {
//				instance = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "root");
				instance = DriverManager.getConnection("jdbc:mysql://bkp.mysql.ukraine.com.ua:3306/bkp_cinema", "bkp_cinema", "root");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}
}

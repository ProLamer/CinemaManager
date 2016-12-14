package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientService {
	private static final String ADD_CLIENT_STATEMENT = "INSERT INTO clients (name,lName, phoneNumber) VALUES (?,?,?);";

	public static int atClient(Person person) {
		Connection connection = ConnectionDataBase.getInstance();
		int userId = 0;
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(ADD_CLIENT_STATEMENT,PreparedStatement.RETURN_GENERATED_KEYS);
			prepareStatement.setString(1, person.getFirstName().getValue());
			prepareStatement.setString(2, person.getLastName().getValue());
			prepareStatement.setString(3, person.getPhoneNumber().getValue());
			int inserted = prepareStatement.executeUpdate();
			if (inserted > 0) {
				ResultSet generatedKeys2 = prepareStatement.getGeneratedKeys();
				if (generatedKeys2.next()) {
					userId = generatedKeys2.getInt(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return userId;
	}

	public static Boolean deleteClient(Person person) {
		Connection connection = ConnectionDataBase.getInstance();

		int inserted = 0;
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(ADD_CLIENT_STATEMENT);
			prepareStatement.setString(1, person.getFirstName().getValue());
			prepareStatement.setString(2, person.getLastName().getValue());
			prepareStatement.setString(3, person.getPhoneNumber().getValue());
			inserted = prepareStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return inserted > 0;
	}

}

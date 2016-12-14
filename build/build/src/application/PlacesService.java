package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.control.Button;

public class PlacesService {
	private static final String ADD_PLACE_STATEMENT = "INSERT INTO places (idplaces, idkinonich, idclient, status, price, administrator, zal) VALUES (?,?,?,?,?,?,?);";
	private static final String DELETE_PLACE_STATEMENT = "DELETE FROM places WHERE idplaces=(?);";
	private static final String UPDATE_PLACE_STATEMENT = "UPDATE places SET status=(?) WHERE idplaces = (?);";
	private static final String GET_INFORMATION_STATEMENT = "SELECT * FROM places WHERE idplaces=(?);";
	private static final String GET_INFORMATION_ABOUT_CLIENT_STATEMENT = "SELECT * FROM clients WHERE id=(?);";
	
	public static Boolean addPlace(int idPlace, int idKinonich,int userId, int status, int priceOfTicket, String admin, String zal) {
		Connection connection = ConnectionDataBase.getInstance();

		int inserted = 0;
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(ADD_PLACE_STATEMENT);
			prepareStatement.setInt(1, idPlace);
			prepareStatement.setInt(2, idKinonich);
			prepareStatement.setInt(3, userId);
			prepareStatement.setInt(4, status);
			prepareStatement.setInt(5, priceOfTicket);
			prepareStatement.setString(6, admin);
			prepareStatement.setString(7, zal);
			inserted = prepareStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return inserted > 0;
	}
	
	public static void deletePlace(int idPlace){
		Connection connection = ConnectionDataBase.getInstance();
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(DELETE_PLACE_STATEMENT);
			prepareStatement.setInt(1, idPlace);
			prepareStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void chooseToPaid(int idPlace){
		Connection connection = ConnectionDataBase.getInstance();
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(UPDATE_PLACE_STATEMENT);
			prepareStatement.setInt(1, 1);
			prepareStatement.setInt(2, idPlace);
			prepareStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static String getInformation(Button button){
		Connection connection = ConnectionDataBase.getInstance();
		String stringInformation = null;
		try {
			int id = Integer.parseInt(button.getId());
			PreparedStatement prepareStatement = connection.prepareStatement(GET_INFORMATION_STATEMENT);
			prepareStatement.setInt(1, id);
			ResultSet setOfPlaces =  prepareStatement.executeQuery();
			while(setOfPlaces.next()){
				String status = setOfPlaces.getString(4);
				if(status.equalsIgnoreCase("0")){
					status = "Заброньовано";
				}else{
					status = "Оплачено";
				}
				stringInformation = "Кіноніч №: "+setOfPlaces.getString(2).concat("\n")+getInformationAboutClient(setOfPlaces.getString(3))
				+"Статус: ".concat(status).concat("\n")+"Ціна: "
				.concat(setOfPlaces.getString(5)).concat("\n")+"Адміністратор: ".concat(setOfPlaces.getString(6));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stringInformation ;
	}
	
	public static String getInformationAboutClient(String idClient){
		Connection connection = ConnectionDataBase.getInstance();
		String stringInformationClient = null;
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(GET_INFORMATION_ABOUT_CLIENT_STATEMENT);
			prepareStatement.setString(1, idClient);
			ResultSet setOfClients =  prepareStatement.executeQuery();
			while(setOfClients.next()){
				stringInformationClient = "Ім'я: "+setOfClients.getString(2).concat("\n")
				+"Прізвище: ".concat(setOfClients.getString(3)).concat("\n")+"Номер телефону: "
				.concat(setOfClients.getString(4)).concat("\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stringInformationClient ;
	}
	
	
}

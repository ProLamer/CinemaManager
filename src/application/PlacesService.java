package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;

public class PlacesService {
	private static final String ADD_PLACE_STATEMENT = "INSERT INTO places (idplaces, idkinonich, idclient, status, price, administrator, zal) VALUES (?,?,?,?,?,?,?);";
	private static final String DELETE_PLACE_STATEMENT = "DELETE FROM places WHERE idplaces=(?) AND idkinonich=(?) AND zal=(?);";
	private static final String UPDATE_PLACE_STATEMENT = "UPDATE places SET status=(?) WHERE idplaces = (?) AND idkinonich=(?) AND zal=(?);";
	private static final String UPDATE_ONLINE_PAID_STATEMENT = "UPDATE places SET onlinePaid=(?) WHERE idplaces = (?) AND idkinonich=(?) AND zal=(?);";
	private static final String GET_INFORMATION_STATEMENT = "SELECT * FROM places WHERE idplaces=(?) AND idkinonich=(?) AND zal=(?);";
	private static final String GET_INFORMATION_ABOUT_CLIENT_STATEMENT = "SELECT * FROM clients WHERE id=(?);";
	private static final String GET_INFORMATION_FOR_PAIN_STATEMENT = "SELECT COUNT(*) FROM places WHERE status=(?) AND idkinonich=(?) AND zal=(?);";
	private static final String GET_INFORMATION_ABOUT_SUMM_STATEMENT = "SELECT * FROM places WHERE status=(?) AND idkinonich=(?) AND zal=(?);";
	private static final String GET_INFORMATION_ABOUT_ONLINESUMM_STATEMENT = "SELECT * FROM places WHERE status=(?) AND idkinonich=(?) AND zal=(?) AND onlinePaid=(?);";

	
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
	
	public static void deletePlace(int idPlace, int idKinonich){
		Connection connection = ConnectionDataBase.getInstance();
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(DELETE_PLACE_STATEMENT);
			prepareStatement.setInt(1, idPlace);
			prepareStatement.setInt(2, idKinonich);
			prepareStatement.setString(3, AllButtons.nameZal);
			prepareStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void chooseToPaid(int idPlace, int idKinonich, Boolean isOnlinePaid){
		Connection connection = ConnectionDataBase.getInstance();
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(UPDATE_PLACE_STATEMENT);
			prepareStatement.setInt(1, 1);
			prepareStatement.setInt(2, idPlace);
			prepareStatement.setInt(3, idKinonich);
			prepareStatement.setString(4, AllButtons.nameZal);
			chooseToOnlinePaid(idPlace, idKinonich, isOnlinePaid);
			prepareStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void chooseToOnlinePaid(int idPlace, int idKinonich, Boolean isOnlinePaid){
		Connection connection = ConnectionDataBase.getInstance();
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(UPDATE_ONLINE_PAID_STATEMENT);
			prepareStatement.setString(1, isOnlinePaid.toString());
			prepareStatement.setInt(2, idPlace);
			prepareStatement.setInt(3, idKinonich);
			prepareStatement.setString(4, AllButtons.nameZal);
			prepareStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static String getInformation(Button button, int idKinonich){
		Connection connection = ConnectionDataBase.getInstance();
		String stringInformation = null;
		String isOnlinePaid="не оплачено";
		try {
			int id = Integer.parseInt(button.getId());
			PreparedStatement prepareStatement = connection.prepareStatement(GET_INFORMATION_STATEMENT);
			prepareStatement.setInt(1, id);
			prepareStatement.setInt(2, idKinonich);
			prepareStatement.setString(3, AllButtons.nameZal);
			ResultSet setOfPlaces =  prepareStatement.executeQuery();
			while(setOfPlaces.next()){
				String status = setOfPlaces.getString(4);
				if(status.equalsIgnoreCase("0")){
					status = "Заброньовано";
				}else{
					status = "Оплачено";
					if(setOfPlaces.getString(8).equalsIgnoreCase("false")){
						isOnlinePaid = "Оплачено на касі";
					}else{
						isOnlinePaid = "Оплачено онлайн";
					}
				}
				stringInformation = "Кіноніч №: "+setOfPlaces.getString(2).concat("\n")+getInformationAboutClient(setOfPlaces.getString(3))
				+"Статус: ".concat(status).concat("\n")+"Ціна: "
				.concat(setOfPlaces.getString(5)).concat("\n")+"Адміністратор: ".concat(setOfPlaces.getString(6)).concat("\n")
				+isOnlinePaid;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("");
			alert.setContentText("Відсутнє підключення до інтернету! Перезапустіть програму.");
			alert.showAndWait();
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
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("");
			alert.setContentText("Відсутнє підключення до інтернету! Перезапустіть програму.");
			alert.showAndWait();
		}
		return stringInformationClient ;
	}
	public static String getInformationAboutKinonich(int status, String zal){
		Connection connection = ConnectionDataBase.getInstance();
		String stringInformation = null;
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(GET_INFORMATION_FOR_PAIN_STATEMENT);
			prepareStatement.setInt(1, status);
			prepareStatement.setInt(2, Controller.getIdKinonich());
			prepareStatement.setString(3, zal);
			ResultSet setOfPlaces =  prepareStatement.executeQuery();
			while(setOfPlaces.next()){
				int count = setOfPlaces.getInt("count(*)");
				if(status==0){
					stringInformation = "Кіноніч №: "+String.valueOf(Controller.getIdKinonich()).concat("\n")
					+"Кількість заброньованих: "+String.valueOf(count).concat("\n");
				}else {
					stringInformation = "Кількість оплачених: "+String.valueOf(count).concat("\n");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("");
			alert.setContentText("Відсутнє підключення до інтернету! Перезапустіть програму.");
			alert.showAndWait();
		}
		return stringInformation ;
	}
	public static Boolean check(int idPlace, int idKinonich, String zal){
		Connection connection = ConnectionDataBase.getInstance();
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(GET_INFORMATION_STATEMENT);
			prepareStatement.setInt(1, idPlace);
			prepareStatement.setInt(2, idKinonich);
			prepareStatement.setString(3, zal);
			ResultSet setOfPlaces =  prepareStatement.executeQuery();
			while(setOfPlaces.next()){
				int id = setOfPlaces.getInt(1);
				if(idPlace==id){
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("");
			alert.setContentText("Відсутнє підключення до інтернету! Перезапустіть програму.");
			alert.showAndWait();
		}
		return false;
	}
	public static String getSumm(int idKinonich, String zal){
		Connection connection = ConnectionDataBase.getInstance();
		int summ=0;
		int price;
		String res= null;
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(GET_INFORMATION_ABOUT_SUMM_STATEMENT);
			prepareStatement.setInt(1, 1);
			prepareStatement.setInt(2, idKinonich);
			prepareStatement.setString(3, zal);
			ResultSet setOfPlaces =  prepareStatement.executeQuery();
			while(setOfPlaces.next()){
				price = setOfPlaces.getInt(5);
				summ+=price;
			}
			res = "Сума = "+String.valueOf(summ);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	public static String getSummAboutOnlinePaid(int idKinonich, String zal){
		Connection connection = ConnectionDataBase.getInstance();
		int summ=0;
		int price;
		String res= null;
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(GET_INFORMATION_ABOUT_ONLINESUMM_STATEMENT);
			prepareStatement.setInt(1, 1);
			prepareStatement.setInt(2, idKinonich);
			prepareStatement.setString(3, zal);
			prepareStatement.setString(4, "true");
			ResultSet setOfPlaces =  prepareStatement.executeQuery();
			while(setOfPlaces.next()){
				price = setOfPlaces.getInt(5);
				summ+=price;
			}
			res = "Сума оплачених онлайн місць = "+String.valueOf(summ);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
		
	}
}

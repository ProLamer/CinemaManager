package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ShowResultsController implements Initializable{
	
	@FXML
	private TableView<ShowTable> showResultsTable;
	@FXML
	private TableColumn<ShowTable,String> administrator;
	@FXML
	private TableColumn<ShowTable,String> numOfBooked;
	@FXML
	private TableColumn<ShowTable,String> numOfPaid;
	@FXML
	private TableColumn<ShowTable,String> salary;
	private final ObservableList<ShowTable> data =
	        FXCollections.observableArrayList(new ShowTable("Організатори",showNum("Організатори",0),showNum("Організатори",1),showSalary("Організатори")),
	        		new ShowTable("Вероніка Марченко",showNum("Вероніка Марченко",0),showNum("Вероніка Марченко",1),showSalary("Вероніка Марченко")),
	        		new ShowTable("Ігор Сокальський",showNum("Ігор Сокальський",0),showNum("Ігор Сокальський",1),showSalary("Ігор Сокальський")),
	        		new ShowTable("Назар Довженко",showNum("Назар Довженко",0),showNum("Назар Довженко",1),showSalary("Назар Довженко")),
	        		new ShowTable("Валентин Довженко",showNum("Валентин Довженко",0),showNum("Валентин Довженко",1),showSalary("Валентин Довженко")));
	
	private static final String GET_INFORMATION_STATEMENT = "SELECT COUNT(*) FROM places WHERE status=(?) AND idkinonich=(?) AND administrator=(?) AND zal=(?);";
	private static final String GET_INFORMATION_ABOUT_SALARY_STATEMENT = "SELECT price FROM places WHERE status=1 AND idkinonich=(?) AND administrator=(?) AND zal=(?);";
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
			administrator.setCellValueFactory(
			    new PropertyValueFactory<>("administrator")
			);
			numOfBooked.setCellValueFactory(
			    new PropertyValueFactory<>("numOfBooked")
			);
			numOfPaid.setCellValueFactory(
			    new PropertyValueFactory<>("numOfPaid")
			);
			salary.setCellValueFactory(
				    new PropertyValueFactory<>("salary")
				);
			
			showResultsTable.getItems().addAll(data);
			
	}
	
	public String showNum(String admin,int status){
		String res = null;
		Connection connection = ConnectionDataBase.getInstance();
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(GET_INFORMATION_STATEMENT);
			prepareStatement.setInt(1, status);
			prepareStatement.setInt(2, Controller.getIdKinonich());
			prepareStatement.setString(3, admin);
			prepareStatement.setString(4, AllButtons.nameZal);
			ResultSet setOfPlaces =  prepareStatement.executeQuery();
			while(setOfPlaces.next()){
				res = setOfPlaces.getString("count(*)");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return res;	
	}
	
	public String showSalary(String admin){
		int salary = 0;
		Connection connection = ConnectionDataBase.getInstance();
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(GET_INFORMATION_ABOUT_SALARY_STATEMENT);;
			prepareStatement.setInt(1, Controller.getIdKinonich());
			prepareStatement.setString(2, admin);
			prepareStatement.setString(3, AllButtons.nameZal);
			ResultSet setOfPlaces =  prepareStatement.executeQuery();
			while(setOfPlaces.next()){
				int res = setOfPlaces.getInt(1);
				if(res<=0){
					salary+=0;
				}else if(res<81&&res>0){
					salary+=0;
				}else if(res<90&&res>80){
					salary+=10;
				}else if(res<220&&res>89){
					salary+=10;
				}else{
					salary+=20;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return String.valueOf(salary);	
	}
}

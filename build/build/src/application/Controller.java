package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class Controller {
	private static final String UPDATE_BOOKPLACES_STATEMENT = "SELECT * FROM places WHERE status=0 AND idkinonich=(?);";
	private static final String UPDATE_PAIDPLACES_STATEMENT = "SELECT * FROM places WHERE status=1 AND idkinonich=(?);";
	private static final String GET_MAXKINONICH_STATEMENT = "SELECT MAX(id) FROM kinonich;";
	private String choiseAdmin;
	private int priceOfTicket;
	private int idPlace;
	private static int idKinonich;
	public static int getIdKinonich() {
		return idKinonich;
	}
	public static void setIdKinonich(int idKinonich) {
		Controller.idKinonich = idKinonich;
	}

	private Set<Integer> selectedSits = new TreeSet<>();
	private Set<Button> bookedSits = new HashSet<>();
	private Set<Integer> paidSits = new TreeSet<>();
	private ObservableList<String> admins = FXCollections.observableArrayList("Вероніка Марченко","Організатори","Ігор Сокальський","Назар Довженко","Валентин Довженко");
	private ObservableList<String> zals = FXCollections.observableArrayList("Синій зал", "Червоний зал","Сихів","Копернік");
	@FXML
	private Button bookButton;
	@FXML
	private Button rebookButton;
	@FXML
	private Button paidButton;
	@FXML
	private TextField name;
	@FXML
	private TextField lName;
	@FXML
	private TextField phoneNumber;
	@FXML
	private TextField price;
	@FXML
	private ChoiceBox<String> administrators;
	@FXML
	private ChoiceBox<String> zal;
	@FXML
	private Button refresh;
	@FXML
	private TextArea messageTextArea;

	@FXML
	public void onBookButtonClicked() {
		Iterator<Button> iter = bookedSits.iterator();
		if(name.getText().equalsIgnoreCase("")||lName.getText().equalsIgnoreCase("")||price.getText().equalsIgnoreCase("")){
			Alert alert = new Alert(AlertType.WARNING);
			//alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Error");
			alert.setHeaderText("Не заповнені всі поля");
			alert.setContentText("Заповніть, будь ласка, всі поля про клієнта.");
			
			alert.showAndWait();
		}else{
		Person person = new Person(name.getText(), lName.getText(), phoneNumber.getText(),Integer.parseInt(price.getText()));
		priceOfTicket = Integer.parseInt(price.getText());
		int userId = ClientService.atClient(person);
			while (iter.hasNext()) {
				selectedSits.add(Integer.parseInt(iter.next().getId()));
			}
			System.out.println("selected " + selectedSits);	
			for (Button button : bookedSits) {
				if (button.getStyleClass().contains("clicked")) {
					button.getStyleClass().add("booked");
					choiseAdmin = administrators.getValue();
					idPlace = Integer.parseInt(button.getId());	
					PlacesService.addPlace(idPlace, idKinonich, userId, 0, priceOfTicket, choiseAdmin, zal.getValue());
				} else {
					button.getStyleClass().removeAll("booked", "clicked");
				}
			}
		}
	}

	@FXML
	public void onRebookButtonClicked() {

		System.out.println("selected " + selectedSits);
		for (Iterator<Button> iter = bookedSits.iterator(); iter.hasNext();) {
			Button button1 = iter.next();
			if (button1.getStyleClass().contains("paid_checked")) {
				Integer id = Integer.parseInt(button1.getId());
				button1.getStyleClass().removeAll("paid_checked", "paid", "booked_checked", "booked", "clicked");
				selectedSits.remove(id);
				PlacesService.deletePlace(id);
				paidSits.remove(id);
				iter.remove();
			} else if (button1.getStyleClass().contains("booked_checked")) {
				button1.getStyleClass().removeAll("booked_checked", "booked", "clicked");
				Integer id = Integer.parseInt(button1.getId());
				PlacesService.deletePlace(id);
				selectedSits.remove(id);
				iter.remove();
			}
		}
	}

	@FXML
	public void onPaidButtonClicked() {
		for (Button button : bookedSits) {
			if (button.getStyleClass().contains("booked_checked")) {
				button.getStyleClass().add("paid");
				idPlace = Integer.parseInt(button.getId());
				paidSits.add(idPlace);
				PlacesService.chooseToPaid(idPlace);
			}
		}
	}

	@FXML
	public void onRefreshButtonClick(){
		updateBookedPlaces(AllButtons.allButtons);
		updatePaidPlaces(AllButtons.allButtons);
	}
	
	@FXML
	public void onMouseClicked(MouseEvent e) {// Обробка кліків по кнопці
		Button button = (Button) e.getSource();
		bookedSits.add(button);
		toggleButton(button);
	}

	public void toggleButton(Button button) {// стилі, в залежності від кліку
		if (button.getStyleClass().contains("paid_checked")) {
			button.getStyleClass().removeAll("paid_checked");
			// bookedSits.remove(button);
		} else if (button.getStyleClass().contains("paid")) {
			button.getStyleClass().add("paid_checked");
			// bookedSits.remove(button);
		} else if (button.getStyleClass().contains("booked_checked")) {
			button.getStyleClass().removeAll("booked_checked");
			bookedSits.remove(button);
		} else if (button.getStyleClass().contains("booked")) {
			button.getStyleClass().add("booked_checked");
		} else if (button.getStyleClass().contains("clicked")) {
			bookedSits.remove(button);
			button.getStyleClass().removeAll("clicked");
		} else {
			button.getStyleClass().add("clicked");
		}
	}
	
	@FXML
	public void onButtonMouseEntered(MouseEvent e){
		Button button = (Button) e.getSource();
		messageTextArea.setText(PlacesService.getInformation(button));
	}
	
	@FXML
	public void onClickMethod() {
		
	}
	
	@FXML
	public void initialize() {
		administrators.setValue("Організатори");
		administrators.setItems(admins);
		
		zal.setValue("Синій зал");
		zal.setItems(zals);
		
		System.out.println(getMaxKinonich());
		
	}
	
	public static void updateBookedPlaces(List<Node> buttons){
		Connection connection = ConnectionDataBase.getInstance();
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(UPDATE_BOOKPLACES_STATEMENT);
			prepareStatement.setInt(1, idKinonich);
			ResultSet setOfBookedPlaces =  prepareStatement.executeQuery();
			while(setOfBookedPlaces.next()){
				String set = setOfBookedPlaces.getString(1);
				for(int i = 0; i<buttons.size();i++){
					if(set.equalsIgnoreCase(buttons.get(i).getId())){
						buttons.get(i).getStyleClass().add("booked");
					}
				}
			}
			prepareStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void updatePaidPlaces(List<Node> buttons){
		Connection connection = ConnectionDataBase.getInstance();
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(UPDATE_PAIDPLACES_STATEMENT);
			prepareStatement.setInt(1, idKinonich);
			ResultSet setOfBookedPlaces =  prepareStatement.executeQuery();
			while(setOfBookedPlaces.next()){
				String set = setOfBookedPlaces.getString(1);
				for(int i = 0; i<buttons.size();i++){
					if(set.equalsIgnoreCase(buttons.get(i).getId())){
						buttons.get(i).getStyleClass().add("paid");
					}
				}
			}
			prepareStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static int getMaxKinonich(){
		Connection connection = ConnectionDataBase.getInstance();
		int set = 0;
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(GET_MAXKINONICH_STATEMENT);
			ResultSet setOfBookedPlaces =  prepareStatement.executeQuery();
			while(setOfBookedPlaces.next()){
				set = setOfBookedPlaces.getInt(1);
			}
			prepareStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		idKinonich = set;
		return set;
	}
}

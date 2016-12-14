package application;
import java.awt.Checkbox;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Controller {
	private static final String UPDATE_BOOKPLACES_STATEMENT = "SELECT * FROM places WHERE status=0 AND idkinonich=(?) AND zal=(?);";
	private static final String UPDATE_PAIDPLACES_STATEMENT = "SELECT * FROM places WHERE status=1 AND idkinonich=(?) AND zal=(?);";
	private static final String GET_PRICES_STATEMENT = "SELECT * FROM places WHERE status=0 AND idkinonich=(?) AND zal=(?) AND idplaces=(?);";
	private static final String GET_ONLINEPAID_STATEMENT = "SELECT * FROM places WHERE status=1 AND idkinonich=(?) AND zal=(?) AND onlinePaid=(?);";
	private static final String GET_MAXKINONICH_STATEMENT = "SELECT MAX(id) FROM kinonich;";
	private String choiseAdmin;
	private int priceOfTicket;
	private int idPlace;
	int result = 0;
	private static int idKinonich;
	public static int getIdKinonich() {
		return idKinonich;
	}
	public static void setIdKinonich(int idKinonich) {
		Controller.idKinonich = idKinonich;
	}
	
	public static List<Node> allButtonsController = new ArrayList<>();
	private static Set<Integer> nowBookedSits = new TreeSet<>();
	private static Set<Integer> nowPaidSits = new TreeSet<>();
	private ObservableList<String> admins = FXCollections.observableArrayList("Організатори","Вероніка Марченко","Ігор Сокальський","Назар Довженко","Валентин Довженко");
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
	private VBox VBoxRed;
	@FXML
	private TextArea messageTextArea;
	@FXML
	private Label labelZal;
	@FXML
	private Label summ;
	@FXML
	private Label rest;
	@FXML
	private TextField getMoney;
	@FXML
	private Button showOnlinePaidButton;
	@FXML
	private CheckBox isOnlinePaid;
	private Main main;


    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMain(Main main) {
        this.main = main;
    }
    
	@FXML
	public void onBookButtonClicked() {
		int counter = 0;
		try{	
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
				for (Iterator<Node> iter = AllButtons.allButtons.iterator(); iter.hasNext();) {
					Button button = (Button) iter.next();
					if (button.getStyleClass().contains("clicked")) {
						counter++;
						button.getStyleClass().add("booked");
						choiseAdmin = administrators.getValue();
						idPlace = Integer.parseInt(button.getId());	
						if(PlacesService.check(idPlace, idKinonich, AllButtons.nameZal)==false){
							PlacesService.addPlace(idPlace, idKinonich, userId, 0, priceOfTicket, choiseAdmin, AllButtons.nameZal);
						}else{
							Alert alert = new Alert(AlertType.ERROR);
							//alert.initOwner(mainApp.getPrimaryStage());
							alert.setTitle("Error");
							alert.setHeaderText("Повторний запис в базу даних");
							alert.setContentText("Такі місця вже записані в базу даних. Обновіть, будь ласка, схему.");
							alert.showAndWait();
							break;
						}
					} 
				}
				if(counter==0){
					Alert alert = new Alert(AlertType.ERROR);
					//alert.initOwner(mainApp.getPrimaryStage());
					alert.setTitle("Error");
					alert.setHeaderText("");
					alert.setContentText("Не вибрано жодного місця!");
					alert.showAndWait();
					counter=0;
				}
				updateBookedPlaces(AllButtons.allButtons);
				updatePaidPlaces(AllButtons.allButtons);
			}
		}catch(NumberFormatException e){
			Alert alert = new Alert(AlertType.ERROR);
			//alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Error");
			alert.setHeaderText("");
			alert.setContentText("Не коректно введений номер кіноночі.");
			alert.showAndWait();
		}
	}

	@FXML
	public void onRebookButtonClicked() {
		int counter = 0;
		allButtonsController.addAll(AllButtons.allButtons);
		for (Iterator<Node> iter = AllButtons.allButtons.iterator(); iter.hasNext();) {
			Button button1 = (Button) iter.next();
			if (button1.getStyleClass().contains("paid_checked")) {
				counter++;
				Integer id = Integer.parseInt(button1.getId());
//				AllButtons.allButtons.get(id).getStyleClass().removeAll("paid_checked", "paid", "booked_checked", "booked", "clicked");
				PlacesService.deletePlace(id,idKinonich);
			} 
			if (button1.getStyleClass().contains("booked_checked")) {
				result=0;
				rest.setText("0");
				summ.setText("0");
				getMoney.setText("0");
				counter++;
				Integer id = Integer.parseInt(button1.getId());
//				AllButtons.allButtons.get(id).getStyleClass().removeAll("booked_checked", "booked", "clicked");
				PlacesService.deletePlace(id,idKinonich);
			}
		}
		if(counter==0){
			Alert alert = new Alert(AlertType.ERROR);
			//alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Error");
			alert.setHeaderText("");
			alert.setContentText("Не вибрано жодного місця!");
			alert.showAndWait();
			counter=0;
		}
		updateBookedPlaces(AllButtons.allButtons);
		updatePaidPlaces(AllButtons.allButtons);
	}

	@FXML
	public void onPaidButtonClicked() {
		int counter=0;
		for (Iterator<Node> iter = AllButtons.allButtons.iterator(); iter.hasNext();) {
			Button button = (Button) iter.next();
			if (button.getStyleClass().contains("booked_checked")) {
				counter++;
				button.getStyleClass().add("paid");
				idPlace = Integer.parseInt(button.getId());
				PlacesService.chooseToPaid(idPlace,idKinonich,isOnlinePaid.isSelected());
				result=0;
				rest.setText("0");
				summ.setText("0");
				getMoney.setText("0");
				
			}
		}
		if(counter==0){
			Alert alert = new Alert(AlertType.ERROR);
			//alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Error");
			alert.setHeaderText("");
			alert.setContentText("Не вибрано жодного місця!");
			alert.showAndWait();
			counter=0;
		}
		updateBookedPlaces(AllButtons.allButtons);
		updatePaidPlaces(AllButtons.allButtons);
		updateBookedPlaces(AllButtons.allButtons);
		updatePaidPlaces(AllButtons.allButtons);
	}

	@FXML
	public void onRefreshButtonClick(){
		
	}
	@FXML
	public void showOnlinePaidButtonClick(){
		Connection connection = ConnectionDataBase.getInstance();
		try {
			int id;
			PreparedStatement prepareStatement = connection.prepareStatement(GET_ONLINEPAID_STATEMENT);
			prepareStatement.setInt(1, idKinonich);
			prepareStatement.setString(2, AllButtons.nameZal);
			prepareStatement.setString(3, "true");
			ResultSet setOfOnlinePaidPlaces =  prepareStatement.executeQuery();
			while(setOfOnlinePaidPlaces.next()){
				id = setOfOnlinePaidPlaces.getInt(1);
				if(AllButtons.allButtons.get(id-1).getStyleClass().contains("showOnlinePaid")){
					AllButtons.allButtons.get(id-1).getStyleClass().remove("showOnlinePaid");
				}else{
					AllButtons.allButtons.get(id-1).getStyleClass().add("showOnlinePaid");
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@FXML
	public void onMouseClicked(MouseEvent e) {// Обробка кліків по кнопці
		Button button = (Button) e.getSource();
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
			subSumm(Integer.valueOf(button.getId()));
			giveRest();
		} else if (button.getStyleClass().contains("booked")) {
			button.getStyleClass().add("booked_checked");
			getSumm(button);
			giveRest();
		} else if (button.getStyleClass().contains("clicked")) {
			button.getStyleClass().removeAll("clicked");
		} else {
			button.getStyleClass().add("clicked");
		}
	}
	@FXML
	public void changedText(){
		giveRest();
	}
	public void getSumm(Button button){
		Connection connection = ConnectionDataBase.getInstance();
		try {
			
			PreparedStatement prepareStatement = connection.prepareStatement(GET_PRICES_STATEMENT);
			prepareStatement.setInt(1, idKinonich);
			prepareStatement.setString(2, AllButtons.nameZal);
			prepareStatement.setInt(3, Integer.valueOf(button.getId()));
			ResultSet setOfBookedPlaces =  prepareStatement.executeQuery();
			while(setOfBookedPlaces.next()){
				if (button.getStyleClass().contains("paid")){
					System.out.println("here");
				}
				result+=setOfBookedPlaces.getInt(5);
				summ.setText(String.valueOf(result));
				
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void subSumm(int idOfPlace){
		Connection connection = ConnectionDataBase.getInstance();
		try {
			
			PreparedStatement prepareStatement = connection.prepareStatement(GET_PRICES_STATEMENT);
			prepareStatement.setInt(1, idKinonich);
			prepareStatement.setString(2, AllButtons.nameZal);
			prepareStatement.setInt(3, idOfPlace);
			ResultSet setOfBookedPlaces =  prepareStatement.executeQuery();
			while(setOfBookedPlaces.next()){
				result-=setOfBookedPlaces.getInt(5);
				summ.setText(String.valueOf(result));
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void giveRest(){
		if(getMoney.getText().equalsIgnoreCase("")||getMoney.getText().equalsIgnoreCase(" ")){
			rest.setText("0");
		}else{
			try{
				int money = Integer.valueOf(getMoney.getText());
				int restMoney = money - Integer.valueOf(summ.getText());
				if (restMoney<0) {
					rest.setText("0");
				}else{
					rest.setText(String.valueOf(restMoney));
				}
			}catch (NumberFormatException e){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("");
				alert.setContentText("Не коректно введено поле: \"Отримано\" ");
				alert.showAndWait();
			}
		}
	}
	
	@FXML
	public void onButtonMouseEntered(MouseEvent e){
		Button button = (Button) e.getSource();
		messageTextArea.setText(PlacesService.getInformation(button,idKinonich));
	}
	
	@FXML
	public void onAnchorPaneEntered(){
		messageTextArea.setText(PlacesService.getInformationAboutKinonich(0,AllButtons.nameZal)
				+PlacesService.getInformationAboutKinonich(1,AllButtons.nameZal)
				+PlacesService.getSumm(idKinonich, AllButtons.nameZal)+"\n"
				+PlacesService.getSummAboutOnlinePaid(idKinonich, AllButtons.nameZal));
		
	}
	
	@FXML
	public void onClickMethod() {
		
	}
	public void listener(){
		zal.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				switch (newValue.intValue()) {
				case 0:{
					AllButtons.allButtons.clear();
					AllButtons.nameZal = "Синій зал";
					Main.showPersonOverview();					
					updateBookedPlaces(AllButtons.allButtons);
					updatePaidPlaces(AllButtons.allButtons);
					break;
				}
				case 1:{
					AllButtons.nameZal = "Червоний зал";
					try {
						AllButtons.allButtons.clear();
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(Main.class.getResource("RedZal.fxml"));
						AnchorPane parent = (AnchorPane)loader.load();
						List<Node> buttons = new ArrayList<>();
						ArrayList<Node> allNodes = Main.getAllNodes(parent);
						for (Node node : allNodes) {
							if (node.getStyleClass().contains("sit") ) {
								buttons.add(node);
								AllButtons.allButtons.add(node);
							}
						}
						updateBookedPlaces(AllButtons.allButtons);
						updatePaidPlaces(AllButtons.allButtons);
						Main.getRootLayout().setCenter(parent);
						
//						labelZal.setText(AllButtons.nameZal);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
					break;
				}
				case 2:{
					AllButtons.nameZal = "Сихів";	
					try {
						AllButtons.allButtons.clear();
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(Main.class.getResource("Syhiv.fxml"));
						ScrollPane scrollPane = (ScrollPane)loader.load();
						AnchorPane personOverview = (AnchorPane) (scrollPane.getContent());
						List<Node> buttons = new ArrayList<>();
						ArrayList<Node> allNodes = Main.getAllNodes(personOverview);
						for (Node node : allNodes) {
							if (node.getStyleClass().contains("sit") ) {
								buttons.add(node);
								AllButtons.allButtons.add(node);
							}
						}
						updateBookedPlaces(AllButtons.allButtons);
						updatePaidPlaces(AllButtons.allButtons);
						Main.getRootLayout().setCenter(scrollPane);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
					break;
				}
				case 3:{
					AllButtons.nameZal = "Копернік";	
					try {
						AllButtons.allButtons.clear();
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(Main.class.getResource("Kopernik.fxml"));
						ScrollPane scrollPane = (ScrollPane)loader.load();
						AnchorPane personOverview = (AnchorPane) (scrollPane.getContent());
						List<Node> buttons = new ArrayList<>();
						ArrayList<Node> allNodes = Main.getAllNodes(personOverview);
						for (Node node : allNodes) {
							if (node.getStyleClass().contains("sit") ) {
								buttons.add(node);
								AllButtons.allButtons.add(node);
							}
						}
						updateBookedPlaces(AllButtons.allButtons);
						updatePaidPlaces(AllButtons.allButtons);
						Main.getRootLayout().setCenter(scrollPane);
					} catch (IOException e) {
						e.printStackTrace();
					}					
					break;
				}
				default:
					break;
				}
				
			}
		});
	}
	@FXML
	public void initialize() {
		administrators.setValue("Організатори");
		administrators.setItems(admins);
		zal.setValue(AllButtons.nameZal);
		zal.setItems(zals);
		listener();
		getMaxKinonich();	
		refresh.setOnAction(event -> {
			updateBookedPlaces(AllButtons.allButtons);
			updatePaidPlaces(AllButtons.allButtons);
        });
		AllButtons.setTooltips(refresh, showOnlinePaidButton, bookButton, rebookButton, paidButton);
//		Timeline ticker = new Timeline(
//         new KeyFrame(
//             Duration.seconds(60), 
//             event -> refresh.fire()
//        	)
//	     );
//	     ticker.setCycleCount(1000000);
//	     ticker.play();
	}
	
	public static void updateBookedPlaces(List<Node> buttons){
		Connection connection = ConnectionDataBase.getInstance();
		setProgressIndicator();
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(UPDATE_BOOKPLACES_STATEMENT);
			prepareStatement.setInt(1, idKinonich);
			prepareStatement.setString(2, AllButtons.nameZal);
			ResultSet setOfBookedPlaces =  prepareStatement.executeQuery();
			while(setOfBookedPlaces.next()){
				int set = setOfBookedPlaces.getInt(1);
				nowBookedSits.add(set);
				for (Iterator<Node> iterButton = buttons.iterator(); iterButton.hasNext();) {
					Button button = (Button) iterButton.next();
						if(Integer.parseInt(button.getId())!=set){
							if(button.getStyleClass().contains("booked")){	
								button.getStyleClass().removeAll("booked","booked_checked","clicked");
							}
						}
				}
			}
			for (Integer integer : nowBookedSits) {
					if(buttons.get(integer-1).getStyleClass().contains("booked")){
					}else{
						buttons.get(integer-1).getStyleClass().addAll("booked");
					}
			}
			if(nowBookedSits.isEmpty()==true){
				for (Iterator<Node> iterButton = buttons.iterator(); iterButton.hasNext();) {
					Button button = (Button) iterButton.next();
						if(button.getStyleClass().contains("booked")){	
							button.getStyleClass().removeAll("booked","booked_checked","clicked");
						}
				}	
				
			}
		
			nowBookedSits.clear();
			prepareStatement.execute();
//			setProgressIndicator(1.0f);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void updatePaidPlaces(List<Node> buttons){
		Connection connection = ConnectionDataBase.getInstance();
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(UPDATE_PAIDPLACES_STATEMENT);
			prepareStatement.setInt(1, idKinonich);
			prepareStatement.setString(2, AllButtons.nameZal);
			ResultSet setOfPaidPlaces =  prepareStatement.executeQuery();
			while(setOfPaidPlaces.next()){
				int set = setOfPaidPlaces.getInt(1);
				nowPaidSits.add(set);
				for (Iterator<Node> iterButton = buttons.iterator(); iterButton.hasNext();) {
					Button button = (Button) iterButton.next();
					if(Integer.parseInt(button.getId())!=set){
						if(button.getStyleClass().contains("paid")){
							button.getStyleClass().removeAll("paid_checked","paid","paidButtonStyle","showOnlinePaid");
						}
					}
				}
			}
			for (Integer integer : nowPaidSits) {
				if(buttons.get(integer-1).getStyleClass().contains("paid")){
				}else{
					buttons.get(integer-1).getStyleClass().addAll("paid");
				}
			}
			if(nowPaidSits.isEmpty()==true){
				for (Iterator<Node> iterButton = buttons.iterator(); iterButton.hasNext();) {
					Button button = (Button) iterButton.next();
						if(button.getStyleClass().contains("paid")){	
							button.getStyleClass().removeAll("paid_checked","paid","booked_checked","booked");
						}
				}	
				
			}
					
			
			nowPaidSits.clear();
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
	
	public static void setProgressIndicator(){		
			final ProgressIndicator pin= new ProgressIndicator(-1.0f);
			pin.toFront();
			Scene scene = new Scene(pin);
			scene.setFill(Color.TRANSPARENT);
			pin.setStyle("-fx-background-color: transparent ;-fx-progress-color: black ;");
			pin.setOpacity(1);
			Stage stage = new Stage();
			stage.initStyle(StageStyle.TRANSPARENT);
			stage.setScene(scene);
			stage.sizeToScene();
			stage.show();
			FadeTransition ft = new FadeTransition(Duration.millis(450), pin);
		    ft.setCycleCount(2);
		    ft.setAutoReverse(true);
		    ft.setOnFinished(new EventHandler<ActionEvent>() {
		        
		        @Override
		        public void handle(ActionEvent arg0) {
		        	stage.hide();
		        
		        }
		    });
		    ft.play();
		}
	
}

package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class RootLayoutController{
	 // Reference to the main application
    @FXML
    private MenuItem showResults;
    @FXML
	private TextArea messageTextArea;
	@FXML
	private BorderPane borderPane;
	private Main main;
	private MenuChooseIdControler children; 
	static Stage appStageForPhones = new Stage();
	 // Ссылка на контроллер поражаемой формы
    
    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMain(Main main) {
        this.main = main;
    }
	
	@FXML
	public void addKinonich(ActionEvent event) throws IOException{
		
		try {
	        // Load root layout from fxml file.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(Main.class
	                .getResource("MenuChooseId.fxml"));
	        Parent parent = loader.load();

	        // Show the scene containing the root layout.
	        Scene scene = new Scene(parent);
//	        Stage appStage = (Stage) (((Node) (event.getSource())).getScene().getWindow());
	        Stage appStage = new Stage();
	        appStage.getIcons().add(
					   new Image(
					      Main.class.getResourceAsStream( "icon1.png" )));
	        appStage.setTitle("№ кіноночі");
	        appStage.setScene(scene);
	        
	        children = loader.getController();  // Теперь текущий контроллер "знает" о существовании "потомка"
	        children.setParent(this);
	        
	        appStage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		MenuChooseIdControler.alertMessage = "Кіноніч успішно додано!";
		
	}
	
	@FXML
	public void showKinonich(ActionEvent event) throws IOException{
		try {
	        // Load root layout from fxml file.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(Main.class
	                .getResource("MenuShowKinonich.fxml"));
	        Parent parent = loader.load();

	        // Show the scene containing the root layout.
	        Scene scene = new Scene(parent);
//	        Stage appStage = (Stage) (((Node) (event.getSource())).getScene().getWindow());
	        Stage appStage = new Stage();
	        appStage.getIcons().add(
					   new Image(
					      Main.class.getResourceAsStream( "icon1.png" )));
	        appStage.setTitle("№ кіноночі");
	        appStage.setScene(scene);
	        
//	        children = loader.getController();  // Теперь текущий контроллер "знает" о существовании "потомка"
//	        children.setParent(this);
	        
	        appStage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		
	}
	@FXML
	public void deleteKinonich(ActionEvent event) throws IOException{
//		try {
//	        // Load root layout from fxml file.
//	        FXMLLoader loader = new FXMLLoader();
//	        loader.setLocation(Main.class
//	                .getResource("MenuChooseId.fxml"));
//	        Parent parent = loader.load();
//
//	        // Show the scene containing the root layout.
//	        Scene scene = new Scene(parent);
////	        Stage appStage = (Stage) (((Node) (event.getSource())).getScene().getWindow());
//	        Stage appStage = new Stage();
//	        appStage.setTitle("№ кіноночі");
//	        appStage.setScene(scene);
//	        
//	        children = loader.getController();  // Теперь текущий контроллер "знает" о существовании "потомка"
//	        children.setParent(this);
//	        
//	        appStage.show();
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	    }
	}
	@FXML
	public void onShowResultsClick(ActionEvent event) throws IOException{
		try {
	        // Load root layout from fxml file.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(Main.class
	                .getResource("ShowResults.fxml"));
	        Parent parent = loader.load();

	        // Show the scene containing the root layout.
	        Scene scene = new Scene(parent);
//	        Stage appStage = (Stage) (((Node) (event.getSource())).getScene().getWindow());
	        Stage appStage = new Stage();
	        appStage.setTitle("Результати");
	        appStage.setScene(scene);
	        appStage.getIcons().add(
					   new Image(
					      Main.class.getResourceAsStream( "icon1.png" )));
//	        children = loader.getController();  // Теперь текущий контроллер "знает" о существовании "потомка"
//	        children.setParent(this);
	        
	        appStage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	@FXML
	public void onShowPhonesClick(ActionEvent event) throws IOException{
		try {
	        // Load root layout from fxml file.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(Main.class
	                .getResource("PhoneNumbers.fxml"));
	        Parent parent = loader.load();

	        // Show the scene containing the root layout.
	        Scene scene = new Scene(parent);
//	        Stage appStage = (Stage) (((Node) (event.getSource())).getScene().getWindow());
	        
	        appStageForPhones.setTitle("Номери телефонів");
	        appStageForPhones.setScene(scene);
	        appStageForPhones.getIcons().add(
					   new Image(
					      Main.class.getResourceAsStream( "icon1.png" )));
//	        children = loader.getController();  // Теперь текущий контроллер "знает" о существовании "потомка"
//	        children.setParent(this);
	        
	        appStageForPhones.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	@FXML
	public void changeData(ActionEvent event) throws IOException{
		try {
	        // Load root layout from fxml file.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(Main.class
	                .getResource("ChangeData.fxml"));
	        Parent parent = loader.load();

	        // Show the scene containing the root layout.
	        Scene scene = new Scene(parent);
	        Stage appStage = new Stage();
	        appStage.getIcons().add(
					   new Image(
					      Main.class.getResourceAsStream( "icon1.png" )));
	        appStage.setTitle("Змінити дані");
	        appStage.setScene(scene);

	        
	        appStage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	@FXML
	public void onHelpButtonClick(ActionEvent event) throws IOException{
		try {
	        // Load root layout from fxml file.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(Main.class
	                .getResource("HelpMenu.fxml"));
	        Parent parent = loader.load();

	        // Show the scene containing the root layout.
	        Scene scene = new Scene(parent);
	        Stage appStage = new Stage();
	        appStage.getIcons().add(
					   new Image(
					      Main.class.getResourceAsStream( "icon1.png" ))); 
	        appStage.setTitle("Допомога");
	        appStage.setScene(scene);

	        
	        appStage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
}

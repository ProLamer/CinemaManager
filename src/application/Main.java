package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

	private static Stage primaryStage;
	private static BorderPane rootLayout;
	public static BorderPane getRootLayout() {
		return rootLayout;
	}
	public static void setRootLayout(BorderPane rootLayout) {
		Main.rootLayout = rootLayout;
	}

	public static VBox vBox;
	

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Бронювання квитків");
		showLoginForm();
				
		primaryStage.getIcons().add(
				   new Image(
				      Main.class.getResourceAsStream("icon1.png" ))); 
	}

	/**
	 * Initializes the root layout.
	 */
	public void showLoginForm(){
		try {
	        // Load root layout from fxml file.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(Main.class
	                .getResource("LoginForm.fxml"));
	        AnchorPane parent = (AnchorPane)loader.load();
	        Scene scene = new Scene(parent);
	        primaryStage.setScene(scene);
	        primaryStage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	public void initRootLayout() {
		try {
	        // Load root layout from fxml file.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(Main.class
	                .getResource("RootLayout1.fxml"));
	        rootLayout = (BorderPane) loader.load();

	        // Show the scene containing the root layout.
	        Scene scene = new Scene(rootLayout);
	        primaryStage.setScene(scene);
	        primaryStage.centerOnScreen();
	        // Give the controller access to the main app.
	        RootLayoutController controller = loader.getController();
	        controller.setMain(this);
	        primaryStage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	/**
	 * Shows the person overview inside the root layout.
	 */

	public static ArrayList<Node> getAllNodes(Parent root) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		addAllDescendents(root, nodes);
		return nodes;
	}

	private static void addAllDescendents(Parent parent, ArrayList<Node> nodes) {
		for (Node node : parent.getChildrenUnmodifiable()) {
			nodes.add(node);
			if (node instanceof Parent)
				addAllDescendents((Parent) node, nodes);
		}
	}

	public static void showPersonOverview() {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("PersonOverview2.fxml"));
			ScrollPane scrollPane = (ScrollPane)loader.load();
			AnchorPane personOverview = (AnchorPane) (scrollPane.getContent());
//			personOverview.getStyleClass().add("background");
			vBox = (VBox) personOverview.getChildrenUnmodifiable().get(0);
			List<Node> buttons = new ArrayList<>();
			ArrayList<Node> allNodes = getAllNodes(personOverview);
			for (Node node : allNodes) {
				if (node.getStyleClass().contains("sit") ) {
					buttons.add(node);
					AllButtons.allButtons.add(node);
				}
			}
			Controller.updateBookedPlaces(buttons);
			Controller.updatePaidPlaces(buttons);
			// Set person overview into the center of root layout.
			rootLayout.setCenter(scrollPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the main stage.
	 * 
	 * @return
	 */
	public static Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
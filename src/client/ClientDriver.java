package client;

import java.io.IOException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * ClientDriver - Displays Login Screen
 * 
 * @author Michael Mamuric
 * @version 1.0
 * Feb 1, 2020
 * 
 */
public class ClientDriver extends Application {
	
	private TextField txtIPAddress;
	private TextField txtName;
	private Pane pane;
	private Label lblWelcome, lblName, lblIP;
	private Button btnLoginSubmit;
	private Image gameImg;
	private ImageView gameImgView;
	
	public static final int PORT_NUMBER = 3333;
	
	
	/**
	 * Main method - Launches JavaFX Application
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Creates and opens a socket to be used for client connections.
	 * 
	 * @return socket
	 */
	
	private Socket openSocket() {
		String name         = txtName.getText().trim();
		String ipAddress    = txtIPAddress.getText().trim();
		Socket clientSocket = null;
		
		if(name.isEmpty() && ipAddress.isEmpty()) {
			Alert isNull = new Alert(AlertType.ERROR);
			isNull.setContentText("Please fill in the username and IP address.");
			isNull.showAndWait();
		}
		else {
			try {
				clientSocket = new Socket(ipAddress, PORT_NUMBER);
			} catch (IOException e) {
				Alert err = new Alert(AlertType.ERROR);
				err.setContentText("Error encountered on setting connection. " + e.toString());
				err.showAndWait();
			}
		}
		
		return clientSocket;
	}
	
	/**
	 * Constructs the UI for the Login Window.
	 * 
	 * @return Pane object to be used by Scene object.
	 * @throws URISyntaxException
	 */
	
	private Pane constructUI() throws URISyntaxException {
		pane = new Pane();
		
		// Original source: https://gifimage.net/wp-content/uploads/2017/10/mario-and-luigi-dancing-gif-11.gif
		URL imgURL = getClass().getResource("/mario-luigi-dance.gif");
	
		gameImg = new Image(imgURL.toURI().toString());
		gameImgView = new ImageView(gameImg);
		gameImgView.setFitHeight(329);
		gameImgView.setFitWidth(608);
		gameImgView.setLayoutX(24);
		gameImgView.setLayoutY(25);
		
		lblWelcome = new Label("Welcome to Mario and Luigi Dash!");
		lblWelcome.getStyleClass().add("welcome");
		lblWelcome.setLayoutX(170);
		lblWelcome.setLayoutY(383);
		
		lblName = new Label("Name");
		lblName.setLayoutX(77);
		lblName.setLayoutY(449);
		
		txtName = new TextField();
		txtName.setLayoutX(121);
		txtName.setLayoutY(445);
		
		lblIP = new Label("IP Address");
		lblIP.setLayoutX(293);
		lblIP.setLayoutY(449);
		
		txtIPAddress = new TextField();
		txtIPAddress.setLayoutX(358);
		txtIPAddress.setLayoutY(445);
		
		btnLoginSubmit = new Button("Play");
		btnLoginSubmit.setLayoutX(528);
		btnLoginSubmit.setLayoutY(445);
			
		pane.getChildren().addAll(gameImgView, lblWelcome, lblName, lblIP, btnLoginSubmit, txtName, txtIPAddress);
		return pane;
	}
	
	/**
	 * Event Handler for the Play Button.
	 * @param stage Stage object of the Login Window.
	 * @return event handler object
	 */
	private EventHandler<ActionEvent> submitAction(Stage stage) {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Socket socket = openSocket();
				if(socket != null) {
					String userName = txtName.getText();
					stage.close();				
					
					ClientGUI g = new ClientGUI(userName, socket);
					try {
						g.start();
					} catch (URISyntaxException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
	}
	
	/**
	 * Starts the JavaFX Application.
	 * 
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.centerOnScreen();
		primaryStage.setResizable(false);
		primaryStage.setTitle("Mario and Luigi Dash");
		
		Scene myScene = new Scene(constructUI(), 643, 505);
		myScene.getStylesheets().add(getClass().getResource("/clientGUI.css").toString());
		primaryStage.setScene(myScene);
		
		// Submit button action
		btnLoginSubmit.setOnAction(submitAction(primaryStage));
		
		primaryStage.show();
	}

}

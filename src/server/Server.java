package server;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Server - Server Portal for the Game
 * 
 * @author Michael Mamuric
 * @version 1.0
 * Feb 1, 2020
 * 
 */
public class Server implements PropertyChangeListener
{
	private ServerSocket serverSocket = null;
	private Socket socket = null;
	private static final int SOCKET_LIST_SIZE = 2;
	private ArrayList<Socket> sockets = new ArrayList<>(SOCKET_LIST_SIZE);
	
	private Pane pane;
	private TextArea txtConnections;
	private Label lblGame, lblServer;
	private ImageView imgLogo;
	private Image img;
	
	/**
	 * Constructs UI for Server Portal
	 * @return Pane to be used by Scene
	 * @throws URISyntaxException
	 */
	private Pane constructUI() throws URISyntaxException {
		pane = new Pane();

		// Original source: https://media.tenor.com/images/191a25d14a0c5b893f65e13892a4b434/tenor.gif
		URL imgURL = getClass().getResource("/serverportal.gif");
		
		img = new Image(imgURL.toURI().toString());
		imgLogo = new ImageView(img);
		imgLogo.setLayoutX(24);
		imgLogo.setLayoutY(18);
		imgLogo.setFitWidth(95);
		imgLogo.setFitHeight(93);
		
		lblGame = new Label("Mario and Luigi Dash");
		lblGame.getStyleClass().add("server");
		lblGame.setLayoutX(142);
		lblGame.setLayoutY(26);
		
		lblServer = new Label("Server Portal");
		lblServer.getStyleClass().add("server");
		lblServer.setLayoutX(142);
		lblServer.setLayoutY(56);
				
		txtConnections = new TextArea();
		txtConnections.setEditable(false);
		txtConnections.setLayoutX(24);
		txtConnections.setLayoutY(126);
		txtConnections.setPrefHeight(261);
		txtConnections.setPrefWidth(337);
		
		pane.getChildren().addAll(imgLogo, lblGame, lblServer, txtConnections);
		
		return pane;
	}

	/**
	 * Start method of Server Portal Application
	 * @throws Exception
	 */
	public void start() throws Exception {
		Stage primaryStage = new Stage();
		
		primaryStage.centerOnScreen();
		primaryStage.setResizable(false);
		primaryStage.setTitle("Server Portal");
		
		Scene myScene = new Scene(constructUI(), 383, 398);
		myScene.getStylesheets().add(getClass().getResource("/clientGUI.css").toString());
		
		primaryStage.setScene(myScene);
		primaryStage.show();
		
		runServer();
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent arg0) {
				Alert confirm = new Alert(AlertType.CONFIRMATION);
				confirm.setTitle("Close the server?");
				confirm.setContentText("Closing the window would cause the server to terminate. Proceed anyway?");
				
				if(confirm.showAndWait().get() == ButtonType.OK) {
					Platform.exit();
					System.exit(0);
				}
				else
					arg0.consume();
			}
		});
	}
	
	/**
	 * Creates Server Socket object
	 * @return Server Socket object
	 */
	private ServerSocket createServerSocket() {
		if(serverSocket == null) {
			try	{
				serverSocket = new ServerSocket(3333);
				txtConnections.appendText("Server is up and running!\n");
			}
			catch (IOException e) {
				txtConnections.appendText("Server is already running!\n");
			}
		}
		
		return serverSocket;
	}
	
	/**
	 * Creates a runnable task which calls runServerInBackground to run method in separate thread.
	 */
	private void runServer() {
		Runnable task = new Runnable() {
			@Override
			public void run() {
				runServerInBackground();
			}
		};
		
		new Thread(task).start();
	}
	
	/**
	 * Runs the Server
	 * 
	 */
	private void runServerInBackground() {
		boolean connected = true;
		
		while(connected) {
			createServerSocket();
			try	{
				socket = serverSocket.accept();
				txtConnections.appendText("Accepted a client connection. IP address: " + socket.getInetAddress() + "\n");
				sockets.add(socket);
									
				if(sockets.size() == 2) {					
					ClientHandler c = new ClientHandler(sockets.get(0), sockets.get(1), this);
					new Thread(c).start();
					sockets.clear();
				}
			}
			catch (IOException e) {
				connected = false;
			}
		}
		
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		serverSocket = null;
		txtConnections.appendText("Server is now turned off.\n");
	}

	/**
	 * PropertyChange method of Server
	 * 
	 */
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		// Confirm if two clients have been connected to the server
		txtConnections.appendText(arg0.getNewValue() +  "\n");
	}
}

package server;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Driver for Server class
 * 
 * @author Michael Mamuric
 * @version 1.0
 * Feb 1, 2020
 *
 */
public class ServerDriver extends Application {
	
	/**
	 * Launches JavaFX Application
	 * @param args
	 */
	
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Makes call to Server start method in order to launch GUI
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		Server s = new Server();
		s.start();
	}

}

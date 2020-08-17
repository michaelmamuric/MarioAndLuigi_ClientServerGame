package utility;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles input data across two simultaneous Client GUIs
 * 
 * @author Michael Mamuric
 * @version 1.0
 * Feb 1, 2020
 *
 */
public class InputListener implements Runnable {
	
	// attributes
	private Socket socket;
	private ObjectInputStream ois;
	private int number;
	private List<PropertyChangeListener> observers = new ArrayList<>();
	
	/**
	 * InputListener constructor for one instance of the game GUI. Called when one of the players connect.
	 * @param socket Socket of one of the player's instance
	 * @param observer Observer of InputListener class
	 */
	
	public InputListener(Socket socket, PropertyChangeListener observer) {
		this.number = 0;
		this.socket = socket;
		observers.add(observer);
	}
	
	/**
	 * InputListener constructor for two instance of the game GUI. Called when both of the players connect.
	 * @param number Number identifier for user
	 * @param socket Socket of one of the player's instance
	 * @param observer Observer of InputListener class
	 */
	
	public InputListener(int number, Socket socket, PropertyChangeListener observer) {
		this.number = number;
		this.socket = socket;
		observers.add(observer);
	}
	
	
	/**
	 * Returns the number identifier of this InputListener instance
	 * @return Number
	 */
	
	public int getNumber() {
		return number;
	}

	/**
	 * Run method of InputListener
	 */
	
	@Override
	public void run() {
		boolean isConnected = true;
		
		try {
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e1) {
			isConnected = false;
		}
		
		while(isConnected) {		
			try {
				Object msg = ois.readObject();
				notifyObservers(msg);
			} catch (ClassNotFoundException | IOException e) {
				isConnected = false;
			}
		}
	}
	
	/**
	 * Notifies the observer of the InputListener class.
	 * @param msg
	 */
	private synchronized void notifyObservers(Object msg) {
		for (PropertyChangeListener list : observers) {
			list.propertyChange(new PropertyChangeEvent(this, null, socket, msg));
		}
	}
}

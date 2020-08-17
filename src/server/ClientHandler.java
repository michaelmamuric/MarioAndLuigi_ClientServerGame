package server;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import utility.InputListener;

/**
 * Handles two simultaneous instances of the main game window
 * 
 * @author Michael Mamuric
 * @version 1.0
 * Feb 1, 2020
 */

public class ClientHandler implements Runnable, PropertyChangeListener {
	
	// attributes
	private Socket socket1, socket2;
	private ObjectOutputStream oos1;
	private ObjectOutputStream oos2;
	private InputListener lis1, lis2;
	private PropertyChangeListener gameServer;

	/**
	 * Creates ObjectOutputStreams for the two connected players and initializes InputListener thread
	 * 
	 * @param socket1 Socket for Player 1
	 * @param socket2 Socket for Player 2
	 * @param observer Observer class of ClientHandler
	 */
	public ClientHandler(Socket socket1, Socket socket2, PropertyChangeListener observer) {
		this.socket1 = socket1;
		this.socket2 = socket2;
		gameServer = observer;
		
		// Notify server that two clients have been connected and can play with each other.
		gameServer.propertyChange(new PropertyChangeEvent(this, null, null, "Two clients have been connected!"));
				
		try {
			oos1 = new ObjectOutputStream(this.socket1.getOutputStream());
			oos2 = new ObjectOutputStream(this.socket2.getOutputStream());
			
			// Set Player numbers
			oos1.writeObject(1);
			oos2.writeObject(2);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		lis1 = new InputListener(1, socket1, this);
		lis2 = new InputListener(2, socket2, this);
	}

	/**
	 * Start InputListener threads.
	 */
	
	@Override
	public void run() {
		new Thread(lis1).start();
		new Thread(lis2).start();
	}
	
	/**
	 * PropertyChange handler for ClientHandler
	 */

	@Override
	public synchronized void propertyChange(PropertyChangeEvent evt) {
		Object obj = evt.getSource();
		Object msg = evt.getNewValue();
		
		if(obj instanceof InputListener)
		{	
			InputListener listenerNumber = (InputListener) obj;
			
			if(listenerNumber.getNumber() == 1) {
				try {
					oos2.writeObject(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} 
			else if(listenerNumber.getNumber() == 2) {
				try {
					oos1.writeObject(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}

package client;

import java.io.Serializable;

/**
 * GameWinner - Creates instance of GameWinner when a player wins a game.
 * 
 * @author Michael Mamuric
 * @version 1.0
 * Feb 1, 2020
 * 
 */

public class GameWinner implements Serializable {
	
	private static final long serialVersionUID = -8814756221977822044L;
	int playerNumber;
	String user, msg;
	
	/**
	 * GameWinner - Creates a game winner that is created when a player wins the game.
	 * Has parameters for the Player Number and User Name. A default message is provided.
	 * 
	 * @param playerNumber Player Number assigned to user
	 * @param user User name
	 */
	public GameWinner(int playerNumber, String user) {
		this.playerNumber = playerNumber;
		this.user = user;
		this.msg = "(Player # " + playerNumber + ") is the winner! Congratulations! [To play again, press ENTER].";
	}
	
	/**
	 * GameWinner - Creates a game winner that is created when a player wins the game.
	 * Has parameters for the Player Number and User Name, and a custom message to be displayed.
	 * 
	 * @param playerNumber Player Number assigned to user
	 * @param user User name
	 */
	public GameWinner(int playerNumber, String user, String msg) {
		this.playerNumber = playerNumber;
		this.user = user;
		this.msg = msg;
	}

	/**
	 * Gets the user name
	 * @return UserName
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Sets the user name
	 * @param user
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Gets the message 
	 * @return
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * Sets the message
	 * @param msg
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	/**
	 * toString method of GameWinner object
	 * 
	 */
	@Override
	public String toString() {
		return getUser() + " " + getMsg();
	}

}

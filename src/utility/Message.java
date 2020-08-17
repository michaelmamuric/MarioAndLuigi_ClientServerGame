package utility;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Michael Mamuric
 * @version 1.0
 * Feb 1, 2020
 *
 * Class Description: A basic message class that can be transported across
 * the network.
 * 
 */
public class Message implements Serializable
{
	//Constants
	static final long serialVersionUID = 5488945625178844229L;
	//Attributes
	private String user;
	private String msg;
	private Date   timeStamp;
	
	/**
	 * Default Constructor
	 */
	public Message() {}

	/**
	 * Creates an instance of Message with user, msg, and timestamp parameters
	 * @param user User/player name
	 * @param msg Message
	 * @param timeStamp Time message was sent
	 */
	public Message(String user, String msg, Date timeStamp)
	{
		this.user = user;
		this.msg = msg;
		this.timeStamp = timeStamp;
	}

	/**
	 * @return the user
	 */
	public String getUser()
	{
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user)
	{
		this.user = user;
	}

	/**
	 * @return the msg
	 */
	public String getMsg()
	{
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	/**
	 * @return the timeStamp
	 */
	public Date getTimeStamp()
	{
		return timeStamp;
	}

	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(Date timeStamp)
	{
		this.timeStamp = timeStamp;
	}
	
	//Operational Methods
	public String toString()
	{
		return "[" + getUser() + ", " + getTimeStamp() + "]: " + getMsg();
	}
}

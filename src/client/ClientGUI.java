package client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utility.InputListener;
import utility.Message;

/**
 * ClientGUI - Displays the Main Game Screen
 * 
 * @author Michael Mamuric
 * @version 1.0
 * Feb 1, 2020
 */
public class ClientGUI implements PropertyChangeListener {
	
	private Stage stage;
	private Socket socket;
	private ObjectOutputStream oos;
	private InputListener lis;
	private Label lblChat, lblMsg, lblP1, lblP2;
	private TextField txtChatMsg, txtTopBar;
	private Button btnMsgSend;
	private TextArea txtAreaChatList;
	private Pane pane;
	private String userName;
	private int playerNumber;

	private Image player1Img, player2Img, trackImg, finishLine;
	private ImageView p1, p2, track, finish;
	
	private static final int START_LINE_X = 34;
	private static final int FINISH_LINE_X = 478;
	private static final int RUN_FORWARD = 3;
	private static final String WAIT_CONNECTION = "Waiting for Player 2. Hang on tight!";
	
	boolean gameOver = false;

	/**
	 * Default constructor for ClientGUI. Creates the ObjectOutputStream and starts the InputListener thread.
	 * 
	 * @param userName Name of the user
	 * @param socket Socket of the user used to connect
	 */
	public ClientGUI(String userName, Socket socket) {		
		this.userName = userName;
		this.socket = socket;
		
		try {
			oos = new ObjectOutputStream(this.socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		lis = new InputListener(socket, this);
		new Thread(lis).start();
	}
		
	/**
	 * Constructs the UI of the Main Game Window.
	 * 
	 * @return Pane to be used by the Scene object.
	 * @throws URISyntaxException
	 */
	private Pane gameUI() throws URISyntaxException {
		pane = new Pane();
		
		URL trackImgURL   = getClass().getResource("/track.jpg");
		// Original source: https://i.pinimg.com/originals/39/63/56/39635620bc39fd89280af66645b69d80.gif
		URL p1URL         = getClass().getResource("/p1.gif");
		// Original source: https://media.giphy.com/media/zLw4usvYkwIbC/giphy.gif
		URL p2URL         = getClass().getResource("/p2.gif");
		// Original source: https://t1.rbxcdn.com/4af428d0a25239333eb299ef04d766c0
		URL finishLineURL = getClass().getResource("/finishline.png");
		
		txtTopBar = new TextField(WAIT_CONNECTION);
		txtTopBar.getStyleClass().add("topBar");
		txtTopBar.setFocusTraversable(false);
		txtTopBar.setLayoutX(29);
		txtTopBar.setLayoutY(30);
		txtTopBar.setEditable(false);
		txtTopBar.setPrefHeight(32);
		txtTopBar.setPrefWidth(593);
		
		trackImg = new Image(trackImgURL.toURI().toString());
		track = new ImageView(trackImg);
		track.setFocusTraversable(false);
		track.setFitHeight(161);
		track.setFitWidth(469);
		track.setLayoutX(29);
		track.setLayoutY(83);
		
		player1Img = new Image(p1URL.toURI().toString());
		p1 = new ImageView(player1Img);
		p1.setFitHeight(57);
		p1.setFitWidth(57);
		p1.setLayoutX(START_LINE_X);
		p1.setLayoutY(95);
		
		player2Img = new Image(p2URL.toURI().toString());
		p2 = new ImageView(player2Img);
		p2.setFitHeight(57);
		p2.setFitWidth(57);
		p2.setLayoutX(START_LINE_X);
		p2.setLayoutY(176);
		
		finishLine = new Image(finishLineURL.toURI().toString());
		finish = new ImageView(finishLine);
		finish.setFocusTraversable(false);
		finish.setFitHeight(161);
		finish.setFitWidth(158);
		finish.setLayoutX(426);
		finish.setLayoutY(83);
		
		lblP1 = new Label("Player 1: Mario");
		lblP1.setFocusTraversable(false);
		lblP1.getStyleClass().add("p1");
		lblP1.setLayoutX(533);
		lblP1.setLayoutY(95);
		
		lblP2 = new Label("Player 2: Luigi");
		lblP2.setFocusTraversable(false);
		lblP2.getStyleClass().add("p2");
		lblP2.setLayoutX(533);
		lblP2.setLayoutY(176);
		
		lblChat = new Label("Chat");
		lblChat.setFocusTraversable(false);
		lblChat.setLayoutX(40);
		lblChat.setLayoutY(256);
		
		lblMsg = new Label("Your Message:");
		lblMsg.setFocusTraversable(false);
		lblMsg.setLayoutX(40);
		lblMsg.setLayoutY(282);
		
		txtChatMsg = new TextField();
		txtChatMsg.setFocusTraversable(false);
		txtChatMsg.setPrefWidth(440);
		txtChatMsg.setLayoutX(123);
		txtChatMsg.setLayoutY(278);
		
		btnMsgSend = new Button("Send");
		btnMsgSend.setFocusTraversable(false);
		btnMsgSend.setLayoutX(573);
		btnMsgSend.setLayoutY(278);
		
		txtAreaChatList = new TextArea();
		txtAreaChatList.setFocusTraversable(false);
		txtAreaChatList.setEditable(false);
		txtAreaChatList.setPrefHeight(175);
		txtAreaChatList.setPrefWidth(574);
		txtAreaChatList.setLayoutX(40);
		txtAreaChatList.setLayoutY(314);
		
		pane.getChildren().addAll(txtTopBar, track, finish, lblChat, lblMsg, txtChatMsg, btnMsgSend, txtAreaChatList, p1, p2,
				lblP1, lblP2);
	
		return pane;
	}
	
	/**
	 * Opens a Confirmation Window when user attempts to close the window.
	 * 
	 */
	
	private void askUserQuit() {
		Alert confirm = new Alert(AlertType.CONFIRMATION);
		confirm.setTitle("Uh-oh...");
		confirm.setContentText("The other player has left the game. Click OK to quit, or Cancel to stay and wait for another player.");
		ButtonType choice = confirm.showAndWait().get();
		
		if(choice == ButtonType.CANCEL) {
			try {
				InetAddress socketIP = socket.getInetAddress();
				socket.close();
				oos.close();
				playerNumber = 0;
				txtTopBar.setText(WAIT_CONNECTION);
				
				p1.setLayoutX(START_LINE_X);
				p2.setLayoutX(START_LINE_X);
				
				socket = new Socket(socketIP, ClientDriver.PORT_NUMBER);
				oos = new ObjectOutputStream(socket.getOutputStream());
				lis = new InputListener(socket, this);
				new Thread(lis).start();
				
			} catch (IOException e) {
				e.printStackTrace();
			}	
		} else {
			try {
				socket.close();
				oos.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
			Platform.exit();
			System.exit(0);		
		}
	}
	
	/**
	 * Asks user the option to play again once the game ends.
	 * @param playerNumber Player Number assigned to the user.
	 */
	
	private void askUserPlayAgain(int playerNumber) {
		Alert confirm = new Alert(AlertType.CONFIRMATION);
		confirm.setTitle("That was a good game!");
		confirm.setContentText("Would you like to play again?");
		ButtonType choice = confirm.showAndWait().get();
		
		if(choice == ButtonType.OK) {
			try {
				InetAddress socketIP = socket.getInetAddress();
				socket.close();
				oos.close();
				this.playerNumber = 0;
				txtTopBar.setText(WAIT_CONNECTION);
				
				p1.setLayoutX(START_LINE_X);
				p2.setLayoutX(START_LINE_X);
				
				socket = new Socket(socketIP, ClientDriver.PORT_NUMBER);
				oos = new ObjectOutputStream(socket.getOutputStream());
				lis = new InputListener(socket, this);
				new Thread(lis).start();
			}  catch (IOException e) {
				e.printStackTrace();
			}	
		}
			
	}
	
	/**
	 * Creates a runnable object that invokes askUserQuit. This is needed to make Confirmation Window run on the FX Thread.
	 * @param playerNumber
	 */
	
	private void reconnectUser(int playerNumber) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				askUserQuit();
			}
		});
	}

	/**
	 * Handles the different PropertyChangeEvents that window receives. 
	 * 
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Object newVal = evt.getNewValue();
		
		// Message from the other user
		if(newVal instanceof Message)
			txtAreaChatList.appendText(newVal + "\n");
		// X - when other client disconnects
		else if(newVal instanceof String) {
			if(newVal.equals("X")) {
				try {
					if(playerNumber != 0) {
						reconnectUser(playerNumber);
					}
					else {
						socket.close();
						oos.close();
						Platform.exit();
						System.exit(0);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		// int - Set player number when ClientHandler is invoked
		else if(newVal instanceof Integer) {
			String character;
			playerNumber = (int) newVal;
			
			if(playerNumber == 1) {
				character = "Mario";
			}
			else {
				character = "Luigi";
			}
			
			txtTopBar.setText("Welcome, " + userName + "! You're Player #" + playerNumber + ". Press ↓ and/or → to make " + character + " run!");
			gameOver = false;
		}
		// KeyEvent - When player presses down or right arrow keys
		else if(newVal instanceof KeyEvent)
		{	
			synchronized(this) {
				KeyCode code = ((KeyEvent)newVal).getCode();
					
				if(code == KeyCode.DOWN || code == KeyCode.RIGHT ) {
					if(playerNumber == 1)
						p2.setLayoutX(p2.getLayoutX() + RUN_FORWARD);
					else if(playerNumber == 2)
						p1.setLayoutX(p1.getLayoutX() + RUN_FORWARD);
				}
				else {
					((KeyEvent) newVal).consume();
				}
			}
		}
		else if(newVal instanceof GameWinner) {
			txtTopBar.setText(newVal.toString());
			gameOver = true;
		}
		
	}
	
	/**
	 * Event Handler for the Send Button in the chat section of the game.
	 * @return Event Handler
	 */
	
	private EventHandler<ActionEvent> sendMsgAction() {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Date    date = new Date();
				Message msg  = new Message(userName, txtChatMsg.getText(), date);
				try {
					if(playerNumber != 0) {
						oos.writeObject(msg);
						txtAreaChatList.appendText(msg + "\n");
					}
					txtChatMsg.setText("");
				} catch (IOException e) {
					e.printStackTrace();
				}				
			}
		};
	}
	
	/**
	 * Event Handler for closing the game window.
	 * @return Event Handler
	 */
	private EventHandler<WindowEvent> guiCloseAction() {
		return new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				Alert confirm = new Alert(AlertType.CONFIRMATION);
				confirm.setHeaderText("Close Window?");
				confirm.setContentText("Closing this window will cause the game to terminate. Proceed anyway?");
				
				if(confirm.showAndWait().get() == ButtonType.CANCEL)
					event.consume();
				else {
					try {
						if(playerNumber != 0)
							oos.writeObject("X");
						
						socket.close();
						oos.close();
						Platform.exit();
						System.exit(0);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
	}
	
	/**
	 * Event Handler for the keyboard events
	 * @return Event Handler
	 */
	
	private EventHandler<KeyEvent> moveCharacter() {		
		return new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				KeyCode code = event.getCode();
				try {
					if(playerNumber != 0) {
						if(!gameOver) {
							if(code == KeyCode.DOWN || code == KeyCode.RIGHT) {
								oos.writeObject(event);
											
								if(playerNumber == 1) {
									if(!p2.isFocused())
										p2.requestFocus();
									
									p1.setLayoutX(p1.getLayoutX() + RUN_FORWARD);
								}
								else if(playerNumber == 2) {
									if(!p1.isFocused())
										p1.requestFocus();
									
									p2.setLayoutX(p2.getLayoutX() + RUN_FORWARD);
								}
											
								if(p1.getLayoutX() >= FINISH_LINE_X) {
									GameWinner win = new GameWinner(playerNumber, userName);
									oos.writeObject(win);
									txtTopBar.setText(win.toString());
									gameOver = true;
								}
											
								if(p2.getLayoutX() >= FINISH_LINE_X) {
									GameWinner win = new GameWinner(playerNumber, userName);
									oos.writeObject(win);
									txtTopBar.setText(win.toString());
									gameOver = true;
								}
							}
						}
						else if(code == KeyCode.ENTER)
							askUserPlayAgain(playerNumber);
						
					} else
						event.consume();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
	}
	
	/**
	 * Start Method of JavaFX Game Application. Constructs the Stage and assigns event handlers.
	 * @throws URISyntaxException
	 * @throws IOException
	 */

	public void start() throws URISyntaxException, IOException {
		stage = new Stage();
		
		stage.centerOnScreen();
		stage.setResizable(false);
			
		Scene myScene = new Scene(gameUI(), 643, 505);
		myScene.getStylesheets().add(getClass().getResource("/clientGUI.css").toString());
		stage.setTitle("Welcome, " + userName + "!");
		stage.setScene(myScene);
			
		btnMsgSend.setOnAction(sendMsgAction());
		stage.setOnCloseRequest(guiCloseAction());
		
		synchronized(this) {
			myScene.setOnKeyReleased(moveCharacter());
		}
		
		stage.show();
	}
}

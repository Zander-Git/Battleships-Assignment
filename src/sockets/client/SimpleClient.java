package sockets.client;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JOptionPane;

import client.view.ClientMainUi;
import components.Cell;
import components.Cell.State;


public class SimpleClient implements Runnable {

	private Socket 					clientSocket;

	private ObjectOutputStream 		output;
	private ObjectInputStream 		input;

	private boolean 				stopClient;

	private Thread 					clientReader;

	private String 					host;
	private int 					port;
	
	ClientMainUi		guiClient;
	
	public SimpleClient(ClientMainUi guiClient) {
		this.guiClient = guiClient;
	}
	
	public void init(String host, int port) throws IOException {
		this.host = host;
		this.port = port;
		openConnection();
	}
	
	public SimpleClient(String host, int port) throws IOException {
		this.host = host;
		this.port = port;
		openConnection();
	}
	
	public void openConnection() throws IOException {

		// Create the sockets and the data streams
		try {

			this.clientSocket = new Socket(this.host, this.port);
			this.output = new ObjectOutputStream(this.clientSocket.getOutputStream());
			this.input = new ObjectInputStream(this.clientSocket.getInputStream());

		} catch (IOException ex) {
			try {
				closeAll();
			} catch (Exception exc) {
				System.err.println("[client: ] error in opening a connection to: " + this.host + " on port: " + this.port);
			}

			throw ex; 
		}
		
		// creates a Thread instance and starts the thread.
		this.clientReader = new Thread(this);
		this.stopClient = false;
		this.clientReader.start();

	}
	
	public void sendMessageToServer(String msg) throws IOException {
		if (this.clientSocket == null || this.output == null)
			throw new SocketException("socket does not exist");
		
		this.output.writeObject(msg);
		
	}
	

	public void handleMessageFromServer(String msg) {
        String[] arrOfStr = msg.split(",", 0); 
        String typeOfMessage = arrOfStr[0];
        int x = Integer.parseInt(arrOfStr[1]); 
        int y = Integer.parseInt(arrOfStr[2]); 
        
        Cell cellToCheck = guiClient.getMyBoardView().getCells(x, y);
        
        switch(typeOfMessage) {
        case "f":
        	//Receive a message to guess or "fire" on  coordinates x,y
            if (cellToCheck.getState() == State.CONTAINS_SHIP) {
            	cellToCheck.setState(State.HIT);
            	guiClient.getMyBoardView().getBoardModel().colourCellsInView(x, y, Color.RED);
            	String replyMessage = "h," + Integer.toString(x)+","+Integer.toString(y);
            	try {
            		sendMessageToServer(replyMessage);
            	}
            	catch (IOException ex){
            		System.err.println(ex);
            	}
            	guiClient.getEnemyBoardView().setUserTurn(false);
            }
            else {
            	cellToCheck.setState(State.MISSED);
            	guiClient.getMyBoardView().getBoardModel().colourCellsInView(x, y, Color.GRAY);
            	String replyMessage = "m," + Integer.toString(x)+","+Integer.toString(y);
            	try {
            		sendMessageToServer(replyMessage);
            	}
            	catch (IOException ex){
            		System.err.println(ex);
            	}
            	guiClient.getEnemyBoardView().setUserTurn(true);
            }
          break;
        case "m":
          // Receive "miss" response
        	guiClient.getEnemyBoardView().handleMiss(x, y);
          break;
        case "h":
            // Receive "hit" response
        	guiClient.getEnemyBoardView().handleHit(x, y);
            break;
        case "r":
            // Receive "Ready" response
        	if (guiClient.getMyBoardView().isUserReady()) {
        		if(!guiClient.getEnemyBoardView().isUserTurn()) {
        			guiClient.getEnemyBoardView().setUserTurn(true);
	        		try {
						sendMessageToServer("r,0,0");
					} catch (IOException e) {
						e.printStackTrace();
					}
        		}
        		else {
                	JOptionPane.showMessageDialog(guiClient.getMyBoardView(),"Start! :)",
        					"" , JOptionPane.INFORMATION_MESSAGE );
        		}
        	}
        	else {
            	JOptionPane.showMessageDialog(guiClient.getMyBoardView(),"Opponent ready :)",
    					"" , JOptionPane.INFORMATION_MESSAGE );
        		try {
					sendMessageToServer("n,0,0");
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
            break;
        case "n":
        	JOptionPane.showMessageDialog(guiClient.getMyBoardView(),"Opponent not ready yet :)",
					"" , JOptionPane.INFORMATION_MESSAGE );
        	break;
        case "g":
        	JOptionPane.showMessageDialog(guiClient.getMyBoardView(),"You lost :( Please close the game",
					"" , JOptionPane.INFORMATION_MESSAGE );
        	break;
      }


	}
	
	/**
	 * Close all connections
	 * @throws IOException
	 */
	private void closeAll() throws IOException {
		try {
			if (this.clientSocket != null)
				this.clientSocket.close();

			if (this.output != null)
				this.output.close();

			if (this.input != null)
				this.input.close();
			
		} finally {
			this.output = null;
			this.input = null;
			this.clientSocket = null;
		}
	}
	
	/**
	 * handles user inputs from the terminal. 
	 * This should run as a separate thread. In this case, main thread. 
	 * 
	 */
	public void runClient() {
		try {
			BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
			String message = null;

			while (true) {
				message = fromConsole.readLine();
				handleUserInput(message);
				if(message.equals("over"))
					break;
			}
			
			System.out.println("[client: ] stopping client...");
			this.stopClient = true;
			fromConsole.close();
		} catch (Exception ex) {
			System.out.println("[client: ] unexpected error while reading from console!");
		}

	}

	public void handleUserInput(String userResponse) {

		if (!this.stopClient) {
			try {
				sendMessageToServer(userResponse);
			} catch (IOException e) {
				System.err.println("[client: ] error when sending message to server: " + e.toString());

				try {
					closeAll();
				} catch (IOException ex) {
					System.err.println("[client: ] error closing the client connections: " + ex.toString());
				}
			}
		}
	}
	
	/**
	 * The thread that communicates with the server. 
	 * receives a message from the server, passes it to handleMessageFromServer(). 
	 * 
	 */
	@Override
	public void run() {

		String msg;

		try {
			while (!this.stopClient) {
				msg = (String) input.readObject();
				handleMessageFromServer(msg);
			}
			
			System.out.println("[client: ] client stopped..");
		} catch (Exception exception) {
			if (!this.stopClient) {
				try {
					closeAll();
				} catch (Exception ex) {
					System.err.println("[client: ] error in closing the client connection...");
				}
			}
		} finally {
			clientReader = null;
		}
		
		System.out.println("[client: ] exiting thread...");
	}

	/**
	 * Main() to initiate the client.
	 * @param args
	 */
	public static void main(String[] args) {
	
		String ip;
		int port; 
		
		ip = args[0];
		port = Integer.parseInt(args[1]);
		

		SimpleClient chatclient = null;
		
		// thread to communicate with the server starts here.
		try {
			chatclient = new SimpleClient(ip, port);
		} catch (IOException e) {
			System.err.println("[client: ] error in openning the client connection to " + ip + " on port: " + port);
		}

		// Main thread continues and in this case used to handle user inputs from the terminal.
		chatclient.runClient();

	}

}

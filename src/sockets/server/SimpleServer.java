package sockets.server;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import components.Cell;
import components.Cell.State;
import observer.Observable;
import observer.Observer;
import server.view.ServerMainUi;



/**
 * Class represents a Server component. 
 * 
 * @author thanuja
 * @version 20.11.2019
 */
public class SimpleServer extends AbstractServerComponent implements Runnable, Observable {

	// reference variable for server socket. 
	private ServerSocket 			serverSocket;

	// reference variable for ClientHandler for the server. 
	private ClientManager 			clientHandler;

	// boolean flag to indicate the server stop. 
	private boolean 				stopServer;

	// reference variabale for the Thread
	private Thread 					serverListenerThread;

	// reference variable for ThreadGroup when handling multiple clients
	private ThreadGroup 			clientThreadGroup;

	// variable to store server's port number
	int port;
	
	//reference to the GUI's receiver panel
	ServerMainUi	guiServer;
	
	// list of observers interested in this class (Observable)
	private List<Observer> observers;
	
	// boolean flag to indicate if the state of the class has changed.
	private boolean			changed;
 	
	
	private String			receivedMessage;
	
	
//	public SimpleServer(ServerMainUi gui) {
//		
//		this.guiServer = gui;
//		this.stopServer = false;
//		
//		/**
//		 * Initializes the ThreadGroup. 
//		 * Use of a ThreadGroup is easier when handling multiple clients, although it is not a must. 
//		 */
//		this.clientThreadGroup = new ThreadGroup("ClientManager threads");
//		
//	}

	/**
	 * Constructor.
	 * 
	 */
	public SimpleServer() {
		
		this.observers = new ArrayList<Observer>();
		
		this.stopServer = false;
		
		/**
		 * Initializes the ThreadGroup. 
		 * Use of a ThreadGroup is easier when handling multiple clients, although it is not a must. 
		 */
		this.clientThreadGroup = new ThreadGroup("ClientManager threads");

	}
	
	/**
	 * Initializes the server. Takes port number, creates a new serversocket instance. 
	 * Starts the server's listening thread. 
	 * @param port
	 * @throws IOException
	 */
	public void initializeServer(int port) throws IOException {

		this.port = port;
		if (serverSocket == null) {
			serverSocket = new ServerSocket(port);
		}

		stopServer = false;
		serverListenerThread = new Thread(this);
		serverListenerThread.start();

	}
	
	/**
	 * handles messages from each client. In this case messages are simply displayed. 
	 * Modified to prepare a response and send back to the same client. Simply changes the input text to upper case. 
	 * This is a shared resource among all client threads, so it has to be synchronized.
	 * 
	 * 
	 * @param msg
	 * @param client
	 */
	public synchronized void handleMessagesFromClient(String msg, ClientManager client) {

//		// format the client message before displaying in server's terminal output. 
//        String formattedMessage = String.format("[client %d] : %s", client.getClientID(), msg); 
//        this.receivedMessage = formattedMessage;
//        this.changed = true;

        String[] arrOfStr = msg.split(",", 0);

        String typeOfMessage = arrOfStr[0];

        int x = Integer.parseInt(arrOfStr[1]);
	
        int y = Integer.parseInt(arrOfStr[2]);
  
        Cell cellToCheck = guiServer.getMyBoardView().getCells(x, y);
        
        switch(typeOfMessage) {
        case "f":
            if (cellToCheck.getState() == State.CONTAINS_SHIP) {
//            	System.out.println("hit");
//            	handleHit();
            	cellToCheck.setState(State.HIT);
            	guiServer.getMyBoardView().getBoardModel().colourCellsInView(x, y, Color.RED);

            	String replyMessage = "h," + Integer.toString(x)+","+Integer.toString(y);

            	sendMessageToClient(replyMessage, client);
            }
            else {
//            	System.out.println("miss");
//            	handleMiss();
            	cellToCheck.setState(State.MISSED);
            	guiServer.getMyBoardView().getBoardModel().colourCellsInView(x, y, Color.GRAY);

            	String replyMessage = "m," + Integer.toString(x)+","+Integer.toString(y);

            	sendMessageToClient(replyMessage, client);
            	
            }
          break;
        case "m":
            // code block
        	guiServer.getEnemyBoardView().handleMiss(x, y);
            break;
          case "h":
              // code block
        	  guiServer.getEnemyBoardView().handleHit(x, y);
              break;
        default:
          // code block
      }

	}
	
	
	
	
	private void handleMiss() {

		//change colour on my board
		//send miss response
		
	}

	private void handleHit() {
		//set cell to hit
		//change color on my board
		//send hit response
		
	}

	/**
	 * Handles displaying of messages received from each client. 
	 * Called from handleMessagesFromClient()
	 * @param message
	 */
	public void display(String message) {
		System.out.println(">> " + message);
	}
	
	
	/**
	 * Gets user's input from the server's command line
	 * sends the user input to all connected clients. 
	 * 
	 * @param msg
	 */
	public void handleUserInput(String msg) {
		
		if(msg.equals(new String("over"))) {
			this.stopServer = true;
			close();
			return;
		}
		
		Thread[] clientThreadList = getClientConnections();
		for (int i = 0; i < clientThreadList.length; i++) {
			try {
				((ClientManager)clientThreadList[i]).sendMessageToClient(msg);
			}
			// Ignore all exceptions when closing clients.
			catch (Exception ex) {
				
			}
		}
		
//		System.out.println(msg);
		
		
	}
	
	/**
	 * Handles, sending a message to client. In this case, it is a string. 
	 * Each client will be calling this to send a message to the client, so it is made synchronized. 
	 * However, this can be handled separately within the ClientManager.
	 * 
	 * @param msg		Message
	 * @param client	Client to be sent
	 */
	public synchronized void sendMessageToClient(String msg, ClientManager client) {
		try {
			client.sendMessageToClient(msg);
		} catch (IOException e) {
			System.err.println("[server: ] Server-to-client message sending failed...");
		}
	}
	
	
	public synchronized void sendMessageToClient(String msg) throws IOException {
		
		Thread[] clientThreadList = getClientConnections();
		
		for (int i = 0; i < clientThreadList.length; i++) {
			((ClientManager) clientThreadList[i]).sendMessageToClient(msg);
		}
//		guiServer.getBoardView().getCells(x, y);
		
	}
	
	
	
	/**
	 * 
	 * @return list of Thread[] pertaining to the clients connected to the server
	 */
	public Thread[] getClientConnections() {
		
		Thread[] clientThreadList = new Thread[clientThreadGroup.activeCount()];
		clientThreadGroup.enumerate(clientThreadList);

		return clientThreadList;
	}
	
	/**
	 * Close the server and associated connections. 
	 */
	public void close() {
		
		if (this.serverSocket == null)
			return;

		try {
			this.stopServer = true;
			this.serverSocket.close();

		} catch (IOException e) {
			System.err.println("[server: ] Error in closing server connection...");
		} finally {

			// Close the client sockets of the already connected clients
			Thread[] clientThreadList = getClientConnections();
			for (int i = 0; i < clientThreadList.length; i++) {
				try {
					((ClientManager) clientThreadList[i]).closeAll();
				}
				// Ignore all exceptions when closing clients.
				catch (Exception ex) {
					
				}
			}
			this.serverSocket = null;
			
		}

	}
	
	/**
	 * Represents the thread that listens to the port, and creates client connections. 
	 * Here, each connection is treated as a separate thread, and each client is associated with the ThreadGroup. 
	 * 
	 */
	@Override
	public void run() {
		
		System.out.println("[server: ] starting server: listening @ port: " + port);

		// increments when a client connects. 
		int clientCount = 0;

		// loops until stopserver flag is set to true. 
		while (!this.stopServer) {

			Socket clientSocket = null;
			try {
				clientSocket = serverSocket.accept();
			} catch (IOException e1) {
				System.err.println("[server: ] Error when handling client connections on port " + port);
			}

			ClientManager cm = new ClientManager(this.clientThreadGroup, clientSocket, clientCount, this);

			// new ClientManager(clientSocket, this);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.err.println("[server: ] server listner thread interruped..");
			}

			clientCount++;

		}		
	}

	/**
	 * 
	 * @return returns the status of the server; i.e., whether the server has stopped.
	 */
	public boolean getServerStatus() {
		return this.stopServer;
	}
	
	public void addGui(ServerMainUi gui) {
		
		this.guiServer = gui;
	}
	
	/**
	 * Main() to start the SimpleServer. 
	 * 
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		SimpleServer server = new SimpleServer();
		// port number to listen
		int port = Integer.parseInt(args[0]); //7778;

		try {
			server.initializeServer(port);

		} catch (IOException e) {
			System.err.println("[server: ] Error in initializing the server on port " + port);
		}
		// Main thread continues...

		System.out.println("get user input...");
		
		new Thread(() -> {
			String line = "";
			BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
			
			try {
				while(true) {				
					line = consoleInput.readLine();
					server.handleUserInput(line);
					if(server.getServerStatus()) {					
						break;
					}								
				}			
			}
			catch(IOException e) {
				System.out.println("Error in System.in user input");
			}
			finally {
				try {
					consoleInput.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		

		
		

	}

	
	/**
	 * Add an observer to the list, if it is not available in the current list
	 */
	@Override
	public void addObserver(Observer obs) {
		if(obs == null) {
			throw new NullPointerException("A null observer..");
		}
		if(!this.observers.contains(obs)) {
			this.observers.add(obs);
		}
	}

	/**
	 * remove an onbserver if it exists in the list
	 */
	@Override
	public void removeObserver(Observer obs) {
		if(obs == null) {
			throw new NullPointerException("A null observer..");
		}
		
		if(this.observers.contains(obs)) {
			this.observers.remove(obs);
		}
		
	}

	/**
	 * notify all observer, when the changed = true;
	 * gets called, from the handleMessagefromCLient() i.e., when a new message is received from the client.
	 */
	@Override
	public void notifyObservers() {
		
		if(!this.changed) {
			return;
		}
		
		for (Observer observer : observers) {
			observer.update();
		}
		this.changed = false;
		
	}

	@Override
	public Object getUpdate() {
		return this.receivedMessage;
	}
}
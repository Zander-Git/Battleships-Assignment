package sockets.server;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import components.Cell;
import components.Cell.State;
import observer.Observable;
import observer.Observer;
import server.view.ServerMainUi;


public class SimpleServer extends AbstractServerComponent implements Runnable, Observable {

	private ServerSocket 			serverSocket;

	private ClientManager 			clientHandler;

	private boolean 				stopServer;

	private Thread 					serverListenerThread;

	private ThreadGroup 			clientThreadGroup;

	int port;
	
	//reference to the GUI
	ServerMainUi	guiServer;
	
	private List<Observer> observers;
	
	private boolean			changed;
 	
	private String			receivedMessage;
	

	public SimpleServer() {
		
		this.observers = new ArrayList<Observer>();
		
		this.stopServer = false;

		this.clientThreadGroup = new ThreadGroup("ClientManager threads");

	}
	

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

        String[] arrOfStr = msg.split(",", 0);
        String typeOfMessage = arrOfStr[0];
        int x = Integer.parseInt(arrOfStr[1]);
        int y = Integer.parseInt(arrOfStr[2]);
  
        Cell cellToCheck = guiServer.getMyBoardView().getCells(x, y);
        
        switch(typeOfMessage) {
        case "f":
            if (cellToCheck.getState() == State.CONTAINS_SHIP) {

            	cellToCheck.setState(State.HIT);
            	guiServer.getMyBoardView().getBoardModel().colourCellsInView(x, y, Color.RED);
            	String replyMessage = "h," + Integer.toString(x)+","+Integer.toString(y);
            	sendMessageToClient(replyMessage, client);
            	guiServer.getEnemyBoardView().setUserTurn(false);
            }
            else {

            	cellToCheck.setState(State.MISSED);
            	guiServer.getMyBoardView().getBoardModel().colourCellsInView(x, y, Color.GRAY);
            	String replyMessage = "m," + Integer.toString(x)+","+Integer.toString(y);
            	sendMessageToClient(replyMessage, client);
            	guiServer.getEnemyBoardView().setUserTurn(true);
            	
            }
          break;
        case "m":
            // Receive "miss" response
        	guiServer.getEnemyBoardView().handleMiss(x, y);
            break;
          case "h":
              // Receive "hit" response
        	  guiServer.getEnemyBoardView().handleHit(x, y);
              break;
          case "r":
              // Receive "Ready" response
          	if (guiServer.getMyBoardView().isUserReady()) {
  	        		try {
  	        			sendMessageToClient("r,0,0");
  					} catch (IOException e) {
  						e.printStackTrace();
  					}
          	}
          	else {
              	JOptionPane.showMessageDialog(guiServer.getMyBoardView(),"Opponent ready :)",
      					"" , JOptionPane.INFORMATION_MESSAGE );
          		try {
          			sendMessageToClient("n,0,0");
  				} catch (IOException e) {
  					e.printStackTrace();
  				}
          	}
              break;
          case "n":
              // Receive "Not Ready" response
          	JOptionPane.showMessageDialog(guiServer.getMyBoardView(),"Opponent not ready yet :)",
  					"" , JOptionPane.INFORMATION_MESSAGE );
          	break;
          case "g":
              // Receive "Game Over" response
          	JOptionPane.showMessageDialog(guiServer.getMyBoardView(),"You lost :( Please close the game",
  					"" , JOptionPane.INFORMATION_MESSAGE );
          	break;
      }

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
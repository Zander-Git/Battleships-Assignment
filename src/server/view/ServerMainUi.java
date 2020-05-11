package server.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import components.BoardView;
import components.ShipSelectionPanel;
import observer.Observable;
import observer.Observer;
import sockets.server.SimpleServer;

public class ServerMainUi implements Observer {

	JFrame frame;  
	ServerConfigPanelView configPanel;
	ShipSelectionPanel selectPanel;
	BoardView myBoard, yourBoard;
	
	Observable		observableServer;
	
	SimpleServer 	serverComponent;
	
	private static ServerMainUi serverUIInstance;

	public static ServerMainUi getServerUIInstance() {
		if(serverUIInstance == null) {
			serverUIInstance = new ServerMainUi();
		}
		return serverUIInstance;
		
	}
	
	private static final int SIZE = 500;

	public ServerMainUi(){

		serverComponent = new SimpleServer();
		serverComponent.addGui(this);
		
		serverComponent.addObserver(this);
		this.setObservable(serverComponent);
		
		frame=new JFrame();	        
		JPanel rootPanel = new JPanel(new BorderLayout());
		JPanel leftPanel = new JPanel(new GridLayout(1, 0));
		JPanel rightPanel = new JPanel(new GridLayout(1, 0));
	
		selectPanel = new ShipSelectionPanel();
		configPanel = new ServerConfigPanelView(this.frame, serverComponent);
		
		myBoard = new BoardView(true, selectPanel, serverComponent);	
		yourBoard = new BoardView(false, null, serverComponent);
		yourBoard.setUserTurn(false);
		
		
	
		frame = new JFrame("Battleships");
		frame.setSize(SIZE, SIZE);
		frame.setLayout(new BorderLayout());

		leftPanel.add(myBoard);
		leftPanel.add(selectPanel);
		rightPanel.add(yourBoard);
		
		rootPanel.add(configPanel, BorderLayout.NORTH);
		rootPanel.add(leftPanel, BorderLayout.WEST);
		rootPanel.add(rightPanel, BorderLayout.EAST); 

		frame.add(rootPanel);

		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setContentPane(rootPanel);
		this.frame.pack();
		this.frame.setVisible(true);       

	}  
	
	public BoardView getMyBoardView() {
		return myBoard;
	}
	
	public BoardView getEnemyBoardView() {
		return yourBoard;
	}
	
	SimpleServer getComponent(){
		return serverComponent;
	}

	@Override
	public void update() {
	
		String receivedMsg = (String) this.observableServer.getUpdate();
		
		
		
		// display the message where appropriate
//		this.receivePanel.updateReceiverWindow(receivedMsg);
		
	}

	@Override
	public void setObservable(Observable obs) {
		
		if(obs == null) {
			throw new NullPointerException("Null observable..");
		}
		
		this.observableServer = obs;
		
	}



}








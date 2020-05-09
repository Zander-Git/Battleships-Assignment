package server.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import components.BoardView;
import components.ShipSelectionPanel;
import sockets.server.SimpleServer;

public class MainUi {

	JFrame frame;  
	ServerConfigPanelView configPanel;
	ShipSelectionPanel selectPanel;
	BoardView myBoard, yourBoard;
	
	SimpleServer 	servercomponent;
	
	private static MainUi serverUIInstance;
	
	/**
	 * Public function to return an instance of the singleton class. 
	 * @return
	 */
	public static MainUi getServerUIInstance() {
		if(serverUIInstance == null) {
			serverUIInstance = new MainUi();
		}
		return serverUIInstance;
		
	}
	
	private static final int SIZE = 500;

	public MainUi(){

		servercomponent = new SimpleServer();
		
		frame=new JFrame();	        
		JPanel rootPanel = new JPanel(new BorderLayout());
		JPanel leftPanel = new JPanel(new GridLayout(1, 0));
		JPanel rightPanel = new JPanel(new GridLayout(1, 0));
	
		selectPanel = new ShipSelectionPanel();
		configPanel = new ServerConfigPanelView(this.frame, servercomponent );
		
//		myBoard = new BoardView(false);	
		myBoard = new BoardView(true, selectPanel);	
		yourBoard = new BoardView(false, null);
		
		
	
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



}








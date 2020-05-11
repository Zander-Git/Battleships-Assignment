package client.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;


import components.*;
import sockets.client.SimpleClient;

public class ClientMainUi {

	JFrame frame;  
	ClientConfigPanelView configPanel;
	ShipSelectionPanel selectPanel;
	BoardView myBoard, yourBoard;

	SimpleClient clientComponent;
	
	private static final int SIZE = 500;

	public ClientMainUi(){     

		this.clientComponent = new SimpleClient(this);

		frame=new JFrame();	        
		JPanel rootPanel = new JPanel(new BorderLayout());
		JPanel leftPanel = new JPanel(new GridLayout(1, 0));
		JPanel rightPanel = new JPanel(new GridLayout(1, 0));
	
		selectPanel = new ShipSelectionPanel();
		configPanel = new ClientConfigPanelView(this.frame, this.clientComponent);
			
		myBoard = new BoardView(true, selectPanel, clientComponent);	
		yourBoard = new BoardView(false, null, clientComponent);

	
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



}
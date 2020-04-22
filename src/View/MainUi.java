package View;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainUi {

	JFrame frame;  
	ClientConfigPanelView configPanel;
	ShipSelectionPanel selectPanel;
	BoardView myBoard, yourBoard;
	
	private static final int SIZE = 500;

	MainUi(){     

		frame=new JFrame();	        
		JPanel rootPanel = new JPanel(new BorderLayout());
		JPanel leftPanel = new JPanel(new GridLayout(1, 0));
		JPanel rightPanel = new JPanel(new GridLayout(1, 0));
	
		selectPanel = new ShipSelectionPanel();
		configPanel = new ClientConfigPanelView(this.frame);
		
//		myBoard = new BoardView(false);	
		myBoard = new BoardView(false, selectPanel);	
		yourBoard = new BoardView(false);
		
		
	
		frame = new JFrame("Battleships");
		frame.setSize(SIZE, SIZE);
		frame.setLayout(new BorderLayout());

		leftPanel.add(myBoard);
		rightPanel.add(selectPanel);
		
		rootPanel.add(configPanel, BorderLayout.NORTH);
		rootPanel.add(leftPanel, BorderLayout.WEST);
		rootPanel.add(rightPanel, BorderLayout.EAST); 

		frame.add(rootPanel);

		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setContentPane(rootPanel);
		this.frame.pack();
		this.frame.setVisible(true);       

	}  


	public static void main(String[] args) {  
		new MainUi();  		
	} 
}








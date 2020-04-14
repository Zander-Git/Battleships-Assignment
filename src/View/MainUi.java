package View;

import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Controller.Controller;
import Model.BoardModel;

public class MainUi {

	    JFrame frame;  
	    BoardView board;	    
	    
	    MainUi(){     	
	    	
	        frame=new JFrame(); 
	        
	        JPanel mainWindow = new JPanel();
	    	JPanel leftPanel = new JPanel();	    	
	    	board = new BoardView();	        
	        
	    	mainWindow.setLayout(new GridLayout(1, 2, 20, 0));
			leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));			
	        
	       leftPanel.add(board);
	       mainWindow.add(leftPanel);
	       
	       frame.add(mainWindow);
	       
	       	this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.frame.setContentPane(mainWindow);
			this.frame.pack();
			this.frame.setVisible(true);       
    	
//	    	BoardView theView = new  BoardView();  
//	    
//	        BoardModel theModel = new BoardModel();
//	    
//	        Controller theController = new Controller(theView,theModel);
//	    
//	        theView.setVisible(true);
        
	        
	    }  
	    public static void main(String[] args) {  
	        new MainUi();  
	    	//new BoardView();
	    } 
	}
	
	
	
	
	



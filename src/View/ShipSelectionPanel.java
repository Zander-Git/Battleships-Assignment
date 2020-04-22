package View;

import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class ShipSelectionPanel extends JPanel{

	private static final long serialVersionUID = 1L;

    JRadioButton bigBoi, littleBoi;  
    ButtonGroup shipGroup; 
    JLabel shipLbl; 
    JButton readyBtn; 
    
    public ShipSelectionPanel() {
    	
    	bigBoi 		= new JRadioButton(); 
    	littleBoi	= new JRadioButton(); 
    	
    	readyBtn 	= new JButton("Ready?");
    	
    	shipGroup = new ButtonGroup();
    	
    	Box box1 = Box.createVerticalBox();
    	Box box2 = Box.createVerticalBox();
         
    	shipLbl = new JLabel("Ships"); 
    	
    	bigBoi.setText("big");
    	littleBoi.setText("lil");
    	
    	bigBoi.setBounds(120, 30, 120, 50); 
      	littleBoi.setBounds(250, 30, 80, 50);
      	shipLbl.setBounds(20, 30, 150, 50); 
      	
      	this.add(shipLbl);      	
      	box1.add(bigBoi);
      	box1.add(littleBoi);      	
      	box2.add(readyBtn);
      	bigBoi.setSelected(true);

      	shipGroup.add(bigBoi);
      	shipGroup.add(littleBoi);
      	
      	this.add(box1);
      	this.add(box2);
      	
    	
    }
    
    public void printBtnText() {
    	System.out.println(getSelectedButtonText());
    }
    
    public String getSelectedButtonText() {
        for (Enumeration<AbstractButton> buttons = shipGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                return button.getText();
            }
        }

        return null;
    }
	
	
}


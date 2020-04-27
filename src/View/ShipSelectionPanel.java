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

    JRadioButton airCarrierBtn, battleshipBtn, submarineBtn, destroyerBtn, patrolBtn,
    verticalBtn, horizontalBtn;  
    ButtonGroup shipGroup, orientaionGroup; 
    JLabel shipLbl; 
    JButton readyBtn; 
    
    public ShipSelectionPanel() {
    	
    	airCarrierBtn 		= new JRadioButton(); 
    	battleshipBtn		= new JRadioButton(); 
    	submarineBtn 		= new JRadioButton(); 
    	destroyerBtn		= new JRadioButton(); 
    	patrolBtn 			= new JRadioButton(); 
    	
    	verticalBtn			= new JRadioButton(); 
    	horizontalBtn 		= new JRadioButton(); 

    	readyBtn 	= new JButton("Ready?");
    	
    	shipGroup = new ButtonGroup();
    	orientaionGroup = new ButtonGroup();
    	
    	Box radioBox = Box.createVerticalBox();
    	Box box2 = Box.createVerticalBox();
    	Box oriBox = Box.createHorizontalBox();
         
    	shipLbl = new JLabel("Ships"); 
    	
    	airCarrierBtn.setText("aircraft carrier");
    	battleshipBtn.setText("battleship");
    	submarineBtn.setText("submarine");
    	destroyerBtn.setText("destroyer");
    	patrolBtn.setText("patrol boat");
    	
    	verticalBtn.setText("vertical");
    	horizontalBtn.setText("horizontal");
    	
    	
    	airCarrierBtn.setBounds(120, 30, 120, 50); 
      	battleshipBtn.setBounds(250, 30, 80, 50);
      	shipLbl.setBounds(20, 30, 150, 50); 
      	
      	this.add(shipLbl);      	
      	radioBox.add(airCarrierBtn);
      	radioBox.add(battleshipBtn);
      	radioBox.add(submarineBtn);
      	radioBox.add(destroyerBtn);
      	radioBox.add(patrolBtn);    	 	
      	airCarrierBtn.setSelected(true);
      	
      	box2.add(readyBtn);      	
      	
      	oriBox.add(horizontalBtn);
      	oriBox.add(verticalBtn);
      	horizontalBtn.setSelected(true);

      	shipGroup.add(airCarrierBtn);
      	shipGroup.add(battleshipBtn);
      	shipGroup.add(submarineBtn);
      	shipGroup.add(destroyerBtn);
      	shipGroup.add(patrolBtn);      	
      	
      	orientaionGroup.add(horizontalBtn);
      	orientaionGroup.add(verticalBtn);
      	
      	this.add(radioBox);
      	this.add(box2);
      	this.add(oriBox);
      	
      	
    	
    }
    
    public void printBtnText() {
    	System.out.println(getShipSelected());
    }
    
    public String getShipSelected() {
        for (Enumeration<AbstractButton> buttons = shipGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                return button.getText();
            }
        }

        return null;
    }
	
    public boolean getOrientationSelected() {
        for (Enumeration<AbstractButton> buttons = orientaionGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected() ) {
                if(button.getText() == "horizontal") {
                	return false;
                }
                else {
                	return true;
                }
            }
        }
		return false; //fix this wierd thing



    }
	//some kind of controller class to set board model ship length, based on button name in ShipModel
}


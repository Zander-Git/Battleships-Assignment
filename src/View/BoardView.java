package View;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

//import Controller.Controller.PrintListener;
import Model.CellModel;

public class BoardView extends JPanel{
	CellView[][] cells=new CellView[10][10];


	//private static final long serialVersionUID = 1L;
	public BoardView() {
		addSquares();
		
	}
	
private void addSquares() {
    	
    	setLayout(new GridBagLayout());
    	GridBagConstraints gbc = new GridBagConstraints();
    	for (int y = 0; y < 10; y++) {
            gbc.gridy = y;
            for (int x = 0; x < 10; x++) {
                gbc.gridx = x;
                cells[x][y] = new CellView(x,y);
                add(cells[x][y], gbc);     
                
                cells[x][y].setX(x);
    			cells[x][y].setY(y);
                                
                cells[x][y].addActionListener(new ActionListener() {
                	

                	@Override
                	public void actionPerformed(ActionEvent e) {
                			
                			

                	}
                	});

            }
    	}    	
    }

public CellView getCell(int x, int y) {
    return cells[x][y];
}
public  void test() {
	
	System.out.println(getCell(2,2).getX());
}

//
//public void printThings(PrintListener printListener) {
//	// TODO Auto-generated method stub
//	
//}

}

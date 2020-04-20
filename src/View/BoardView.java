package View;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

//import Controller.Controller.PrintListener;
import Model.CellModel;

public class BoardView extends JPanel{
	Cell[][] cells=new Cell[10][10];
	private int gridSize = 10;
	private boolean ownBoard;

	private final ActionListener actionListener = actionEvent -> {
        Object source = actionEvent.getSource();
            handleCell((Cell) source);
        
    };    

	

	//private static final long serialVersionUID = 1L;
	public BoardView(boolean ownBoard) {
		this.ownBoard = ownBoard;
		addSquares(ownBoard);
		
	}
	
private void handleCell(View.Cell source) {
		// TODO Auto-generated method stub
		
	}

private void addSquares(boolean isOwnBoard) {
    	
    Container grid = new Container();
    grid.setLayout(new GridLayout(gridSize, gridSize));

    for (int row = 0; row < gridSize; row++) {
        for (int col = 0; col < gridSize; col++) {
            cells[row][col] = new Cell(row, col, actionListener);
            grid.add(cells[row][col]);

            if(isOwnBoard) {
            	cells[row][col].setEnabled(false);
            	
            }
        }
    }
    //createMines();
//    myPanel.add(grid, BorderLayout.CENTER);
    //return grid;
}  	
    

public Cell getCell(int x, int y) {
    return cells[x][y];
}
public  void test() {
	
	System.out.println(getCell(2,2).getX());
}


}

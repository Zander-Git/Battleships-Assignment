package View;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

public class BoardView extends JPanel{
	
	private static final long serialVersionUID = 1L;
	Cell[][] cells=new Cell[10][10];
	private int gridSize = 10;
	private boolean ownBoard;
	ShipSelectionPanel shipPanel;
	
	private final ActionListener actionListener = actionEvent -> {
        Object source = actionEvent.getSource();
            handleCell((Cell) source);
        
    };    

	
	public BoardView(boolean ownBoard) {
		this.ownBoard = ownBoard;
		addSquares();
		
	}
	
	public BoardView(boolean ownBoard, ShipSelectionPanel shipPanel) {
		this.shipPanel = shipPanel;
		this.ownBoard = ownBoard;
		addSquares();
		
	}
	
private void handleCell(View.Cell source) {
//	source.colorCell();
//	shipPanel.printBtnText();
	
	if (shipPanel.getSelectedButtonText() == "big") {
		source.colorCell(Color.BLUE);
	}else {
		source.colorCell();
	}
	
}
		
	

private void addSquares() {
    	
    Container grid = new Container();
    grid.setLayout(new GridLayout(gridSize, gridSize));

    for (int row = 0; row < gridSize; row++) {
        for (int col = 0; col < gridSize; col++) {
            cells[row][col] = new Cell(row, col, actionListener);
            grid.add(cells[row][col]);

            if(ownBoard) {
            	cells[row][col].setEnabled(false);
            	
            }
        }
    }

    this.add(grid);

}  	
    

public Cell getCell(int x, int y) {
    return cells[x][y];
}
public  void test() {
	
	System.out.println(getCell(2,2).getX());
}


}

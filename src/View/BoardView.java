package View;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import Model.BoardModel;

import Model.Ship;
import Model.Ship.Type;
import View.Cell.State;

public class BoardView extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private int gridSize = 10;
	Cell[][] cells=new Cell[gridSize][gridSize];	
	List<Ship> ships = new ArrayList<Ship>();
	private boolean ownBoard;
	ShipSelectionPanel shipPanel;
	BoardModel boardModel;
	JButton readyBtn;
	
	final ActionListener actionListener = actionEvent -> {
        Object source = actionEvent.getSource();
//        if (source == readyBtn) {
//            //checkBoard
//        	System.out.println("test");
//        }else
            handleCell((Cell) source);
            
    };    

	
//	public BoardView(boolean ownBoard) {
//		this.ownBoard = ownBoard;
//        this.boardModel = new BoardModel(ownBoard);
//		addCells();
//		
//	}
	
	public BoardView(boolean ownBoard, ShipSelectionPanel shipPanel) {
		this.shipPanel = shipPanel;
		this.ownBoard = ownBoard;
		this.boardModel = new BoardModel(ownBoard, this);
		addCells();
		
		JButton readyBtn 	= new JButton("Ready?");

		readyBtn.addActionListener(new ActionListener(){  
		    public void actionPerformed(ActionEvent e){  
		    	if(boardModel.isBoardReady()) {
		    		System.out.println("ready :)");
		    	}
		    	else {
		    		System.out.println("not ready :(");
		    	}
	    }

	    });  
		
		this.add(readyBtn);
	
		for (Type shipType : Type.values() ) {
			ships.add(new Ship(shipType));
		}

	}
	
private void handleCell(Cell source) {
//inner anonymous class?
	

	
	for(int i=0; i<ships.size();i++) {
	if (shipPanel.getShipSelected() == ships.get(i).getName()) {
		Ship selectedShip = ships.get(i);		
		

		source.setState(State.CONTAINS_SHIP);
		boardModel.placeShip(selectedShip, source.getRow(), source.getCol(), isVertical());
//		source.setShip(selectedShip);
	}
	
}
//System.out.println(boardModel.getCell(source).getShip().getName());


	
}
		
public boolean isVertical() {
	return shipPanel.getOrientationSelected();
	
}

private void addCells() {
    	
    Container grid = new Container();
    grid.setLayout(new GridLayout(gridSize, gridSize));
    
    for (int row = 0; row < gridSize; row++) {
        for (int col = 0; col < gridSize; col++) {
            cells[row][col] = new Cell(row, col, actionListener);
            grid.add(cells[row][col]);
            cells[row][col].addMouseListener(new java.awt.event.MouseAdapter() {		//this does not belong here, but it works?
            	Color originalColor = null;
            	public void mouseEntered(MouseEvent evt) {
            		Cell button = (Cell)evt.getComponent();
                	originalColor = button.getBackground();
                	
                	if(button.getState() == State.NO_SHIP) {
                	button.setBackground(Color.DARK_GRAY);
                	}
                }
                public void mouseExited(MouseEvent evt) {
                	Cell button = (Cell)evt.getComponent();
                	if(button.getState() == State.NO_SHIP) {
                		button.setBackground(originalColor);
                    	}
                	
                	
                }
            });

            if(ownBoard) {
            	cells[row][col].setEnabled(false);
            	
            }
        }
    }

    this.add(grid);

}  	
    
public void colorCellFromBoardModel(int posX, int posY, Color color) {
	cells[posX][posY].setBackground(color);

}
//public CellModel getCell(int x, int y) {
//    return cells[x][y];
//}




}

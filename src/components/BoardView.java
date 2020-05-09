package components;

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

import components.Cell.State;
import model.BoardModel;
import model.Ship;
import model.Ship.Type;

public class BoardView extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private int gameMode;
	private final int SETUP = 0;
	private final int PLAYING = 1;
	private final int FINISHED = 2;
	private int gridSize = 10;
	Cell[][] cells=new Cell[gridSize][gridSize];	
	List<Ship> ships = new ArrayList<Ship>();
	private boolean ownBoard;
	ShipSelectionPanel shipPanel;
	BoardModel boardModel;
	JButton readyBtn;

	
	final ActionListener actionListener = actionEvent -> {
        Object source = actionEvent.getSource();
            cellAction((Cell) source);
            
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
		
		if (ownBoard) {
		JButton readyBtn 	= new JButton("Ready?");
		readyBtn.addActionListener(new ActionListener(){  
		    public void actionPerformed(ActionEvent e){  
		    	if(boardModel.isBoardReady()) {
		    		System.out.println("ready :)");
		    		startGame();
		    		readyBtn.setVisible(false);
		    		
		    	}else {
		    		System.out.println("not ready :(");
		    	}
	    }
	    });  
		this.add(readyBtn);
		
		}
		
		for (Type shipType : Type.values() ) {
			ships.add(new Ship(shipType));
		}

	}


protected void startGame() {
	if(ownBoard) {
		setCellsEnabled(false);
	}else {
		setCellsEnabled(true);
		for (int row = 0; row < gridSize; row++) {
	        for (int col = 0; col < gridSize; col++) {
	        	cells[row][col].setBackground(Color.RED);
	        }
		}
	}
	
}

private void cellAction(Cell source) {
//inner anonymous class?
	
	if(ownBoard) {
		for(int i=0; i<ships.size();i++) {
			if (shipPanel.getShipSelected() == ships.get(i).getName()) {
				Ship selectedShip = ships.get(i);	
				source.setState(State.CONTAINS_SHIP);
				boardModel.placeShip(selectedShip, source.getRow(), source.getCol(), isVertical());
				}
	
			}
	}
	else {
		//if connectionOPne
		//check oponent cells at source.x&y
		//if hit
			//reduce health, check for other
			//if no other, sink. otherwuse health--
			//colour cell acordingly, maybe others
		//else chill
	}
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
            cells[row][col].addMouseListener(new java.awt.event.MouseAdapter() {		//could probably delete
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

            if(!ownBoard) {
//            	cells[row][col].setEnabled(false);
//            	cells[row][col].setBackground(Color.GRAY);
            	
            }
        }
    }

    this.add(grid);
}  	
    
public void colorCellFromBoardModel(int posX, int posY, Color color) {
	cells[posX][posY].setBackground(color);

}

private void setCellsEnabled(boolean enabled) {
	for (int row = 0; row < gridSize; row++) {
        for (int col = 0; col < gridSize; col++) {
        	cells[row][col].setEnabled(enabled);
        }
	} 	
}


public int getGameMode() {
	return gameMode;
}


public void setGameMode(int gameMode) {
	this.gameMode = gameMode;
}

}

package Model;

import View.BoardView;
import View.Cell;

public class BoardModel {
	
	public static final int BOARD_DIMENSION = 10;
    private Cell[][] cells;
    private int posX, posY;
    private Ship shipPlacing;
    private BoardView boardView;
	
    public BoardModel(boolean ownBoard, BoardView boardView) {

    	this.boardView = boardView;
        cells = new Cell[BOARD_DIMENSION][BOARD_DIMENSION];
        // populates the squares array
        for (int i = 0; i < BOARD_DIMENSION; i++) {
            for (int j = 0; j < BOARD_DIMENSION; j++) {
                cells[i][j] = new Cell(i, j, null);
            }
        }

    }
	
    public Cell getCell(int x, int y) {
        return cells[x][y];
    }
    
    public Cell getCell(Cell cell) {
    	int row = cell.getRow();
    	int col = cell.getCol();
        return cells[row][col];
    }
        
    public int getShipLength() {
    	return shipPlacing.getLength();
    }

    public void placeShip(Ship ship, int x, int y, boolean isVertical){
    	this.posX = x;
    	this.posY = y;
    	this.shipPlacing = ship;
    	shipPlacing.setVertical(isVertical);

    	if (inBoundry()) {
    			if(isntOverlapping()) {	
    			removeShipType();
    			placeShip();
    			}
    			else {
    				System.out.println("overlapping");
    			}
    	}
    	else {
    		System.out.println("out of bounds");
    	}
    	}
 
    public boolean inBoundry() {
    // checks if it is within the board
    int end = (shipPlacing.isVertical()) ? posY + getShipLength() - 1 : posX
            + getShipLength() - 1;
    if (posX < 0 || posY < 0 || end >= BOARD_DIMENSION) {
        return false;
    }
    return true;
    }
    
    public boolean isntOverlapping() {
        for (int i = 0; i < getShipLength(); i++) {
            if (shipPlacing.isVertical()) {
                if (cells[posX][posY + i].isShip())
                    return false;
            } else {
                if (cells[posX + i][posY].isShip())
                    return false;
            }
            
        }
    	return true;
    }
    
    public void removeShipType() {
    	for(int i = 0; i<BOARD_DIMENSION;i++) {
    		for(int j = 0;j<BOARD_DIMENSION; j++) {
    			if (cells[i][j].getShip() == shipPlacing) {
        			cells[i][j].setShip(null);
        			boardView.colorCellFromBoardModel(i, j, null);

    		}
    	}
    	}
    }
    	
    public boolean placeShip() {
        // puts ship on squares
        for (int i = 0; i < getShipLength(); i++) {
            if (shipPlacing.isVertical()) {
            	cells[posX][posY + i].setShip(shipPlacing);
                shipPlacing.setCell(cells[posX][posY + i]);
                boardView.colorCellFromBoardModel(posX, posY + i, shipPlacing.getColor());
                
            } else if (!shipPlacing.isVertical()) {
            	cells[posX + i][posY].setShip(shipPlacing);
                shipPlacing.setCell(cells[posX + i][posY]);
                boardView.colorCellFromBoardModel(posX+ i, posY, shipPlacing.getColor());
            }
        }

        return true;
    }
    

    
    
    
    
}

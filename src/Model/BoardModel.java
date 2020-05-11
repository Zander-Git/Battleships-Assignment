package model;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import components.BoardView;
import components.Cell;
import components.Cell.State;
import model.Ship.Type;

public class BoardModel {
	
	public static final int BOARD_DIMENSION = 10;
    private Cell[][] cells;
    private int posX, posY;
	boolean allPlaced;

	private Ship shipPlacing;
    private BoardView boardView;
	List<Ship> ships = new ArrayList<Ship>();
	
    public BoardModel(boolean ownBoard, BoardView boardView) {

    	this.boardView = boardView;
    	
        cells = new Cell[BOARD_DIMENSION][BOARD_DIMENSION];
        for (int i = 0; i < BOARD_DIMENSION; i++) {
            for (int j = 0; j < BOARD_DIMENSION; j++) {
                cells[i][j] = new Cell(i, j, null);
            }
        }
        
        
		for (Type shipType : Type.values() ) {
			ships.add(new Ship(shipType));
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
    			placeShipInMemory();
    			}
    			else {
    				JOptionPane.showMessageDialog(boardView,"overlapping",
    							"overlapping 2" , JOptionPane.ERROR_MESSAGE );
    			}
    	}
    	else {
			JOptionPane.showMessageDialog(boardView,"out of bounds",
					"out of bounds" , JOptionPane.ERROR_MESSAGE );
    	}
    	}
 
    public boolean inBoundry() {
    // checks if it is within the board
    int end = (shipPlacing.isVertical()) ? posX
            + getShipLength() - 1 : posY + getShipLength() - 1;
    if (posX < 0 || posY < 0 || end >= BOARD_DIMENSION) {
        return false;
    }
    return true;
    }
    
    public boolean isntOverlapping() {
    	
        for (int i = 0; i < getShipLength(); i++) {
            if (shipPlacing.isVertical()) {
                if (cells[posX + i][posY].isShip() && !cells[posX + i][posY].compareShips(shipPlacing))            	
                	 return false;
                   
            } else {
            	if (cells[posX][posY + i].isShip() && !cells[posX][posY + i].compareShips(shipPlacing))
                    return false;
            }
            
        }
    	return true;
    }
    
    public boolean isBoardReady() {
    	//for all ships
    	ships.forEach((shipss) ->{    		
    		int shipCount = 0;
    		setAllPlaced(true);
        	for(int i = 0; i<BOARD_DIMENSION;i++) {
        		for(int j = 0;j<BOARD_DIMENSION; j++) {        			
        			if(cells[i][j].isShip()) {
        				if(cells[i][j].getShip().getName().equals(shipss.getName())) {
            				shipCount++;
        				}
        			}
        		}
        	}        	
        	if(shipss.getLength() != shipCount) {
    			System.out.println("place " + shipss.getName());
    			setAllPlaced(false);
    		}        	
        }
    			);      	
    	return isAllPlaced();
    }
     
    public void removeShipType() {
    	for(int i = 0; i<BOARD_DIMENSION;i++) {
    		for(int j = 0;j<BOARD_DIMENSION; j++) {
    			if (cells[i][j].getShip() == shipPlacing) {
        			cells[i][j].setShip(null);
        			cells[i][j].setState(State.NO_SHIP);
        			boardView.colorCellFromBoardModel(i, j, null);

    		}
    	}
    	}
    }
    	
    public boolean placeShipInMemory() {
        // puts ship on squares
        for (int i = 0; i < getShipLength(); i++) {
            if (shipPlacing.isVertical()) {
            	SubmitShip(posX + i, posY);            	                
            } else if (!shipPlacing.isVertical()) {
            	SubmitShip(posX, posY + i);
            }
        }

        return true;
    }
    
    private void SubmitShip(int x, int y) {
    	cells[x][y].setShip(shipPlacing);
    	cells[x][y].setState(State.CONTAINS_SHIP);
        shipPlacing.setCell(cells[x][y]);
        colourCellsInView(x, y, shipPlacing.getColor());
    }
    
    public boolean isAllPlaced() {
		return allPlaced;
	}

	public void setAllPlaced(boolean allPlaced) {
		this.allPlaced = allPlaced;
	}

    public void colourCellsInView(int x, int y, Color color) {
    	boardView.colorCellFromBoardModel(x, y,color);
    }
    
    
    
}


package Model;

import java.awt.Color;
import java.util.ArrayList;

import View.Cell;


public class Ship {
	
	private int length;

	
    private Type type;
    private ArrayList<Cell> cells;
    private boolean vertical;
    private int health;
	private Color color;

	
    public Ship(Type type) {
        this.type = type;
        this.vertical = false;
        this.health = type.length;
        this.color = type.shipColor;
        cells = new ArrayList<Cell>();
    }
    
    public void setCell(Cell cell) {
        this.cells.add(cell);
    }
    
	public String getName() {
		return type.getName();
	}
	
	public int getLength() {
		return type.length;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setVertical(boolean isVertical) {
		vertical = isVertical;
	}
	
	public boolean isVertical() {
		return vertical;
	}

	
    public enum Type {
        AIRCRAFT_CARRIER(5, "aircraft carrier", Color.BLUE ), BATTLESHIP(4, "battleship", Color.GREEN), SUBMARINE(
                3, "submarine", Color.YELLOW), DESTROYER(3, "destroyer", Color.ORANGE), PATROL_BOAT(2,
                "patrol boat", Color.MAGENTA);

        private int length;
        private String name;
        private Color shipColor;

        /**
         * Constructs a new Type of Ship
         * @param length
         *          The length of the Type
         * @param name 
         *          The name of the Type
         */
        Type(int length, String name, Color color) {
            this.length = length;
            this.name = name;
            this.shipColor = color;
        }

        /**
         * Gets the name of the Type
         * @return 
         *          the name of the Type
         */
        public String getName() {
            return name;
        }
        
    	public Color getColor() {
    		return shipColor;
    	}
        
    }



	

}

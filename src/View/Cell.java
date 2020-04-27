package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.Objects;

import javax.swing.JButton;

import Model.Ship;


public class Cell extends JButton {

	private static final long serialVersionUID = 1L;
	private final int row, col;
    private Ship ship;
	private State state;    

//	private State state;
	
	public Cell(final int row, final int col,
         final ActionListener actionListener) {
        this.row = row;
        this.col = col;
        this.ship = null;
//        this.state = State.NO_SHIP;
        addActionListener(actionListener);

    }

	public int getRow() {
		return row;
	}


	public int getCol() {
		return col;
	}


    public Ship getShip() {
    	return ship;
    }
    
    public void setShip(Ship ship) {
		this.ship = ship;
	}
    
    public boolean isShip() {
        return (ship != null);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Cell cell = (Cell) obj;
        return row == cell.row &&
               col == cell.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
    
	@Override
	public Dimension getPreferredSize() {
      return new Dimension(40, 40);
  }

    protected void colorCell() {
        if (getBackground() != Color.DARK_GRAY) {
            setBackground(Color.DARK_GRAY);
        } else {
            setBackground(null);
        }
    }

    protected void colorCell(Color color) {
            setBackground(color);
        }


	public State getState() {
		return state;
	}
	
	public void setState(State state) {
		this.state = state;
	}
	
    public enum State {
        CONTAINS_SHIP, NO_SHIP, UNKNOWN
    }
    

}

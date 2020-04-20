package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.Objects;

import javax.swing.JButton;


public class Cell extends JButton {
    private final int row;
	private final int col;
    private       int value;
//    private Ship ship;    

	private State state;
	
	Cell(final int row, final int col,
         final ActionListener actionListener) {
        this.row = row;
        this.col = col;
//        this.ship = null;
        this.state = State.NO_SHIP;
        addActionListener(actionListener);
        setText("");
    }

    public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

    public State getState() {
    	return state;
    }
    public void setState(State state) {
		this.state = state;
	}
    
//    public Ship getShip() {
//    	return ship;
//    }
//    
//    public void setShip(Ship ship) {
//		this.ship = ship;
//	}
	
    int getValue() {
        return value;
    }

    void setValue(int value) {
        this.value = value;
    }


    void reset() {
        setValue(0);
        setEnabled(true);
        setText("");
        
    }

    void reveal() {
        setEnabled(false);
//        setText(isAMine() ? "X" : String.valueOf(value));
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

    public enum State {
        CONTAINS_SHIP, NO_SHIP, UNKNOWN
    }



}

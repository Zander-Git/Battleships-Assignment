package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class CellView extends JButton implements ActionListener {
    
    private int x, y;
	public CellView(int x, int y) {
    	this.x=x;
    	this.y=y;
    	setBorder(new LineBorder(Color.GRAY));
        setBackground(null);
    	this.addActionListener(this);
    	
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	colorCell();
            	 AbstractButton btnSource = ((AbstractButton)e.getSource());
            	 btnSource.addActionListener(actionListener);
            	//actionControllerTHing()
            }
        });
        
    }
	public void printThings(ActionListener listenForWhatever) {
		this.addActionListener(listenForWhatever);
		//System.out.println(getX() + " " + getY());
	}

    protected void colorCell() {
        if (getBackground() != Color.DARK_GRAY) {
            setBackground(Color.DARK_GRAY);
        } else {
            setBackground(null);
        }
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(20, 20);
    }

    public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("poop");		
	}
}
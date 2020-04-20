package View;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Model.BoardModel;

public class MainUi {

	JFrame frame;  
	private static final int GRIDSIZE = 10;
	private static final int SIZE = 500;
	private Cell[][] cells;

	private final ActionListener actionListener = actionEvent -> {
		Object source = actionEvent.getSource();
		handleCell((Cell) source);

	};  

	MainUi(){     
		cells = new Cell[GRIDSIZE][GRIDSIZE];

		frame=new JFrame();	        
		JPanel rootPanel = new JPanel();
		JPanel leftPanel = new JPanel();
		JPanel yourPanel = new JPanel(new GridLayout(1, 0));
		JPanel myPanel = new JPanel(new GridLayout(1, 0));	

		frame = new JFrame("Battleships");
		frame.setSize(SIZE, SIZE);
		frame.setLayout(new BorderLayout());
		initializeGrid(yourPanel, false);
		initializeGrid(myPanel, true);

		rootPanel.setLayout(new GridLayout(1, 2, 20, 0));

		rootPanel.add(myPanel);
		rootPanel.add(yourPanel, BorderLayout.NORTH); 

		frame.add(rootPanel);

		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setContentPane(rootPanel);
		this.frame.pack();
		this.frame.setVisible(true);       


	}  


	private void handleCell(Cell source) {
		source.colorCell();

	}


	private void initializeGrid(JPanel myPanel, boolean b) {
		Container grid = new Container();
		grid.setLayout(new GridLayout(GRIDSIZE, GRIDSIZE));

		for (int row = 0; row < GRIDSIZE; row++) {
			for (int col = 0; col < GRIDSIZE; col++) {
				cells[row][col] = new Cell(row, col, actionListener);
				grid.add(cells[row][col]);

				if(b) {
					cells[row][col].setEnabled(false);
				}
			}
		}		
		myPanel.add(grid, BorderLayout.CENTER);
	}





	public static void main(String[] args) {  
		new MainUi();  		
	} 
}








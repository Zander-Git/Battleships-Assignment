package components;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import components.Cell.State;
import model.BoardModel;
import model.Ship;
import model.Ship.Type;
import sockets.client.SimpleClient;
import sockets.server.SimpleServer;

public class BoardView extends JPanel{
	
	private static final long serialVersionUID = 1L;

	private int gridSize = 10;
	Cell[][] cells=new Cell[gridSize][gridSize];	
	List<Ship> ships = new ArrayList<Ship>();
	private boolean ownBoard, userTurn, userReady;
	ShipSelectionPanel shipPanel;
	BoardModel boardModel;
	JButton readyBtn;
	int hits = 0;
	int maxHits = 0;
	
	SimpleClient sClient;
	SimpleServer sServer;
	
	final ActionListener actionListener = actionEvent -> {
        Object source = actionEvent.getSource();
            cellAction((Cell) source);
            
    };    

	
    
public BoardView(boolean ownBoard, ShipSelectionPanel shipPanel, SimpleClient _sClient) {
	sClient = _sClient;
	init(ownBoard, shipPanel);
}

public BoardView(boolean ownBoard, ShipSelectionPanel shipPanel, SimpleServer _sServer) {
	sServer = _sServer;
	init(ownBoard, shipPanel);
}

private void init(boolean ownBoard, ShipSelectionPanel shipPanel) {
		this.shipPanel = shipPanel;
		this.ownBoard = ownBoard;
		this.boardModel = new BoardModel(ownBoard, this);
		addCells();
		setUserReady(false);

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
			maxHits = maxHits + shipType.getLength();
		}

	}

protected void startGame() {
	setUserReady(true);
	setCellsEnabled(false);
	sendMessage("r,0,0");
	
}

private void cellAction(Cell source) {
	
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
		if(isUserTurn()) {
			int x = source.getRow();
			int y = source.getCol();
			
			String fireMsg = "f," + Integer.toString(x)+","+Integer.toString(y);
			sendMessage(fireMsg);
		}


	}
}

private void sendMessage(String msg) {
	try {
		if (sClient!=null && sServer == null) {
			sClient.sendMessageToServer(msg);

		}
		else if (sClient==null && sServer != null) {
			sServer.sendMessageToClient(msg);
		}
		else {
			throw new Exception("Neither client nor server initialised.");
		}
	}
	catch (Exception e){
		System.out.println(e.toString());
		System.exit(1);
	}
}

public void handleMiss(int x, int y) {
	cells[x][y].setBackground(Color.GRAY);
	cells[x][y].setEnabled(false);
	setUserTurn(false);
}

public void handleHit(int x, int y) {
	cells[x][y].setBackground(Color.RED);
	cells[x][y].setEnabled(false);
	setUserTurn(true);
	hits++;
	if (hits == maxHits ) {
		gameOver();
	}
	
}

public void gameOver() {
	String msg = "g,0,0";
	
	try {
		if (sClient!=null && sServer == null) {
			sClient.sendMessageToServer(msg);

		}
		else if (sClient==null && sServer != null) {
			sServer.sendMessageToClient(msg);
		}
		else {
			throw new Exception("Neither client nor server initialised.");
		}
	}
	catch (Exception e){
		System.out.println(e.toString());
		System.exit(1);
	}
	
	JOptionPane.showMessageDialog(this,"You Won! Please close the game",
			"" , JOptionPane.INFORMATION_MESSAGE );
	setCellsEnabled(false);
	
}

public void setUserTurn(boolean isYourTurn) {
	this.userTurn = isYourTurn;
}

public boolean isUserTurn() {
	return userTurn;
}

public Cell getCells(int x, int y) {
	return boardModel.getCell(x,y);
}

public BoardModel getBoardModel() {
	return boardModel;
}

public boolean isVertical() {
	return shipPanel.getOrientationIfVertical();
}

private void addCells() {
    	
    Container grid = new Container();
    grid.setLayout(new GridLayout(gridSize, gridSize));
    
    for (int row = 0; row < gridSize; row++) {
        for (int col = 0; col < gridSize; col++) {
            cells[row][col] = new Cell(row, col, actionListener);
            grid.add(cells[row][col]);
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

public boolean isUserReady() {
	return userReady;
}

public void setUserReady(boolean userReady) {
	this.userReady = userReady;
}



}

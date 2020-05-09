package Controller;
import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import client.view.BoardView;
import client.view.Cell;
import model.BoardModel;
import model.CellModel;

public class Controller {    

    private BoardView theView;
    private BoardModel theModel;     

    public Controller(BoardView theView, BoardModel theModel) {

        this.theView = theView;

        this.theModel = theModel;        

        // Tell the client.view that when ever the calculate button
        // is clicked to execute the actionPerformed method
        // in the printListener inner class         

        this.theView.printThings(new PrintListener());       

    }    

    class PrintListener implements ActionListener{ 

        public void actionPerformed(ActionEvent e) {            

            int X = 0, Y = 0;             
            try{            

                X = theView.getSquares(x,y).getX();
                Y = theView.getY();                

                System.out.println(theModel.getCell(1, 1+4).getX());
            }

            catch(NumberFormatException ex){
                System.out.println("error");

            }            
        }
    }
}
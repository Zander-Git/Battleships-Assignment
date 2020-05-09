package server.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


import sockets.server.SimpleServer;



public class ServerConfigPanelView extends JPanel{

	private static final long serialVersionUID = 1L;
	JLabel lblPortNumber;
	JTextField txtPortNumber;
	JLabel lblIPAddress;
	JTextField txtIPAddress;
	JButton btnStart;
	JButton btnClear;
	JButton btnTest;
	
	JFrame frmServerWindow;
	SimpleServer serverComponent;
	
	//CHANGE TO SERVER
	
	public ServerConfigPanelView(JFrame frmServerWindow, SimpleServer serverComponent) {
		
		this.frmServerWindow = frmServerWindow;
		this.serverComponent = serverComponent;
		
		this.lblPortNumber = new JLabel("Port number: ");
		this.txtPortNumber = new JTextField(10);
//		
//		this.lblIPAddress = new JLabel("IP address: ");
//		this.txtIPAddress = new JTextField(10);
		
		this.btnClear = new JButton("Clear");
		this.btnStart = new JButton("Start");

		
		this.setLayout(new GridLayout(3, 2, 4, 8));
		
//		this.add(this.lblIPAddress, 0);
//		this.add(this.txtIPAddress, 1);
		this.add(this.lblPortNumber, 0);
		this.add(this.txtPortNumber, 1);
		this.add(this.btnClear, 2);
		this.add(this.btnStart,3);
	
		setButtonActions();
	}
	
	private void setButtonActions() {
		
		this.btnClear.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
//				txtIPAddress.setText("");
				txtPortNumber.setText("");
			}
		});
		

		
		
		this.btnStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				int portNumber;
				
				if(txtPortNumber.getText().length() <=0 ) {
					System.err.println("Port number not provided..");
					
					JOptionPane.showMessageDialog(frmServerWindow, "Port number must be provided..",
							"Server Error" , JOptionPane.WARNING_MESSAGE );
					return;
					
				}
				
				try {
					portNumber = Integer.parseInt(txtPortNumber.getText());						
				}
				catch(NumberFormatException ex) {
					System.err.println("Port number should be an integer..");
					JOptionPane.showMessageDialog(frmServerWindow, "Port number must be an integer value..",
							"Server Error" , JOptionPane.WARNING_MESSAGE );
					return;
				}
				
				try {
					serverComponent.initializeServer(portNumber);
				} catch (IOException e1) {
					System.err.println("error in creating a socket in the server...");
					
					JOptionPane.showMessageDialog(frmServerWindow, "Error in creating server socket!",
							"Server Error" , JOptionPane.ERROR_MESSAGE );
					return;
					
				}
				
				
			}
		});
		
		
	}
}

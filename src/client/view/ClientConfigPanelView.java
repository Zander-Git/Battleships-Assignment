package client.view;

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

import sockets.client.SimpleClient;



public class ClientConfigPanelView extends JPanel{

	private static final long serialVersionUID = 1L;
	JLabel lblPortNumber;
	JTextField txtPortNumber;
	JLabel lblIPAddress;
	JTextField txtIPAddress;
	JButton btnStart;
	JButton btnClear;
	JButton btnTest;
	
	JFrame frmclientMainWindow;
	SimpleClient clientComponent;
	
	public ClientConfigPanelView(JFrame frmMainWindow, SimpleClient clientComponent) {
		
		this.frmclientMainWindow = frmMainWindow;
		this.clientComponent = clientComponent;
		
		this.lblPortNumber = new JLabel("Port number: ");
		this.txtPortNumber = new JTextField(10);
		
		this.lblIPAddress = new JLabel("IP address: ");
		this.txtIPAddress = new JTextField("127.0.0.1");
		
		
		this.btnClear = new JButton("Clear");
		this.btnStart = new JButton("Connect");
		
		this.setLayout(new GridLayout(3, 2, 4, 8));
		
		this.add(this.lblIPAddress, 0);
		this.add(this.txtIPAddress, 1);
		this.add(this.lblPortNumber, 2);
		this.add(this.txtPortNumber, 3);
		this.add(this.btnClear, 4);
		this.add(this.btnStart,5);
	
		setButtonActions();
	}
	
	private void setButtonActions() {
		
		this.btnClear.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				txtIPAddress.setText("");
				txtPortNumber.setText("");
			}
		});
		
		
		
		this.btnStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					clientComponent.init(txtIPAddress.getText(), Integer.parseInt(txtPortNumber.getText()));
				} catch (NumberFormatException | IOException e1) {
					System.err.println("Error in connecting to the server");
					JOptionPane.showMessageDialog(frmclientMainWindow, "Error in connecting the server!",
							"Server Error" , JOptionPane.ERROR_MESSAGE );
				}
				
			}
		});
		
		
	}
}

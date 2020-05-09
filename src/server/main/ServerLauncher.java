package server.main;


import server.view.ServerMainUi;

public class ServerLauncher {
	

	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				new ServerMainUi();

			}
		});
	}


}

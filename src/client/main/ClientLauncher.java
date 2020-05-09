package client.main;

import client.view.MainUi;


public class ClientLauncher {

	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				new MainUi();

			}
		});
	}
}
/**
  Created by Robin Link
*/

package edu.kit.ipd.dbis.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.ResourceBundle;

public class GrapeUI extends JPanel {

	private final String programName = "Grape";
	private ResourceBundle language;

	public GrapeUI(final ResourceBundle language) {
		this.language = language;
		run();
	}

	private void run() {
		JFrame mainWindow = new JFrame(programName);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mainWindow.setJMenuBar(MenuUI.makeMenuBar(language));

		mainWindow.pack();
		mainWindow.setVisible(true);
	}
}

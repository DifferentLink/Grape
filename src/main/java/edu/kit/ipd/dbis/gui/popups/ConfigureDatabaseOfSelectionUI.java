package edu.kit.ipd.dbis.gui.popups;

import edu.kit.ipd.dbis.controller.DatabaseController;
import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ResourceBundle;

public class ConfigureDatabaseOfSelectionUI extends ConfigureDatabaseUI {

	private String filePath;
	private List<Integer> graphs;

	public ConfigureDatabaseOfSelectionUI(DatabaseController databaseController, ResourceBundle language, Theme theme, String filePath, List<Integer> graphs) {
		super(databaseController, language, theme);
		this.filePath = filePath;
		this.graphs = graphs;
	}

	private class ConfigureDatabaseAction implements ActionListener {

		private final JFrame configureDatabaseOfSelectionUI;

		public ConfigureDatabaseAction(JFrame configureDatabaseOfSelectionUI) {
			this.configureDatabaseOfSelectionUI = configureDatabaseOfSelectionUI;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			databaseController.saveSelection(urlInput.getText(), userInput.getText(),
					passwordInput.getText(), nameInput.getText(), filePath, graphs);
			configureDatabaseOfSelectionUI.dispose();
		}
	}
}

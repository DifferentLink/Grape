package edu.kit.ipd.dbis.gui.popups;

import edu.kit.ipd.dbis.controller.DatabaseController;
import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ResourceBundle;

public class ConfigureDatabaseOfSelectionUI extends JFrame {

	private String filePath;
	private List<Integer> graphs;
	private final DatabaseController databaseController;
	private final ResourceBundle language;
	private final Theme theme;

	JTextArea nameInput;
	JTextArea urlInput;
	JTextArea userInput;
	JTextArea passwordInput;

	public ConfigureDatabaseOfSelectionUI(DatabaseController databaseController, ResourceBundle language, Theme theme, String filePath, List<Integer> graphs) {
		this.filePath = filePath;
		this.graphs = graphs;

		super.setTitle(language.getString("configureDatabase"));
		this.databaseController = databaseController;
		this.language = language;
		this.theme = theme;

		JPanel inputContainer = new JPanel();
		inputContainer.setLayout(new GridLayout(4, 2, 2, 10));

		JLabel nameLabel = new JLabel(language.getString("name"));
		nameInput = new JTextArea("graphs");
		inputContainer.add(nameLabel);
		inputContainer.add(nameInput);

		JLabel urlLabel = new JLabel(language.getString("url"));
		urlInput = new JTextArea("jdbc:mysql://localhost:3306/library");
		inputContainer.add(urlLabel);
		inputContainer.add(urlInput);

		JLabel userLabel = new JLabel(language.getString("user"));
		userInput = new JTextArea("user");
		inputContainer.add(userLabel);
		inputContainer.add(userInput);

		JLabel passwordLabel = new JLabel(language.getString("password"));
		passwordInput = new JTextArea("password");
		inputContainer.add(passwordLabel);
		inputContainer.add(passwordInput);

		JPanel buttonContainer = new JPanel();
		buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.X_AXIS));
		JButton configureButton = new JButton(language.getString("configure"));
		configureButton.addActionListener(new ConfigureDatabaseOfSelectionUI.ConfigureDatabaseOfSelectionAction(this));
		theme.style(configureButton);
		configureButton.setMaximumSize(new Dimension(50, 30));
		buttonContainer.add(Box.createHorizontalGlue());
		buttonContainer.add(configureButton);

		JLabel container = new JLabel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		container.add(inputContainer);
		container.add(Box.createVerticalStrut(10));
		container.add(buttonContainer);
		this.add(container);
		this.setMinimumSize(new Dimension(300, 200));
	}

	private class ConfigureDatabaseOfSelectionAction implements ActionListener {

		private final JFrame configureDatabaseOfSelectionUI;

		public ConfigureDatabaseOfSelectionAction(JFrame configureDatabaseOfSelectionUI) {
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

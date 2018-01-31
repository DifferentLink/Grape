/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui.popups;

import edu.kit.ipd.dbis.controller.DatabaseController;
import edu.kit.ipd.dbis.database.exceptions.sql.*;
import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ConfigureDatabaseUI extends JFrame {

	private final DatabaseController databaseController;
	private final ResourceBundle language;
	private final Theme theme;

	JTextArea nameInput;
	JTextArea urlInput;
	JTextArea userInput;
	JTextArea passwordInput;

	public ConfigureDatabaseUI(DatabaseController databaseController, ResourceBundle language, Theme theme) { // todo [Design Deviation] new menu entry

		super.setTitle("Configure Database"); // todo use language resource
		this.databaseController = databaseController;
		this.language = language;
		this.theme = theme;

		JPanel inputContainer = new JPanel();
		inputContainer.setLayout(new GridLayout(4, 2, 2, 10));

		JLabel nameLabel = new JLabel("Name"); // todo use language resource
		nameInput = new JTextArea("database");
		inputContainer.add(nameLabel);
		inputContainer.add(nameInput);

		JLabel urlLabel = new JLabel("URL"); // todo use language resource
		urlInput = new JTextArea("mysql://");
		inputContainer.add(urlLabel);
		inputContainer.add(urlInput);

		JLabel userLabel = new JLabel("User"); // todo use language resource
		userInput = new JTextArea("user");
		inputContainer.add(userLabel);
		inputContainer.add(userInput);

		JLabel passwordLabel = new JLabel("Password"); // todo use language resource
		passwordInput = new JTextArea("password");
		inputContainer.add(passwordLabel);
		inputContainer.add(passwordInput);

		JPanel buttonContainer = new JPanel();
		buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.X_AXIS));
		JButton configureButton = new JButton("Configure"); // todo use language resource
		configureButton.addActionListener(new ConfigureDatabaseAction(this));
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

	private class ConfigureDatabaseAction implements ActionListener {

		private final JFrame configureDatabaseUI;

		public ConfigureDatabaseAction(JFrame configureDatabaseUI) {
			this.configureDatabaseUI = configureDatabaseUI;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			databaseController.newDatabase(urlInput.getText(), userInput.getText(),
					passwordInput.getText(), nameInput.getText());
			configureDatabaseUI.dispose();
		}
	}
}

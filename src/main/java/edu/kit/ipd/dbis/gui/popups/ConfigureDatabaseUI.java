/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui.popups;

import edu.kit.ipd.dbis.controller.DatabaseController;
import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

public class ConfigureDatabaseUI extends JFrame {

	private final DatabaseController databaseController;
	private final ResourceBundle language;
	private final Theme theme;

	JTextArea urlInput;
	JTextArea userInput;
	JTextArea passwordInput;

	public ConfigureDatabaseUI(DatabaseController databaseController, ResourceBundle language, Theme theme) { // todo [Design Deviation] new menu entry

		super.setTitle("Configure Database"); // todo use language resource
		this.databaseController = databaseController;
		this.language = language;
		this.theme = theme;

		JPanel inputContainer = new JPanel();
		inputContainer.setLayout(new GridLayout(3, 2, 2, 10));

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
		configureButton.addActionListener(new ConfigureDatabaseAction());
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
		this.setMinimumSize(new Dimension(300, 150));
	}

	private class ConfigureDatabaseAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			try {
				databaseController.newDatabase(urlInput.getText(), userInput.getText(), passwordInput.getText(), "name"); // todo replace name with correct input
			} catch (Exception e) { // todo replace with correct exception as soon as available
				e.printStackTrace();
			}
		}
	}
}
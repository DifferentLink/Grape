/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui.popups;

import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class NewDatabaseUI extends JFrame {

	public NewDatabaseUI(ResourceBundle language, Theme theme) {

		super.setTitle("New Database"); // todo use language resource

		JPanel inputContainer = new JPanel();
		inputContainer.setLayout(new GridLayout(3, 2, 2, 10));

		JLabel urlLabel = new JLabel("URL"); // todo use language resource
		JTextArea urlInput = new JTextArea("mysql://");
		inputContainer.add(urlLabel);
		inputContainer.add(urlInput);

		JLabel userLabel = new JLabel("User"); // todo use language resource
		JTextArea userInput = new JTextArea("user");
		inputContainer.add(userLabel);
		inputContainer.add(userInput);

		JLabel passwordLabel = new JLabel("Password"); // todo use language resource
		JTextArea passwordInput = new JTextArea("password");
		inputContainer.add(passwordLabel);
		inputContainer.add(passwordInput);

		JPanel buttonContainer = new JPanel();
		buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.X_AXIS));
		JButton createButton = new JButton("Create"); // todo use language resource
		theme.style(createButton);
		createButton.setMaximumSize(new Dimension(50, 30));
		buttonContainer.add(Box.createHorizontalGlue());
		buttonContainer.add(createButton);

		JLabel container = new JLabel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		container.add(inputContainer);
		container.add(Box.createVerticalStrut(10));
		container.add(buttonContainer);
		this.add(container);
		this.setMinimumSize(new Dimension(200, 150));
	}
}

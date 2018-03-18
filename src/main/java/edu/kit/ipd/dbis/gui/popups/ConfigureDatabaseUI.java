package edu.kit.ipd.dbis.gui.popups;

import edu.kit.ipd.dbis.controller.DatabaseController;
import edu.kit.ipd.dbis.database.exceptions.sql.AccessDeniedForUserException;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.DatabaseDoesNotExistException;
import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.swing.JPasswordField;

/**
 * A window to configure the database
 */
public class ConfigureDatabaseUI extends JFrame {

	private final JFrame mainWindow;
	private final DatabaseController databaseController;
	private final ResourceBundle language;
	private final Theme theme;

	JTextField nameInput;
	JTextField urlInput;
	JTextField userInput;
	JPasswordField passwordInput;

	/**
	 * @param mainWindow
	 * @param databaseController the responsible controller
	 * @param language the language to use
	 * @param theme the theme to style the window
	 */
	public ConfigureDatabaseUI(
			JFrame mainWindow, DatabaseController databaseController, ResourceBundle language, Theme theme) {

		super.setTitle(language.getString("configureDatabase"));
		this.mainWindow = mainWindow;
		this.databaseController = databaseController;
		this.language = language;
		this.theme = theme;

		try {
			Image logo = ImageIO.read(getClass().getResource("/icons/GrapeLogo.png"));
			this.setIconImage(logo);
		} catch (IOException ignored) { }

		JPanel inputContainer = new JPanel();
		inputContainer.setLayout(new GridLayout(4, 2, 2, 10));

		JLabel nameLabel = new JLabel(language.getString("name"));
		nameInput = new JTextField("graphs");
		inputContainer.add(nameLabel);
		inputContainer.add(nameInput);

		JLabel urlLabel = new JLabel(language.getString("url"));
		urlInput = new JTextField("jdbc:mysql://localhost:3306/library");
		inputContainer.add(urlLabel);
		inputContainer.add(urlInput);

		JLabel userLabel = new JLabel(language.getString("user"));
		userInput = new JTextField("user");
		inputContainer.add(userLabel);
		inputContainer.add(userInput);

		JLabel passwordLabel = new JLabel(language.getString("password"));

		passwordInput = new JPasswordField("password");
		inputContainer.add(passwordLabel);
		inputContainer.add(passwordInput);

		JPanel buttonContainer = new JPanel();
		buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.X_AXIS));
		JButton configureButton = new JButton(language.getString("configure"));
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
		this.getRootPane().setDefaultButton(configureButton);
		this.add(container);
		this.setMinimumSize(new Dimension(300, 200));
		this.setLocationRelativeTo(null);
		this.addWindowListener(new CloseListener(mainWindow));
	}

	private class ConfigureDatabaseAction implements ActionListener {

		private final JFrame configureDatabaseUI;

		ConfigureDatabaseAction(JFrame configureDatabaseUI) {
			this.configureDatabaseUI = configureDatabaseUI;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			try {
				databaseController.newDatabase(urlInput.getText(), userInput.getText(),
						passwordInput.getText(), nameInput.getText());
				configureDatabaseUI.dispose();
			} catch (DatabaseDoesNotExistException e) {
				urlInput.setBackground(new Color(255, 99, 71));
			} catch (AccessDeniedForUserException e) {
				passwordInput.setBackground(new Color(255, 99, 71));
				userInput.setBackground(new Color(255, 99, 71));
			} catch (ConnectionFailedException e) {
				nameInput.setBackground(new Color(255, 99, 71));
			}

		}
	}

	private class CloseListener implements WindowListener {

		private final JFrame window;

		CloseListener(JFrame window) {
			this.window = window;
		}

		@Override
		public void windowOpened(WindowEvent windowEvent) {	}

		@Override
		public void windowClosing(WindowEvent windowEvent) {
			if (!databaseController.isDatabaseLoaded()) {
				System.exit(0);
			}
		}

		@Override
		public void windowClosed(WindowEvent windowEvent) {
			if (window != null) {
				window.setEnabled(true);
			}
		}

		@Override
		public void windowIconified(WindowEvent windowEvent) { }

		@Override
		public void windowDeiconified(WindowEvent windowEvent) { }

		@Override
		public void windowActivated(WindowEvent windowEvent) { }

		@Override
		public void windowDeactivated(WindowEvent windowEvent) {

		}
	}
}

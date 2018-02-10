package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.controller.DatabaseController;
import edu.kit.ipd.dbis.controller.GenerateController;
import edu.kit.ipd.dbis.controller.GraphEditorController;
import edu.kit.ipd.dbis.controller.StatusbarController;
import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * The statusbar at the bottom of Grape's GUI
 */
public class StatusbarUI extends JPanel {

	private final int statusbarHeight = 15;
	private final StatusbarController statusbarController;
	private DatabaseController databaseController;
	private GenerateController generateController;
	private GraphEditorController graphEditorController;
	private boolean isCalculationRunning = true;
	private JLabel statusText;
	private String remainingCalculations = "-";
	private String selectedRow = "Position -";
	private String databaseInfo = "-";

	/**
	 * @param statusbarController the controller responsible for the statusbar
	 * @param databaseController the controller responsible for database management
	 * @param language the language used
	 * @param theme the theme used to style the statusbar
	 */
	public StatusbarUI(StatusbarController statusbarController, DatabaseController databaseController,ResourceBundle language, Theme theme) {
		this.statusbarController = statusbarController;
		this.databaseController = databaseController;
		this.generateController = GenerateController.getInstance();
		this.graphEditorController = GraphEditorController.getInstance();

		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(Box.createHorizontalStrut(2));
		//this.add(makePauseButton(new Dimension(statusbarHeight, statusbarHeight), theme));
		this.add(Box.createHorizontalStrut(5));

		statusText = new JLabel(language.getString("noDatabaseLoaded"));
		this.add(statusText);

		this.setMaximumSize(new Dimension(Integer.MAX_VALUE, statusbarHeight));
		this.setMinimumSize(new Dimension(Integer.MIN_VALUE, statusbarHeight));
		this.setBorder(BorderFactory.createLineBorder(theme.foregroundColor, 1));
		this.setBackground(theme.backgroundColor);
		this.setForeground(theme.foregroundColor);

		JButton log = new JButton("Log");
		log.addActionListener(new ShowLogAction(new LogUI(statusbarController, language, theme), log));
		log.setBackground(theme.backgroundColor);
		log.setForeground(theme.foregroundColor);
		log.setFont(theme.defaultFont);
		log.setSize(new Dimension(Integer.MAX_VALUE, statusbarHeight));
		log.setBorder(BorderFactory.createEmptyBorder());

		this.generateController.setStatusbarUI(this);
		this.graphEditorController.setStatusbarUI(this);
		this.databaseController.setStatusbarUI(this);

		this.add(Box.createHorizontalGlue());
		this.add(log);
	}

	private JButton makePauseButton(Dimension size, Theme theme) {
		JButton pauseButton = new JButton();
		pauseButton.setMaximumSize(size);
		pauseButton.setBackground(theme.backgroundColor);
		pauseButton.setForeground(theme.foregroundColor);
		pauseButton.setBorder(BorderFactory.createEmptyBorder());
		pauseButton.addActionListener(new PauseRunAction(pauseButton));

		try {
			Image image = ImageIO.read(getClass().getResource("/icons/ButtonRun_Pause.png"));
			image = image.getScaledInstance(statusbarHeight - 2, statusbarHeight - 2, Image.SCALE_SMOOTH);
			pauseButton.setIcon(new ImageIcon(image));
		} catch (IOException e) {
			pauseButton.setText("P");
			pauseButton.setFont(theme.defaultFont);
		}

		pauseButton.setSize(size);
		return pauseButton;
	}

	private class PauseRunAction implements ActionListener {

		private final JButton button;

		private PauseRunAction(JButton button) {
			this.button = button;
		}


		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			if (isCalculationRunning) {
				statusbarController.pauseCalculation();
				try {
					Image image = ImageIO.read(getClass().getResource("/icons/ButtonRun_Continue.png"));
					image = image.getScaledInstance(statusbarHeight - 2, statusbarHeight - 2, Image.SCALE_SMOOTH);
					button.setIcon(new ImageIcon(image));
				} catch (IOException e) {
					button.setText("R");
				}
			} else {
				statusbarController.continueCalculation();
				try {
					Image image = ImageIO.read(getClass().getResource("/icons/ButtonRun_Pause.png"));
					image = image.getScaledInstance(statusbarHeight - 2, statusbarHeight - 2, Image.SCALE_SMOOTH);
					button.setIcon(new ImageIcon(image));
				} catch (IOException e) {
					button.setText("P");
				}
			}
			isCalculationRunning = !isCalculationRunning;
		}
	}

	private class ShowLogAction implements ActionListener {
		private final LogUI logUI;
		private Component component;

		public ShowLogAction(LogUI logUI, Component component) {
			this.logUI = logUI;
			this.component = component;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			logUI.drawLog(component);
		}
	}

	/**
	 * @param row the number of the row to display as the currently displayed row
	 */
	public void changeSelectedRow(final int row) {
		selectedRow = "Position " + (row + 1);
		updateStatusbarText();
	}

	/**
	 * @param numberOfUncalculatedGraphs the number of uncalculated graphs
	 */
	public void setRemainingCalculations(final int numberOfUncalculatedGraphs) {
		remainingCalculations = numberOfUncalculatedGraphs + " remaining calculations";
		updateStatusbarText();
	}

	/**
	 * @param databaseName the name of the database to display
	 * @param numberOfGraphs the total number of graphs in the database
	 */
	public void setDatabaseInfo(final String databaseName, final int numberOfGraphs) {
		databaseInfo = "Database: " + databaseName + " (" + numberOfGraphs + ")";
		updateStatusbarText();
	}

	private void updateStatusbarText() {
		statusText.setText(remainingCalculations + " | " + selectedRow + " | " + databaseInfo);
	}
}
/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.controller.StatusbarController;
import edu.kit.ipd.dbis.gui.themes.Theme;
import edu.kit.ipd.dbis.log.Event;
import edu.kit.ipd.dbis.log.EventType;
import edu.kit.ipd.dbis.log.History;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashSet;
import java.util.ResourceBundle;

public class StatusbarUI extends JPanel {

	private final int statusbarHeight = 15;
	private final StatusbarController statusbarController;
	private boolean isCalculationRunning = true;

	public StatusbarUI(StatusbarController statusbarController, ResourceBundle language, Theme theme) {
		this.statusbarController = statusbarController;

		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(Box.createHorizontalStrut(2));
		this.add(makePauseButton(new Dimension(statusbarHeight, statusbarHeight), theme));
		this.add(Box.createHorizontalStrut(5));

		JLabel statusText = new JLabel(language.getString("noDatabaseLoaded"));
		this.add(statusText);

		this.setMaximumSize(new Dimension(Integer.MAX_VALUE, statusbarHeight));
		this.setMinimumSize(new Dimension(Integer.MIN_VALUE, statusbarHeight));
		this.setBorder(BorderFactory.createLineBorder(theme.foregroundColor, 1));
		this.setBackground(theme.backgroundColor);
		this.setForeground(theme.foregroundColor);

		JButton log = new JButton("Log");
		log.addActionListener(new ShowLogAction(new LogUI(new History(20), language, theme), log));
		log.setBackground(theme.backgroundColor);
		log.setForeground(theme.foregroundColor);
		log.setFont(theme.defaultFont);
		log.setSize(new Dimension(Integer.MAX_VALUE, statusbarHeight));
		log.setBorder(BorderFactory.createEmptyBorder());

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
}
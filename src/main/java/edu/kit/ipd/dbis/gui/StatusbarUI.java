/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

public class StatusbarUI extends GUI {
	public static JPanel makeStatusbarUI(ResourceBundle language, Theme theme) {

		final int statusbarHeight = 15;

		JPanel statusbarUI = new JPanel();
		statusbarUI.setLayout(new BoxLayout(statusbarUI, BoxLayout.X_AXIS));
		statusbarUI.add(makePauseButton(new Dimension(statusbarHeight, statusbarHeight), theme));
		statusbarUI.add(Box.createHorizontalStrut(5));

		JLabel statusText = new JLabel(language.getString("noDatabaseLoaded"));
		statusbarUI.add(statusText);

		statusbarUI.setMaximumSize(new Dimension(Integer.MAX_VALUE, statusbarHeight));
		statusbarUI.setMinimumSize(new Dimension(Integer.MIN_VALUE, statusbarHeight));
		statusbarUI.setBorder(BorderFactory.createLineBorder(theme.foregroundColor, 1));
		statusbarUI.setBackground(theme.backgroundColor);
		statusbarUI.setForeground(theme.foregroundColor);

		JButton log = new JButton("Log");
		log.setBackground(theme.backgroundColor);
		log.setForeground(theme.foregroundColor);
		log.setFont(theme.defaultFont);
		log.getSize(new Dimension(Integer.MAX_VALUE, statusbarHeight));
		log.setBorder(BorderFactory.createEmptyBorder());

		statusbarUI.add(Box.createHorizontalGlue());
		statusbarUI.add(log);

		return statusbarUI;
	}

	private static JButton makePauseButton(Dimension size, Theme theme) {
		JButton pauseButton = new JButton();
		pauseButton.setMaximumSize(size);
		pauseButton.setBackground(theme.backgroundColor);
		pauseButton.setForeground(theme.foregroundColor);
		pauseButton.setBorder(BorderFactory.createEmptyBorder());

		try {
			Image icon = ImageIO.read(new File("src/main/resources/ButtonRun_Continue.png"));
			pauseButton.setIcon(new ImageIcon(icon));
		} catch (IOException e) {
			System.out.println("Missing icon of button run!");
			pauseButton.setText("P");
			pauseButton.setFont(theme.defaultFont);
		}

		return pauseButton;
	}

	/**
	 * Updates the GUI element.
	 */
	@Override
	public void update() {

	}
}
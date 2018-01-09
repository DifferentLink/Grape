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

public class StatusbarUI extends GUIElement {

	final int statusbarHeight = 15;

	public StatusbarUI(Controller controller, ResourceBundle language, Theme theme) {
		super(controller, language, theme);

		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
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
		log.setBackground(theme.backgroundColor);
		log.setForeground(theme.foregroundColor);
		log.setFont(theme.defaultFont);
		log.getSize(new Dimension(Integer.MAX_VALUE, statusbarHeight));
		log.setBorder(BorderFactory.createEmptyBorder());

		this.add(Box.createHorizontalGlue());
		this.add(log);

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
	 * Updates the GUIWindow element.
	 */
	@Override
	public void update() {

	}
}
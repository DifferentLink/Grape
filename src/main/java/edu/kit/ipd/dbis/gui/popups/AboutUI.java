package edu.kit.ipd.dbis.gui.popups;

import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.BorderLayout;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * A window to display grape's about text
 */
public class AboutUI extends JFrame {

	/**
	 * @param theme the theme used to style the popup
	 */
	public AboutUI(Theme theme, ResourceBundle language) {
		this.setBackground(theme.backgroundColor);
		try {
			this.setIconImage(ImageIO.read(getClass().getResource("/icons/GrapeLogo.png")));
		} catch (IOException ignored) { }
		JPanel container = new JPanel(new BorderLayout());
		container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		JTextArea aboutText = new JTextArea(language.getString("aboutText"));
		aboutText.setBackground(theme.backgroundColor);
		aboutText.setEditable(false);
		container.add(aboutText, BorderLayout.CENTER);
		this.add(container);
		this.pack();
		this.setLocationRelativeTo(null);
	}
}

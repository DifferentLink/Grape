package edu.kit.ipd.dbis.gui.popups;

import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.io.IOException;

/**
 * A window to display grape's about text
 */
public class AboutUI extends JFrame {

	/**
	 * @param theme the theme used to style the popup
	 */
	public AboutUI(Theme theme) {
		this.setBackground(theme.backgroundColor);
		try {
			this.setIconImage(ImageIO.read(getClass().getResource("/icons/GrapeLogo.png")));
		} catch (IOException ignored) { }
		JPanel container = new JPanel(new BorderLayout());
		container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		JLabel aboutText = new JLabel("About text");
		container.add(aboutText, BorderLayout.CENTER);
		this.add(container);
		this.pack();
		this.setLocationRelativeTo(null);
	}
}

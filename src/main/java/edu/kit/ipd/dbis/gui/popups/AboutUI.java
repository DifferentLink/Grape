/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.popups;

import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class AboutUI extends JFrame {
	public AboutUI(Theme theme) {
		this.setBackground(theme.backgroundColor);
		try {
			this.setIconImage(ImageIO.read(getClass().getResource("/icons/GrapeLogo.png")));
		} catch (IOException ignored) {}
		JPanel container = new JPanel(new BorderLayout());
		container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		JLabel aboutText = new JLabel("About text");
		container.add(aboutText, BorderLayout.CENTER);
		this.add(container);
		this.pack();
	}
}

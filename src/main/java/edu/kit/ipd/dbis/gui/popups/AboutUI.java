/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.popups;

import edu.kit.ipd.dbis.gui.themes.Theme;
import javax.swing.*;
import java.awt.*;

public class AboutUI extends JFrame {
	public AboutUI(Theme theme) {
		this.setBackground(theme.backgroundColor);
		this.setIconImage(null);
		JPanel container = new JPanel(new BorderLayout());
		container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		JLabel aboutText = new JLabel("About text");
		container.add(aboutText, BorderLayout.CENTER);
		this.add(container);
		this.pack();
	}
}

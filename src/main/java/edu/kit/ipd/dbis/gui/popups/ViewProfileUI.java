/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui.popups;

import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class ViewProfileUI extends JFrame {
	public ViewProfileUI(String profile, ResourceBundle language, Theme theme) throws HeadlessException {
		super.setTitle("Profile"); // todo use language resource

		JPanel content = new JPanel(new BorderLayout());
		JTextArea text = new JTextArea();
		text.setEditable(false);
		text.setText(profile);
		text.setBackground(theme.backgroundColor);
		text.setForeground(theme.foregroundColor);

		content.add(text, BorderLayout.CENTER);
		setMinimumSize(new Dimension(250, 250));
		this.add(content);
		pack();
		setLocationRelativeTo(null);
	}
}

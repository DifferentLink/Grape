/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui.filter;

import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class SimpleFilter extends JPanel {
	private JLabel dot = new JLabel("⚫");

	public SimpleFilter(ResourceBundle language, Theme theme) {
		this.setBackground(theme.backgroundColor);
		this.setForeground(theme.foregroundColor);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(dot);

		JTextField filterInput = new JTextField("...");
		this.add(filterInput);

		this.setBorder(BorderFactory.createLineBorder(theme.backgroundColor, 1));
		this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
	}
}

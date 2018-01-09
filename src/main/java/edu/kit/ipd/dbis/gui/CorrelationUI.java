/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.util.ResourceBundle;

public class CorrelationUI extends GUI {
	public static JPanel makeCorrelationUI(ResourceBundle language, Theme theme) {
		JPanel correlationUI = new JPanel();
		correlationUI.add(new JLabel("CorrelationUI"));
		correlationUI.setBackground(theme.backgroundColor);
		correlationUI.setForeground(theme.foregroundColor);
		correlationUI.setFont(theme.defaultFont);
		return correlationUI;
	}

	/**
	 * Updates the GUI element.
	 */
	@Override
	public void update() {

	}
}

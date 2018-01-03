/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.gui.theme.Theme;

import javax.swing.*;
import java.util.ResourceBundle;

public class CorrelationUI {
	public static JPanel makeCorrelationUI(ResourceBundle language, Theme theme) {
		JPanel correlationUI = new JPanel();
		correlationUI.add(new JLabel("CorrelationUI"));
		correlationUI.setBackground(theme.backgroundColor);
		correlationUI.setForeground(theme.foregroundColor);
		correlationUI.setFont(theme.defaultFont);
		return correlationUI;
	}
}

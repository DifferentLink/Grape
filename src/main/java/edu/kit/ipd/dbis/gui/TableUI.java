/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.util.ResourceBundle;

public class TableUI {
	public static JPanel makeTableUI(ResourceBundle language, Theme theme) {
		JPanel tableUI = new JPanel();
		tableUI.add(new JLabel("TableUI"));
		tableUI.setBackground(theme.backgroundColor);
		tableUI.setForeground(theme.foregroundColor);
		tableUI.setFont(theme.defaultFont);
		return tableUI;
	}
}

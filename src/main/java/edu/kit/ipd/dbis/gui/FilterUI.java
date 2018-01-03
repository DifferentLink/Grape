/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.gui.theme.Theme;

import javax.swing.*;
import java.util.ResourceBundle;

public class FilterUI extends JPanel {

	private FilterUI() {}

	public static JPanel makeFilterUI(ResourceBundle language, Theme theme) {
		JPanel filterUI = new JPanel();
		filterUI.add(new JLabel("FilterUI"));
		filterUI.setBackground(theme.backgroundColor);
		filterUI.setForeground(theme.foregroundColor);
		filterUI.setFont(theme.defaultFont);
		return filterUI;
	}
}

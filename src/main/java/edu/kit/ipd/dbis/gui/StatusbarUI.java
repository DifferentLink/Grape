/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.gui.theme.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class StatusbarUI {
	public static JPanel makeStatusbarUI(ResourceBundle language, Theme theme) {
		JPanel statusbarUI = new JPanel();
		statusbarUI.add(new JLabel("StatusbarUI"));

		statusbarUI.setMaximumSize(new Dimension(Integer.MAX_VALUE, 10));
		statusbarUI.setBorder(BorderFactory.createLineBorder(theme.foregroundColor, 1, false));
		statusbarUI.setBackground(theme.backgroundColor);
		statusbarUI.setForeground(theme.foregroundColor);
		statusbarUI.setFont(theme.defaultFont);
		return statusbarUI;
	}
}

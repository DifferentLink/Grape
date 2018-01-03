/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.gui.theme.Theme;

import javax.swing.*;
import java.util.ResourceBundle;

public class StatusbarUI {
	public static JPanel makeStatusbarUI(ResourceBundle language, Theme theme) {
		JPanel statusbarUI = new JPanel();
		statusbarUI.add(new JLabel("StatusbarUI"));
		statusbarUI.setBackground(theme.backgroundColor);
		statusbarUI.setForeground(theme.foregroundColor);
		statusbarUI.setFont(theme.defaultFont);
		return statusbarUI;
	}
}

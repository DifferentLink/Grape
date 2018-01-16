/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.themes;

import javax.swing.*;
import java.awt.*;

/**
 * Defines Grape's theme
 */
public abstract class Theme {
	public Color fontColor;
	public int defaultFontSize;
	public Font defaultFont;
	public Color foregroundColor;
	public Color backgroundColor;
	public int dividerSize;
	public Color shadowColor;
	public Color assertiveBackground;
	public Color tableSelectionColor;
	public Color buttonBackgorundColor;
	public Color buttonHighlightColor;
	public Color buttonDisabledColor;
	public Color disabledTextColor;
	public Color buttonTextColor;
	public Color outlineColor;
	public int outlineThickness;

	public void style(JPanel panel) {
		panel.setBackground(backgroundColor);
		panel.setForeground(foregroundColor);
		panel.setBorder(BorderFactory.createLineBorder(outlineColor, outlineThickness));
	}

	public void style(JButton button) {
		button.setBackground(buttonBackgorundColor);
		button.setForeground(buttonTextColor);
		button.setBorder(BorderFactory.createLineBorder(outlineColor, outlineThickness));
	}

	public void style(JComboBox comboBox) {
		comboBox.setBackground(backgroundColor);
		comboBox.setForeground(foregroundColor);
		comboBox.setBorder(BorderFactory.createLineBorder(outlineColor, outlineThickness));
	}

	public void style(JLabel label) {
		label.setBackground(backgroundColor);
		label.setForeground(foregroundColor);
	}
}

package edu.kit.ipd.dbis.gui.themes;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;

/**
 * Defines the properties a theme has to have
 */
public abstract class Theme {
	public Color fontColor;
	public int defaultFontSize;
	public Font defaultFont;
	public Font smallFont;
	public Color foregroundColor;
	public Color backgroundColor;
	public int dividerSize;
	public Color shadowColor;
	public Color assertiveBackground;
	public Color unassertiveBackground;
	public Color tableSelectionColor;
	public Color tableSelectionColorDark;
	public Color buttonBackgorundColor;
	public Color buttonHighlightColor;
	public Color buttonDisabledColor;
	public Color disabledTextColor;
	public Color buttonTextColor;
	public Color outlineColor;
	public int outlineThickness;
	public Color neutralColor;
	public Color lightNeutralColor;

	/**
	 * @param panel the panel to style
	 */
	public void style(JPanel panel) {
		panel.setBackground(backgroundColor);
		panel.setForeground(foregroundColor);
		panel.setBorder(BorderFactory.createLineBorder(outlineColor, outlineThickness));
	}

	/**
	 * @param button the button to style
	 */
	public void style(JButton button) {
		button.setBackground(buttonBackgorundColor);
		button.setForeground(buttonTextColor);
		button.setBorder(BorderFactory.createLineBorder(outlineColor, outlineThickness));
	}

	/**
	 * @param comboBox the combobox to stlye
	 */
	public void style(JComboBox comboBox) {
		comboBox.setBackground(backgroundColor);
		comboBox.setForeground(foregroundColor);
		comboBox.setBorder(BorderFactory.createLineBorder(outlineColor, outlineThickness));
	}

	/**
	 * @param label the label to style
	 */
	public void style(JLabel label) {
		label.setBackground(backgroundColor);
		label.setForeground(foregroundColor);
	}
}

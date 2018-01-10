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

	abstract public void style(JButton button);
}

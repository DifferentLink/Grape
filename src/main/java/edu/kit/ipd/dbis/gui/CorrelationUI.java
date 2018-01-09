/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.util.ResourceBundle;

public class CorrelationUI extends GUIElement {

	public CorrelationUI(ResourceBundle language, Theme theme) {
		super(language, theme);
	}

	public GUIElement makeCorrelationUI(ResourceBundle language, Theme theme) {
		GUIElement correlationUI = new CorrelationUI(super.language, super.theme);
		correlationUI.add(new JLabel("CorrelationUI"));
		correlationUI.setBackground(theme.backgroundColor);
		correlationUI.setForeground(theme.foregroundColor);
		correlationUI.setFont(theme.defaultFont);
		return correlationUI;
	}

	/**
	 * Updates the GUIWindow element.
	 */
	@Override
	public void update() {

	}
}

/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.util.ResourceBundle;

public class FilterUI extends GUIElement {

	public FilterUI(ResourceBundle language, Theme theme) {
		super(language, theme);
	}

	public GUIElement makeFilterUI() {
		GUIElement filterUI = new FilterUI(super.language, super.theme);
		filterUI.add(new JLabel("FilterUI"));
		filterUI.setBackground(super.theme.backgroundColor);
		filterUI.setForeground(super.theme.foregroundColor);
		filterUI.setFont(super.theme.defaultFont);
		return filterUI;
	}

	/**
	 * Updates the GUIWindow element.
	 */
	@Override
	public void update() {

	}
}

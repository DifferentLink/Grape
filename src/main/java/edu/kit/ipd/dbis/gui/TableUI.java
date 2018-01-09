/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.util.ResourceBundle;

public class TableUI extends GUIElement {
	public TableUI(ResourceBundle language, Theme theme) {
		super(language, theme);
	}

	public GUIElement makeTableUI() {
		GUIElement tableUI = new TableUI(language, theme);
		tableUI.add(new JLabel("TableUI"));
		tableUI.setBackground(theme.backgroundColor);
		tableUI.setForeground(theme.foregroundColor);
		tableUI.setFont(theme.defaultFont);
		return tableUI;
	}

	/**
	 * Updates the GUIWindow element.
	 */
	@Override
	public void update() {

	}
}

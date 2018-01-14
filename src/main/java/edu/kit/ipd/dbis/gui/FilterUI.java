/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class FilterUI extends GUIElement {

	public FilterUI(Controller controller, ResourceBundle language, Theme theme) {
		super(controller, language, theme);

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBackground(theme.backgroundColor);
		this.setForeground(theme.foregroundColor);
		String[] filterMenuEntries = {"Save selected filter...", "Load Filter..."}; // todo use language resource
		JComboBox<String> filter = new JComboBox<>(filterMenuEntries);
		filter.setMaximumSize(new Dimension(100, 30));
		filter.setMinimumSize(new Dimension(100, 0));
		filter.setBackground(theme.backgroundColor);
		filter.setForeground(theme.foregroundColor);



		this.add(filter);
	}

	/**
	 * Updates the GUIWindow element.
	 */
	@Override
	public void update() {

	}
}

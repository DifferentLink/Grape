/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.filter;

import edu.kit.ipd.dbis.gui.Controller;
import edu.kit.ipd.dbis.gui.GUIElement;
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

		JPanel filterMenu = new JPanel();
		filterMenu.setLayout(new BoxLayout(filterMenu, BoxLayout.X_AXIS));
		String[] filterMenuEntries = {"Save selected filter...", "Load Filter..."}; // todo use language resource
		JComboBox<String> filter = new JComboBox<>(filterMenuEntries);
		filter.setMaximumSize(new Dimension(100, 30));
		filter.setMinimumSize(new Dimension(100, 0));
		filter.setBackground(theme.backgroundColor);
		filter.setForeground(theme.foregroundColor);
		filterMenu.add(filter);

		this.add(filterMenu, BorderLayout.NORTH);

		SimpleFilter emptyFilter = new SimpleFilter(language, theme);
		this.add(emptyFilter, BorderLayout.CENTER);

		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
		buttons.setBackground(theme.backgroundColor);
		buttons.setForeground(theme.foregroundColor);
	}

	/**
	 * Updates the GUIWindow element.
	 */
	@Override
	public void update() {

	}
}

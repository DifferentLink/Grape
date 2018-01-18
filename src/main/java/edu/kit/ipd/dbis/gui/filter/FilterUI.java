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
	private FilterManagement filterManagement;
	private JPanel filterMenu;
	private JPanel filterGroupsUI;
	private JPanel simpleFilterUI;

	public FilterUI(Controller controller, ResourceBundle language, Theme theme) {
		super(controller, language, theme);

		filterManagement = new FilterManagement();

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBackground(theme.backgroundColor);
		this.setForeground(theme.foregroundColor);

		filterMenu = new JPanel();
		filterMenu.setLayout(new BoxLayout(filterMenu, BoxLayout.X_AXIS));
		String[] filterMenuEntries = {"Save selected filter...", "Load Filter..."}; // todo use language resource
		JComboBox<String> filter = new JComboBox<>(filterMenuEntries);
		filter.setMaximumSize(new Dimension(100, 100));
		filter.setMinimumSize(new Dimension(100, 0));
		filter.setBackground(theme.backgroundColor);
		filter.setForeground(theme.foregroundColor);
		filterMenu.add(filter);

		this.add(filterMenu, BorderLayout.NORTH);

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

		filterGroupsUI = new JPanel();
		filterGroupsUI.setLayout(new BoxLayout(filterGroupsUI, BoxLayout.Y_AXIS));

		for (FilterGroup filterGroup : filterManagement.getFilterGroups()) {
			filterGroupsUI.add(drawFilterGroup(filterGroup));
		}

		for (SimpleFilter simpleFilter : filterManagement.getSimpleFilter()) {
			simpleFilterUI.add(drawSimpleFilter(simpleFilter));
		}
	}

	private JPanel drawSimpleFilter(SimpleFilter simpleFilter) {
		return new JPanel();
	}

	private JPanel drawFilterGroup(FilterGroup filterGroup) {
		return new JPanel();
	}
}

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
	private JPanel filter;

	public FilterUI(Controller controller, ResourceBundle language, Theme theme) {
		super(controller, language, theme);

		filterManagement = new FilterManagement();

		this.setLayout(new BorderLayout());
		this.setBackground(theme.backgroundColor);
		this.setForeground(theme.foregroundColor);

		filterMenu = new JPanel();
		filterMenu.setLayout(new BoxLayout(filterMenu, BoxLayout.X_AXIS));
		String[] filterMenuEntries = {"Save selected filter...", "Load Filter..."}; // todo use language resource
		JComboBox<String> filterDropdown = new JComboBox<>(filterMenuEntries);
		filterDropdown.setMaximumSize(new Dimension(100, 100));
		filterDropdown.setMinimumSize(new Dimension(100, 0));
		filterDropdown.setBackground(theme.backgroundColor);
		filterDropdown.setForeground(theme.foregroundColor);
		filterMenu.add(filterDropdown);

		this.add(filterMenu, BorderLayout.NORTH);

		filter = new JPanel();
		this.add(filter, BorderLayout.CENTER);

		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
		buttons.setBackground(theme.backgroundColor);
		buttons.setForeground(theme.foregroundColor);

		JButton newFilter = new JButton(" New Filter "); // todo replace with string from language
		JButton newFilterGroup = new JButton(" New Group "); // todo replace with string from language
		theme.style(newFilter);
		theme.style(newFilterGroup);
		buttons.add(newFilter);
		buttons.add(Box.createHorizontalStrut(5));
		buttons.add(newFilterGroup);
		buttons.setBorder(BorderFactory.createLineBorder(theme.foregroundColor, 1));

		this.add(Box.createVerticalGlue());
		this.add(buttons, BorderLayout.SOUTH);

		this.setBorder(BorderFactory.createLineBorder(Color.lightGray, 2));
	}

	/**
	 * Updates the GUIWindow element.
	 */
	@Override
	public void update() {

		filter = new JPanel();
		filter.setLayout(new BoxLayout(filter, BoxLayout.Y_AXIS));

		for (FilterGroup filterGroup : filterManagement.getFilterGroups()) {
			filter.add(drawFilterGroup(filterGroup));
		}

		for (SimpleFilter simpleFilter : filterManagement.getSimpleFilter()) {
			filter.add(drawSimpleFilter(simpleFilter));
		}
	}

	private JPanel drawSimpleFilter(SimpleFilter simpleFilter) {
		return new JPanel();
	}

	private JPanel drawFilterGroup(FilterGroup filterGroup) {
		return new JPanel();
	}
}

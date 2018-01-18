/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.filter;

import edu.kit.ipd.dbis.gui.Controller;
import edu.kit.ipd.dbis.gui.GUIElement;
import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
		filterMenu.setLayout(new GridBagLayout());

		String[] filterMenuEntries = {"Save selected filter...", "Load Filter..."}; // todo use language resource
		JComboBox<String> filterDropdown = new JComboBox<>(filterMenuEntries);
		filterDropdown.setMaximumSize(new Dimension(100, 100));
		filterDropdown.setMinimumSize(new Dimension(100, 0));
		filterDropdown.setBackground(theme.lightNeutralColor);
		filterDropdown.setForeground(theme.foregroundColor);

		GridBagConstraints menuConstraints = new GridBagConstraints();
		menuConstraints.gridwidth = GridBagConstraints.REMAINDER;
		menuConstraints.anchor = GridBagConstraints.CENTER;
		menuConstraints.fill = GridBagConstraints.VERTICAL;
		menuConstraints.weightx = 1;
		filterMenu.add(filterDropdown, menuConstraints);
		filterMenu.setBackground(theme.lightNeutralColor);
		filterMenu.setBorder(null);
		this.add(filterMenu, BorderLayout.NORTH);

		filter = new JPanel();
		filter.setLayout(new BoxLayout(filter, BoxLayout.Y_AXIS));
		theme.style(filter);
		filter.setBorder(null);
		this.add(filter, BorderLayout.CENTER);

		JPanel buttons = new JPanel(new GridBagLayout());
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
		buttons.setBackground(theme.backgroundColor);
		buttons.setForeground(theme.foregroundColor);

		JPanel buttonsAlignment = new JPanel(new GridBagLayout());
		GridBagConstraints buttonConstraints = new GridBagConstraints();
		buttonConstraints.gridwidth = GridBagConstraints.REMAINDER;
		buttonConstraints.anchor = GridBagConstraints.CENTER;
		buttonConstraints.fill = GridBagConstraints.VERTICAL;
		buttonConstraints.weightx = 1;

		JButton newFilter = new JButton(" New Filter "); // todo replace with string from language
		theme.style(newFilter);
		newFilter.addActionListener(new NewFilterAction());
		JButton newFilterGroup = new JButton(" New Group "); // todo replace with string from language
		theme.style(newFilterGroup);
		newFilterGroup.addActionListener(new NewFilterGroupAction());
		buttons.add(newFilter, buttonConstraints);
		buttons.add(Box.createHorizontalStrut(5));
		buttons.add(newFilterGroup, buttonConstraints);
		buttons.setBorder(BorderFactory.createLineBorder(theme.foregroundColor, 1));

		buttonsAlignment.add(buttons);
		theme.style(buttonsAlignment);

		this.add(Box.createVerticalGlue(), BorderLayout.SOUTH);
		this.add(buttonsAlignment, BorderLayout.SOUTH);

		this.setBorder(BorderFactory.createLineBorder(Color.lightGray, 2));
	}

	/**
	 * Updates the GUIWindow element.
	 */
	@Override
	public void update() {

		filter.removeAll();
		filter.add(Box.createVerticalStrut(2));

		for (FilterGroup filterGroup : filterManagement.getFilterGroups()) {
			filter.add(drawFilterGroup(filterGroup));
		}

		for (SimpleFilter simpleFilter : filterManagement.getSimpleFilter()) {
			filter.add(drawSimpleFilter(simpleFilter));
		}
	}

	private JPanel drawSimpleFilter(SimpleFilter simpleFilter) {
		JPanel simpleFilterUI = new JPanel();
		simpleFilterUI.setLayout(new BoxLayout(simpleFilterUI, BoxLayout.X_AXIS));
		simpleFilterUI.add(new JLabel("⚫"));
		JCheckBox isActive = new JCheckBox();
		isActive.addActionListener(new ToggleFilterAction(simpleFilter, isActive.isSelected()));
		simpleFilterUI.add(isActive);
		JTextArea filterInput = new JTextArea(" ...");
		filterInput.setBorder(BorderFactory.createLineBorder(theme.neutralColor));
		simpleFilterUI.add(filterInput);
		simpleFilterUI.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
		simpleFilterUI.setBackground(theme.backgroundColor);
		return simpleFilterUI;
	}

	private JPanel drawFilterGroup(FilterGroup filterGroup) {
		JPanel filterGroupHeaderUI = new JPanel();
		filterGroupHeaderUI.setLayout(new BoxLayout(filterGroupHeaderUI, BoxLayout.X_AXIS));
		JCheckBox isActive = new JCheckBox();
		filterGroupHeaderUI.add(isActive);
		JTextArea filterInput = new JTextArea(" Filter Group " + filterGroup.getID());
		filterInput.setBorder(BorderFactory.createLineBorder(theme.neutralColor));
		filterGroupHeaderUI.add(filterInput);
		filterGroupHeaderUI.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
		filterGroupHeaderUI.setBackground(theme.backgroundColor);

		JPanel simpleFilterContainer = new JPanel();
		simpleFilterContainer.setLayout(new BoxLayout(simpleFilterContainer, BoxLayout.Y_AXIS));
		for (SimpleFilter simpleFilter : filterGroup.getSimpleFilter()) {
			simpleFilterContainer.add(drawSimpleFilter(simpleFilter));
		}

		JPanel filterGroupUI = new JPanel();
		filterGroupUI.setLayout(new BoxLayout(filterGroupUI, BoxLayout.Y_AXIS));
		filterGroupUI.add(filterGroupHeaderUI);
		filterGroupUI.add(simpleFilterContainer);
		filterGroupUI.setBorder(BorderFactory.createLineBorder(theme.backgroundColor, 2));

		return filterGroupUI;
	}

	private class NewFilterAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			filterManagement.addNewSimpleFilter();
			update();
			repaint();
			revalidate();
		}
	}

	private class NewFilterGroupAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			filterManagement.addNewFilterGroup();
			update();
			repaint();
			revalidate();
		}
	}

	private class ToggleFilterAction implements ActionListener {

		private final Filter filter;
		private final boolean isActive;

		public ToggleFilterAction(Filter filter, boolean isActive) {
			this.filter = filter;
			this.isActive = isActive;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			filter.setActive(isActive);
		}
	}
}

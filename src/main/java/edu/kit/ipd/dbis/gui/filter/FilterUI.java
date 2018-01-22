/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.filter;

import edu.kit.ipd.dbis.controller.FilterController;
import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

public class FilterUI extends JPanel {
	private FilterController controller;
	private FilterManagement filterManagement;
	private JPanel filterMenu;
	private JPanel filter;
	private Theme theme;

	private final int simpleFilterUIHeight = 22;

	public FilterUI(FilterController controller, ResourceBundle language, Theme theme) {

		this.controller = controller;
		this.theme = theme;

		filterManagement = new FilterManagement();

		this.setLayout(new BorderLayout());
		this.setBackground(theme.backgroundColor);
		this.setForeground(theme.foregroundColor);

		filterMenu = new JPanel();
		filterMenu.setLayout(new GridBagLayout());

		String[] filterMenuEntries = {"Save selected filter...", "Load Filter..."}; // todo use language resource
		JComboBox<String> filterDropdown = new JComboBox<>(filterMenuEntries);
		filterDropdown.setFocusable(false);
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
		filter.setLayout(new BorderLayout());
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
		newFilter.setBackground(theme.assertiveBackground);
		newFilter.addActionListener(new NewFilterAction());
		JButton newFilterGroup = new JButton(" New Group "); // todo replace with string from language
		newFilterGroup.setBackground(theme.assertiveBackground);
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
	public void update() {

		filter.removeAll();
		filter.add(Box.createVerticalStrut(2));
		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

		if (filterManagement.getFilterGroups().size() > 0) {
			JPanel textContainerGroup = new JPanel(new BorderLayout());
			JLabel groupLabel = new JLabel("Filter Groups:");
			groupLabel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, theme.foregroundColor));
			groupLabel.setFont(theme.smallFont);
			textContainerGroup.add(groupLabel, BorderLayout.CENTER);
			textContainerGroup.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
			container.add(textContainerGroup);

			for (FilterGroup filterGroup : filterManagement.getFilterGroups()) {
				container.add(drawFilterGroup(filterGroup));
			}
			container.add(Box.createVerticalStrut(6));
		}

		if (filterManagement.getSimpleFilter().size() > 0) {
			JPanel textContainerSimple = new JPanel(new BorderLayout());
			JLabel simpleLabel = new JLabel("Simple Filter:");
			simpleLabel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, theme.foregroundColor));
			simpleLabel.setFont(theme.smallFont);
			textContainerSimple.add(simpleLabel, BorderLayout.CENTER);
			textContainerSimple.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
			container.add(textContainerSimple);

			for (SimpleFilter simpleFilter : filterManagement.getSimpleFilter()) {
				container.add(drawSimpleFilter(simpleFilter));
				container.add(Box.createVerticalStrut(2));
			}
		}

		JScrollPane scrollPane = new JScrollPane(container);
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, Integer.MAX_VALUE));
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		filter.add(scrollPane, BorderLayout.CENTER);
	}

	private JPanel drawSimpleFilter(SimpleFilter simpleFilter) {
		JPanel simpleFilterUI = new JPanel();
		simpleFilterUI.setLayout(new BoxLayout(simpleFilterUI, BoxLayout.X_AXIS));
		simpleFilterUI.add(new JLabel("⚫"));
		JCheckBox isActive = new JCheckBox();
		isActive.addActionListener(new ToggleFilterAction(simpleFilter, isActive.isSelected()));
		simpleFilterUI.add(isActive);
		JTextArea filterInput = new JTextArea(simpleFilter.getText());
		filterInput.setBorder(BorderFactory.createLineBorder(theme.neutralColor));
		simpleFilterUI.add(filterInput);
		JButton deleteFilter = new JButton("X");
		deleteFilter.addActionListener(new RemoveFilterAction(simpleFilter.getID()));
		deleteFilter.setBackground(theme.backgroundColor);
		deleteFilter.setPreferredSize(new Dimension(simpleFilterUIHeight, simpleFilterUIHeight));
		simpleFilterUI.add(deleteFilter);
		simpleFilterUI.setMaximumSize(new Dimension(Integer.MAX_VALUE, simpleFilterUIHeight));
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
		JButton deleteFilterGroup = new JButton("X");
		deleteFilterGroup.addActionListener(new RemoveFilterAction(filterGroup.getID()));
		deleteFilterGroup.setBackground(theme.backgroundColor);
		deleteFilterGroup.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		deleteFilterGroup.setPreferredSize(new Dimension(simpleFilterUIHeight, simpleFilterUIHeight));
		filterGroupHeaderUI.add(deleteFilterGroup);
		filterGroupHeaderUI.setMaximumSize(new Dimension(Integer.MAX_VALUE, simpleFilterUIHeight));
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

	private class RemoveFilterAction implements ActionListener {
		private int id;

		public RemoveFilterAction(int id) {
			this.id = id;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			filterManagement.remove(id);
			update();
			repaint();
			revalidate();
		}
	}

	private class FilterInputChange implements DocumentListener {

		private final Filter filter;
		private final JTextArea textArea;

		public FilterInputChange(Filter filter, JTextArea textArea) {
			this.filter = filter;
			this.textArea = textArea;
		}

		@Override
		public void insertUpdate(DocumentEvent documentEvent) {
		}

		@Override
		public void removeUpdate(DocumentEvent documentEvent) {
		}

		@Override
		public void changedUpdate(DocumentEvent documentEvent) {
		}
	}
}

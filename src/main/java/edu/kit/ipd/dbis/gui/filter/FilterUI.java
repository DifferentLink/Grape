/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.filter;

import edu.kit.ipd.dbis.controller.FilterController;
import edu.kit.ipd.dbis.filter.exceptions.InvalidInputException;
import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.ResourceBundle;

public class FilterUI extends JPanel {
	private final FilterController filterController;
	private UIFilterManager uiFilterManager;
	private JPanel filterMenu;
	private JPanel filter;
	private Theme theme;
	private JComboBox<String> filterDropdown;

	private final int simpleFilterUIHeight = 22;

	public FilterUI(FilterController filterController, ResourceBundle language, Theme theme) {

		this.filterController = filterController;
		this.theme = theme;

		uiFilterManager = new UIFilterManager();

		this.setLayout(new BorderLayout());
		this.setBackground(theme.backgroundColor);
		this.setForeground(theme.foregroundColor);

		filterMenu = new JPanel();
		filterMenu.setLayout(new GridBagLayout());

		String[] filterMenuEntries = {"Save selected filter...", "Load filter..."}; // todo use language resource
		filterDropdown = new JComboBox<>(filterMenuEntries);
		filterDropdown.addActionListener(new ManageFilterAction());
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
		newFilter.addActionListener(new NewFilterAction(filterController));
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

		if (uiFilterManager.getFilterGroups().size() > 0) {
			JPanel textContainerGroup = new JPanel(new BorderLayout());
			JLabel groupLabel = new JLabel("Filter Groups:"); // todo use language resource
			groupLabel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, theme.foregroundColor));
			groupLabel.setFont(theme.smallFont);
			textContainerGroup.add(groupLabel, BorderLayout.CENTER);
			textContainerGroup.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
			container.add(textContainerGroup);

			for (FilterGroup filterGroup : uiFilterManager.getFilterGroups()) {
				container.add(drawFilterGroup(filterGroup));
			}
			container.add(Box.createVerticalStrut(6));
		}

		if (uiFilterManager.getSimpleFilter().size() > 0) {
			JPanel textContainerSimple = new JPanel(new BorderLayout());
			JLabel simpleLabel = new JLabel("Simple Filters:"); // todo use language resource
			simpleLabel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, theme.foregroundColor));
			simpleLabel.setFont(theme.smallFont);
			textContainerSimple.add(simpleLabel, BorderLayout.CENTER);
			textContainerSimple.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
			container.add(textContainerSimple);

			for (SimpleFilter simpleFilter : uiFilterManager.getSimpleFilter()) {
				container.add(drawSimpleFilter(simpleFilter)); // todo throws InvalidInputException
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
		isActive.setSelected(simpleFilter.isActive());
		isActive.addActionListener(new ToggleFilterAction(simpleFilter, isActive));
		simpleFilterUI.add(isActive);
		JTextArea filterInput = new JTextArea(simpleFilter.getText());
		filterInput.getDocument().addDocumentListener(new SimpleFilterInputChange(simpleFilter, filterInput));
		filterInput.setBorder(BorderFactory.createLineBorder(theme.neutralColor));
		try {
			filterController.updateFilter(simpleFilter.getText(), simpleFilter.getID());
			filterInput.setBackground(Color.WHITE);
		} catch (InvalidInputException e) {
			filterInput.setBackground(theme.lightNeutralColor);
		}
		simpleFilterUI.add(filterInput);
		JButton deleteFilter = new JButton("X");
		deleteFilter.addActionListener(new RemoveFilterAction(simpleFilter.getID()));
		deleteFilter.setBackground(theme.backgroundColor);
		deleteFilter.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
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
		isActive.setSelected(filterGroup.isActive());
		isActive.addActionListener(new ToggleFilterAction(filterGroup, isActive));
		filterGroupHeaderUI.add(isActive);
		JTextArea filterInput = new JTextArea(filterGroup.getText());
		filterInput.getDocument().addDocumentListener(new FilterGroupInputChange(filterGroup, filterInput));
		filterInput.setBorder(BorderFactory.createLineBorder(theme.neutralColor));
		try {
			filterController.updateFilterGroup(filterGroup.getText(), filterGroup.getID());
			filterInput.setBackground(Color.WHITE);
		} catch (InvalidInputException e) {
			filterInput.setBackground(theme.lightNeutralColor);
		}
		filterGroupHeaderUI.add(filterInput);
		filterGroupHeaderUI.add(Box.createHorizontalStrut(2));

		JButton addSimpleFilterToGroup = new JButton("+");
		addSimpleFilterToGroup.addActionListener(new SimpleFilterToGroupAction(filterGroup));
		addSimpleFilterToGroup.setBackground(theme.assertiveBackground);
		addSimpleFilterToGroup.setBorder(BorderFactory.createLineBorder(theme.outlineColor, 1));
		addSimpleFilterToGroup.setMaximumSize(new Dimension(simpleFilterUIHeight, simpleFilterUIHeight));
		addSimpleFilterToGroup.setPreferredSize(new Dimension(simpleFilterUIHeight, simpleFilterUIHeight));
		filterGroupHeaderUI.add(addSimpleFilterToGroup);

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

		private final FilterController filterController;

		private NewFilterAction(FilterController filterController) {
			this.filterController = filterController;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			uiFilterManager.addNewSimpleFilter();
			update();
			repaint();
			revalidate();
		}
	}

	private class NewFilterGroupAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			uiFilterManager.addNewFilterGroup("");
			update();
			repaint();
			revalidate();
		}
	}

	private class ToggleFilterAction implements ActionListener { // todo use filtercontroller

		private final Filter filter;
		private final JCheckBox checkBox;

		public ToggleFilterAction(Filter filter, JCheckBox checkBox) {
			this.filter = filter;
			this.checkBox = checkBox;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			filter.setActive(checkBox.isSelected());
			if (checkBox.isSelected()) {
				filterController.activate(filter.getID());
			} else {
				filterController.deactivate(filter.getID());
			}
		}
	}

	private class RemoveFilterAction implements ActionListener {
		private int id;

		public RemoveFilterAction(int id) {
			this.id = id;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			uiFilterManager.remove(id);
			filterController.removeFiltersegment(id);
			update();
			repaint();
			revalidate();
		}
	}

	private class SimpleFilterInputChange implements DocumentListener { // todo distinguish between simplefilters and groups

		private final SimpleFilter filter;
		private final JTextArea textArea;

		public SimpleFilterInputChange(SimpleFilter simpleFilter, JTextArea textArea) {
			this.filter = simpleFilter;
			this.textArea = textArea;
		}

		@Override
		public void insertUpdate(DocumentEvent documentEvent) { // todo use filtercontroller and only update()/remove()
			update();
		}

		@Override
		public void removeUpdate(DocumentEvent documentEvent) {
			update();
		}

		@Override
		public void changedUpdate(DocumentEvent documentEvent) {
			update();
		}

		private void update() {
			filter.setText(textArea.getText());
			try {
				filterController.updateFilter(textArea.getText(), filter.getID());
				textArea.setBackground(Color.WHITE);
			} catch (InvalidInputException e) {
				textArea.setBackground(theme.lightNeutralColor);
			}
		}
	}

	private class FilterGroupInputChange implements DocumentListener {
		private final FilterGroup filterGroup;
		private final JTextArea textArea;

		public FilterGroupInputChange(FilterGroup filterGroup, JTextArea textArea) {
			this.filterGroup = filterGroup;
			this.textArea = textArea;
		}

		@Override
		public void insertUpdate(DocumentEvent documentEvent) {
			update();
		}

		@Override
		public void removeUpdate(DocumentEvent documentEvent) {
			update();
		}

		@Override
		public void changedUpdate(DocumentEvent documentEvent) {
			update();
		}

		private void update() {
			filterGroup.setText(textArea.getText());
			try {
				filterController.updateFilterGroup(textArea.getText(), filterGroup.getID());
				textArea.setBackground(Color.WHITE);
			} catch (InvalidInputException e) {
				textArea.setBackground(theme.lightNeutralColor);
			}
		}

	}

	private class ManageFilterAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			switch ((String) Objects.requireNonNull(filterDropdown.getSelectedItem())) {
				case "Save selected filter..." : uiFilterManager.exportVisibleFilters(); break; // todo make compatible with different languages (use language resource)
				case "Load filter..." : uiFilterManager.importFilters(); break; // todo make compatible with different languages (use language resource)
			}
		}
	}

	private class SimpleFilterToGroupAction implements ActionListener {

		private final FilterGroup filterGroup;

		public SimpleFilterToGroupAction(FilterGroup filterGroup) {
			this.filterGroup = filterGroup;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			uiFilterManager.addNewSimpleFilterToGroup(filterGroup);
			update();
			repaint();
			revalidate();
		}
	}
}

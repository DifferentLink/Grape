package edu.kit.ipd.dbis.gui.filter;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyFactory;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import javax.swing.BoxLayout;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class FilterSuggestions extends JPopupMenu {

	private final JTextArea inputField;

	public FilterSuggestions(JTextArea inputField) {
		this.inputField = inputField;
	}

	public void drawFilterSuggestions(String input, Component component) {
		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		JScrollPane scrollPane = new JScrollPane(container);
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, Integer.MAX_VALUE));
		this.add(scrollPane);
		this.setMinimumSize(new Dimension(500, 100));
		this.setMaximumSize(new Dimension(500, 200));
		Point position = new Point(component.getLocationOnScreen().x - this.getWidth() + component.getWidth(),
				component.getLocationOnScreen().y - this.getHeight());
		this.setLocation(position);
		this.setVisible(true);
		showSuggestionsFor(input);
	}

	public void showSuggestionsFor(String input) {
		Set<Property> properties = PropertyFactory.createAllProperties(new PropertyGraph());
		List<String> availableFilterExpressions = new LinkedList<>();
		properties.forEach(property -> availableFilterExpressions.add(property.getClass().getSimpleName()));
		this.removeAll();
		revalidate();
		String[] parts = input.split(" ");
		String lastKeyword = parts[parts.length - 1];
		if (!lastKeyword.equals("")) {
			for (String string : availableFilterExpressions) {
				if (string.contains(lastKeyword)) {
					JMenuItem menuItem = new JMenuItem(string);
					menuItem.addActionListener(new ApplySuggestionAction());
					this.add(menuItem);
				}
			}
		} else {
			for (String string : availableFilterExpressions) {
				this.add(string);
			}
		}
	}


	private class ApplySuggestionAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			String[] parts = inputField.getText().split(" ");
			String lastKeyword = parts[parts.length - 1];
			try {
				inputField.setText(inputField.getText(0, lastKeyword.length()));
			} catch (BadLocationException ignored) {}
		}
	}
}

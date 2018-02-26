package edu.kit.ipd.dbis.gui.filter;

import edu.kit.ipd.dbis.gui.listener.HasFocusListener;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

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
		List<String> availableFilterExpressions = new LinkedList<>(); // todo get available filter expressions & relations
		this.removeAll();
		revalidate();
		String[] parts = input.split(" ");
		String lastKeyword = parts[parts.length - 1];

		for (String string : availableFilterExpressions) {
			if (string.contains(lastKeyword)) {
				JMenuItem menuItem = new JMenuItem(string);
				menuItem.addActionListener(new ApplySuggestionAction());
				this.add(menuItem);
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

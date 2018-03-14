package edu.kit.ipd.dbis.gui.correlation;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyFactory;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class CorrelationSuggestions extends JPopupMenu {

	private final JTextField inputField;

	public CorrelationSuggestions(JTextField inputField) {
		this.inputField = inputField;
		updateSuggestions();
	}

	private void updateSuggestions() {
		List<String> relations = new ArrayList<>();
		relations.add("Max");
		relations.add("Min");
		relations.add("Least");
		Collections.sort(relations);

		List<String> correlationFunctions = new ArrayList<>();
		correlationFunctions.add("Pearson");
		correlationFunctions.add("MutualCorrelation");
		Collections.sort(correlationFunctions);

		List<String> properties = new LinkedList<>();
		Set<Property> numberProperties = PropertyFactory.createNumberProperties(new PropertyGraph());
		numberProperties.forEach(property -> properties.add(property.getClass().getSimpleName()));
		Collections.sort(properties);

		this.removeAll();
		revalidate();

		String[] parts = inputField.getText().split(" ");
		String lastKeyword = parts[parts.length - 1];
		List<String> displayedSuggestions;
		if (inputField.getText().matches("\\w*")) {
			displayedSuggestions = relations;
		} else if (inputField.getText().matches("\\w+ \\w*")) {
			displayedSuggestions = correlationFunctions;
		} else if (inputField.getText().matches("\\w+ \\w+ \\w*")) {
			displayedSuggestions = properties;
		} else {
			displayedSuggestions = new LinkedList<>();
		}

		if (!lastKeyword.equals("")) {
			for (String string : displayedSuggestions) {
				if (string.toLowerCase().contains(lastKeyword.toLowerCase())) {
					JMenuItem menuItem = new JMenuItem(string);
					menuItem.addActionListener(new ApplySuggestionAction(string));
					this.add(menuItem);
				}
			}
		} else {
			for (String string : displayedSuggestions) {
				this.add(string);
			}
		}

		this.revalidate();
	}


	private class ApplySuggestionAction implements ActionListener {

		private final String suggestion;

		public ApplySuggestionAction(String suggestion) {
			this.suggestion = suggestion;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			String inputText = inputField.getText();
			String[] parts = inputText.split(" ");
			String lastKeyword = parts[parts.length - 1];
			try {
				String cutText = inputField.getText(0, inputText.length() - lastKeyword.length());
				inputField.setText(cutText + suggestion);
			} catch (BadLocationException ignored) { }
		}
	}
}

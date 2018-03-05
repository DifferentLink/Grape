package edu.kit.ipd.dbis.gui.filter;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyFactory;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class FilterSuggestions extends JPopupMenu {

	private final JTextField inputField;

	public FilterSuggestions(JTextField inputField) {
		this.inputField = inputField;
		updateSuggestions();
	}

	private void updateSuggestions() {
		Set<Property> properties = PropertyFactory.createNumberProperties(new PropertyGraph());
		List<String> availableFilterExpressions = new LinkedList<>();
		properties.forEach(property -> availableFilterExpressions.add(property.getClass().getSimpleName()));
		this.removeAll();
		revalidate();
		String[] parts = inputField.getText().split(" ");
		String lastKeyword = parts[parts.length - 1];
		if (!lastKeyword.equals("")) {
			for (String string : availableFilterExpressions) {
				if (string.toLowerCase().contains(lastKeyword.toLowerCase())) {
					JMenuItem menuItem = new JMenuItem(string);
					menuItem.addActionListener(new ApplySuggestionAction(string));
					this.add(menuItem);
				}
			}
		} else {
			for (String string : availableFilterExpressions) {
				this.add(string);
			}
		}
		Collections.sort(availableFilterExpressions);
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

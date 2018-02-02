/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui.popups;

import edu.kit.ipd.dbis.gui.NonEditableTableModel;
import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

public class CorrelationRequestUI extends JFrame {

	public CorrelationRequestUI(ResourceBundle language, Theme theme) {

		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

		JLabel correlationText = new JLabel("Request: " + "Max Pearson 3"); // todo use language & use real request
		theme.style(correlationText);
		container.add(correlationText);

		String[] defaultColumns = {"ID", "#Vertices", "#Edges"}; // todo dynamically create table
		Object[][] data = {{"001", "5", "6"},
				{"002", "3", "5"},
				{"003", "2", "4"}};

		JTable table = new JTable(new NonEditableTableModel(defaultColumns, data));
		container.add(table);

		JPanel buttonAlignment = new JPanel();
		buttonAlignment.setLayout(new BoxLayout(buttonAlignment, BoxLayout.X_AXIS));
		buttonAlignment.add(Box.createHorizontalGlue());
		JButton addToTable = new JButton("Show");
		addToTable.addActionListener(new AddToTableAction(this));
		theme.style(addToTable);
		buttonAlignment.add(addToTable);

		container.add(buttonAlignment);
		this.add(container);
		this.pack();
		this.setResizable(false);
	}

	private class AddToTableAction implements ActionListener {
		private final CorrelationRequestUI correlationRequestUI;

		public AddToTableAction(CorrelationRequestUI correlationRequestUI) {
			this.correlationRequestUI = correlationRequestUI;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			correlationRequestUI.dispose();
		}
	}
}

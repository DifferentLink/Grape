/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui.popups;

import edu.kit.ipd.dbis.controller.CorrelationController;
import edu.kit.ipd.dbis.correlation.CorrelationOutput;
import edu.kit.ipd.dbis.correlation.exceptions.InvalidCorrelationInputException;
import edu.kit.ipd.dbis.gui.NonEditableTableModel;
import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

public class CorrelationRequestUI extends JFrame {

	private final CorrelationController correlationController;
	private final String correlationRequest;

	public CorrelationRequestUI(CorrelationController correlationController, String correlationRequest, ResourceBundle language, Theme theme) throws InvalidCorrelationInputException {
		this.correlationController = correlationController;
		this.correlationRequest = correlationRequest;

		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

		JLabel correlationText = new JLabel("Request: " + correlationRequest); // todo use language resource
		theme.style(correlationText);
		container.add(correlationText);

		JTable table = populateTable();
		container.add(table);

		JPanel buttonAlignment = new JPanel();
		buttonAlignment.setLayout(new BoxLayout(buttonAlignment, BoxLayout.X_AXIS));
		buttonAlignment.add(Box.createHorizontalGlue());
		JButton addToTable = new JButton("Close"); // todo use language resource
		addToTable.addActionListener(new CloseAction(this));
		theme.style(addToTable);
		buttonAlignment.add(addToTable);

		container.add(buttonAlignment);
		this.add(container);
		this.pack();
		this.setResizable(false);
	}

	private class CloseAction implements ActionListener {
		private final CorrelationRequestUI correlationRequestUI;

		public CloseAction(CorrelationRequestUI correlationRequestUI) {
			this.correlationRequestUI = correlationRequestUI;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			correlationRequestUI.dispose();
		}
	}

	private JTable populateTable() throws InvalidCorrelationInputException {
		List<CorrelationOutput> columns = correlationController.addNewCorrelation(correlationRequest);
		int tableSize = columns.size() + 1;
		String[][] data = new String[3][tableSize];

		data[0][0] = "first property";
		data[1][0] = "second property";
		data[2][0] = "correlation value";
		for (int i = 1; i < tableSize; i++) {
			CorrelationOutput column = columns.get(i - 1);
			data[0][i] = column.getFirstProperty();
			data[1][i] = column.getSecondProperty();
			data[2][i] = String.valueOf(column.getOutputNumber());
		}

		// return new JTable(new NonEditableTableModel(null, data));
		JTable tab = new JTable(data, new String[]{"", "", ""});
		tab.setDefaultEditor(Object.class, null);
		return tab;
	}
}

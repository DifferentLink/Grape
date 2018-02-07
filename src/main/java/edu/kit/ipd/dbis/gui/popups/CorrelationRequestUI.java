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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

		JLabel correlationText = new JLabel("Request: " + correlationRequest); // todo use language
		theme.style(correlationText);
		container.add(correlationText);

		JTable table = populateTable();
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

	private JTable populateTable() throws InvalidCorrelationInputException {
		List<CorrelationOutput> rows = correlationController.addNewCorrelation(correlationRequest);
		for (CorrelationOutput row : rows) {

		}

		return new JTable(new NonEditableTableModel(null, null));
	}
}

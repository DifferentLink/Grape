/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class TableUI extends GUIElement {

	private JTable table;

	private String[] defaultColumns = {"ID", "#Vertices", "#Edges"};
	private Object[][] data = {{"001", "5", "6"},
			{"002", "3", "5"},
			{"003", "2", "4"}};

	public TableUI(Controller controller, ResourceBundle language, Theme theme, Dimension size) {
		super(controller, language, theme);

		table = new JTable(new NonEditableTableModel(defaultColumns, data));
		JScrollPane tableScrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		table.setSelectionBackground(theme.tableSelectionColor);

		tableScrollPane.setBackground(theme.backgroundColor);
		tableScrollPane.setForeground(theme.foregroundColor);
		tableScrollPane.setFont(theme.defaultFont);
		tableScrollPane.setViewportView(table);
		//tableScrollPane.setPreferredSize(size);

		this.add(tableScrollPane);
	}

	/**
	 * Updates the GUIWindow element.
	 */
	@Override
	public void update() {

	}
}

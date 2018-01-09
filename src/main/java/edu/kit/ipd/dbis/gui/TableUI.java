/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.util.ResourceBundle;

public class TableUI extends GUIElement {

	private JTable table;

	private String[] defaultColumns = {"ID", "#Vertices", "#Edges"};
	private Object[][] data = {{"001", "5", "6"},
			{"002", "3", "5"},
			{"003", "2", "4"}};

	public TableUI(ResourceBundle language, Theme theme) {
		super(language, theme);

		table = new JTable(data, defaultColumns);
		JScrollPane tableScrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);

		tableScrollPane.setBackground(theme.backgroundColor);
		tableScrollPane.setForeground(theme.foregroundColor);
		tableScrollPane.setFont(theme.defaultFont);

		this.add(tableScrollPane);
	}

	/**
	 * Updates the GUIWindow element.
	 */
	@Override
	public void update() {

	}
}

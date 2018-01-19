/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui;

import javax.swing.table.AbstractTableModel;

public class NonEditableTableModel extends AbstractTableModel {

	private String[] columnNames;
	private Object[][] data;

	public NonEditableTableModel(String[] columnNames, Object[][] data) {
		this.columnNames = columnNames;
		this.data = data;
	}

	@Override
	public int getRowCount() {
		return data.length;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int i, int i1) {
		return data[i][i1];
	}

	@Override
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}
}

package edu.kit.ipd.dbis.gui;

import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * An extension of the default table model with non-editable cells.
 */
public class NonEditableTableModel extends DefaultTableModel {

	/**
	 * @param columnNames the names of the columns
	 * @param data the table
	 */
	public NonEditableTableModel(String[] columnNames, Object[][] data) {
		super(data, columnNames);
	}

	/**
	 * Parses a ResultSet from the database to create the table for the GUI
	 * @param resultSet the ResultSet
	 * @throws SQLException thrown if a database exception occurs
	 */
	void update(ResultSet resultSet) throws SQLException {
		this.setColumnCount(0);
		this.setRowCount(0);

		if (resultSet != null) {
			ResultSetMetaData metaData = resultSet.getMetaData();
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				this.addColumn(metaData.getColumnName(i));
			}

			while (resultSet.next()) {
				Object[] row = new Object[metaData.getColumnCount()];
				for (int i = 1; i <= metaData.getColumnCount(); i++) {
					row[i - 1] = resultSet.getObject(i);
				}
				this.addRow(row);
			}
		}

		this.fireTableDataChanged();
		this.fireTableStructureChanged();
	}

	/**
	 * Cells aren't editable
	 * @param row the row
	 * @param column the column
	 * @return false, since no cell is editable
	 */
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
}

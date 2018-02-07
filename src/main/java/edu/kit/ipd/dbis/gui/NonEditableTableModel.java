/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui;

import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class NonEditableTableModel extends DefaultTableModel {

	public NonEditableTableModel(String[] columnNames, Object[][] data) {
		super(data, columnNames);
	}

	public void update(ResultSet resultSet) throws SQLException {
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

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
}

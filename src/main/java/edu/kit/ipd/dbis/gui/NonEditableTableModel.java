/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui;

import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

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
				Vector<Object> row = new Vector<>();
				for (int i = 1; i <= metaData.getColumnCount(); i++) {
					row.add(resultSet.getObject(i));
				}
				this.addRow(row);
			}
		}

		this.fireTableDataChanged();
		this.fireTableStructureChanged();
	}

	@Override
	public int getRowCount() {
		return super.getRowCount();
	}

	@Override
	public int getColumnCount() {
		return super.getColumnCount();
	}

	@Override
	public Object getValueAt(int i, int i1) {
		return super.getValueAt(i, i1);
	}

	@Override
	public Class getColumnClass(int column) {
		return super.getColumnClass(column);
	}
}

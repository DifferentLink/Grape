/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyFactory;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import javax.swing.table.DefaultTableModel;
import java.util.*;

public class NonEditableTableModel extends DefaultTableModel {

	private String[] columnNames;
	private Object[][] data;

	private List<Property> allProperties = new LinkedList<>();
	private LinkedList<Property> visibleColumns = new LinkedList<>();

	public NonEditableTableModel(String[] columnNames, Object[][] data) {
		super(data, columnNames);
		this.columnNames = columnNames;
		this.data = data;
	}

	public void update(Collection<PropertyGraph<Integer, Integer>> graphs) {
		this.setColumnCount(0);
		this.setRowCount(0);
		allProperties = new LinkedList<>();
		if (graphs.iterator().hasNext()) {
			Collection<Property> properties = PropertyFactory.createAllProperties(new PropertyGraph());
			allProperties.addAll(properties);
		}
		columnNames = new String[allProperties.size()];
		for (int i = 0; i < allProperties.size(); i++) {
			String name = allProperties.get(i).getClass().getSimpleName();
			columnNames[i] = name;
			this.addColumn(name);
		}
		graphs.forEach(propertyGraph -> this.addRow(graphToTableRow(propertyGraph)));
		this.fireTableDataChanged();
		this.fireTableStructureChanged();
	}

	private Object[] graphToTableRow(PropertyGraph<Integer, Integer> graph) {
		LinkedList<Object> row = new LinkedList<>();
		graph.getProperties().forEach(property -> row.add(property.getValue()));
		return row.toArray();
	}

	private void validateVisibleColumns() {
		visibleColumns.forEach(name -> {
			if (!allProperties.contains(name)) {
				visibleColumns.remove(name); // todo check if this works
			}
		});
	}

	private Set<Property> getHiddenProperties() {
		Set<Property> hiddenProperties = new TreeSet<>();
		allProperties.forEach(property -> {
			if (!visibleColumns.contains(property)) {
				hiddenProperties.add(property);
			}
		});
		return hiddenProperties;
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

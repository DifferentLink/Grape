package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.gui.themes.Theme;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.Component;

/**
 * A JTable with rows that alternate in color
 */
public class AlternateTable extends JTable {

	private final Theme theme;

	/**
	 * @param tableModel the JTable's TableModel
	 * @param theme the theme to style the table
	 */
	public AlternateTable(TableModel tableModel, Theme theme) {
		super(tableModel);
		this.theme = theme;
	}

	/**
	 * Gives the rows alternating colors
	 * @param tableCellRenderer the renderer used to render a cell
	 * @param row the row
	 * @param column the column
	 * @return the rendered column
	 */
	@Override
	public Component prepareRenderer(TableCellRenderer tableCellRenderer, int row, int column) {
		Component component = super.prepareRenderer(tableCellRenderer, row, column);
		if (row % 2 == 0 && !isCellSelected(row, column)) {
			component.setBackground(theme.lightNeutralColor);
		} else if (!isCellSelected(row, column)) {
			component.setBackground(theme.backgroundColor);
		} else if (row % 2 == 0) {
			component.setBackground(theme.tableSelectionColorDark);
		} else {
			component.setBackground(theme.tableSelectionColor);
		}
		return component;
	}
}

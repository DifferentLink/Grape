/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;

public class AlternateTable extends JTable {

	private final Theme theme;

	public AlternateTable(TableModel tableModel, Theme theme) {
		super(tableModel);
		this.theme = theme;
	}

	@Override
	public Component prepareRenderer(TableCellRenderer tableCellRenderer, int row, int column) {
		Component component = super.prepareRenderer(tableCellRenderer, row, column);
		if (row % 2 == 0) {
			component.setBackground(theme.lightNeutralColor);
		} else {
			component.setBackground(theme.backgroundColor);
		}
		return component;
	}
}

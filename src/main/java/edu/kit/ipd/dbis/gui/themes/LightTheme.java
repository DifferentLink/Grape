/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.themes;

import java.awt.*;

public class LightTheme extends Theme {

	public LightTheme() {
		super.fontColor = Color.BLACK;
		super.defaultFontSize = 12;
		super.defaultFont = new Font("Courier New", Font.PLAIN, defaultFontSize);
		super.backgroundColor = Color.WHITE;
		super.foregroundColor = Color.BLACK;
		super.shadowColor = super.backgroundColor;
		super.dividerSize = 2;
		super.assertiveBackground = Color.GREEN;
		super.tableSelectionColor = Color.YELLOW;
	}
}

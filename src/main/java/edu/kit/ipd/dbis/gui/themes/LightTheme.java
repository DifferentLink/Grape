/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.themes;

import java.awt.*;

public class LightTheme extends Theme {

	public LightTheme() { // todo choose proper colors for GUI
		super.fontColor = Color.BLACK;
		super.defaultFontSize = 12;
		super.defaultFont = new Font("Courier New", Font.PLAIN, defaultFontSize);
		super.backgroundColor = Color.WHITE;
		super.foregroundColor = Color.BLACK;
		super.shadowColor = super.backgroundColor;
		super.dividerSize = 2;
		super.assertiveBackground = Color.GREEN;
		super.tableSelectionColor = Color.YELLOW;
		super.buttonBackgorundColor = Color.GRAY;
		super.buttonHighlightColor = Color.GREEN;
		super.buttonDisabledColor = Color.GRAY;
		super.disabledTextColor = Color.DARK_GRAY;
	}
}

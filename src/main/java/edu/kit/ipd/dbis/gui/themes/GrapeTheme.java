/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui.themes;

import java.awt.*;

public class GrapeTheme extends Theme {
	public GrapeTheme() { // todo choose proper colors for GUI
		super.fontColor = Color.BLACK;
		super.defaultFontSize = 14;
		super.defaultFont = new Font("Open Sans", Font.PLAIN, defaultFontSize);
		super.smallFont = new Font("Open Sans", Font.PLAIN, defaultFontSize - 1);
		super.backgroundColor = new Color(0xF0F0F0);
		super.foregroundColor = Color.BLACK;
		super.shadowColor = super.backgroundColor;
		super.dividerSize = 2;
		super.outlineColor = Color.DARK_GRAY;
		super.outlineThickness = 1;
		super.assertiveBackground = new Color(0xC4C8F5);
		super.unassertiveBackground = new Color(0xF4C9BA);
		super.tableSelectionColor = Color.YELLOW;
		super.buttonTextColor = Color.BLACK;
		super.buttonBackgorundColor = Color.WHITE;
		super.buttonHighlightColor = Color.GREEN;
		super.buttonDisabledColor = Color.GRAY;
		super.disabledTextColor = Color.DARK_GRAY;
		super.neutralColor = Color.LIGHT_GRAY;
		super.lightNeutralColor = new Color(222, 222, 222);
	}
}

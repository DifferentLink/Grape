/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.themes;

import javax.swing.*;
import java.awt.*;

public class LightTheme extends Theme {

	public LightTheme() { // todo choose proper colors for GUI
		super.fontColor = Color.BLACK;
		super.defaultFontSize = 12;
		super.defaultFont = new Font("Open Sans", Font.PLAIN, defaultFontSize);
		super.backgroundColor = Color.WHITE;
		super.foregroundColor = Color.BLACK;
		super.shadowColor = super.backgroundColor;
		super.dividerSize = 2;
		super.outlineColor = Color.BLACK;
		super.outlineThickness = 1;
		super.assertiveBackground = Color.GREEN;
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

/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.util.ResourceBundle;

public abstract class GUIMenu extends JMenuBar {
	protected ResourceBundle language;
	protected Theme theme;

	public GUIMenu(ResourceBundle language, Theme theme) {
		this.language = language;
		this.theme = theme;
	}
}

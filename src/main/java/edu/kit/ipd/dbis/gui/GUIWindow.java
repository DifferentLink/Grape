/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.util.ResourceBundle;

public abstract class GUIWindow extends JFrame {
	protected Controller controller;
	protected ResourceBundle language;
	protected Theme theme;

	/**
	 * Updates the GUIWindow element.
	 */
	public abstract void update();

	public GUIWindow(Controller controller, ResourceBundle language, Theme theme) {
		this.controller = controller;
		this.language = language;
		this.theme = theme;
	}
}

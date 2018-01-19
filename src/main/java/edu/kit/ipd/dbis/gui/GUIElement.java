/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.util.ResourceBundle;

public abstract class GUIElement extends JPanel {
	protected Controller controller;
	protected Theme theme;
	protected ResourceBundle language;

	public GUIElement(Controller controller, ResourceBundle language, Theme theme) {
		this.controller = controller;
		this.language = language;
		this.theme = theme;
	}

	/**
	 * Updates the GUIWindow element.
	 */
	public abstract void update();

}

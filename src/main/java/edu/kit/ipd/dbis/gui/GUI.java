/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.gui.themes.Theme;

import java.util.ResourceBundle;

public abstract class GUI {
	// protected Controller controller; todo
	protected Theme theme;
	protected ResourceBundle language;

	/**
	 * Updates the GUI element.
	 */
	public abstract void update();
}

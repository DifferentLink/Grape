/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.util.ResourceBundle;

public abstract class GUIWindow extends JFrame {
	// protected Controller controller; todo
	protected Theme theme;
	protected ResourceBundle language;

	/**
	 * Updates the GUIWindow element.
	 */
	public abstract void update();
}

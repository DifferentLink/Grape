/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.gui.themes.Theme;

import java.awt.*;
import java.util.ResourceBundle;

public class FilterUI extends GUIElement {

	public FilterUI(Controller controller, ResourceBundle language, Theme theme) {
		super(controller, language, theme);

		this.add(new Label("Filter UI"));
	}

	public GUIElement makeFilterUI() {
		return null;
	}

	/**
	 * Updates the GUIWindow element.
	 */
	@Override
	public void update() {

	}
}

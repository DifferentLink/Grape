/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.grapheditor;

import edu.kit.ipd.dbis.gui.Controller;
import edu.kit.ipd.dbis.gui.GUIElement;
import edu.kit.ipd.dbis.gui.GUIWindow;
import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.util.ResourceBundle;

public class GraphEditorUI extends GUIElement {

	RenderableGraph graph;
	GraphEditorHistory history;

	public GraphEditorUI(Controller controller, ResourceBundle language, Theme theme) {
		super(controller, language, theme);
	}

	public GUIElement makeGraphEditorUI() {
		return null;
	}

	/**
	 * Updates the GUIWindow element.
	 */
	@Override
	public void update() {

	}


}

/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.grapheditor;

import edu.kit.ipd.dbis.gui.GUIElement;
import edu.kit.ipd.dbis.gui.GUIWindow;
import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.util.ResourceBundle;

public class GraphEditorUI extends GUIElement {

	public GraphEditorUI(ResourceBundle language, Theme theme) {
		super(language, theme);
	}

	public GUIElement makeGraphEditorUI() {
		GUIElement graphEditorUI = new GraphEditorUI(language, theme);
		graphEditorUI.add(new JLabel("GraphEditorUI"));
		graphEditorUI.setBackground(theme.backgroundColor);
		graphEditorUI.setForeground(theme.foregroundColor);
		return graphEditorUI;
	}

	/**
	 * Updates the GUIWindow element.
	 */
	@Override
	public void update() {

	}
}

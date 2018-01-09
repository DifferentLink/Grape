/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.grapheditor;

import edu.kit.ipd.dbis.gui.GUI;
import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.util.ResourceBundle;

public class GraphEditorUI extends GUI {
	public static JPanel makeGraphEditorUI(ResourceBundle language, Theme theme) {
		JPanel graphEditorUI = new JPanel();
		graphEditorUI.add(new JLabel("GraphEditorUI"));
		graphEditorUI.setBackground(theme.backgroundColor);
		graphEditorUI.setForeground(theme.foregroundColor);
		return graphEditorUI;
	}

	/**
	 * Updates the GUI element.
	 */
	@Override
	public void update() {

	}
}

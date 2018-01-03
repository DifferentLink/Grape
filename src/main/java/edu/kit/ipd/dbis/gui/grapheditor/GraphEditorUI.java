/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.grapheditor;

import edu.kit.ipd.dbis.gui.theme.Theme;

import javax.swing.*;
import java.util.ResourceBundle;

public class GraphEditorUI {
	public static JPanel makeGraphEditorUI(ResourceBundle language, Theme theme) {
		JPanel graphEditorUI = new JPanel();
		graphEditorUI.add(new JLabel("GraphEditorUI"));
		graphEditorUI.setBackground(theme.backgroundColor);
		graphEditorUI.setForeground(theme.foregroundColor);
		return graphEditorUI;
	}
}

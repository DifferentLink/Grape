/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.grapheditor;

import javax.swing.*;
import java.util.ResourceBundle;

public class GraphEditorUI {
	public static JPanel makeGraphEditorUI(ResourceBundle language) {
		JPanel graphEditorUI = new JPanel();
		graphEditorUI.add(new JLabel("GraphEditorUI"));
		return graphEditorUI;
	}
}

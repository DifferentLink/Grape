/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.grapheditor;

import edu.kit.ipd.dbis.gui.Controller;
import edu.kit.ipd.dbis.gui.GUIElement;
import edu.kit.ipd.dbis.gui.GUIWindow;
import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;

public class GraphEditorUI extends GUIElement {

	RenderableGraph graph;
	GraphEditorHistory history;

	public GraphEditorUI(Controller controller, ResourceBundle language, Theme theme) {
		super(controller, language, theme);

		this.add(new Editor(), BorderLayout.CENTER);
	}

	/**
	 * Updates the GUIWindow element.
	 */
	@Override
	public void update() {

	}

	private class Editor extends JComponent {

		public Editor() {

			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent mouseEvent) {
				}

				@Override
				public void mouseReleased(MouseEvent mouseEvent) {
				}
			});

			this.addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseDragged(MouseEvent mouseEvent) {
				}
			});
		}

		@SuppressWarnings("Duplicates")
		public void paint(Graphics g) {

			Graphics2D kanvas = (Graphics2D) g;

			kanvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
	}
}

/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.grapheditor;

import edu.kit.ipd.dbis.gui.Controller;
import edu.kit.ipd.dbis.gui.GUIElement;
import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ResourceBundle;

public class GraphEditorUI extends GUIElement {

	private RenderableGraph graph;
	private GraphEditorHistory history;
	private Editor graphEditor;
	private JButton undo;
	private JButton redo;
	private JButton denser;
	private JButton switchColor;
	private JButton center;
	private JButton preview;
	private JButton apply;
	private JLabel graphInfo;
	private JComboBox<String> coloringType;

	private int barHeight = 25;
	private Dimension buttonHeight = new Dimension(barHeight - 2, barHeight - 2);
	private int buttonSeparation = 2;

	public GraphEditorUI(Controller controller, ResourceBundle language, Theme theme) {
		super(controller, language, theme);

		this.setLayout(new BorderLayout());

		// todo get text from language resource
		JPanel topBarButtons = new JPanel();
		topBarButtons.setLayout(new BoxLayout(topBarButtons, BoxLayout.X_AXIS));
		topBarButtons.setPreferredSize(new Dimension(Integer.MAX_VALUE, barHeight));
		theme.style(topBarButtons);
		undo = new JButton("U");
		theme.style(undo);
		undo.setPreferredSize(buttonHeight);
		redo = new JButton("R");
		theme.style(redo);
		redo.setPreferredSize(buttonHeight);
		denser = new JButton("D+");
		theme.style(denser);
		denser.setPreferredSize(buttonHeight);
		switchColor = new JButton("<>");
		theme.style(switchColor);
		switchColor.setPreferredSize(buttonHeight);
		String[] availableColorings = {"Total Coloring", "Vertex Coloring"};
		coloringType = new JComboBox<>();
		coloringType.addItem(availableColorings[0]);
		coloringType.addItem(availableColorings[1]);
		theme.style(coloringType);

		topBarButtons.add(undo);
		topBarButtons.add(redo);
		topBarButtons.add(denser);
		topBarButtons.add(switchColor);
		topBarButtons.add(coloringType);

		JPanel bottomBarButtons = new JPanel();
		bottomBarButtons.setLayout(new BoxLayout(bottomBarButtons, BoxLayout.X_AXIS));
		bottomBarButtons.setPreferredSize(new Dimension(Integer.MAX_VALUE, barHeight));
		theme.style(bottomBarButtons);
		center = new JButton("Center");
		theme.style(center);
		preview = new JButton("Preview");
		theme.style(preview);
		apply = new JButton("Apply");
		theme.style(apply);
		bottomBarButtons.add(Box.createHorizontalStrut(buttonSeparation));
		bottomBarButtons.add(center);
		bottomBarButtons.add(Box.createHorizontalGlue());
		bottomBarButtons.add(preview);
		bottomBarButtons.add(Box.createHorizontalStrut(buttonSeparation));
		bottomBarButtons.add(apply);
		bottomBarButtons.add(Box.createHorizontalStrut(buttonSeparation));

		graphEditor = new Editor();

		this.add(topBarButtons, BorderLayout.NORTH);
		this.add(graphEditor, BorderLayout.CENTER); // todo use GridBagLayout Manager for to properly display graph information
		this.add(bottomBarButtons, BorderLayout.SOUTH);
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

		public void paint(Graphics g) {

			Graphics2D kanvas = (Graphics2D) g;
			kanvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			kanvas.setBackground(theme.backgroundColor);
		}
	}
}

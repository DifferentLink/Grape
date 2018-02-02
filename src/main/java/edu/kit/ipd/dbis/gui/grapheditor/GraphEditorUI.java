/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.grapheditor;

import edu.kit.ipd.dbis.controller.GraphEditorController;
import edu.kit.ipd.dbis.controller.InvalidGraphInputException;
import edu.kit.ipd.dbis.gui.themes.Theme;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ResourceBundle;

public class GraphEditorUI extends JPanel {

	private RenderableGraph graph = new RenderableGraph();
	private GraphEditorHistory history = new GraphEditorHistory();
	private Editor graphEditor;
	private JButton undo;
	private JButton redo;
	private JButton denser;
	private JButton switchColor;
	private JButton center;
	private JButton preview;
	private JButton apply;
	private JLabel graphInfo; // todo add to GUI
	private JComboBox<String> coloringType;

	private Theme theme;
	private int barHeight = 25;
	private Dimension buttonSize = new Dimension(barHeight - 2, barHeight - 2);
	private int buttonSeparation = 2;

	public GraphEditorUI(GraphEditorController graphEditorController, ResourceBundle language, Theme theme) {

		this.theme = theme;
		this.setLayout(new BorderLayout());

		// todo get text from language resource
		JPanel topBarButtons = new JPanel();
		topBarButtons.setLayout(new BoxLayout(topBarButtons, BoxLayout.X_AXIS));
		topBarButtons.setPreferredSize(new Dimension(Integer.MAX_VALUE, barHeight));
		theme.style(topBarButtons);
		topBarButtons.setBorder(null);
		undo = new JButton("U"); // todo replace with icon
		undo.addActionListener(new UndoAction());
		theme.style(undo);
		undo.setMinimumSize(buttonSize);
		undo.setPreferredSize(buttonSize);
		undo.setMaximumSize(buttonSize);
		redo = new JButton("R"); // todo replace with icon
		redo.addActionListener(new RedoAction());
		theme.style(redo);
		redo.setMinimumSize(buttonSize);
		redo.setPreferredSize(buttonSize);
		redo.setMaximumSize(buttonSize);
		denser = new JButton("D+"); // todo replace with icon
		denser.addActionListener(new NextDenserGraphAction(graphEditorController));
		theme.style(denser);
		denser.setMinimumSize(buttonSize);
		denser.setPreferredSize(buttonSize);
		denser.setMaximumSize(buttonSize);
		switchColor = new JButton("<>"); // todo replace with icon
		switchColor.addActionListener(new SwitchColorAction(graphEditorController));
		theme.style(switchColor);
		switchColor.setMinimumSize(buttonSize);
		switchColor.setPreferredSize(buttonSize);
		switchColor.setMaximumSize(buttonSize);
		String[] availableColorings = {"Total Coloring", "Vertex Coloring"}; // todo replace with language resource
		coloringType = new JComboBox<>();
		coloringType.addItem(availableColorings[0]);
		coloringType.addItem(availableColorings[1]);
		theme.style(coloringType);

		topBarButtons.add(Box.createHorizontalStrut(buttonSeparation));
		topBarButtons.add(undo);
		topBarButtons.add(Box.createHorizontalStrut(buttonSeparation));
		topBarButtons.add(redo);
		topBarButtons.add(Box.createHorizontalGlue());
		topBarButtons.add(denser);
		topBarButtons.add(Box.createHorizontalStrut(buttonSeparation));
		topBarButtons.add(switchColor);
		topBarButtons.add(Box.createHorizontalStrut(buttonSeparation));
		topBarButtons.add(coloringType);

		JPanel bottomBarButtons = new JPanel();
		bottomBarButtons.setLayout(new BoxLayout(bottomBarButtons, BoxLayout.X_AXIS));
		bottomBarButtons.setPreferredSize(new Dimension(Integer.MAX_VALUE, barHeight));
		theme.style(bottomBarButtons);
		bottomBarButtons.setBorder(null);
		center = new JButton("Center"); // todo replace with language resource
		theme.style(center);
		center.addActionListener(new CenterVerticesAction());
		preview = new JButton("Preview");// todo replace with language resource
		preview.addActionListener(new PreviewAction(graphEditorController));
		theme.style(preview);
		apply = new JButton("Apply");// todo replace with language resource
		apply.addActionListener(new ApplyAction(graphEditorController));

		theme.style(apply);
		bottomBarButtons.add(Box.createHorizontalStrut(buttonSeparation));
		bottomBarButtons.add(center);
		bottomBarButtons.add(Box.createHorizontalGlue());
		bottomBarButtons.add(preview);
		bottomBarButtons.add(Box.createHorizontalStrut(buttonSeparation));
		bottomBarButtons.add(apply);
		bottomBarButtons.add(Box.createHorizontalStrut(buttonSeparation));

		graphEditor = new Editor();
		graphEditor.setBackground(theme.backgroundColor);
		graphEditor.setForeground(theme.foregroundColor);

		this.add(topBarButtons, BorderLayout.NORTH);
		this.add(graphEditor, BorderLayout.CENTER); // todo use GridBagLayout Manager for to properly display graph information
		this.add(bottomBarButtons, BorderLayout.SOUTH);
	}

	public void displayGraph(PropertyGraph<Integer, Integer> graph) {
		this.graph = new RenderableGraph(graph);
		history = new GraphEditorHistory();
	}

	private class Editor extends JComponent {

		private Point mStart;
		private Point mTarget;
		private int currentMouseButtonKey = MouseEvent.BUTTON1;

		private Editor() {

			history.addToHistory(graph);

			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent mouseEvent) {
					mStart = new Point(mouseEvent.getX(), mouseEvent.getY());
					mTarget = mStart;
					currentMouseButtonKey = mouseEvent.getButton();
				}

				@Override
				public void mouseReleased(MouseEvent mouseEvent) {
					mTarget = mouseEvent.getPoint();

					if (mouseEvent.getButton() == MouseEvent.BUTTON1) { // Released left mouse button
						Vertex vertex = graph.getVertexAt(mouseEvent.getPoint());
						if (vertex != null) {
							Vertex start = graph.getVertexAt(mStart);
							if (start == null) {
								start = new Vertex(mStart);
							}

							Vertex target = graph.getVertexAt(mTarget);
							if (target == null) {
								target = new Vertex(mTarget);
							}
							graph = graph.deepCopy();
							graph.add(new Edge(start, target));
							history.addToHistory(graph);
						} else {
							graph = graph.deepCopy();
							graph.add(new Vertex(mTarget));
							history.addToHistory(graph);
						}
					} else if (mouseEvent.getButton() == MouseEvent.BUTTON3) { // Released right mouse button
						graph.remove(mTarget);
					}
					repaint();
				}
			});

			this.addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseDragged(MouseEvent mouseEvent) {
					if (currentMouseButtonKey == MouseEvent.BUTTON2) {
						mTarget = mouseEvent.getPoint();
						graph.move(new Point(mTarget.x - mStart.x, mTarget.y - mStart.y));
						repaint();
						mStart = mouseEvent.getPoint();
					}
				}
			});
		}

		public void paint(Graphics g) {

			Graphics2D kanvas = (Graphics2D) g;
			kanvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			kanvas.setPaint(theme.backgroundColor);
			kanvas.fill(new Rectangle(0, 0, this.getWidth(), this.getHeight()));

			graph.getEdges().forEach(edge -> {
				Shape currentEdgeShape = edge.draw();
				kanvas.setPaint(edge.getColor());
				kanvas.fill(currentEdgeShape);
				kanvas.setStroke(new BasicStroke(edge.getThickness()));
				kanvas.setPaint(edge.getColor());
				kanvas.draw(currentEdgeShape);
			});

			graph.getVertices().forEach(vertex -> {
				Shape vertexShape = vertex.draw();
				kanvas.setPaint(vertex.getFillColor());
				kanvas.fill(vertexShape);
				kanvas.setStroke(new BasicStroke(vertex.getOutlineThickness()));
				kanvas.setPaint(vertex.getOutlineColor());
				kanvas.draw(vertexShape);
			});

			graph.getSubgraphs().forEach(subgraph -> {
				Shape subgraphOutline = subgraph.outline();
				kanvas.setPaint(theme.outlineColor);
				kanvas.fill(subgraphOutline);
				kanvas.draw(subgraphOutline);
			});
		}
	}

	private class UndoAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			RenderableGraph previousGraph = history.moveBack();

			if (previousGraph != null) {
				graph = previousGraph;
				repaint();
			}
		}
	}

	private class RedoAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			RenderableGraph nextGraph = history.moveForward();

			if (nextGraph != null) {
				graph = nextGraph;
				repaint();
			}
		}
	}

	private class PreviewAction implements ActionListener {

		private final GraphEditorController graphEditorController;

		private PreviewAction(GraphEditorController graphEditorController) {
			this.graphEditorController = graphEditorController;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {

			if (coloringType.getSelectedItem().toString().equals("Total Coloring")) { // todo make this work with different languages
				graphEditorController.getTotalColoring(graph.asPropertyGraph()); // todo this simply returns a coloring. Use this color in the RenderableGraph
				history.addToHistory(graph);
			} else if (coloringType.getSelectedItem().toString().equals("Vertex Coloring")) { // todo make this work with different languages
				graphEditorController.getVertexColoring(graph.asPropertyGraph()); // todo this simply returns a coloring. Use this color in the RenderableGraph
				history.addToHistory(graph);
			}
		}
	}

	private class ApplyAction implements ActionListener {

		private final GraphEditorController graphEditorController;

		private ApplyAction(GraphEditorController graphEditorController) {
			this.graphEditorController = graphEditorController;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			graph.makeConnected();
			PropertyGraph propertyGraph = graph.asPropertyGraph();
			if (graphEditorController.isValidGraph(propertyGraph)) {
				graphEditorController.addEditedGraph(propertyGraph, graph.getId());
				graph = new RenderableGraph();
				history = new GraphEditorHistory();
			} else {
				System.out.println("Invalid graph");
			}
		}
	}

	private class NextDenserGraphAction implements ActionListener {

		private final GraphEditorController graphEditorController;

		private NextDenserGraphAction(GraphEditorController graphEditorController) {
			this.graphEditorController = graphEditorController;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			graphEditorController.addNextDenserToDatabase(graph.asPropertyGraph());
		}
	}

	private class SwitchColorAction implements ActionListener {

		private final GraphEditorController graphEditorController;

		private SwitchColorAction(GraphEditorController graphEditorController) {
			this.graphEditorController = graphEditorController;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) { // todo implement methods to apply a coloring to the visible graph
			graphEditorController.getAlternateTotalColoring(graph.getId()); // todo use correct coloring and check if graph hasn't been edited yet
		}
	}

	private class CenterVerticesAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			GraphLook.arrangeInCircle(graph.getVertices(), new Point(0, 0), new Point(getWidth(), getHeight()));
			repaint();
		}
	}
}

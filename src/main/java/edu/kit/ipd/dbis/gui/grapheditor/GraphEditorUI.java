package edu.kit.ipd.dbis.gui.grapheditor;

import edu.kit.ipd.dbis.controller.GraphEditorController;
import edu.kit.ipd.dbis.controller.exceptions.InvalidGraphInputException;
import edu.kit.ipd.dbis.gui.themes.Theme;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.TotalColoringAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ResourceBundle;

/**
 * The graph editor in the GUI
 */
public class GraphEditorUI extends JPanel {

	private RenderableGraph graph = new RenderableGraph();
	private PropertyGraph<Integer, Integer> propertyGraph;
	private VertexFactory factory = new VertexFactory();

	private GraphEditorHistory history = new GraphEditorHistory();
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

	private static ColoringType currentColoringType;
	VertexColoringAlgorithm.Coloring<Integer> currentVertexColoring;
	TotalColoringAlgorithm.TotalColoring<Integer, Integer> currentTotalColoring;

	private enum ColoringType {
		VERTEX {
			@Override
			public String toString() {
				return "Vertex Coloring";
			}
		},
		TOTAL {
			@Override
			public String toString() {
				return "Total Coloring";
			}
		};

		public abstract String toString();
	}

	private Theme theme;
	private int barHeight = 25;
	private Dimension buttonSize = new Dimension(barHeight - 2, barHeight - 2);
	private int buttonSeparation = 2;

	/**
	 * Creates a graph editor to add to the GUI
	 *
	 * @param graphEditorController the responsible controller
	 * @param language              the language used
	 * @param theme                 the theme used to style the GUI
	 */
	public GraphEditorUI(GraphEditorController graphEditorController, ResourceBundle language, Theme theme) {
		currentColoringType = ColoringType.VERTEX;
		this.theme = theme;
		this.setLayout(new BorderLayout());

		JPanel topBarButtons = new JPanel();
		topBarButtons.setLayout(new BoxLayout(topBarButtons, BoxLayout.X_AXIS));
		topBarButtons.setPreferredSize(new Dimension(Integer.MAX_VALUE, barHeight));
		theme.style(topBarButtons);
		topBarButtons.setBorder(null);
		undo = new JButton("U");
		undo.addActionListener(new UndoAction());
		theme.style(undo);
		undo.setMinimumSize(buttonSize);
		undo.setPreferredSize(buttonSize);
		undo.setMaximumSize(buttonSize);
		redo = new JButton("R");
		redo.addActionListener(new RedoAction());
		theme.style(redo);
		redo.setMinimumSize(buttonSize);
		redo.setPreferredSize(buttonSize);
		redo.setMaximumSize(buttonSize);
		denser = new JButton("D+");
		denser.addActionListener(new NextDenserGraphAction(graphEditorController));
		theme.style(denser);
		denser.setMinimumSize(buttonSize);
		denser.setPreferredSize(buttonSize);
		denser.setMaximumSize(buttonSize);
		switchColor = new JButton("<>");
		switchColor.addActionListener(new SwitchColorAction(graphEditorController));
		theme.style(switchColor);
		switchColor.setMinimumSize(buttonSize);
		switchColor.setPreferredSize(buttonSize);
		switchColor.setMaximumSize(buttonSize);

		coloringType = new JComboBox<>();
		coloringType.addItem(ColoringType.VERTEX.toString());
		coloringType.addItem(ColoringType.TOTAL.toString());
		theme.style(coloringType);
		coloringType.addItemListener(new SwitchColoringTypeAction(graphEditorController));

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
		center = new JButton(language.getString("center"));
		theme.style(center);
		center.addActionListener(new CenterVerticesAction());
		preview = new JButton(language.getString("preview"));
		preview.addActionListener(new PreviewAction(graphEditorController));
		theme.style(preview);
		apply = new JButton(language.getString("apply"));
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
		this.add(graphEditor, BorderLayout.CENTER);
		this.add(bottomBarButtons, BorderLayout.SOUTH);
	}

	/**
	 * Displays the given graph in the editor using the selected coloring type
	 *
	 * @param graph the graph to display
	 */
	public void displayGraph(PropertyGraph<Integer, Integer> graph) {
		propertyGraph = graph;
		if (currentColoringType == ColoringType.VERTEX) {
			currentVertexColoring = GraphEditorController.getVertexColoring(graph);
			this.graph = new RenderableGraph(graph, currentVertexColoring, this.factory);
		} else {
			currentTotalColoring = GraphEditorController.getTotalColoring(graph);
			this.graph = new RenderableGraph(graph, currentTotalColoring, this.factory);
		}
		this.history = new GraphEditorHistory();
		this.history.addToHistory(this.graph);
		arrangeGraph();
		graphEditor.repaint();
	}

	/**
	 * Displays the given graph in the editor with colored vertices
	 *
	 * @param graph    the graph to display
	 * @param coloring the vertex coloring to display
	 */
	public void displayGraph(PropertyGraph<Integer, Integer> graph,
							 VertexColoringAlgorithm.Coloring<Integer> coloring) {
		propertyGraph = graph;
		this.graph = new RenderableGraph(graph, coloring, this.factory);
		this.history = new GraphEditorHistory();
		this.history.addToHistory(this.graph);
		arrangeGraph();
		graphEditor.repaint();
	}

	/**
	 * Displays the given graph in the editor with colored vertices and edges
	 *
	 * @param graph    the graph to display
	 * @param coloring the vertex coloring to display
	 */
	public void displayGraph(PropertyGraph<Integer, Integer> graph,
							 TotalColoringAlgorithm.TotalColoring<Integer, Integer> coloring) {
		propertyGraph = graph;
		this.graph = new RenderableGraph(graph, coloring, this.factory);
		this.history = new GraphEditorHistory();
		this.history.addToHistory(this.graph);
		arrangeGraph();
		graphEditor.repaint();
	}

	/**
	 * Shows an empty graph in the editor
	 */
	public void showEmptyGraph() {
		this.graph = new RenderableGraph();
		this.history = new GraphEditorHistory();
		this.history.addToHistory(graph);
		repaint();
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
								start = factory.createVertex(mStart);
							}

							Vertex target = graph.getVertexAt(mTarget);
							if (target == null) {
								target = factory.createVertex(mTarget);
							}
							graph = graph.deepCopy();
							graph.add(new Edge(start, target));
							history.addToHistory(graph);
						} else {
							graph = graph.deepCopy();
							graph.add(factory.createVertex(mTarget));
							history.addToHistory(graph);
						}
					} else if (mouseEvent.getButton() == MouseEvent.BUTTON3) { // Released right mouse button
						graph = graph.deepCopy();
						graph.remove(mTarget);
						history.addToHistory(graph);
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
				Shape subgraphOutline = RenderableGraph.outline(subgraph);
				float[] dash = new float[]{10.0f};
				kanvas.setStroke(new BasicStroke(1.5f,
						BasicStroke.CAP_BUTT,
						BasicStroke.JOIN_MITER,
						10.0f, dash, 0.0f));
				kanvas.setPaint(Color.darkGray);
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
			propertyGraph = graph.asPropertyGraph();
			history.addToHistory(graph);
			if (graphEditorController.isConnected(propertyGraph)) {
				displayColoring(graphEditorController);
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
			propertyGraph = graph.asPropertyGraph();
			propertyGraph.setId(graph.getId());
			if (graphEditorController.isConnected(propertyGraph) && graphEditorController.isValidGraph(propertyGraph)){
				graphEditorController.addEditedGraph(propertyGraph, graph.getId());
				graph = new RenderableGraph();
				history = new GraphEditorHistory();
				history.addToHistory(graph);
				graphEditor.repaint();
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
		public void actionPerformed(ActionEvent actionEvent) {
			if (currentColoringType == ColoringType.VERTEX) {
				currentVertexColoring = graphEditorController.getNextVertexColoring(
						propertyGraph, currentVertexColoring);
				displayGraph(propertyGraph, currentVertexColoring);
			} else {
				currentTotalColoring = graphEditorController.getNextTotalColoring(
						propertyGraph, currentTotalColoring);
				displayGraph(propertyGraph, currentTotalColoring);
			}
		}
	}

	private class CenterVerticesAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			arrangeGraph();
			repaint();
		}
	}

	private class SwitchColoringTypeAction implements ItemListener {
		private final GraphEditorController graphEditorController;

		private SwitchColoringTypeAction(GraphEditorController graphEditorController) {
			this.graphEditorController = graphEditorController;
		}

		@Override
		public void itemStateChanged(ItemEvent itemEvent) {
			if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
				if (currentColoringType == ColoringType.VERTEX) {
					currentColoringType = ColoringType.TOTAL;
				} else {
					currentColoringType = ColoringType.VERTEX;
				}
				displayColoring(graphEditorController);
			}
		}
	}

	protected void displayColoring(GraphEditorController graphEditorController) {
		if (currentColoringType == ColoringType.VERTEX) {
			currentVertexColoring = graphEditorController.getVertexColoring(propertyGraph);
			displayGraph(propertyGraph, currentVertexColoring);
		} else {
			currentTotalColoring = graphEditorController.getTotalColoring(propertyGraph);
			displayGraph(propertyGraph, currentTotalColoring);
		}
	}

	private void arrangeGraph() {
		int wide = Math.min(graphEditor.getWidth(), graphEditor.getHeight());
		Point upperLeft;
		Point lowerRight;
		if (graphEditor.getWidth() > graphEditor.getHeight()) {
			upperLeft = new Point((graphEditor.getWidth() / 2) - (graphEditor.getHeight() / 2), 0);
			lowerRight = new Point((graphEditor.getWidth() / 2) + (graphEditor.getHeight() / 2), graphEditor.getHeight());
		} else {
			upperLeft = new Point(0, (graphEditor.getHeight() / 2) - (graphEditor.getWidth() / 2));
			lowerRight = new Point(graphEditor.getWidth(), (graphEditor.getHeight() / 2) + (graphEditor.getWidth() / 2));
		}
		GraphLook.arrangeInCircle(graph.getSubgraphs(), graph.getVerticesNotContainedInSubgraphs(),
				upperLeft, lowerRight);
		repaint();
	}
}

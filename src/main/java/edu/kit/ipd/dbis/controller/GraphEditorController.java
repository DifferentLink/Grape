package edu.kit.ipd.dbis.controller;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.InsertionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.UnexpectedObjectException;
import edu.kit.ipd.dbis.gui.GrapeUI;
import edu.kit.ipd.dbis.gui.StatusbarUI;
import edu.kit.ipd.dbis.gui.grapheditor.GraphEditorUI;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.density.NextDenserGraphFinder;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.density.NoDenserGraphException;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.TotalColoringAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.TotalColoring;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.VertexColoring;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;

import java.util.List;

import static edu.kit.ipd.dbis.log.EventType.ADD;
import static edu.kit.ipd.dbis.log.EventType.REMOVE;

/**
 * The type Graph editor controller.
 */
public class GraphEditorController {

	private GraphDatabase database;
	private StatusbarController statusbar;
	private FilterController filter;
	private GraphEditorUI graphEditor;

	private GrapeUI grapeUI;
	private StatusbarUI statusbarUI;

	public void setGrapeUI(GrapeUI grapeUI) {
		this.grapeUI = grapeUI;
	}

	//TODO: Singleton pattern
	private static GraphEditorController editor;

	public void setStatusbarUI(StatusbarUI statusbarUI) {
		this.statusbarUI = statusbarUI;
	}

	private GraphEditorController() {
		this.statusbar = StatusbarController.getInstance();
		this.filter = FilterController.getInstance();
	}

	/**
	 * Gets instance.
	 *
	 * @return the instance
	 */
	public static GraphEditorController getInstance() {
		if (editor == null) {
			editor = new GraphEditorController();
		}
		return editor;
	}

	/**
	 * Replaces the old database with the given database.
	 *
	 * @param database the current database
	 */
	public void setDatabase(GraphDatabase database) {
		this.database = database;
	}

	public void setGraphEditor(GraphEditorUI graphEditor) {
		this.graphEditor = graphEditor;
	}

	/**
	 * checks the given graph for duplicates then adds the graph to the not yet calculated
	 * graphlist of CalculationController and deletes the old graph from the
	 * database.
	 *
	 * @param newGraph the PropertyGraph<V,E> to add.
	 * @param oldID    the id of the modified graph from the Grapheditor.
	 */
	public void addEditedGraph(PropertyGraph<Integer, Integer> newGraph, int oldID) {
		Boolean isDuplicate = null;
		try {
			isDuplicate = database.graphExists(newGraph);
		} catch (ConnectionFailedException e) {
			statusbar.addMessage(e.getMessage());
		}
		if (!isDuplicate) {
			try {
				database.addGraph(newGraph);
				statusbar.addEvent(ADD, newGraph.getId());
				database.deleteGraph(oldID);
				statusbar.addEvent(REMOVE, oldID);
				this.grapeUI.updateTable();
			} catch (ConnectionFailedException | UnexpectedObjectException | InsertionFailedException e) {
				statusbar.addMessage(e.getMessage());
			}
		}
	}

	/**
	 * Add new graph.
	 *
	 * @param graph the graph
	 */
	public void addNewGraph(PropertyGraph<Integer, Integer> graph) throws InvalidGraphInputException { // todo only duplicate check??
		if (isValidGraph(graph)) {
			try {
				database.addGraph(graph);
				statusbar.continueCalculation();
				statusbar.addEvent(ADD, graph.getId());
				this.grapeUI.updateTable();
			} catch (ConnectionFailedException
					| InsertionFailedException | UnexpectedObjectException e) {
				statusbar.addMessage(e.getMessage());
			}
		}
	}

	public PropertyGraph<Integer, Integer> getGraphById(int id) {
		PropertyGraph<Integer, Integer> graph = null;
		try {
			graph = database.getGraphById(id);
		} catch (ConnectionFailedException | UnexpectedObjectException e) {
			statusbar.addMessage(e.getMessage());
		}
		return graph;
	}

	/**
	 * checks if the graph is valid
	 *
	 * @param graph the PropertyGraph<V,E> to check.
	 * @return true if the given graph is valid.
	 */
	public Boolean isValidGraph(PropertyGraph<Integer, Integer> graph) throws InvalidGraphInputException {
		Boolean duplicate = true;
		try {
			duplicate = database.graphExists(graph);
		} catch (ConnectionFailedException e) {
			statusbar.addMessage(e.getMessage());
		}
		if (duplicate) {
			throw new InvalidGraphInputException("Given graph is a duplicate.");
		}
		return true;
	}

	/**
	 * triggers the calculation of the next denser graph for a specific graph
	 *
	 * @param graph the PropertyGraph<V,E> to calculate.
	 */
	public void addNextDenserToDatabase(PropertyGraph<Integer, Integer> graph) {
		NextDenserGraphFinder denserGraphFinder = new NextDenserGraphFinder(graph);

		PropertyGraph<Integer, Integer> denserGraph;
		try {
			denserGraph = denserGraphFinder.getNextDenserGraph();
			database.addGraph(denserGraph);
			statusbar.continueCalculation();
			this.grapeUI.updateTable();
		} catch (ConnectionFailedException | UnexpectedObjectException | InsertionFailedException |
				NoDenserGraphException e) {
			statusbar.addMessage(e.getMessage());
		}
	}

	public void addNextDenserToDatabase(final int id) {
		try {
			NextDenserGraphFinder denserGraphFinder = new NextDenserGraphFinder(database.getGraphById(id));

			PropertyGraph<Integer, Integer> denserGraph;
			try {
				denserGraph = denserGraphFinder.getNextDenserGraph();
				database.addGraph(denserGraph);
				statusbar.continueCalculation();
				this.grapeUI.updateTable();
			} catch (NoDenserGraphException e) {
				statusbar.addMessage(e.getMessage());
			} catch (InsertionFailedException e) {}
		} catch (ConnectionFailedException | UnexpectedObjectException ignored) {}

	}

	/**
	 * Returns a vertex coloring for the input graph.
	 *
	 * @param graph input graph
	 * @return the graphcolorization.
	 */
	public static VertexColoringAlgorithm.Coloring<Integer> getVertexColoring(PropertyGraph<Integer, Integer> graph) {
		return ((List<VertexColoringAlgorithm.Coloring<Integer>>) graph.getProperty(VertexColoring.class).getValue()).get(0);
	}

	/**
	 * Returns a total coloring for the input graph.
	 *
	 * @param graph input graph
	 * @return the graph coloring.
	 */
	public static TotalColoringAlgorithm.TotalColoring<Integer, Integer> getTotalColoring(PropertyGraph<Integer, Integer> graph) {
		return ((List<TotalColoringAlgorithm.TotalColoring<Integer, Integer>>) graph.getProperty(TotalColoring.class).getValue()).get(0);
	}

	/**
	 * Returns a coloring which is not equivalent to current coloring.
	 *
	 * @param graph input graph
	 * @return alternative coloring
	 */
	public VertexColoringAlgorithm.Coloring<Integer> getNextVertexColoring(
			PropertyGraph<Integer, Integer> graph,
			VertexColoringAlgorithm.Coloring<Integer> currentColoring) {
		List<VertexColoringAlgorithm.Coloring<Integer>> colorings =
				(List<VertexColoringAlgorithm.Coloring<Integer>>) graph.getProperty(VertexColoring.class).getValue();
		int index = colorings.indexOf(currentColoring);
		if (index + 1 == colorings.size()) {
			return colorings.get(0);
		} else {
			return colorings.get(index + 1);
		}
	}

	/**
	 * Returns a total coloring which is not equivalent to current coloring.
	 *
	 * @param graph input graph
	 * @return alternative coloring
	 */
	public TotalColoringAlgorithm.TotalColoring<Integer, Integer> getNextTotalColoring(
			PropertyGraph<Integer, Integer> graph,
			TotalColoringAlgorithm.TotalColoring<Integer, Integer> currentColoring) {
		List<TotalColoringAlgorithm.TotalColoring<Integer, Integer>> colorings =
				(List<TotalColoringAlgorithm.TotalColoring<Integer, Integer>>) graph.getProperty(TotalColoring.class).getValue();
		int index = colorings.indexOf(currentColoring);
		if (index + 1 == colorings.size()) {
			return colorings.get(0);
		} else {
			return colorings.get(index + 1);
		}
	}

	public void emptyGraphToGraphEditor() {
		graphEditor.showEmptyGraph();
	}
}

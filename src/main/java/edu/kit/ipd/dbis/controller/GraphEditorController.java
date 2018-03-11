package edu.kit.ipd.dbis.controller;

import edu.kit.ipd.dbis.controller.exceptions.InvalidGraphInputException;
import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.InsertionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.UnexpectedObjectException;
import edu.kit.ipd.dbis.gui.GrapeUI;
import edu.kit.ipd.dbis.gui.StatusbarUI;
import edu.kit.ipd.dbis.gui.grapheditor.GraphEditorUI;
import edu.kit.ipd.dbis.log.EventType;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.density.NextDenserGraphFinder;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.density.NoDenserGraphException;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.ProfileDensityAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.TotalColoringAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.Profile;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.TotalColoring;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.VertexColoring;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static edu.kit.ipd.dbis.log.EventType.ADD;

/**
 * The type Graph editor controller.
 */
public final class GraphEditorController {

	private GraphDatabase database;
	private StatusbarController statusbar;
	private GraphEditorUI graphEditor;

	private GrapeUI grapeUI;
	private StatusbarUI statusbarUI;

	/**
	 * Sets grape ui.
	 *
	 * @param grapeUI the GUI the graph editor is part of
	 */
	public void setGrapeUI(GrapeUI grapeUI) {
		this.grapeUI = grapeUI;
	}

	private static GraphEditorController editor;

	/**
	 * Sets statusbar ui.
	 *
	 * @param statusbarUI the statusbar ui
	 */
	public void setStatusbarUI(StatusbarUI statusbarUI) {
		this.statusbarUI = statusbarUI;
	}

	private GraphEditorController() {
		this.statusbar = StatusbarController.getInstance();
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

	/**
	 * Sets graph editor.
	 *
	 * @param graphEditor the graph editor
	 */
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
		boolean isDuplicate = false;
		try {
			isDuplicate = database.graphExists(newGraph);
		} catch (ConnectionFailedException e) {
			statusbar.addMessage(e.getMessage());
		}
		if (!isDuplicate) {
			try {
				newGraph.calculateProperties();
				database.addGraph(newGraph);
				statusbar.addEvent(ADD, newGraph.getId());
			} catch (ConnectionFailedException | UnexpectedObjectException | InsertionFailedException e) {
				statusbar.addMessage(e.getMessage());
			}
		}
		this.grapeUI.updateTable();
		statusbar.setNumberOfGraphs();
	}

	/**
	 * Add new graph.
	 *
	 * @param graph the graph
	 * @throws InvalidGraphInputException the invalid graph input exception
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

	/**
	 * Gets graph by id.
	 *
	 * @param id the id
	 * @return the graph by id
	 */
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
	public Boolean isValidGraph(PropertyGraph<Integer, Integer> graph) {
		boolean duplicate = false;
		try {
			duplicate = database.graphExists(graph);
		} catch (ConnectionFailedException e) {
			statusbar.addMessage(e.getMessage());
		}
		if (duplicate) {
			statusbar.addMessage("Given graph already exists");
			this.grapeUI.updateTable();
		}
		return !duplicate;
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
			if (!database.graphExists(denserGraph)) {
				denserGraph.calculateProperties();
				database.addGraph(denserGraph);
				statusbar.addEvent(EventType.ADD, denserGraph.getId(), "Next denser graph added");
				this.grapeUI.updateTable();
				statusbar.setNumberOfGraphs();
			} else {
				statusbar.addMessage("Denser graph already exists");
			}
		} catch (NoDenserGraphException | UnexpectedObjectException | InsertionFailedException |
				ConnectionFailedException e) {
			statusbar.addMessage(e.getMessage());
		}
	}

	public void addNextDenserToDatabase(final int id) {
		try {
			NextDenserGraphFinder denserGraphFinder = new NextDenserGraphFinder(database.getGraphById(id));
			PropertyGraph<Integer, Integer> denserGraph;
			denserGraph = denserGraphFinder.getNextDenserGraph();
			denserGraph.calculateProperties();
			boolean graphExists = database.graphExists(denserGraph);
			database.addGraph(denserGraph);
			//TODO: same problem: graph marked as deleted?
			if (graphExists) {
				statusbar.addMessage("Database already contains next denser graph");
			} else {
				statusbar.addEvent(EventType.ADD, denserGraph.getId(), "Next denser graph added");
			}
			this.grapeUI.updateTable();
			statusbar.setNumberOfGraphs();
		} catch (NoDenserGraphException | UnexpectedObjectException | InsertionFailedException |
				ConnectionFailedException e) {
			statusbar.addMessage(e.getMessage());
		}
	}

	public String getProfile(final int id) {
		int[][] profile = new int[][]{{}};
		try {
			PropertyGraph<Integer, Integer> graph = database.getGraphById(id);
			ProfileDensityAlgorithm.Profile p = (ProfileDensityAlgorithm.Profile) graph.getProperty(Profile.class).getValue();
			profile = p.getMatrix();

		} catch (ConnectionFailedException | UnexpectedObjectException e) {
			statusbar.addMessage(e.getMessage());
		}
		StringBuilder result = new StringBuilder();
		Set<Integer> negative = new HashSet<>();
		for (int[] aProfile : profile) {
			for (int j = 0; j < profile[0].length; j++) {
				if (aProfile[j] == -1) {
					negative.add(j);
				}
			}
		}
		for (int i = 0; i < profile.length; i++) {
			for (int j = 0; j < profile[0].length; j++) {
				if (negative.contains(j) && profile[i][j] != -1) {
					result.append(" ");
				}
				result.append(profile[i][j]);
			}
			if (i < profile.length) {
				result.append("\n");
			}
		}
		return result.toString();
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
	 * @param graph           input graph
	 * @param currentColoring the current coloring
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
	 * @param graph           input graph
	 * @param currentColoring the current coloring
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

	/**
	 * Empty graph to graph editor.
	 */
	public void emptyGraphToGraphEditor() {
		graphEditor.showEmptyGraph();
	}
}

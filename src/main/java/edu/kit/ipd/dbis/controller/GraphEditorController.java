package edu.kit.ipd.dbis.controller;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.*;
import edu.kit.ipd.dbis.gui.NonEditableTableModel;
import edu.kit.ipd.dbis.gui.StatusbarUI;
import edu.kit.ipd.dbis.gui.grapheditor.GraphEditorUI;
import edu.kit.ipd.dbis.gui.grapheditor.RenderableGraph;
import edu.kit.ipd.dbis.log.Event;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.color.MinimalTotalColoring;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.color.MinimalVertexColoring;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.density.NextDenserGraphFinder;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.density.NoDenserGraphException;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.TotalColoringAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.VertexColoring;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;

import java.sql.SQLException;
import java.util.Collections;

import static edu.kit.ipd.dbis.log.EventType.ADD;
import static edu.kit.ipd.dbis.log.EventType.MESSAGE;
import static edu.kit.ipd.dbis.log.EventType.REMOVE;

/**
 * The type Graph editor controller.
 */
public class GraphEditorController {

	private GraphDatabase database;
	private StatusbarController statusbar;
	private FilterController filter;
	private NonEditableTableModel tableModel;
	private GraphEditorUI graphEditor;
	private StatusbarUI statusbarUI;

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

	public void setTableModel(NonEditableTableModel tableModel) {
		this.tableModel = tableModel;
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
				this.statusbarUI.setRemainingCalculations(0);
				this.tableModel.update(filter.getFilteredAndSortedGraphs());
			} catch (ConnectionFailedException | UnexpectedObjectException | InsertionFailedException | SQLException e) {
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
				this.statusbarUI.setRemainingCalculations(0);
				this.tableModel.update(filter.getFilteredAndSortedGraphs());
			} catch (ConnectionFailedException
					| InsertionFailedException | UnexpectedObjectException | SQLException e) {
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
			statusbar.addEvent(ADD, denserGraph.getId());
			this.statusbarUI.setRemainingCalculations(0);
			this.tableModel.update(filter.getFilteredAndSortedGraphs());
		} catch (ConnectionFailedException | UnexpectedObjectException | InsertionFailedException | SQLException |
				NoDenserGraphException e) {
			statusbar.addMessage(e.getMessage());
		}
	}

	/**
	 * calculates a valid colorization for a specific graph.
	 *
	 * @param graph the PropertyGraph<V,E> to calculate.
	 * @return the graphcolorization.
	 */
	public VertexColoringAlgorithm.Coloring getVertexColoring(PropertyGraph<Integer, Integer> graph) {
		MinimalVertexColoring coloring = new MinimalVertexColoring(graph);
		return coloring.getColoring();
	}

	/**
	 * calculates a valid colorization for a specific graph
	 *
	 * @param graph the PropertyGraph<V,E> to calculate.
	 * @return the graphcolorization.
	 */
	public TotalColoringAlgorithm.TotalColoring getTotalColoring(PropertyGraph<Integer, Integer> graph) {
		MinimalTotalColoring coloring = new MinimalTotalColoring(graph);
		return coloring.getColoring();
	}

	/**
	 * calculates a cloring which is not equivalent to current coloring
	 *
	 * @param id the PropertyGraph<V,E> to calculate.
	 * @return the net valid alternative Coloring.
	 * @throws //NoEqivalentColoringException thrown if there is no equivalent colorization for a specific graph
	 */
	public VertexColoring getAlternateVertexColoring(int id) {
		return null;
	}

	/**
	 * calculates a cloring which is not equivalent to current coloring
	 *
	 * @param id the PropertyGraph<V,E> to calculate.
	 * @return the next valid alternative Coloring.
	 * @throws //NoEquivalentColoringException thrown if there is no equivalent colorization for a specific graph
	 */
	public TotalColoringAlgorithm.TotalColoring getAlternateTotalColoring(int id) {
		return null;
	}

	public void emptyGraphToGraphEditor() {
		graphEditor.showEmptyGraph();
	}
}

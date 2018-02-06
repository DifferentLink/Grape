package edu.kit.ipd.dbis.controller;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.*;
import edu.kit.ipd.dbis.log.Event;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.color.MinimalTotalColoring;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.color.MinimalVertexColoring;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.density.NextDenserGraphFinder;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.TotalColoringAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.VertexColoring;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;

import java.util.Collections;

import static edu.kit.ipd.dbis.log.EventType.ADD;
import static edu.kit.ipd.dbis.log.EventType.MESSAGE;
import static edu.kit.ipd.dbis.log.EventType.REMOVE;

/**
 * The type Graph editor controller.
 */
public class GraphEditorController {

	private GraphDatabase database;
	private StatusbarController log;

	//TODO: Singleton pattern
	private static GraphEditorController editor;

	private GraphEditorController() {
		this.log = StatusbarController.getInstance();
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
			log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.EMPTY_SET));
		}
		if (!isDuplicate) {
			try {
				database.addGraph(newGraph);
				log.addEvent(ADD, newGraph.getId());
				try {
					database.deleteGraph(oldID);
					log.addEvent(REMOVE, oldID);
				} catch (ConnectionFailedException e) {
					log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.EMPTY_SET));
				}
			} catch ( ConnectionFailedException | UnexpectedObjectException | InsertionFailedException e) {
				log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.EMPTY_SET));
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
				log.addEvent(ADD, graph.getId());
			} catch ( ConnectionFailedException
					| InsertionFailedException | UnexpectedObjectException e) {
				log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.EMPTY_SET));
			}
		}
	}

	public PropertyGraph<Integer, Integer> getGraphById(int id) {
		PropertyGraph<Integer, Integer> graph = null;
		try {
			graph = database.getGraphById(id);
		} catch ( ConnectionFailedException | UnexpectedObjectException e) {
			log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.EMPTY_SET));
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
			log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.EMPTY_SET));
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
		denserGraph = denserGraphFinder.getNextDenserGraph();
		try {
			database.addGraph(denserGraph);
			log.addEvent(ADD, denserGraph.getId());
		} catch ( ConnectionFailedException
				| UnexpectedObjectException | InsertionFailedException e) {
			log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.EMPTY_SET));
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

}

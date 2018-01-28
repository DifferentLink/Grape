package edu.kit.ipd.dbis.controller;


import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.*;
import edu.kit.ipd.dbis.log.Event;
import edu.kit.ipd.dbis.org.jgrapht.additions.generate.BulkGraphGenerator;
import edu.kit.ipd.dbis.org.jgrapht.additions.generate.BulkRandomConnectedGraphGenerator;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static edu.kit.ipd.dbis.log.EventType.MESSAGE;

/**
 * The type Generate controller.
 */
public class GenerateController {

	private GraphDatabase database;
	private BulkGraphGenerator generator;
	private StatusbarController log;

	//TODO: Singleton pattern
	private static GenerateController generate;

	private GenerateController() {
		this.log = StatusbarController.getInstance();
		this.generator = new BulkRandomConnectedGraphGenerator();
	}

	public static GenerateController getInstance() {
		if (generate == null) {
			generate = new GenerateController();
		}
		return generate;
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
	 * Gives the graph generator the command to generate the graphs and saves them in the Database.
	 *
	 * @param minVertices lower bound of vertices
	 * @param maxVertices upper bound of vertices
	 * @param minEdges    lower bound of edges.
	 * @param maxEdges    upper bound of edges.
	 * @param amount      the number of graphs
	 */
	public void generateGraphs(int minVertices, int maxVertices, int minEdges, int maxEdges, int amount) {
		// solange generieren bis die gewünschte anzahl von graphen existiert!
		Set<PropertyGraph> graphs = new HashSet<PropertyGraph>();
		generator.generateBulk(graphs, amount, minVertices, maxVertices, minEdges, maxEdges);
		this.saveGraphs(graphs);
	}

	public void generateEmptyGraph() { // todo please implement me
	}

	/**
	 * Calculates a graph with the BFS Code and saves it to the Database.
	 *
	 * @param bfsCode the BFS Code of the graph to save.
	 */
	public void generateBFSGraph(String bfsCode) {

	}

	/**
	 * Deletes the given graph from the GUI table.
	 *
	 * @param id the ID of the PropertyGraph<V,E>.
	 */
	public void delGraph(int id) {
		try {
			database.deleteGraph(id);
		} catch (TablesNotAsExpectedException | AccessDeniedForUserException | ConnectionFailedException | DatabaseDoesNotExistException e) {
			log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.EMPTY_SET));
		}
	}

	/**
	 * Saves the graphs in the Database in the list of not yet calculated graphs.
	 *
	 * @param graphs the set of PropertyGraph<V,E>
	 */
	private void saveGraphs(Set<PropertyGraph> graphs) {
		for (PropertyGraph graph : graphs) {
			try {
				database.addGraph(graph);
			} catch (DatabaseDoesNotExistException | TablesNotAsExpectedException | AccessDeniedForUserException | ConnectionFailedException | InsertionFailedException | UnexpectedObjectException e) {
				log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.EMPTY_SET));
			}
		}
	}

	private Boolean isValidBFS(String bfsCode) {
		return true;
	}

	private Boolean isValidGeneratorInput(int minVertices, int maxVertices, int minEdges, int maxEdges, int amount) {
		return true;
	}
}

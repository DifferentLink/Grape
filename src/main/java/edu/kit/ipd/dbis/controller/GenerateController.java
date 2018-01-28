package edu.kit.ipd.dbis.controller;


import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.*;
import edu.kit.ipd.dbis.log.Event;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
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
		// todo: solange generieren bis die gewünschte anzahl von graphen existiert!
		Set<PropertyGraph> graphs = new HashSet<PropertyGraph>();
		generator.generateBulk(graphs, amount, minVertices, maxVertices, minEdges, maxEdges);
		this.saveGraphs(graphs);
	}

	public void generateEmptyGraph() { // todo please implement me
		generateGraphs(0, 0, 0, 0, 1);
	}

	/**
	 * Creates a graph with the BFS Code and saves it in the Database.
	 *
	 * @param bfsCode the BFS Code of the graph to save.
	 */
	public void generateBFSGraph(String bfsCode) throws IllegalArgumentException {
		if (isValidBFS(bfsCode)) {
			// Parsing String into int[]
			String[] splitCode = bfsCode.split("\\[,]");
			int[] code = new int[splitCode.length];
			for (int i = 0; i < splitCode.length; i++) {
				code[i] = Integer.parseInt(splitCode[i]);
			}
			// Creating BfsCode Object
			BfsCodeAlgorithm.BfsCodeImpl bfs = new BfsCodeAlgorithm.BfsCodeImpl(code);
			PropertyGraph graph = new PropertyGraph(bfs);
			try {
				database.addGraph(graph);
			} catch (DatabaseDoesNotExistException | TablesNotAsExpectedException | ConnectionFailedException
					| AccessDeniedForUserException | UnexpectedObjectException | InsertionFailedException e) {
				log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.EMPTY_SET));
			}
		} else {
			throw new IllegalArgumentException("BfsCode is not valid");
		}
	}

	/**
	 * Deletes the given graph from the GUI table.
	 *
	 * @param id the ID of the PropertyGraph<V,E>.
	 */
	public void delGraph(int id) {
		try {
			database.deleteGraph(id);
		} catch (TablesNotAsExpectedException | AccessDeniedForUserException | ConnectionFailedException
				| DatabaseDoesNotExistException e) {
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
			} catch (DatabaseDoesNotExistException | TablesNotAsExpectedException | AccessDeniedForUserException
					| ConnectionFailedException | InsertionFailedException | UnexpectedObjectException e) {
				log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.EMPTY_SET));
			}
		}
	}

	private Boolean isValidBFS(String bfsCode) {
		if (!bfsCode.contains("[") || !bfsCode.contains("]")) {
			return false;
		}
		String[] splitCode = bfsCode.split("\\[*,]");
		for (int i = 0; i < splitCode.length; i++) {
			if (splitCode[i].length() != 1) {
				return false;
			} else if (!isNumeric(splitCode[i])) {
				return false;
			}
		}
		return true;
	}

	private Boolean isValidGeneratorInput(int minVertices, int maxVertices, int minEdges, int maxEdges, int amount) {
		if (minVertices >= 0 && minEdges >= 0 && maxEdges >= 0 && maxVertices >= 0 && amount >= 0) {
			return true;
		} else {
			return false;
		}
	}

	private Boolean isNumeric(String text) {
		try {
			int number = Integer.parseInt(text);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

}

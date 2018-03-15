package edu.kit.ipd.dbis.controller.util;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.InsertionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.UnexpectedObjectException;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

public class CalculationWorker extends Thread {

	private final PropertyGraph<Integer, Integer> graph;
	private final GraphDatabase database;

	public CalculationWorker(final PropertyGraph<Integer, Integer> graph, GraphDatabase database) {
		this.graph = graph;
		this.database = database;
	}

	@Override
	public void run() {
		try {
			graph.calculateProperties();
			database.replaceGraph(graph.getId(), graph);
		} catch (ConnectionFailedException | InsertionFailedException | UnexpectedObjectException ignored) { }
	}
}

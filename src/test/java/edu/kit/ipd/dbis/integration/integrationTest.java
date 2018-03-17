package edu.kit.ipd.dbis.integration;

import edu.kit.ipd.dbis.controller.util.CalculationMaster;
import edu.kit.ipd.dbis.controller.util.CalculationWorker;
import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.InsertionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.UnexpectedObjectException;
import edu.kit.ipd.dbis.filter.Filtermanagement;
import edu.kit.ipd.dbis.filter.exceptions.InvalidInputException;
import edu.kit.ipd.dbis.org.jgrapht.additions.generate.BulkGraphGenerator;
import edu.kit.ipd.dbis.org.jgrapht.additions.generate.BulkRandomConnectedGraphGenerator;
import edu.kit.ipd.dbis.org.jgrapht.additions.generate.NotEnoughGraphsException;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/*
* This Test class tests the integration of grape.
* */
public class integrationTest {

	static GraphDatabase database;
	static BulkGraphGenerator graphGenerator;
	static Filtermanagement filter;

	@BeforeClass
	public static void setUp() {
		//set Database here!
		graphGenerator = new BulkRandomConnectedGraphGenerator();
		filter = new Filtermanagement();
		try {
			filter.setDatabase(database);
		} catch (ConnectionFailedException ignored) {
		}
	}

	@Test
	public void generateIntegration() {
		int amount = 5;
		int minVertices = 2;
		int maxVertices = 5;
		int minEdges = 2;
		int maxEdges = 5;
		int graphAmount = 0;
		int uncalculated = 0;

		Set<PropertyGraph<Integer, Integer>> graphs = new HashSet<>();
		try {
			graphGenerator.generateBulk(graphs, amount, minVertices, maxVertices, minEdges, maxEdges);
		} catch (NotEnoughGraphsException ignored) {
		}
		for (PropertyGraph graph : graphs) {
			try {
				database.addGraph(graph);
			} catch (ConnectionFailedException | InsertionFailedException | UnexpectedObjectException ignored) {
			}
		}
		List<Thread> jobs = new LinkedList<>();
		for (PropertyGraph<Integer, Integer> graph : graphs) {
			jobs.add(new CalculationWorker(graph, database));
		}
		CalculationMaster.executeCalculation(jobs);
		try {
			graphAmount = database.getNumberOfGraphs();
			uncalculated = database.getNumberOfUncalculatedGraphs();
		} catch (ConnectionFailedException ignored) {
		}

		assertEquals(amount, graphAmount);
		assertEquals(0, uncalculated);
	}

	@Test
	public void filterIntegration() {
		int amount = 5;
		int minVertices = 2;
		int maxVertices = 6;
		int minEdges = 2;
		int maxEdges = 8;
		int filterId = 1;
		String filterInput = "numberofedges = 4";

		Set<PropertyGraph<Integer, Integer>> graphs = new HashSet<>();
		try {
			graphGenerator.generateBulk(graphs, amount, minVertices, maxVertices, minEdges, maxEdges);
		} catch (NotEnoughGraphsException ignored) {
		}
		for (PropertyGraph graph : graphs) {
			try {
				database.addGraph(graph);
			} catch (ConnectionFailedException | InsertionFailedException | UnexpectedObjectException ignored) {
			}
		}
		List<Thread> jobs = new LinkedList<>();
		for (PropertyGraph<Integer, Integer> graph : graphs) {
			jobs.add(new CalculationWorker(graph, database));
		}
		CalculationMaster.executeCalculation(jobs);
		try {
			filter.updateFilter(filterInput, filterId);
		} catch (ConnectionFailedException | InsertionFailedException | InvalidInputException | UnexpectedObjectException ignored) {
		}

		try {
			ResultSet result = filter.getFilteredAndSortedGraphs();
		} catch (ConnectionFailedException e) {
		}

	}

	@Test
	public void integration() {

	}


}

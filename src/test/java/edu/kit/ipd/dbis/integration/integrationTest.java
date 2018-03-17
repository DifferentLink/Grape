package edu.kit.ipd.dbis.integration;

import edu.kit.ipd.dbis.controller.util.CalculationMaster;
import edu.kit.ipd.dbis.controller.util.CalculationWorker;
import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.InsertionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.UnexpectedObjectException;
import edu.kit.ipd.dbis.database.file.FileManager;
import edu.kit.ipd.dbis.filter.Filtermanagement;
import edu.kit.ipd.dbis.filter.exceptions.InvalidInputException;
import edu.kit.ipd.dbis.org.jgrapht.additions.generate.BulkGraphGenerator;
import edu.kit.ipd.dbis.org.jgrapht.additions.generate.BulkRandomConnectedGraphGenerator;
import edu.kit.ipd.dbis.org.jgrapht.additions.generate.NotEnoughGraphsException;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	public static void setUp() throws Exception {
		try {
			String url = "jdbc:mysql://127.0.0.1/library";
			String user = "user";
			String password = "password";
			String name = "grape";

			FileManager fileManager = new FileManager();
			database = fileManager.createGraphDatabase(url, user, password, name);
			fileManager.deleteGraphDatabase(database);
			database = fileManager.createGraphDatabase(url, user, password, name);
		} catch (Exception e){
			Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/?user=travis&password=");
			connection.prepareStatement("CREATE DATABASE IF NOT EXISTS library").executeUpdate();
			String url = "jdbc:mysql://127.0.0.1/library";
			String user = "travis";
			String password = "";
			String name = "integrationtest";

			FileManager fileManager = new FileManager();
			database = fileManager.createGraphDatabase(url, user, password, name);
		}
		graphGenerator = new BulkRandomConnectedGraphGenerator();
		filter = new Filtermanagement();
		try {
			filter.setDatabase(database);
		} catch (ConnectionFailedException ignored) {
		}
	}

	@Before
	public void clear() throws SQLException, ConnectionFailedException {
		LinkedList<Integer> ids = database.getFilterTable().getIds();
		for (Integer id : ids) {
			if (id != 0) {
				database.deleteFilter(id);
			}
		}

		LinkedList<Integer> ids2 = database.getGraphTable().getIds();
		for (Integer id : ids) {
			if (id != 0) {
				database.deleteFilter(id);
			}
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
		graphAmount = graphs.size();
		assertEquals(amount, graphAmount);
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
			uncalculated = database.getNumberOfUncalculatedGraphs();
		} catch (ConnectionFailedException ignored) {
		}
		assertEquals(amount, graphAmount);
		assertEquals(0, uncalculated);
	}

	@Test
	public void filterIntegration() throws SQLException {
		int amount = 5;
		int minVertices = 2;
		int maxVertices = 6;
		int minEdges = 2;
		int maxEdges = 8;
		int filterId = 1;
		int numberEdges = 0;
		int expectedAmount = 0;
		int actualAmount = 0;


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
			filter.activate(filterId);
		} catch (ConnectionFailedException | InsertionFailedException | InvalidInputException | UnexpectedObjectException ignored) {
		}
		ResultSet result = null;
		//Get Number of Graphs
		try {
			result = filter.getFilteredAndSortedGraphs();
		} catch (ConnectionFailedException e) {
		}
		assert result != null;
		while (result.next()) {
			actualAmount++;
		}
		//own Filter method
		try {
			String[][] filterlist = new String[][]{};
			result = database.getGraphs(filterlist, "id", true);
		} catch (ConnectionFailedException e) {
		}
		assert result != null;
		while (result.next()) {
			if(result.getInt("numberofedges") == 4) {
				expectedAmount++;
			}
		}
		assertEquals(expectedAmount, actualAmount);
	}

	@Test
	public void integration() {

	}


}

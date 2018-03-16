package edu.kit.ipd.dbis.database.graph;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.AccessDeniedForUserException;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.DatabaseDoesNotExistException;
import edu.kit.ipd.dbis.database.exceptions.sql.UnexpectedObjectException;
import edu.kit.ipd.dbis.database.file.FileManager;
import edu.kit.ipd.dbis.filter.Filtermanagement;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.generate.RandomConnectedGraphGenerator;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.DoubleProperty;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.IntegerProperty;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.BfsCode;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.NumberOfVertices;
import org.jgrapht.alg.util.IntegerVertexFactory;
import org.jgrapht.generate.GraphGenerator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import sun.awt.image.ImageWatched;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

public class GraphTableComponentTests {

	private static GraphDatabase database;

	@BeforeClass
	public static void setUp() throws Exception {
		Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/?user=travis&password=");
		connection.prepareStatement("CREATE DATABASE IF NOT EXISTS library").executeUpdate();
		String url = "jdbc:mysql://127.0.0.1/library";
		String user = "travis";
		String password = "";
		String name = "grape";

		FileManager fileManager = new FileManager();
		database = fileManager.createGraphDatabase(url, user, password, name);

	}

	@Test
	public void insertTest() throws UnexpectedObjectException, SQLException, IOException {
		GraphGenerator gen = new RandomConnectedGraphGenerator(2, 2, 1, 1);
		PropertyGraph<Integer, Integer> graph = new PropertyGraph<>();
		gen.generateGraph(graph, new IntegerVertexFactory(1), null);

		database.getGraphTable().insert(graph);
		assertEquals(database.getGraphTable().hasUncalculated(), true);
		database.getGraphTable().delete(1);

	}

	@Test (expected = UnexpectedObjectException.class)
	public void insertTest1() throws UnexpectedObjectException, SQLException, IOException {
		class SomeObject implements Serializable {

		}
		database.getGraphTable().insert(new SomeObject());

	}

	@Test
	public void getContentByIdTest() throws UnexpectedObjectException, SQLException, IOException, ClassNotFoundException {
		GraphGenerator gen = new RandomConnectedGraphGenerator(2, 2, 1, 1);
		PropertyGraph<Integer, Integer> graph = new PropertyGraph<>();
		gen.generateGraph(graph, new IntegerVertexFactory(1), null);
		graph.calculateProperties();

		database.getGraphTable().insert(graph);
		BfsCode bfs = (BfsCode) graph.getProperty(BfsCode.class);
		BfsCodeAlgorithm.BfsCode code = (BfsCodeAlgorithm.BfsCode) bfs.getValue();

		BfsCode bfs2 = (BfsCode) database.getGraphTable().getContent(1).getProperty(BfsCode.class);
		BfsCodeAlgorithm.BfsCode code2 = (BfsCodeAlgorithm.BfsCode) bfs2.getValue();

		assertEquals(code2.toString(), code.toString());
		database.getGraphTable().delete(1);

	}

	@Test
	public void getContentTest() throws UnexpectedObjectException, SQLException, IOException {
		GraphGenerator gen = new RandomConnectedGraphGenerator(2, 2, 1, 1);
		PropertyGraph<Integer, Integer> graph1 = new PropertyGraph<>();
		gen.generateGraph(graph1, new IntegerVertexFactory(1), null);
		GraphGenerator gen2 = new RandomConnectedGraphGenerator(3, 3, 3, 3);
		PropertyGraph<Integer, Integer> graph2 = new PropertyGraph<>();
		gen2.generateGraph(graph2, new IntegerVertexFactory(1), null);
		graph1.calculateProperties();
		graph2.calculateProperties();

		String[][] filter = new String[1][7];
		filter[0][0] = "id";
		filter[0][1] = "+";
		filter[0][2] = "0";
		filter[0][3] = "=";
		filter[0][4] = "1";
		filter[0][5] = "+";
		filter[0][6] = "0";

		database.getGraphTable().insert(graph1);
		database.getGraphTable().insert(graph2);
		ResultSet rs = database.getGraphTable().getContent(filter, "id", true);

		rs.next();
		assertEquals(rs.getInt("id"), 1);
		database.getGraphTable().delete(1);
		database.getGraphTable().delete(2);

	}

	@Test (expected = NullPointerException.class)
	public void getContentTest1() throws ClassNotFoundException, SQLException, UnexpectedObjectException, IOException {
		database.getGraphTable().getContent(1).getId();
	}

	@Test (expected = NullPointerException.class)
	public void switchStateTest() throws UnexpectedObjectException, SQLException, IOException, ClassNotFoundException {
		GraphGenerator gen = new RandomConnectedGraphGenerator(2, 2, 1, 1);
		PropertyGraph<Integer, Integer> graph = new PropertyGraph<>();
		gen.generateGraph(graph, new IntegerVertexFactory(1), null);
		graph.calculateProperties();

		database.getGraphTable().insert(graph);
		database.getGraphTable().switchState(1);
		database.getGraphTable().deleteAll();

		database.getGraphTable().getContent(1).getId();
	}

	@Test (expected = NullPointerException.class)
	public void deleteTest() throws SQLException, UnexpectedObjectException, IOException, ClassNotFoundException {
		GraphGenerator gen = new RandomConnectedGraphGenerator(2, 2, 1, 1);
		PropertyGraph<Integer, Integer> graph = new PropertyGraph<>();
		gen.generateGraph(graph, new IntegerVertexFactory(1), null);
		graph.calculateProperties();

		database.getGraphTable().insert(graph);
		database.getGraphTable().delete(1);
		database.getGraphTable().getContent(1).getId();
	}

	@Test
	public void getColumnsTest() throws SQLException {
		LinkedList<String> columns = database.getGraphTable().getColumns();
		PropertyGraph<Integer, Integer> graph = new PropertyGraph<>();
		Collection<Property> properties = graph.getProperties();

		for (Property property : properties) {
			if (property.getClass().getSuperclass() == IntegerProperty.class) {
				assertEquals(columns.contains(property.getClass().getSimpleName().toLowerCase()), true);
			} else if (property.getClass().getSuperclass() == DoubleProperty.class) {
				assertEquals(columns.contains(property.getClass().getSimpleName().toLowerCase()), true);
			}
		}

	}

	@Test
	public void getNumberOfRowsTest() throws UnexpectedObjectException, SQLException, IOException {
		GraphGenerator gen = new RandomConnectedGraphGenerator(2, 2, 1, 1);
		PropertyGraph<Integer, Integer> graph1 = new PropertyGraph<>();
		gen.generateGraph(graph1, new IntegerVertexFactory(1), null);
		GraphGenerator gen2 = new RandomConnectedGraphGenerator(3, 3, 3, 3);
		PropertyGraph<Integer, Integer> graph2 = new PropertyGraph<>();
		gen2.generateGraph(graph2, new IntegerVertexFactory(1), null);
		GraphGenerator gen3 = new RandomConnectedGraphGenerator(4, 4, 4, 4);
		PropertyGraph<Integer, Integer> graph3 = new PropertyGraph<>();
		gen3.generateGraph(graph3, new IntegerVertexFactory(1), null);
		GraphGenerator gen4 = new RandomConnectedGraphGenerator(5, 5, 5, 5);
		PropertyGraph<Integer, Integer> graph4 = new PropertyGraph<>();
		gen4.generateGraph(graph4, new IntegerVertexFactory(1), null);

		graph1.calculateProperties();
		graph2.calculateProperties();
		graph3.calculateProperties();
		graph4.calculateProperties();

		database.getGraphTable().insert(graph1);
		database.getGraphTable().insert(graph2);
		database.getGraphTable().insert(graph3);
		database.getGraphTable().insert(graph4);

		assertEquals(database.getGraphTable().getNumberOfRows(), 4);
		database.getGraphTable().delete(1);
		database.getGraphTable().delete(2);
		database.getGraphTable().delete(3);
		database.getGraphTable().delete(4);

	}

	@Test
	public void getUncalculatedGraphTest() throws UnexpectedObjectException, SQLException, IOException, ClassNotFoundException {
		GraphGenerator gen = new RandomConnectedGraphGenerator(2, 2, 1, 1);
		PropertyGraph<Integer, Integer> graph1 = new PropertyGraph<>();
		gen.generateGraph(graph1, new IntegerVertexFactory(1), null);

		database.getGraphTable().insert(graph1);
		assertEquals(database.getGraphTable().getUncalculatedGraph(), graph1);

		database.getGraphTable().delete(1);

	}

	@Test
	public void getValuesTest() throws UnexpectedObjectException, SQLException, IOException, ConnectionFailedException {
		GraphGenerator gen = new RandomConnectedGraphGenerator(2, 2, 1, 1);
		PropertyGraph<Integer, Integer> graph1 = new PropertyGraph<>();
		gen.generateGraph(graph1, new IntegerVertexFactory(1), null);
		GraphGenerator gen2 = new RandomConnectedGraphGenerator(3, 3, 3, 3);
		PropertyGraph<Integer, Integer> graph2 = new PropertyGraph<>();
		gen2.generateGraph(graph2, new IntegerVertexFactory(1), null);

		graph1.calculateProperties();
		graph2.calculateProperties();

		database.getGraphTable().insert(graph1);
		database.getGraphTable().insert(graph2);

		String[][] filter = new String[1][7];
		filter[0][0] = "0";
		filter[0][1] = "+";
		filter[0][2] = "0";
		filter[0][3] = "=";
		filter[0][4] = "0";
		filter[0][5] = "+";
		filter[0][6] = "0";
		LinkedList<Double> values = database.getValues(filter, "numberofvertices");

		assertEquals(values.contains(2.0), true);
		assertEquals(values.contains(3.0), true);

		database.getGraphTable().delete(1);
		database.getGraphTable().delete(2);

	}

	@Test
	public void uncalculatedGraphsTest() throws UnexpectedObjectException, SQLException, IOException, ClassNotFoundException {

		assertEquals(database.getGraphTable().hasUncalculated(), false);

		GraphGenerator gen = new RandomConnectedGraphGenerator(2, 2, 1, 1);
		PropertyGraph<Integer, Integer> graph1 = new PropertyGraph<>();
		gen.generateGraph(graph1, new IntegerVertexFactory(1), null);
		GraphGenerator gen2 = new RandomConnectedGraphGenerator(3, 3, 3, 3);
		PropertyGraph<Integer, Integer> graph2 = new PropertyGraph<>();
		gen2.generateGraph(graph2, new IntegerVertexFactory(1), null);

		database.getGraphTable().insert(graph1);
		database.getGraphTable().insert(graph2);

		assertEquals(database.getGraphTable().hasUncalculated(), true);
		assertEquals(database.getGraphTable().numberOfUncalculatedGraphs(), 2);
		database.getGraphTable().delete(1);
		assertEquals(database.getGraphTable().getUncalculatedGraph(), graph2);

		database.getGraphTable().delete(2);

	}

	@Test
	public void graphExistsTest() throws SQLException, IOException, UnexpectedObjectException {
		GraphGenerator gen = new RandomConnectedGraphGenerator(2, 2, 1, 1);
		PropertyGraph<Integer, Integer> graph1 = new PropertyGraph<>();
		gen.generateGraph(graph1, new IntegerVertexFactory(1), null);
		graph1.calculateProperties();

		assertEquals(database.getGraphTable().graphExists(graph1), false);
		database.getGraphTable().insert(graph1);
		assertEquals(database.getGraphTable().graphExists(graph1), true);
		database.getGraphTable().delete(1);
	}

	@Test
	public void mergeTest() throws DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException, SQLException, IOException, UnexpectedObjectException {
		Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/?user=travis&password=");
		connection.prepareStatement("CREATE DATABASE IF NOT EXISTS library").executeUpdate();
		String url = "jdbc:mysql://127.0.0.1/library";
		String user = "travis";
		String password = "";
		String name = "grape2";

		FileManager fileManager = new FileManager();
		GraphDatabase newDatabase = fileManager.createGraphDatabase(url, user, password, name);

		GraphGenerator gen = new RandomConnectedGraphGenerator(2, 2, 1, 1);
		PropertyGraph<Integer, Integer> graph1 = new PropertyGraph<>();
		gen.generateGraph(graph1, new IntegerVertexFactory(1), null);
		GraphGenerator gen2 = new RandomConnectedGraphGenerator(3, 3, 3, 3);
		PropertyGraph<Integer, Integer> graph2 = new PropertyGraph<>();
		gen2.generateGraph(graph2, new IntegerVertexFactory(1), null);

		graph1.calculateProperties();
		graph2.calculateProperties();

		database.getGraphTable().insert(graph1);
		newDatabase.getGraphTable().insert(graph2);
		assertEquals(database.getGraphTable().getNumberOfRows(), 1);

		database.merge(newDatabase);
		assertEquals(database.getGraphTable().getNumberOfRows(), 2);

		newDatabase.getGraphTable().delete(1);
		database.getGraphTable().delete(1);
		database.getGraphTable().delete(2);

	}

}

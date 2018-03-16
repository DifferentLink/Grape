package edu.kit.ipd.dbis.database.graph;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.UnexpectedObjectException;
import edu.kit.ipd.dbis.database.file.FileManager;
import edu.kit.ipd.dbis.filter.Filtermanagement;
import edu.kit.ipd.dbis.org.jgrapht.additions.generate.RandomConnectedGraphGenerator;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.jgrapht.alg.util.IntegerVertexFactory;
import org.jgrapht.generate.GraphGenerator;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class GraphTableComponentTests {

	private static GraphDatabase database;

	@Before
	public void setUp() throws Exception {
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

		database.getGraphTable().insert(graph);
		assertEquals(database.getGraphTable().getContent(1), graph);
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
		database.getGraphTable().getContent(1);
	}

	@Test (expected = NullPointerException.class)
	public void switchStateTest() throws UnexpectedObjectException, SQLException, IOException, ClassNotFoundException {
		GraphGenerator gen = new RandomConnectedGraphGenerator(2, 2, 1, 1);
		PropertyGraph<Integer, Integer> graph = new PropertyGraph<>();
		gen.generateGraph(graph, new IntegerVertexFactory(1), null);

		database.getGraphTable().insert(graph);
		database.getGraphTable().switchState(1);
		database.getGraphTable().deleteAll();

		database.getGraphTable().getContent(1);
	}

	@Test (expected = NullPointerException.class)
	public void deleteTest() throws SQLException, UnexpectedObjectException, IOException, ClassNotFoundException {
		GraphGenerator gen = new RandomConnectedGraphGenerator(2, 2, 1, 1);
		PropertyGraph<Integer, Integer> graph = new PropertyGraph<>();
		gen.generateGraph(graph, new IntegerVertexFactory(1), null);

		database.getGraphTable().insert(graph);
		database.getGraphTable().delete(1);
		database.getGraphTable().getContent(1);
	}

}

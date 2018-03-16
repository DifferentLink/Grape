package edu.kit.ipd.dbis.database.graph;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.UnexpectedObjectException;
import edu.kit.ipd.dbis.database.file.FileManager;
import edu.kit.ipd.dbis.org.jgrapht.additions.generate.RandomConnectedGraphGenerator;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.jgrapht.alg.util.IntegerVertexFactory;
import org.jgrapht.generate.GraphGenerator;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
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
		database.getGraphTable().deleteAll();

	}

	@Test (expected = UnexpectedObjectException.class)
	public void insertTest1() throws UnexpectedObjectException, SQLException, IOException {
		class SomeObject implements Serializable {

		}
		database.getGraphTable().insert(new SomeObject());

	}

}

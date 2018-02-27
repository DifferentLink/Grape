package edu.kit.ipd.dbis.database.filter;
import static org.junit.Assert.assertEquals;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.connection.tables.FilterTable;
import edu.kit.ipd.dbis.database.connection.tables.GraphTable;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.InsertionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.UnexpectedObjectException;
import edu.kit.ipd.dbis.database.file.FileManager;
import edu.kit.ipd.dbis.filter.BasicFilter;
import edu.kit.ipd.dbis.filter.Filtermanagement;
import edu.kit.ipd.dbis.filter.Relation;
import edu.kit.ipd.dbis.filter.exceptions.InvalidInputException;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FilterTableComponentTests {

	private static GraphDatabase database;

	@Before
	public void setUp() throws Exception {
		Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/?user=travis&password=");
		connection.prepareStatement("CREATE DATABASE IF NOT EXISTS library").executeUpdate();
		String url = "jdbc:mysql://127.0.0.1/library";
		String user = "travis";
		String password = "";
		String name = "grape";

		GraphTable graphs = new GraphTable(url, user, password, name);
		FilterTable filters = new FilterTable(url, user, password, name);
		GraphDatabase graphDatabase = new GraphDatabase(graphs, filters);

		FileManager fileManager = new FileManager();
		fileManager.deleteGraphDatabase(graphDatabase);
		database = fileManager.createGraphDatabase(url, user, password, name);

	}

	@Test
	public void switchStateTest() throws UnexpectedObjectException, IOException, SQLException, ClassNotFoundException {
		String input = "averagedegree = 10";
		boolean isActivated = false;
		double value = 10;
		Relation relation = Relation.EQUAL;
		String property = "averagedegree = 10";
		int id = 1;
		BasicFilter filter = new BasicFilter(input, isActivated, value, relation, property, id);

		database.getFilterTable().insert(filter);
		database.getFilterTable().switchState(1);
		assertEquals(database.getFilterTable().getContent(1).getIsActivated(), true);
		database.getFilterTable().delete(1);

	}

	@Test (expected = NullPointerException.class)
	public void deleteTest() throws IOException, SQLException, UnexpectedObjectException, ClassNotFoundException {
		String input = "greatestdegree = 55";
		boolean isActivated = false;
		double value = 55;
		Relation relation = Relation.EQUAL;
		String property = "greatestdegree = 55";
		int id = 2;
		BasicFilter filter = new BasicFilter(input, isActivated, value, relation, property, id);

		database.getFilterTable().insert(filter);
		assertEquals(database.getFilterTable().getContent(2).getIsActivated(), false);
		database.getFilterTable().delete(2);
		database.getFilterTable().getContent(2).getIsActivated();

	}

	@Test
	public void getColumnsTest() throws SQLException {
		assertEquals(database.getFilterTable().getColumns().contains("id"), true);
		assertEquals(database.getFilterTable().getColumns().contains("filter"), true);
		assertEquals(database.getFilterTable().getColumns().contains("state"), true);
	}

	@Test (expected = UnexpectedObjectException.class)
	public void insertTest() throws IOException, SQLException, UnexpectedObjectException {
		PropertyGraph graph = new PropertyGraph();
		database.getFilterTable().insert(graph);

	}

	@Test
	public void getContentTest() throws IOException, SQLException, UnexpectedObjectException {
		String input = "greatestdegree = 55";
		boolean isActivated = false;
		double value = 55;
		Relation relation = Relation.EQUAL;
		String property = "greatestdegree = 55";
		int id = 2;
		BasicFilter filter1 = new BasicFilter(input, isActivated, value, relation, property, id);

		input = "averagedegree = 10";
		isActivated = false;
		value = 10;
		relation = Relation.EQUAL;
		property = "averagedegree = 10";
		id = 1;
		BasicFilter filter2 = new BasicFilter(input, isActivated, value, relation, property, id);

		database.getFilterTable().insert(filter1);
		database.getFilterTable().insert(filter2);
		assertEquals(database.getFilterTable().getContent().get(0).getName(), "averagedegree = 10");
		assertEquals(database.getFilterTable().getContent().get(1).getName(), "greatestdegree = 55");
		database.getFilterTable().delete(1);
		database.getFilterTable().delete(2);

	}

	@Test
	public void getContentByIdTest() throws IOException, SQLException, UnexpectedObjectException, ClassNotFoundException {
		String input = "largestcliquesize = 0";
		boolean isActivated = false;
		double value = 0;
		Relation relation = Relation.EQUAL;
		String property = "largestcliquesize = 0";
		int id = 1;
		BasicFilter filter = new BasicFilter(input, isActivated, value, relation, property, id);

		database.getFilterTable().insert(filter);
		assertEquals(database.getFilterTable().getContent(1), "largestcliquesize = 0");
		database.getFilterTable().delete(1);

	}

}

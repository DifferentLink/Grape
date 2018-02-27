package edu.kit.ipd.dbis.database.filter;
import static org.junit.Assert.assertEquals;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.InsertionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.UnexpectedObjectException;
import edu.kit.ipd.dbis.database.file.FileManager;
import edu.kit.ipd.dbis.filter.BasicFilter;
import edu.kit.ipd.dbis.filter.Filtermanagement;
import edu.kit.ipd.dbis.filter.Relation;
import edu.kit.ipd.dbis.filter.exceptions.InvalidInputException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
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

		FileManager fileManager = new FileManager();
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
		double value = 10;
		Relation relation = Relation.EQUAL;
		String property = "greatestdegree = 55";
		int id = 2;
		BasicFilter filter = new BasicFilter(input, isActivated, value, relation, property, id);

		database.getFilterTable().insert(filter);
		assertEquals(database.getFilterTable().getContent(2).getIsActivated(), false);
		database.getFilterTable().delete(2);
		database.getFilterTable().getContent(2).getIsActivated();

	}

}

package edu.kit.ipd.dbis.database.graph;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.file.FileManager;
import org.junit.Before;

import java.sql.Connection;
import java.sql.DriverManager;

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

}

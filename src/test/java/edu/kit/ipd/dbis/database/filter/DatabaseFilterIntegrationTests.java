package edu.kit.ipd.dbis.database.filter;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.connection.tables.FilterTable;
import edu.kit.ipd.dbis.database.connection.tables.GraphTable;
import edu.kit.ipd.dbis.database.file.FileManager;
import edu.kit.ipd.dbis.filter.Filtermanagement;
import edu.kit.ipd.dbis.filter.exceptions.InvalidInputException;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class DatabaseFilterIntegrationTests {

	private static GraphDatabase database;
	private static Filtermanagement manager;

	@Ignore
	@Before
	public void delete() throws Exception {
		String url = "jdbc:mysql://localhost:3306/library";
		String username = "user";
		String password = "password";
		GraphTable graphs = new GraphTable(url, username, password, "grape2");
		FilterTable filter = new FilterTable(url, username, password, "grape2filters");
		database = new GraphDatabase(graphs, filter);
		FileManager files = new FileManager();
		files.deleteGraphDatabase(database);

		manager = new Filtermanagement();
		graphs = new GraphTable(url, username, password, "grape2");
		filter = new FilterTable(url, username, password, "grape2filters");
		database = new GraphDatabase(graphs, filter);
		manager.setDatabase(database);
	}

	@Ignore
	@Test
	public void addConnectedFilterToDatabase() throws Exception, InvalidInputException {
		manager.addFilter("AverageDegree + 27 = AverageDegree / 66", 1);
		assertEquals(true,
				database.getFilterById(1).getName().equals("AverageDegree + 27 = AverageDegree / 66"));
	}

}

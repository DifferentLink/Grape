package database;

import edu.kit.ipd.dbis.filter.*;
import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.connection.tables.FilterTable;
import edu.kit.ipd.dbis.database.connection.tables.GraphTable;
import edu.kit.ipd.dbis.database.exceptions.sql.*;
import edu.kit.ipd.dbis.database.file.FileManager;
import edu.kit.ipd.dbis.gui.filter.FilterGroup;
import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FilterDatabaseIntegrationTest {

	@Before
	public void delete() throws Exception {
		String url = "jdbc:mysql://localhost:3306/library";
		String username = "user";
		String password = "password";

		GraphTable graphs = new GraphTable(url, username, password, "grape2");
		FilterTable filter = new FilterTable(url, username, password, "grape2filters");
		GraphDatabase database = new GraphDatabase(graphs, filter);
		FileManager files = new FileManager();
		files.deleteGraphDatabase(database);
	}

	@Test
	public void addConnectedFilterToDatabase() throws Exception, InvalidInputException {
		String url = "jdbc:mysql://localhost:3306/library";
		String username = "user";
		String password = "password";

		Filtermanagement manager = new Filtermanagement();
		GraphTable graphs = new GraphTable(url, username, password, "grape2");
		FilterTable filter = new FilterTable(url, username, password, "grape2filters");
		GraphDatabase database = new GraphDatabase(graphs, filter);
		manager.setDatabase(database);

		manager.addFilter("AverageDegree + 27 = AverageDegree / 66", 1);
		assertEquals(database.getFilterById(1).getName(), "AverageDegree + 27 = AverageDegree / 66");
	}

	@Test
	public void removeFilterfromDatabase() throws Exception, InvalidInputException {
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/library";
		String username = "user";
		String password = "password";

		Filtermanagement manager = new Filtermanagement();
		GraphTable graphs = new GraphTable(url, username, password, "grape2");
		FilterTable filter = new FilterTable(url, username, password, "grape2filters");
		GraphDatabase database = new GraphDatabase(graphs, filter);
		manager.setDatabase(database);

		manager.addFilter("AverageDegree = 10", 2);
		assertEquals(database.getFilterById(2).getName(), "AverageDegree = 10");

		manager.removeFiltersegment(2);
		boolean b = false;
		try {
			database.getFilterById(2).getName();
		} catch (UnexpectedObjectException e) {
			b = true;
		}
		assertEquals(true, b);

	}

	@Test
	public void removeFiltergroupfromDatabase() throws Exception {
		String url = "jdbc:mysql://localhost:3306/library";
		String username = "user";
		String password = "password";

		Filtermanagement manager = new Filtermanagement();
		GraphTable graphs = new GraphTable(url, username, password, "grape2");
		FilterTable filter = new FilterTable(url, username, password, "grape2filters");
		GraphDatabase database = new GraphDatabase(graphs, filter);
		manager.setDatabase(database);

		manager.addFiltergroup("Beispielgruppe", 3);
		assertEquals(database.getFilterById(3).getName(), "Beispielgruppe");

		manager.removeFiltersegment(3);

		boolean b = false;
		try {
			database.getFilterById(3).getName();
		} catch (UnexpectedObjectException e) {
			b = true;
		}
		assertEquals(true, b);
	}

	@Test
	public void addFilterToFiltergroupInDatabase() throws Exception, InvalidInputException {
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/library";
		String username = "user";
		String password = "password";

		Filtermanagement manager = new Filtermanagement();
		GraphTable graphs = new GraphTable(url, username, password, "grape2");
		FilterTable filter = new FilterTable(url, username, password, "grape2filters");
		GraphDatabase database = new GraphDatabase(graphs, filter);
		manager.setDatabase(database);

		manager.addFiltergroup("EineGruppe", 4);
		assertEquals(database.getFilterById(4).getName(), "EineGruppe");

		manager.addFilterToGroup("AverageDegree = 10", 42, 4);
		Filtergroup group = (Filtergroup) database.getFilterById(4);
		List<Filter> filters = group.getAvailableFilter();
		boolean found = false;
		for (Filter f : filters) {
			if (f.getID() == 42) {
				found = true;
				assertEquals(f.getName(), "AverageDegree = 10");
			}
		}
		assertEquals(found, true);
	}

	@Test
	public void removeFilterFromFiltergroupInDatabase() throws Exception, InvalidInputException {
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/library";
		String username = "user";
		String password = "password";

		Filtermanagement manager = new Filtermanagement();
		GraphTable graphs = new GraphTable(url, username, password, "grape2");
		FilterTable filter = new FilterTable(url, username, password, "grape2filters");
		GraphDatabase database = new GraphDatabase(graphs, filter);
		manager.setDatabase(database);

		manager.addFiltergroup("asdf", 5);
		assertEquals(database.getFilterById(5).getName(), "asdf");

		manager.addFilterToGroup("AverageDegree = 83", 66, 5);
		Filtergroup group = (Filtergroup) database.getFilterById(5);
		List<Filter> filters = group.getAvailableFilter();
		boolean found = false;
		for (Filter f : filters) {
			if (f.getID() == 66) {
				found = true;
				assertEquals(f.getName(), "AverageDegree = 83");
			}
		}
		assertEquals(found, true);

		manager.removeFiltersegment(66);
		group = (Filtergroup) database.getFilterById(5);
		filters = group.getAvailableFilter();
		found = false;
		for (Filter f : filters) {
			if (f.getID() == 66) {
				found = true;
			}
		}
		assertEquals(false, found);
	}

	@Test
	public void testSetDatabaseMethod() throws  Exception, InvalidInputException {
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/library";
		String username = "user";
		String password = "password";

		Filtermanagement manager = new Filtermanagement();
		GraphTable graphs = new GraphTable(url, username, password, "grape2");
		FilterTable filter = new FilterTable(url, username, password, "grape2filters");
		GraphDatabase database = new GraphDatabase(graphs, filter);
		manager.setDatabase(database);

		manager.addFiltergroup("jklö", 6);
		assertEquals(database.getFilterById(6).getName(), "jklö");
		manager.addFilterToGroup("AverageDegree = 0", 31, 6);

		Filtergroup group = (Filtergroup) database.getFilterById(6);
		List<Filter> filters = group.getAvailableFilter();
		boolean found = false;
		for (Filter f : filters) {
			if (f.getID() == 31) {
				found = true;
				assertEquals(f.getName(), "AverageDegree = 0");
			}
		}
		assertEquals(found, true);


		manager.removeFiltersegment(31);
		group = (Filtergroup) database.getFilterById(6);
		filters = group.getAvailableFilter();
		found = false;
		for (Filter f : filters) {
			if (f.getID() == 31) {
				found = true;
			}
		}
		assertEquals(false, found);

		GraphTable graphs2 = new GraphTable(url, username, password, "grape2Modified");
		FilterTable filter2 = new FilterTable(url, username, password, "grape2filtersModified");
		GraphDatabase database2 = new GraphDatabase(graphs, filter);
		manager.setDatabase(database2);

		manager.setDatabase(database);

	}
}

package edu.kit.ipd.dbis.database.filter;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.connection.tables.FilterTable;
import edu.kit.ipd.dbis.database.connection.tables.GraphTable;
import edu.kit.ipd.dbis.database.exceptions.sql.UnexpectedObjectException;
import edu.kit.ipd.dbis.database.file.FileManager;
import edu.kit.ipd.dbis.filter.Filter;
import edu.kit.ipd.dbis.filter.Filtergroup;
import edu.kit.ipd.dbis.filter.Filtermanagement;
import edu.kit.ipd.dbis.filter.exceptions.InvalidInputException;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

public class DatabaseFilterIntegrationTests {

	/*
	@Ignore
	@Before
	public void delete() throws Exception {
		String url = "jdbc:mysql://127.0.0.1/library";
		String username = "user";
		String password = "password";
		GraphTable graphs = new GraphTable(url, username, password, "grape2");
		FilterTable filter = new FilterTable(url, username, password, "grape2filters");
		GraphDatabase database = new GraphDatabase(graphs, filter);
		FileManager files = new FileManager();
		files.deleteGraphDatabase(database);

	}*/

	//@Ignore
	@Test
	public void addConnectedFilterToDatabase() throws Exception, InvalidInputException {
		Connection conn = DriverManager.getConnection
				("jdbc:mysql://127.0.0.1/?user=travis&password=");
		Statement s = conn.createStatement();
		int Result = s.executeUpdate("CREATE DATABASE library");
		String url = "jdbc:mysql://127.0.0.1/library";
		String username = "travis";
		String password = "";
		Filtermanagement manager = new Filtermanagement();
		GraphTable graphs = new GraphTable(url, username, password, "grape2");
		FilterTable filter = new FilterTable(url, username, password, "grape2filters");
		GraphDatabase database = new GraphDatabase(graphs, filter);
		manager.setDatabase(database);
		manager.updateFilter("AverageDegree + 27 = AverageDegree / 66", 1);
		assertEquals(true,
				database.getFilterById(1).getName().equals("AverageDegree + 27 = AverageDegree / 6"));
	}

	@Ignore
	@Test (expected = NullPointerException.class)
	public void removeFilterfromDatabase() throws Exception, InvalidInputException {
		String url = "jdbc:mysql://127.0.0.1/library";
		String username = "user";
		String password = "password";
		Filtermanagement manager = new Filtermanagement();
		GraphTable graphs = new GraphTable(url, username, password, "grape2");
		FilterTable filter = new FilterTable(url, username, password, "grape2filters");
		GraphDatabase database = new GraphDatabase(graphs, filter);
		manager.setDatabase(database);

		manager.updateFilter("AverageDegree = 10", 2);
		assertEquals(database.getFilterById(2).getName().equals("AverageDegree = 10"), true);
		manager.removeFiltersegment(2);
		database.getFilterById(2).getName();

	}

	@Ignore
	@Test (expected = NullPointerException.class)
	public void removeFiltergroupfromDatabase() throws Exception, InvalidInputException {
		String url = "jdbc:mysql://localhost:3306/library";
		String username = "user";
		String password = "password";
		Filtermanagement manager = new Filtermanagement();
		GraphTable graphs = new GraphTable(url, username, password, "grape2");
		FilterTable filter = new FilterTable(url, username, password, "grape2filters");
		GraphDatabase database = new GraphDatabase(graphs, filter);
		manager.setDatabase(database);

		manager.updateFiltergroup("Beispielgruppe", 3);
		manager.activate(3);
		assertEquals(database.getFilterById(3).getName().equals("Beispielgruppe"), true);

		manager.removeFiltersegment(3);
		database.getFilterById(3).getName();

	}

	@Ignore
	@Test
	public void removeFilterFromFiltergroupInDatabase() throws Exception, InvalidInputException {
		String url = "jdbc:mysql://localhost:3306/library";
		String username = "user";
		String password = "password";
		Filtermanagement manager = new Filtermanagement();
		GraphTable graphs = new GraphTable(url, username, password, "grape2");
		FilterTable filter = new FilterTable(url, username, password, "grape2filters");
		GraphDatabase database = new GraphDatabase(graphs, filter);
		manager.setDatabase(database);

		manager.updateFiltergroup("NeueGruppe", 5);
		manager.activate(5);
		assertEquals(database.getFilterById(5).getName().equals("NeueGruppe"), true);

		manager.updateFilter("NumberOfEdges = 60", 60, 5);
		assertEquals(database.getFilterById(5) instanceof Filtergroup, true);

		if (database.getFilterById(5) instanceof Filtergroup) {
			Filtergroup group = (Filtergroup) database.getFilterById(5);
			List<Filter> filters = group.getAvailableFilter();
			boolean found = false;
			for (Filter f : filters) {
				if (f.getID() == 60) {
					found = true;
					assertEquals(f.getName().equals("NumberOfEdges = 60"), true);
				}
			}
			assertEquals(found, true);
		}

		manager.removeFiltersegment(60);
		assertEquals(database.getFilterById(5) instanceof Filtergroup, true);
		if (database.getFilterById(5) instanceof Filtergroup) {
			Filtergroup group = (Filtergroup) database.getFilterById(5);
			List<Filter> filters = group.getAvailableFilter();
			for (Filter f : filters) {
				assertEquals((f.getID() == 60), false);
			}
		}

	}

	@Ignore
	@Test
	public void testSetDatabaseMethod() throws  Exception, InvalidInputException {
		String url = "jdbc:mysql://localhost:3306/library";
		String username = "user";
		String password = "password";
		Filtermanagement manager = new Filtermanagement();
		GraphTable graphs = new GraphTable(url, username, password, "grape2");
		FilterTable filter = new FilterTable(url, username, password, "grape2filters");
		GraphDatabase database = new GraphDatabase(graphs, filter);
		manager.setDatabase(database);

		manager.updateFiltergroup("DiesIstEineGruppe", 6);
		manager.activate(6);
		assertEquals(database.getFilterById(6).getName().equals("DiesIstEineGruppe"),true);

		manager.updateFilter("TotalColoringNumberOfColors = 0", 31, 6);
		assertEquals((database.getFilterById(6) instanceof Filtergroup), true);

		if (database.getFilterById(6) instanceof Filtergroup) {
			Filtergroup group = (Filtergroup) database.getFilterById(6);
			List<Filter> filters = group.getAvailableFilter();
			boolean found = false;
			for (Filter f : filters) {
				if (f.getID() == 31) {
					found = true;
					assertEquals(f.getName().equals("TotalColoringNumberOfColors = 0"), true);
				}
			}
			assertEquals(found, true);
		}

		manager.removeFiltersegment(31);
		assertEquals((database.getFilterById(6) instanceof Filtergroup), true);
		if (database.getFilterById(6) instanceof Filtergroup) {
			Filtergroup group = (Filtergroup) database.getFilterById(6);
			List<Filter> filters = group.getAvailableFilter();
			for (Filter f : filters) {
				assertEquals((f.getID() == 31), false);
			}
		}

		GraphTable graphs2 = new GraphTable(url, username, password, "grape2Modified");
		FilterTable filter2 = new FilterTable(url, username, password, "grape2filtersModified");
		GraphDatabase database2 = new GraphDatabase(graphs2, filter2);
		manager.setDatabase(database2);
		assertEquals(manager.getDatabase().getGraphTable().getName()
				+ manager.getDatabase().getFilterTable().getName(), "grape2Modifiedgrape2filtersModified");

	}

}

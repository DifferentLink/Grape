package edu.kit.ipd.dbis.database.filter;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.connection.tables.FilterTable;
import edu.kit.ipd.dbis.database.connection.tables.GraphTable;
import edu.kit.ipd.dbis.database.file.FileManager;
import edu.kit.ipd.dbis.filter.Filter;
import edu.kit.ipd.dbis.filter.Filtergroup;
import edu.kit.ipd.dbis.filter.Filtermanagement;
import edu.kit.ipd.dbis.filter.exceptions.InvalidInputException;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class DatabaseFilterIntegrationTests {

	private static GraphDatabase database;
	private static GraphDatabase newDatabase;
	private static Filtermanagement manager;

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

			name = "grape2modified";
			GraphTable graphTable = new GraphTable(url, user, password, name);
			name = "grape2modifiedFilters";
			FilterTable filterTable = new FilterTable(url, user,password, name);
			newDatabase = new GraphDatabase(graphTable, filterTable);


		} catch (Exception e){
			Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/?user=travis&password=");
			connection.prepareStatement("CREATE DATABASE IF NOT EXISTS library").executeUpdate();
			String url = "jdbc:mysql://127.0.0.1/library";
			String user = "travis";
			String password = "";
			String name = "databasefilterintegrationtests";

			FileManager fileManager = new FileManager();
			database = fileManager.createGraphDatabase(url, user, password, name);

			name = "grape2modified";
			FileManager fileManager2 = new FileManager();
			newDatabase = fileManager2.createGraphDatabase(url, user, password, name);
		}
		manager = new Filtermanagement();
		manager.setDatabase(database);

	}

	@Test
	public void addConnectedFilterToDatabase() throws Exception, InvalidInputException {

		manager.updateFilter("AverageDegree + 27 = AverageDegree / 66", 1);
		assertEquals(true,
				database.getFilterById(1).getName().equals("AverageDegree + 27 = AverageDegree / 66"));

	}

	@Test (expected = NullPointerException.class)
	public void removeFilterfromDatabase() throws Exception, InvalidInputException {

		manager.updateFilter("AverageDegree = 10", 2);
		assertEquals(database.getFilterById(2).getName().equals("AverageDegree = 10"), true);

		manager.removeFiltersegment(2);
		database.getFilterById(2).getName();

	}

	@Test (expected = NullPointerException.class)
	public void removeFiltergroupfromDatabase() throws Exception {

		manager.updateFiltergroup("Beispielgruppe", 3);
		manager.activate(3);
		assertEquals(database.getFilterById(3).getName().equals("Beispielgruppe"), true);

		manager.removeFiltersegment(3);
		database.getFilterById(3).getName();

	}

	@Test
	public void removeFilterFromFiltergroupInDatabase() throws Exception, InvalidInputException {

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

	@Test
	public void testSetDatabaseMethod() throws  Exception, InvalidInputException {
		String url = "jdbc:mysql://localhost:3306/library";
		String username = "travis";
		String password = "";

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

		manager.setDatabase(newDatabase);
		assertEquals(manager.getDatabase().getGraphTable().getName()
				+ manager.getDatabase().getFilterTable().getName(), "grape2modifiedgrape2modifiedFilters");

	}

}

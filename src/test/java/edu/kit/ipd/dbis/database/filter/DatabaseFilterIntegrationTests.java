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

import java.util.List;

public class DatabaseFilterIntegrationTests {

	private static GraphDatabase database;
	private static Filtermanagement manager;

	//@Ignore
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

	//@Ignore
	@Test
	public void addConnectedFilterToDatabase() throws Exception, InvalidInputException {
		manager.addFilter("AverageDegree + 27 = AverageDegree / 66", 1);
		assertEquals(true,
				database.getFilterById(1).getName().equals("AverageDegree + 27 = AverageDegree / 66"));
	}

	//@Ignore
	@Test (expected = NullPointerException.class)
	public void removeFilterfromDatabase() throws Exception, InvalidInputException {
		manager.addFilter("AverageDegree = 10", 2);
		System.out.println(database.getFilterById(2).getName().equals("AverageDegree = 10"));
		manager.removeFiltersegment(2);
		database.getFilterById(2).getName();

	}

	//@Ignore
	@Test (expected = NullPointerException.class)
	public void removeFiltergroupfromDatabase() throws Exception, InvalidInputException {
		Filtergroup filtergroup = new Filtergroup("Beispielgruppe", true, 3);
		manager.addFilterGroup(filtergroup);
		assertEquals(database.getFilterById(3).getName().equals("Beispielgruppe"), true);

		manager.removeFiltersegment(3);
		database.getFilterById(3).getName();

	}

	//@Ignore
	@Test
	public void removeFilterFromFiltergroupInDatabase() throws Exception, InvalidInputException {
		Filtergroup filtergroup = new Filtergroup("NeueGruppe", true, 5);
		manager.addFilterGroup(filtergroup);
		assertEquals(database.getFilterById(5).getName().equals("NeueGruppe"), true);

		manager.addFilterToGroup("NumberOfEdges = 60", 60, 5);
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

}

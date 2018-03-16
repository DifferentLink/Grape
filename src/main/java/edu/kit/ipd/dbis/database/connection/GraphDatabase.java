package edu.kit.ipd.dbis.database.connection;

import edu.kit.ipd.dbis.database.connection.tables.FilterTable;
import edu.kit.ipd.dbis.database.connection.tables.GraphTable;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.InsertionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.UnexpectedObjectException;
import edu.kit.ipd.dbis.filter.Filtersegment;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.LinkedList;

/**
 * This class represents a Graphdatabase that contains graphs and filters.
 */
public class GraphDatabase {

	private String directory;
	private GraphTable graphTable;
	private FilterTable filterTable;

	/**
	 * Creates a new GraphDatabase by setting its GraphTable and FilterTable
	 * @param graphTable a GraphTable
	 * @param filterTable a FilterTable
	 */
	public GraphDatabase(GraphTable graphTable, FilterTable filterTable) {
		this.graphTable = graphTable;
		this.filterTable = filterTable;
		this.directory = null;
	}

	/**
	 * getter-method
	 * @return the GraphTable-object
	 */
	public GraphTable getGraphTable() {
		return this.graphTable;
	}

	/**
	 * getter-method
	 * @return the FilterTable-object
	 */
	public FilterTable getFilterTable() {
		return this.filterTable;
	}

	/**
	 * getter-method
	 * @return the directory
	 */
	public String getDirectory() {
		return this.directory;
	}

	/**
	 * setter-method
	 * @param directory the directory
	 */
	public void setDirectory(String directory) {
		this.directory = directory;
	}

	/**
	 * Inserts graph into the MySQL-Table that belongs to the current MySQLDatabase,
	 * sets its automatically generated id and determines whether it should be marked as calculated or not.
	 * @param graph object that should be inserted into the current MySQLDatabase.
	 * @throws ConnectionFailedException if a database connection could not be established
	 * @throws InsertionFailedException if the PropertyGraph-object could not be inserted to the database
	 * @throws UnexpectedObjectException if the PropertyGraph-object is not as expected
	 */
	public void addGraph(PropertyGraph<Integer, Integer> graph)
			throws ConnectionFailedException, InsertionFailedException, UnexpectedObjectException {
		try {
			this.graphTable.insert(graph);
		} catch (SQLSyntaxErrorException e) {
			throw new ConnectionFailedException("Selected table does not exist " +
					"or does not contain the required columns to store graph" + graph.getId() + ".");
		} catch (SQLException e) {
			throw new ConnectionFailedException(e.getMessage());
		} catch (IOException e) {
			throw new InsertionFailedException("Graph " + graph.getId() + " could not be saved.");
		}
	}

	/**
	 * Inserts filter into the MySQL-Table belonging to the current MySQLDatabase.
	 * @param filter object that should be inserted into the current MySQLDatabase.
	 * @throws ConnectionFailedException if a database connection could not be established
	 * @throws InsertionFailedException if the Filtersegment-object could not be inserted to the database
	 * @throws UnexpectedObjectException if the Filtersegment-object is not as expected
	 */
	public void addFilter(Filtersegment filter)
			throws ConnectionFailedException, InsertionFailedException, UnexpectedObjectException {
		try {
			this.filterTable.insert(filter);
		} catch (SQLSyntaxErrorException e) {
			throw new ConnectionFailedException("Selected table does not exist " +
					"or does not contain the required columns to store filter " + filter.getID() + ".");
		} catch (SQLException e) {
			throw new ConnectionFailedException(e.getMessage());
		} catch (IOException e) {
			throw new InsertionFailedException("Filter " + filter.getID() + " could not be saved.");
		}
	}

	/**
	 * PropertyGraph-Object with the given id will be marked as deleted.
	 * @param id identifies a PropertyGraph-Object in the current MySQL-Database.
	 * @throws ConnectionFailedException if a database connection could not be established
	 */
	public void deleteGraph(int id) throws ConnectionFailedException {
		try {
			this.graphTable.switchState(id);
		} catch (SQLSyntaxErrorException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new ConnectionFailedException(e.getMessage());
		}
	}

	/**
	 * FilterSegment-Object with the given id will be deleted.
	 * @param id identifies a FilterSegment-Object in the current MySQL-Database.
	 * @throws ConnectionFailedException if a database connection could not be established
	 */
	public void deleteFilter(int id) throws ConnectionFailedException {
		try {
			this.filterTable.delete(id);
		} catch (SQLSyntaxErrorException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new ConnectionFailedException(e.getMessage());
		}
	}

	/**
	 * PropertyGraph-Object with the given id will be restored (unmarked as deleted).
	 * @param id identifies PropertyGraph-Object in the current MySQL-Database.
	 * @throws ConnectionFailedException if a database connection could not be established
	 */
	public void restoreGraph(int id) throws ConnectionFailedException {

		try {
			this.graphTable.switchState(id);
		} catch (SQLSyntaxErrorException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new ConnectionFailedException(e.getMessage());
		}
	}

	/**
	 * The state (determines whether a FilterSegment-Object is activated or not)
	 * of the given FilterSegment-Object will be changed.
	 * @param id identifies a FilterSegment-Object.
	 * @throws ConnectionFailedException if a database connection could not be established
	 */
	public void changeStateOfFilter(int id) throws ConnectionFailedException {
		try {
			this.filterTable.switchState(id);
		} catch (SQLSyntaxErrorException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new ConnectionFailedException(e.getMessage());
		}
	}

	/**
	 * Every PropertyGraph-Object that is marked as deleted will be removed irreversibly
	 * from the current MySQL-Database.
	 * @throws ConnectionFailedException if a database connection could not be established
	 */
	public void permanentlyDeleteGraphs() throws ConnectionFailedException {
		try {
			this.graphTable.deleteAll();
		} catch (SQLSyntaxErrorException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new ConnectionFailedException();
		}
	}

	/**
	 * The PropertyGraph-Object with the given id will be
	 * removed irreversibly from the current MySQL-Table.
	 * @param id id of the PropertyGraph-Object that should be removed
	 * @throws ConnectionFailedException if a database connection could not be established
	 */
	public void permanentlyDeleteGraph(int id) throws ConnectionFailedException {
		try {
			this.graphTable.delete(id);
		} catch (SQLSyntaxErrorException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new ConnectionFailedException(e.getMessage());
		}
	}

	/**
	 * replaces the PropertyGraph-Object identified by the given id. Additionally
	 * it will be checked whether the new graph is already calculated or not.
	 * @param id identifies a PropertyGraph-Object that will be replaced.
	 * @param graph new object that should replace the old one.
	 * @throws ConnectionFailedException if a database connection could not be established
	 * @throws InsertionFailedException if the PropertyGraph-object could not be inserted to the database
	 * @throws UnexpectedObjectException if the PropertyGraph-object is not as expected
	 */
	public void replaceGraph(int id, PropertyGraph<Integer, Integer> graph)
			throws ConnectionFailedException, InsertionFailedException, UnexpectedObjectException {
		this.permanentlyDeleteGraph(id);
		graph.setId(id);
		this.addGraph(graph);
	}

	/**
	 * replaces the FilterSegment-Object identified by the given id.
	 * @param id identifies a FilterSegment-Object that will be replaced.
	 * @param filter new object that should replace the old one.
	 * @throws ConnectionFailedException if a database connection could not be established
	 * @throws InsertionFailedException if the Filtersegment-object could not be inserted to the database
	 * @throws UnexpectedObjectException if the Filtersegment-object is not as expected
	 */
	public void replaceFilter(int id, Filtersegment filter)
			throws ConnectionFailedException, UnexpectedObjectException, InsertionFailedException {
		this.deleteFilter(id);
		this.addFilter(filter);
	}

	/**
	 * Every PropertyGraph-Object in databese that does not already exist in the
	 * current MySQL-Database, will be inserted to the current MySQL-Database.
	 * @param database GraphDatabase-Object that should be merged with the current MySQL-Database.
	 * @throws ConnectionFailedException if a database connection could not be established
	 */
	public void merge(GraphDatabase database) throws ConnectionFailedException {
		try {
			this.graphTable.merge(database.getGraphTable());
		} catch (SQLSyntaxErrorException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new ConnectionFailedException(e.getMessage());
		}
	}

	/**
	 * Checks whether a propertyGraph-Object already exists.
	 * @param graph a PropertyGraph-Object
	 * @return true if the given graph is isomorphic to another PropertyGraphObject in the current MySQL-Database.
	 * @throws ConnectionFailedException if a database connection could not be established
	 */
	public boolean graphExists(PropertyGraph<Integer, Integer> graph) throws ConnectionFailedException {
		try {
			return this.graphTable.graphExists(graph);
		} catch (SQLSyntaxErrorException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new ConnectionFailedException(e.getMessage());
		}
	}

	/**
	 * Returns all FilterSegment-Objects.
	 * @return all FilterSegment-Objects in the current MySQL-Database.
	 * @throws ConnectionFailedException if a database connection could not be established
	 */
	public LinkedList<Filtersegment> getFilters() throws ConnectionFailedException {
		try {
			return this.filterTable.getContent();
		} catch (SQLSyntaxErrorException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new ConnectionFailedException(e.getMessage());
		}
	}

	/**
	 * Identifies a FilterSegment-Object and returns it.
	 * @param id identifies a FilterSegment-Object.
	 * @return identified FilterSegment-Object in the MySQL-Database.
	 * @throws UnexpectedObjectException if the object with the given id is not a Filtersegment-object
	 * @throws ConnectionFailedException if a database connection could not be established
	 */
	public Filtersegment getFilterById(int id) throws UnexpectedObjectException, ConnectionFailedException {
		try {
			return this.filterTable.getContent(id);
		} catch (SQLSyntaxErrorException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new ConnectionFailedException(e.getMessage());
		} catch (ClassNotFoundException | IOException e) {
			throw new UnexpectedObjectException("The Object identified by id " + id + " is not a Filter-object.");
		}
	}

	/**
	 * Returns all PropertyGraph-Objects that fulfill the current filters.
	 * @param filters determines how the GraphTable should be filtered
	 * @param column determines how the GraphTable should be sorted
	 * @param ascending determines if the GraphTable should be sorted ascending or descending
	 * @return all PropertyGraph-Objects in the MySQL-Database that fulfill the current filters.
	 * @throws ConnectionFailedException if a database connection could not be established
	 */
	public ResultSet getGraphs(String[][] filters, String column, boolean ascending) throws ConnectionFailedException {
		try {
			return this.graphTable.getContent(filters, column, ascending);
		} catch (SQLSyntaxErrorException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new ConnectionFailedException(e.getMessage());
		}
	}

	/**
	 * Identifies a PropertyGraph-Object and returns it.
	 * @param id identifies a PropertyGraph-Object.
	 * @return identified PropertyGraph-Object in the MySQL-Database.
	 * @throws ConnectionFailedException if a database connection could not be established
	 * @throws UnexpectedObjectException if the object with the given id is not a PropertyGraph-object
	 */
	public PropertyGraph<Integer, Integer> getGraphById(int id)
			throws ConnectionFailedException, UnexpectedObjectException {
		try {
			return this.graphTable.getContent(id);
		} catch (SQLSyntaxErrorException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new ConnectionFailedException(e.getMessage());
		} catch (ClassNotFoundException | IOException e) {
			throw new UnexpectedObjectException("The Object identified by id " + id + " is not a PropertyGraph-object.");
		}
	}

	/**
	 * @return a PropertyGraph-Object in the MySQL-Database that is marked as uncalculated.
	 * @throws UnexpectedObjectException if the object with the given id is not a PropertyGraph-object
	 * @throws ConnectionFailedException if a database connection could not be established
	 */
	public PropertyGraph<Integer, Integer> getUncalculatedGraph()
			throws ConnectionFailedException, UnexpectedObjectException {
		try {
			return this.graphTable.getUncalculatedGraph();
		} catch (SQLSyntaxErrorException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new ConnectionFailedException(e.getMessage());
		} catch (ClassNotFoundException | IOException e) {
			throw new UnexpectedObjectException("The given Object is not a PropertyGraph-object.");
		}
	}

	/**
	 * Determines if the current database contains any PropertyGraph-objects that are marked as uncalculated
	 * @return true if there are uncalculated graphs
	 * @throws ConnectionFailedException if a database connection could not be established
	 */
	public boolean hasUncalculatedGraphs() throws ConnectionFailedException {
		try {
			return this.graphTable.hasUncalculated();
		} catch (SQLSyntaxErrorException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new ConnectionFailedException(e.getMessage());
		}
	}

	/**
	 * Returns the values of a certain column of every graph that matches the filter criteria
	 * @param filters determines how the database should be filters
	 * @param column the column
	 * @return the value of every given column
	 * @throws ConnectionFailedException if a database connection could not be established
	 */
	public LinkedList<Double> getValues(String[][] filters, String column) throws ConnectionFailedException {
		try {
			return this.graphTable.getValues(filters, column);
		} catch (SQLSyntaxErrorException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new ConnectionFailedException(e.getMessage());
		}
	}

	/**
	 * returns the total number of PropertyGraph-objects in this database
	 * @return the number of graphs
	 * @throws ConnectionFailedException if a database connection could not be established
	 */
	public int getNumberOfGraphs() throws ConnectionFailedException {
		try {
			return this.graphTable.getNumberOfRows();
		} catch (SQLException e) {
			throw new ConnectionFailedException();
		}
	}

	/**
	 * Returns number of uncalculated graphs
	 * @return number of uncalculated graphs
	 * @throws ConnectionFailedException if a database connection could not be established
	 */
	public int getNumberOfUncalculatedGraphs() throws ConnectionFailedException {
		try {
			return this.graphTable.numberOfUncalculatedGraphs();
		} catch (SQLException e) {
			throw new ConnectionFailedException();
		}
	}

}

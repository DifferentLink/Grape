package edu.kit.ipd.dbis.database.connection;

import edu.kit.ipd.dbis.database.exceptions.sql.*;
import edu.kit.ipd.dbis.filter.Filtersegment;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public interface DatabaseManager {

	/**
	 * Inserts graph into the MySQL-Table that belongs to the current MySQLDatabase,
	 * sets its automatically generated id and determines whether it should be marked as calculated or not.
	 * @param graph object that should be inserted into the current MySQLDatabase.
	 * @throws ConnectionFailedException if a database connection could not be established
	 * @throws InsertionFailedException if the PropertyGraph-object could not be inserted to the database
	 * @throws UnexpectedObjectException if the PropertyGraph-object is not as expected
	 */
	void addGraph(PropertyGraph<Integer, Integer> graph)
			throws ConnectionFailedException, InsertionFailedException, UnexpectedObjectException;

	/**
	 * Inserts filter into the MySQL-Table belonging to the current MySQLDatabase.
	 * @param filter object that should be inserted into the current MySQLDatabase.
	 * @throws ConnectionFailedException if a database connection could not be established
	 * @throws InsertionFailedException if the Filtersegment-object could not be inserted to the database
	 * @throws UnexpectedObjectException if the Filtersegment-object is not as expected
	 */
	void addFilter(Filtersegment filter)
			throws ConnectionFailedException, InsertionFailedException, UnexpectedObjectException;

	/**
	 * PropertyGraph-Object with the given id will be marked as deleted.
	 * @param id identifies a PropertyGraph-Object in the current MySQL-Database.
	 * @throws ConnectionFailedException if a database connection could not be established
	 */
	void deleteGraph(int id) throws ConnectionFailedException;

	/**
	 * FilterSegment-Object with the given id will be deleted.
	 * @param id identifies a FilterSegment-Object in the current MySQL-Database.
	 * @throws ConnectionFailedException if a database connection could not be established
	 */
	void deleteFilter(int id) throws ConnectionFailedException;

	/**
	 * PropertyGraph-Object with the given id will be restored (unmarked as deleted).
	 * @param id identifies PropertyGraph-Object in the current MySQL-Database.
	 * @throws ConnectionFailedException if a database connection could not be established
	 */
	void restoreGraph(int id) throws ConnectionFailedException;

	/**
	 * The state (determines whether a FilterSegment-Object is activated or not)
	 * of the given FilterSegment-Object will be changed.
	 * @param id identifies a FilterSegment-Object.
	 * @throws ConnectionFailedException if a database connection could not be established
	 */
	void changeStateOfFilter(int id) throws ConnectionFailedException;

	/**
	 * Every PropertyGraph-Object that is marked as deleted will be removed irreversibly
	 * from the current MySQL-Database.
	 * @throws ConnectionFailedException if a database connection could not be established
	 */
	void permanentlyDeleteGraphs() throws ConnectionFailedException;

	/**
	 * The PropertyGraph-Object with the given id will be
	 * removed irreversibly from the current MySQL-Table.
	 * @param id id of the PropertyGraph-Object that should be removed
	 * @throws ConnectionFailedException if a database connection could not be established
	 */
	void permanentlyDeleteGraph(int id) throws ConnectionFailedException;

	/**
	 * replaces the PropertyGraph-Object identified by the given id. Additionally
	 * it will be checked whether the new graph is already calculated or not.
	 * @param id identifies a PropertyGraph-Object that will be replaced.
	 * @param graph new object that should replace the old one.
	 * @throws ConnectionFailedException if a database connection could not be established
	 * @throws InsertionFailedException if the PropertyGraph-object could not be inserted to the database
	 * @throws UnexpectedObjectException if the PropertyGraph-object is not as expected
	 */
	void replaceGraph(int id, PropertyGraph<Integer, Integer> graph)
			throws ConnectionFailedException, InsertionFailedException, UnexpectedObjectException;

	/**
	 * replaces the FilterSegment-Object identified by the given id.
	 * @param id identifies a FilterSegment-Object that will be replaced.
	 * @param filter new object that should replace the old one.
	 * @throws ConnectionFailedException if a database connection could not be established
	 * @throws InsertionFailedException if the Filtersegment-object could not be inserted to the database
	 * @throws UnexpectedObjectException if the Filtersegment-object is not as expected
	 */
	void replaceFilter(int id, Filtersegment filter)
			throws ConnectionFailedException, UnexpectedObjectException, InsertionFailedException;

	/**
	 * Every PropertyGraph-Object in databese that does not already exist in the
	 * current MySQL-Database, will be inserted to the current MySQL-Database.
	 * @param database GraphDatabase-Object that should be merged with the current MySQL-Database.
	 * @throws ConnectionFailedException if a database connection could not be established
	 */
	void merge(GraphDatabase database) throws ConnectionFailedException;

	/**
	 * Checks whether a propertyGraph-Object already exists.
	 * @param graph a PropertyGraph-Object
	 * @return true if the given graph is isomorphic to another PropertyGraphObject in the current MySQL-Database.
	 * @throws ConnectionFailedException if a database connection could not be established
	 */
	boolean graphExists(PropertyGraph<Integer, Integer> graph) throws ConnectionFailedException;

	/**
	 * Returns all FilterSegment-Objects.
	 * @return all FilterSegment-Objects in the current MySQL-Database.
	 * @throws ConnectionFailedException if a database connection could not be established
	 */
	List<Filtersegment> getFilters() throws ConnectionFailedException;

	/**
	 * Identifies a FilterSegment-Object and returns it.
	 * @param id identifies a FilterSegment-Object.
	 * @return identified FilterSegment-Object in the MySQL-Database.
	 * @throws UnexpectedObjectException if the object with the given id is not a Filtersegment-object
	 * @throws ConnectionFailedException if a database connection could not be established
	 */
	Filtersegment getFilterById(int id) throws UnexpectedObjectException, ConnectionFailedException;

	/**
	 * Returns all PropertyGraph-Objects that fulfill the current filters.
	 * @param filters determines how the GraphTable should be filtered
	 * @param column determines how the GraphTable should be sorted
	 * @param ascending determines if the GraphTable should be sorted ascending or descending
	 * @return all PropertyGraph-Objects in the MySQL-Database that fulfill the current filters.
	 * @throws ConnectionFailedException if a database connection could not be established
	 */
	ResultSet getGraphs(String[][] filters, String column, boolean ascending) throws ConnectionFailedException;

	/**
	 * Identifies a PropertyGraph-Object and returns it.
	 * @param id identifies a PropertyGraph-Object.
	 * @return identified PropertyGraph-Object in the MySQL-Database.
	 * @throws ConnectionFailedException if a database connection could not be established
	 * @throws UnexpectedObjectException if the object with the given id is not a PropertyGraph-object
	 */
	PropertyGraph<Integer, Integer> getGraphById(int id) throws ConnectionFailedException, UnexpectedObjectException;

	/**
	 * @return a PropertyGraph-Object in the MySQL-Database that is marked as uncalculated.
	 * @throws UnexpectedObjectException if the object with the given id is not a PropertyGraph-object
	 * @throws ConnectionFailedException if a database connection could not be established
	 */
	PropertyGraph<Integer, Integer> getUncalculatedGraph() throws ConnectionFailedException, UnexpectedObjectException;

	/**
	 * Determines if the current database contains any PropertyGraph-objects that are marked as uncalculated
	 * @return true if there are uncalculated graphs
	 * @throws ConnectionFailedException if a database connection could not be established
	 */
	boolean hasUncalculatedGraphs() throws ConnectionFailedException;

	/**
	 * Returns the values of a certain column of every graph that matches the filter criteria
	 * @param filters determines how the database should be filters
	 * @param column the column
	 * @return the value of every given column
	 * @throws ConnectionFailedException if a database connection could not be established
	 */
	List<Double> getValues(String[][] filters, String column) throws ConnectionFailedException;

}


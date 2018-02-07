package edu.kit.ipd.dbis.database.connection;

import edu.kit.ipd.dbis.database.exceptions.sql.*;
import edu.kit.ipd.dbis.filter.Filtersegment;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.sql.ResultSet;
import java.util.LinkedList;

/**
 *
 */
public interface DatabaseManager {

	/**
	 * Inserts graph into the MySQL-Table that belongs to the current MySQLDatabase,
	 * sets its automatically generated id and determines whether it should be marked as calculated or not.
	 * @param graph object that should be inserted into the current MySQLDatabase.
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 * @throws AccessDeniedForUserException
	 * @throws ConnectionFailedException
	 * @throws InsertionFailedException
	 * @throws UnexpectedObjectException
	 */
	void addGraph(PropertyGraph<Integer, Integer> graph)
			throws DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException,
			InsertionFailedException, UnexpectedObjectException;

	/**
	 * Inserts filter into the MySQL-Table belonging to the current MySQLDatabase.
	 * @param filter object that should be inserted into the current MySQLDatabase.
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 * @throws AccessDeniedForUserException
	 * @throws ConnectionFailedException
	 * @throws InsertionFailedException
	 * @throws UnexpectedObjectException
	 */
	void addFilter(Filtersegment filter)
			throws DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException,
			InsertionFailedException, UnexpectedObjectException;

	/**
	 * PropertyGraph-Object with the given id will be marked as deleted.
	 * @param id identies a PropertyGraph-Object in the current MySQL-Database.
	 * @throws ConnectionFailedException
	 * @throws AccessDeniedForUserException
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 */
	void deleteGraph(int id)
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException;

	/**
	 * FilterSegment-Object with the given id will be deleted.
	 * @param id identifies a FilterSegment-Object in the current MySQL-Database.
	 * @throws AccessDeniedForUserException
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 * @throws ConnectionFailedException
	 */
	void deleteFilter(int id)
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException;

	/**
	 * PropertyGraph-Object with the given id will be restored (unmarked as deleted).
	 * @param id identifies PropertyGraph-Object in the current MySQL-Database.
	 * @throws AccessDeniedForUserException
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 * @throws ConnectionFailedException
	 */
	void restoreGraph(int id)
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException;

	/**
	 * The state (determines whether a FilterSegment-Object is activated or not)
	 * of the given FilterSegment-Object will be changed.
	 * @param id identifies a FilterSegment-Object.
	 * @throws AccessDeniedForUserException
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 * @throws ConnectionFailedException
	 */
	void changeStateOfFilter(int id)
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException;

	/**
	 * Every PropertyGraph-Object that is marked as deleted will be removed irreversibly
	 * from the current MySQL-Database.
	 * @throws AccessDeniedForUserException
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 * @throws ConnectionFailedException
	 */
	void permanentlyDeleteGraphs()
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException;

	/**
	 * The PropertyGraph-Object with the given id will be
	 * removed irreversibly from the current MySQL-Table.
	 * @param id id of the PropertyGraph-Object that should be removed
	 * @throws AccessDeniedForUserException
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 * @throws ConnectionFailedException
	 */
	void permanentlyDeleteGraph(int id)
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException;

	/**
	 * replaces the PropertyGraph-Object identified by the given id. Additionally
	 * it will be checked whether the new graph is already calculated or not.
	 * @param id identifies a PropertyGraph-Object that will be replaced.
	 * @param graph new object that should replace the old one.
	 * @throws ConnectionFailedException
	 * @throws ConnectionFailedException
	 * @throws InsertionFailedException
	 * @throws AccessDeniedForUserException
	 * @throws UnexpectedObjectException
	 * @throws DatabaseDoesNotExistException
	 */
	void replaceGraph(int id, PropertyGraph<Integer, Integer> graph)
			throws ConnectionFailedException, InsertionFailedException, AccessDeniedForUserException,
			UnexpectedObjectException, DatabaseDoesNotExistException;

	/**
	 * replaces the FilterSegment-Object identified by the given id.
	 * @param id identifies a FilterSegment-Object that will be replaced.
	 * @param filter new object that should replace the old one.
	 * @throws DatabaseDoesNotExistException
	 * @throws AccessDeniedForUserException
	 * @throws ConnectionFailedException
	 * @throws ConnectionFailedException
	 * @throws UnexpectedObjectException
	 * @throws InsertionFailedException
	 */
	void replaceFilter(int id, Filtersegment filter)
			throws DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException,
			UnexpectedObjectException, InsertionFailedException;

	/**
	 * Every PropertyGraph-Object in databese that does not already exist in the
	 * current MySQL-Database, will be inserted to the current MySQL-Database.
	 * @param database GraphDatabase-Object that should be merged with the current MySQL-Database.
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 * @throws AccessDeniedForUserException
	 * @throws ConnectionFailedException
	 */
	void merge(GraphDatabase database)
			throws DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException;

	/**
	 * Checks whether a propertyGraph-Object already exists.
	 * @param graph a PropertyGraph-Object
	 * @return true if the given graph is isomorphic to another PropertyGraphObject in the current MySQL-Database.
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 * @throws AccessDeniedForUserException
	 * @throws ConnectionFailedException
	 */
	boolean graphExists(PropertyGraph<Integer, Integer> graph)
			throws DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException;

	/**
	 * Returns all FilterSegment-Objects.
	 * @return all FilterSegment-Objects in the current MySQL-Database.
	 * @throws UnexpectedObjectException
	 * @throws ConnectionFailedException
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 * @throws AccessDeniedForUserException
	 */
	LinkedList<Filtersegment> getFilters()
			throws UnexpectedObjectException, DatabaseDoesNotExistException, ConnectionFailedException,
			AccessDeniedForUserException;

	/**
	 * Identifies a FilterSegment-Object and returns it.
	 * @param id identifies a FilterSegment-Object.
	 * @return identified FilterSegment-Object in the MySQL-Database.
	 * @throws UnexpectedObjectException
	 * @throws ConnectionFailedException
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 * @throws AccessDeniedForUserException
	 */
	Filtersegment getFilterById(int id)
			throws UnexpectedObjectException, DatabaseDoesNotExistException, ConnectionFailedException,
			AccessDeniedForUserException;

	/**
	 * Returns all PropertyGraph-Objects that fulfill the current filters.
	 * @param filters determines how the GraphTable should be filtered
	 * @param column determines how the GraphTable should be sorted
	 * @param ascending determines if the GraphTable should be sorted ascending or descending
	 * @return all PropertyGraph-Objects in the MySQL-Database that fulfill the current filters.
	 * @throws AccessDeniedForUserException
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 * @throws ConnectionFailedException
	 */
	ResultSet getGraphs(String[][] filters, String column, boolean ascending)
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException;

	/**
	 * Identifies a PropertyGraph-Object and returns it.
	 * @param id identifies a PropertyGraph-Object.
	 * @return identified PropertyGraph-Object in the MySQL-Database.
	 * @throws ConnectionFailedException
	 * @throws ConnectionFailedException
	 * @throws DatabaseDoesNotExistException
	 * @throws AccessDeniedForUserException
	 * @throws UnexpectedObjectException
	 */
	PropertyGraph<Integer, Integer> getGraphById(int id)
			throws ConnectionFailedException, DatabaseDoesNotExistException, AccessDeniedForUserException,
			UnexpectedObjectException;

	/**
	 * @return a PropertyGraph-Object in the MySQL-Database that is marked as uncalculated.
	 * @throws AccessDeniedForUserException
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 * @throws ConnectionFailedException
	 */
	PropertyGraph<Integer, Integer> getUncalculatedGraph()
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException,
			UnexpectedObjectException;

	boolean hasUncalculatedGraphs()
			throws DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException;

	/**
	 * Returns the values of a certain column of every graph that matches the filter criteria
	 * @param filters determines how the database should be filters
	 * @param column the column
	 * @return the value of every given column
	 * @throws AccessDeniedForUserException
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 * @throws ConnectionFailedException
	 */
	LinkedList<Double> getValues(String[][] filters, String column)
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException;

}


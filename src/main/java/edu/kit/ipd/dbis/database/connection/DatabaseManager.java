package edu.kit.ipd.dbis.database.connection;

import edu.kit.ipd.dbis.filter.Filtersegment;
import edu.kit.ipd.dbis.database.exceptions.sql.*;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

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
	 * @throws TablesNotAsExpectedException
	 * @throws AccessDeniedForUserException
	 * @throws ConnectionFailedException
	 * @throws InsertionFailedException
	 * @throws UnexpectedObjectException
	 */
	void addGraph(PropertyGraph graph)
			throws DatabaseDoesNotExistException, TablesNotAsExpectedException, AccessDeniedForUserException,
			ConnectionFailedException, InsertionFailedException, UnexpectedObjectException;

	/**
	 * Inserts filter into the MySQL-Table belonging to the current MySQLDatabase.
	 * @param filter object that should be inserted into the current MySQLDatabase.
	 * @throws DatabaseDoesNotExistException
	 * @throws TablesNotAsExpectedException
	 * @throws AccessDeniedForUserException
	 * @throws ConnectionFailedException
	 * @throws InsertionFailedException
	 * @throws UnexpectedObjectException
	 */
	void addFilter(Filtersegment filter)
			throws DatabaseDoesNotExistException, TablesNotAsExpectedException, AccessDeniedForUserException,
			ConnectionFailedException, InsertionFailedException, UnexpectedObjectException;

	/**
	 * PropertyGraph-Object with the given id will be marked as deleted.
	 * @param id identies a PropertyGraph-Object in the current MySQL-Database.
	 * @throws TablesNotAsExpectedException
	 * @throws AccessDeniedForUserException
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 */
	void deleteGraph(int id)
			throws TablesNotAsExpectedException, AccessDeniedForUserException, DatabaseDoesNotExistException,
			ConnectionFailedException;

	/**
	 * FilterSegment-Object with the given id will be deleted.
	 * @param id identifies a FilterSegment-Object in the current MySQL-Database.
	 * @throws AccessDeniedForUserException
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 * @throws TablesNotAsExpectedException
	 */
	void deleteFilter(int id)
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException,
			TablesNotAsExpectedException;

	/**
	 * PropertyGraph-Object with the given id will be restored (unmarked as deleted).
	 * @param id identifies PropertyGraph-Object in the current MySQL-Database.
	 * @throws AccessDeniedForUserException
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 * @throws TablesNotAsExpectedException
	 */
	void restoreGraph(int id)
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException,
			TablesNotAsExpectedException;

	/**
	 * The state (determines whether a FilterSegment-Object is activated or not)
	 * of the given FilterSegment-Object will be changed.
	 * @param id identifies a FilterSegment-Object.
	 * @throws AccessDeniedForUserException
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 * @throws TablesNotAsExpectedException
	 */
	void changeStateOfFilter(int id)
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException,
			TablesNotAsExpectedException;

	/**
	 * Every PropertyGraph-Object that is marked as deleted will be removed irreversibly
	 * from the current MySQL-Database.
	 * @throws AccessDeniedForUserException
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 * @throws TablesNotAsExpectedException
	 */
	void permanentlyDeleteGraphs()
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException,
			TablesNotAsExpectedException;

	/**
	 * The PropertyGraph-Object with the given id will be
	 * removed irreversibly from the current MySQL-Table.
	 * @param id id of the PropertyGraph-Object that should be removed
	 * @throws AccessDeniedForUserException
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 * @throws TablesNotAsExpectedException
	 */
	void permanentlyDeleteGraph(int id)
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException,
			TablesNotAsExpectedException;

	/**
	 * replaces the PropertyGraph-Object identified by the given id. Additionally
	 * it will be checked whether the new graph is already calculated or not.
	 * @param id identifies a PropertyGraph-Object that will be replaced.
	 * @param graph new object that should replace the old one.
	 * @throws TablesNotAsExpectedException
	 * @throws ConnectionFailedException
	 * @throws InsertionFailedException
	 * @throws AccessDeniedForUserException
	 * @throws UnexpectedObjectException
	 * @throws DatabaseDoesNotExistException
	 */
	void replaceGraph(int id, PropertyGraph graph)
			throws TablesNotAsExpectedException, ConnectionFailedException, InsertionFailedException,
			AccessDeniedForUserException, UnexpectedObjectException, DatabaseDoesNotExistException;

	/**
	 * replaces the FilterSegment-Object identified by the given id.
	 * @param id identifies a FilterSegment-Object that will be replaced.
	 * @param filter new object that should replace the old one.
	 * @throws DatabaseDoesNotExistException
	 * @throws AccessDeniedForUserException
	 * @throws ConnectionFailedException
	 * @throws TablesNotAsExpectedException
	 * @throws UnexpectedObjectException
	 * @throws InsertionFailedException
	 */
	void replaceFilter(int id, Filtersegment filter)
			throws DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException,
			TablesNotAsExpectedException, UnexpectedObjectException, InsertionFailedException;

	/**
	 * Every PropertyGraph-Object in databese that does not already exist in the
	 * current MySQL-Database, will be inserted to the current MySQL-Database.
	 * @param database GraphDatabase-Object that should be merged with the current MySQL-Database.
	 * @throws DatabaseDoesNotExistException
	 * @throws TablesNotAsExpectedException
	 * @throws AccessDeniedForUserException
	 * @throws ConnectionFailedException
	 */
	void merge(GraphDatabase database)
			throws DatabaseDoesNotExistException, TablesNotAsExpectedException, AccessDeniedForUserException,
			ConnectionFailedException;

	/**
	 * Checks whether a propertyGraph-Object already exists.
	 * @param graph a PropertyGraph-Object
	 * @return true if the given graph is isomorphic to another PropertyGraphObject in the current MySQL-Database.
	 * @throws DatabaseDoesNotExistException
	 * @throws TablesNotAsExpectedException
	 * @throws AccessDeniedForUserException
	 * @throws ConnectionFailedException
	 */
	boolean graphExists(PropertyGraph graph)
			throws DatabaseDoesNotExistException, TablesNotAsExpectedException, AccessDeniedForUserException,
			ConnectionFailedException;

	/**
	 * Returns all FilterSegment-Objects.
	 * @return all FilterSegment-Objects in the current MySQL-Database.
	 * @throws UnexpectedObjectException
	 * @throws TablesNotAsExpectedException
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 * @throws AccessDeniedForUserException
	 */
	LinkedList<Filtersegment> getFilters()
			throws UnexpectedObjectException, TablesNotAsExpectedException, DatabaseDoesNotExistException,
			ConnectionFailedException, AccessDeniedForUserException;

	/**
	 * Identifies a FilterSegment-Object and returns it.
	 * @param id identifies a FilterSegment-Object.
	 * @return identified FilterSegment-Object in the MySQL-Database.
	 * @throws UnexpectedObjectException
	 * @throws TablesNotAsExpectedException
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 * @throws AccessDeniedForUserException
	 */
	Filtersegment getFilterById(int id)
			throws UnexpectedObjectException, TablesNotAsExpectedException, DatabaseDoesNotExistException,
			ConnectionFailedException, AccessDeniedForUserException ;

	/**
	 * Returns all PropertyGraph-Objects that fulfill the current filters.
	 * @param filters determines how the GraphTable should be filtered
	 * @param column determines how the GraphTable should be sorted
	 * @param ascending determines if the GraphTable should be sorted ascending or descending
	 * @return all PropertyGraph-Objects in the MySQL-Database that fulfill the current filters.
	 * @throws AccessDeniedForUserException
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 * @throws TablesNotAsExpectedException
	 */
	LinkedList<PropertyGraph> getGraphs(String[][] filters, String column, boolean ascending)
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException,
			TablesNotAsExpectedException;

	/**
	 * Identifies a PropertyGraph-Object and returns it.
	 * @param id identifies a PropertyGraph-Object.
	 * @return identified PropertyGraph-Object in the MySQL-Database.
	 * @throws TablesNotAsExpectedException
	 * @throws ConnectionFailedException
	 * @throws DatabaseDoesNotExistException
	 * @throws AccessDeniedForUserException
	 * @throws UnexpectedObjectException
	 */
	PropertyGraph getGraphById(int id)
			throws TablesNotAsExpectedException, ConnectionFailedException, DatabaseDoesNotExistException,
			AccessDeniedForUserException, UnexpectedObjectException;

	/**
	 *
	 * @return all PropertyGraph-Objects in the MySQL-Database that are marked as uncalculated.
	 * @throws AccessDeniedForUserException
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 * @throws TablesNotAsExpectedException
	 */
	LinkedList<PropertyGraph> getUncalculatedGraphs()
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException,
			TablesNotAsExpectedException;

	LinkedList<Double> getValues(String[][] filters, String column)
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException,
			TablesNotAsExpectedException;
}


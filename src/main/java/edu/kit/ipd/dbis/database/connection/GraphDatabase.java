package edu.kit.ipd.dbis.database.connection;

import edu.kit.ipd.dbis.database.connection.tables.FilterTable;
import edu.kit.ipd.dbis.database.connection.tables.GraphTable;
import edu.kit.ipd.dbis.database.exceptions.sql.*;
import edu.kit.ipd.dbis.filter.Filtersegment;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * This class represents a Graphdatabase that contains graphs and filters.
 */
public class GraphDatabase implements DatabaseManager {

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

	@Override
	public void addGraph(PropertyGraph<Integer, Integer> graph)
			throws DatabaseDoesNotExistException, TablesNotAsExpectedException, AccessDeniedForUserException,
			ConnectionFailedException, InsertionFailedException, UnexpectedObjectException {
		try {
			this.graphTable.insert(graph);
		} catch (SQLException e) {
			throw new TablesNotAsExpectedException();
		} catch (IOException e) {
			throw new InsertionFailedException();
		}
	}

	@Override
	public void addFilter(Filtersegment filter)
			throws DatabaseDoesNotExistException, TablesNotAsExpectedException, AccessDeniedForUserException,
			ConnectionFailedException, InsertionFailedException, UnexpectedObjectException {
		try {
			this.filterTable.insert(filter);
		} catch (SQLException e) {
			throw new TablesNotAsExpectedException();
		} catch (IOException e) {
			throw new InsertionFailedException();
		}
	}

	@Override
	public void deleteGraph(int id)
			throws TablesNotAsExpectedException, AccessDeniedForUserException, DatabaseDoesNotExistException,
			ConnectionFailedException {
		try {
			this.graphTable.switchState(id);
		} catch (SQLException e) {
			throw new TablesNotAsExpectedException();
		}
	}

	@Override
	public void deleteFilter(int id)
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException,
			TablesNotAsExpectedException {
		try {
			this.filterTable.delete(id);
		} catch (SQLException e) {
			throw new TablesNotAsExpectedException();
		}
	}

	@Override
	public void restoreGraph(int id)
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException,
			TablesNotAsExpectedException {

		try {
			this.graphTable.switchState(id);
		} catch (SQLException e) {
			throw new TablesNotAsExpectedException();
		}
	}

	@Override
	public void changeStateOfFilter(int id)
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException,
			TablesNotAsExpectedException {
		try {
			this.filterTable.switchState(id);
		} catch (SQLException e) {
			throw new TablesNotAsExpectedException();
		}
	}

	@Override
	public void permanentlyDeleteGraphs()
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException,
			TablesNotAsExpectedException {

		try {
			this.graphTable.deleteAll();
		} catch (SQLException e) {
			throw new TablesNotAsExpectedException();
		}
	}

	@Override
	public void permanentlyDeleteGraph(int id)
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException,
			TablesNotAsExpectedException {
		try {
			this.graphTable.delete(id);
		} catch (SQLException e) {
			throw new TablesNotAsExpectedException();
		}
	}

	@Override
	public void replaceGraph(int id, PropertyGraph<Integer, Integer> graph)
			throws TablesNotAsExpectedException, ConnectionFailedException, InsertionFailedException,
			AccessDeniedForUserException, UnexpectedObjectException, DatabaseDoesNotExistException {
		this.permanentlyDeleteGraph(id);
		graph.setId(id);
		this.addGraph(graph);
	}

	@Override
	public void replaceFilter(int id, Filtersegment filter)
			throws DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException,
			TablesNotAsExpectedException, UnexpectedObjectException, InsertionFailedException {
		this.deleteFilter(id);
		this.addFilter(filter);
	}

	@Override
	public void merge(GraphDatabase database)
			throws DatabaseDoesNotExistException, TablesNotAsExpectedException, AccessDeniedForUserException,
			ConnectionFailedException {
		try {
			this.graphTable.merge(database.getGraphTable());
		} catch (SQLException e) {
			throw new TablesNotAsExpectedException();
		}
	}

	@Override
	public boolean graphExists(PropertyGraph<Integer, Integer> graph)
			throws DatabaseDoesNotExistException, TablesNotAsExpectedException, AccessDeniedForUserException,
			ConnectionFailedException {
		try {
			return this.graphTable.graphExists(graph);
		} catch (SQLException e) {
			throw new TablesNotAsExpectedException();
		}
	}

	@Override
	public LinkedList<Filtersegment> getFilters()
			throws DatabaseDoesNotExistException, TablesNotAsExpectedException, AccessDeniedForUserException,
			ConnectionFailedException {
		try {
			return this.filterTable.getContent();
		} catch (SQLException e) {
			throw new TablesNotAsExpectedException();
		}
	}

	@Override
	public Filtersegment getFilterById(int id)
			throws UnexpectedObjectException, TablesNotAsExpectedException, DatabaseDoesNotExistException,
			ConnectionFailedException, AccessDeniedForUserException {
		try {
			return this.filterTable.getContent(id);
		} catch (SQLException e) {
			throw new TablesNotAsExpectedException();
		} catch (IOException e) {
			throw new UnexpectedObjectException();
		} catch (ClassNotFoundException e) {
			throw new UnexpectedObjectException();
		}
	}

	@Override
	public ResultSet getGraphs(String[][] filters, String column, boolean ascending)
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException,
			TablesNotAsExpectedException {
		try {
			return this.graphTable.getContent(filters, column, ascending);
		} catch (SQLException e) {
			throw new TablesNotAsExpectedException();
		}
	}

	@Override
	public PropertyGraph<Integer, Integer> getGraphById(int id)
			throws TablesNotAsExpectedException, ConnectionFailedException, DatabaseDoesNotExistException,
			AccessDeniedForUserException, UnexpectedObjectException {
		try {
			return this.graphTable.getContent(id);
		} catch (SQLException e) {
			throw new TablesNotAsExpectedException();
		} catch (IOException e) {
			throw new UnexpectedObjectException();
		} catch (ClassNotFoundException e) {
			throw new UnexpectedObjectException();
		}
	}

	@Override
	public PropertyGraph<Integer, Integer> getUncalculatedGraph()
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException,
			TablesNotAsExpectedException, UnexpectedObjectException {
		try {
			return this.graphTable.getUncalculatedGraph();
		} catch (SQLException e) {
			throw new TablesNotAsExpectedException();
		} catch (IOException e) {
			throw new UnexpectedObjectException();
		} catch (ClassNotFoundException e) {
			throw new ConnectionFailedException();
		}
	}

	@Override
	public boolean hasUncalculatedGraphs()
			throws DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException,
			TablesNotAsExpectedException {
		try {
			return this.graphTable.hasUncalculated();
		} catch (SQLException e) {
			throw new TablesNotAsExpectedException();
		}
	}

	@Override
	public LinkedList<Double> getValues(String[][] filters, String column)
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException,
			TablesNotAsExpectedException {

		try {
			return this.graphTable.getValues(filters, column);
		} catch (SQLException e) {
			throw new TablesNotAsExpectedException();
		}
	}
}

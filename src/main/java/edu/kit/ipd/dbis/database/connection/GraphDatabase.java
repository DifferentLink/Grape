package edu.kit.ipd.dbis.database.connection;

import edu.kit.ipd.dbis.database.connection.tables.FilterTable;
import edu.kit.ipd.dbis.database.connection.tables.GraphTable;
import edu.kit.ipd.dbis.database.exceptions.sql.*;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.Filter.Filtersegment;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Set;

public class GraphDatabase implements DatabaseManager {

	private String directory;
	private GraphTable graphTable;
	private FilterTable filterTable;

	/**
	 *
	 */
	public GraphDatabase(GraphTable graphTable, FilterTable filterTable) {
		this.graphTable = graphTable;
		this.filterTable = filterTable;
		this.directory = null;
	}

	/**
	 *
	 * @return
	 */
	public GraphTable getGraphTable() {
		return this.graphTable;
	}

	/**
	 *
	 * @return
	 */
	public FilterTable getFilterTable() {
		return this.filterTable;
	}

	public String getDirectory() {
		return this.directory;
	}

	/**
	 *
	 */
	public void setDirectory(String directory) {
		this.directory = directory;
	}

	@Override
	public void addGraph(PropertyGraph graph)
			throws DatabaseDoesNotExistException, TablesNotAsExpectedException, AccessDeniedForUserException,
			ConnectionFailedException, InsertionFailedException, UnexpectedObjectException {
		try {

			this.graphTable.insert(graph);
		} catch (DatabaseDoesNotExistException e) {
			throw new DatabaseDoesNotExistException();
		} catch (SQLException e) {
			throw new TablesNotAsExpectedException();
		} catch (AccessDeniedForUserException e) {
			throw new AccessDeniedForUserException();
		} catch (ConnectionFailedException e) {
			throw new ConnectionFailedException();
		} catch (IOException e) {
			throw new InsertionFailedException();
		} catch (UnexpectedObjectException e) {
			throw new UnexpectedObjectException();
		}
	}

	@Override
	public void addFilter(Filtersegment filter)
			throws DatabaseDoesNotExistException, TablesNotAsExpectedException, AccessDeniedForUserException,
			ConnectionFailedException, InsertionFailedException, UnexpectedObjectException {
		try {
			this.filterTable.insert(filter);
		} catch (DatabaseDoesNotExistException e) {
			throw new DatabaseDoesNotExistException();
		} catch (SQLException e) {
			throw new TablesNotAsExpectedException();
		} catch (AccessDeniedForUserException e) {
			throw new AccessDeniedForUserException();
		} catch (ConnectionFailedException e) {
			throw new ConnectionFailedException();
		} catch (IOException e) {
			throw new InsertionFailedException();
		} catch (UnexpectedObjectException e) {
			throw new UnexpectedObjectException();
		}
	}

	@Override
	public void deleteGraph(int id)
			throws TablesNotAsExpectedException, AccessDeniedForUserException, DatabaseDoesNotExistException,
			ConnectionFailedException {
		try {
			this.graphTable.switchState(id);
		} catch (AccessDeniedForUserException e) {
			throw new AccessDeniedForUserException();
		} catch (DatabaseDoesNotExistException e) {
			throw new DatabaseDoesNotExistException();
		} catch (ConnectionFailedException e) {
			throw new ConnectionFailedException();
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
		} catch (AccessDeniedForUserException e) {
			throw new AccessDeniedForUserException();
		} catch (DatabaseDoesNotExistException e) {
			throw new DatabaseDoesNotExistException();
		} catch (ConnectionFailedException e) {
			throw new ConnectionFailedException();
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
		} catch (AccessDeniedForUserException e) {
			throw new AccessDeniedForUserException();
		} catch (DatabaseDoesNotExistException e) {
			throw new DatabaseDoesNotExistException();
		} catch (ConnectionFailedException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new TablesNotAsExpectedException();
		}
	}

	@Override
	public void changeStateOfFilter(int id) throws AccessDeniedForUserException, DatabaseDoesNotExistException,
			ConnectionFailedException, TablesNotAsExpectedException {
		try {
			this.filterTable.switchState(id);
		} catch (AccessDeniedForUserException e) {
			throw new AccessDeniedForUserException();
		} catch (DatabaseDoesNotExistException e) {
			throw new DatabaseDoesNotExistException();
		} catch (ConnectionFailedException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new TablesNotAsExpectedException();
		}
	}

	@Override
	public void permanentlyDeleteGraphs() throws AccessDeniedForUserException, DatabaseDoesNotExistException,
			ConnectionFailedException, TablesNotAsExpectedException {

		try {
			this.graphTable.deleteAll();
		} catch (AccessDeniedForUserException e) {
			throw new AccessDeniedForUserException();
		} catch (DatabaseDoesNotExistException e) {
			throw new DatabaseDoesNotExistException();
		} catch (ConnectionFailedException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new TablesNotAsExpectedException();
		}
	}

	public void permanentlyDeleteGraph(int id) throws AccessDeniedForUserException, DatabaseDoesNotExistException,
			ConnectionFailedException, TablesNotAsExpectedException {
		try {
			this.graphTable.delete(id);
		} catch (AccessDeniedForUserException e) {
			throw new AccessDeniedForUserException();
		} catch (DatabaseDoesNotExistException e) {
			throw new DatabaseDoesNotExistException();
		} catch (ConnectionFailedException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new TablesNotAsExpectedException();
		}
	}

	@Override
	public void replaceGraph(int id, PropertyGraph graph) throws TablesNotAsExpectedException,
			ConnectionFailedException, InsertionFailedException, AccessDeniedForUserException, UnexpectedObjectException, DatabaseDoesNotExistException {
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
		} catch (DatabaseDoesNotExistException e) {
			throw new DatabaseDoesNotExistException();
		} catch (SQLException e) {
			throw new TablesNotAsExpectedException();
		} catch (AccessDeniedForUserException e) {
			throw new AccessDeniedForUserException();
		} catch (ConnectionFailedException e) {
			throw new ConnectionFailedException();
		}
	}

	@Override
	public boolean graphExists(PropertyGraph graph)
			throws DatabaseDoesNotExistException, TablesNotAsExpectedException, AccessDeniedForUserException,
			ConnectionFailedException {
		try {
			return this.graphTable.graphExists(graph);
		} catch (DatabaseDoesNotExistException e) {
			throw new DatabaseDoesNotExistException();
		} catch (SQLException e) {
			throw new TablesNotAsExpectedException();
		} catch (AccessDeniedForUserException e) {
			throw new AccessDeniedForUserException();
		} catch (ConnectionFailedException e) {
			throw new ConnectionFailedException();
		}
	}

	@Override
	public Set<Filtersegment> getFilters() throws DatabaseDoesNotExistException, TablesNotAsExpectedException,
			AccessDeniedForUserException, ConnectionFailedException {
		try {
			return this.filterTable.getContent();
		} catch (DatabaseDoesNotExistException e) {
			throw new DatabaseDoesNotExistException();
		} catch (SQLException e) {
			throw new TablesNotAsExpectedException();
		} catch (AccessDeniedForUserException e) {
			throw new AccessDeniedForUserException();
		} catch (ConnectionFailedException e) {
			throw new ConnectionFailedException();
		}
	}

	@Override
	public Filtersegment getFilterById(int id) throws UnexpectedObjectException, TablesNotAsExpectedException,
			DatabaseDoesNotExistException, ConnectionFailedException, AccessDeniedForUserException {
		try {
			return this.filterTable.getContent(id);
		} catch (AccessDeniedForUserException e) {
			throw new AccessDeniedForUserException();
		} catch (ConnectionFailedException e) {
			throw new ConnectionFailedException();
		} catch (DatabaseDoesNotExistException e) {
			throw new DatabaseDoesNotExistException();
		} catch (SQLException e) {
			throw new TablesNotAsExpectedException();
		} catch (IOException e) {
			throw new UnexpectedObjectException();
		} catch (ClassNotFoundException e) {
			throw new UnexpectedObjectException();
		} catch (UnexpectedObjectException e) {
			throw new UnexpectedObjectException();
		}
	}

	@Override //TODO: löschen
	public Set<Filtersegment> getActivatedFilters() {
		return null;
	}

	@Override
	public LinkedList<PropertyGraph> getGraphs(String[][] filters, String column, boolean ascending)
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException,
			TablesNotAsExpectedException {
		try {
			return this.graphTable.getContent(filters, column, ascending);
		} catch (AccessDeniedForUserException e) {
			throw new AccessDeniedForUserException();
		} catch (DatabaseDoesNotExistException e) {
			throw new DatabaseDoesNotExistException();
		} catch (ConnectionFailedException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new TablesNotAsExpectedException();
		}
	}

	@Override
	public PropertyGraph getGraphById(int id) throws TablesNotAsExpectedException, ConnectionFailedException,
			DatabaseDoesNotExistException, AccessDeniedForUserException, UnexpectedObjectException {
		try {
			return this.graphTable.getContent(id);
		} catch (AccessDeniedForUserException e) {
			throw new AccessDeniedForUserException();
		} catch (DatabaseDoesNotExistException e) {
			throw new DatabaseDoesNotExistException();
		} catch (ConnectionFailedException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new TablesNotAsExpectedException();
		} catch (IOException e) {
			throw new UnexpectedObjectException();
		} catch (ClassNotFoundException e) {
			throw new UnexpectedObjectException();
		} catch (UnexpectedObjectException e) {
			throw new UnexpectedObjectException();
		}
	}

	@Override //TODO: löschen
	public Set<PropertyGraph> getCalculatedGraphs() throws Exception {
		return null;
	}

	@Override
	public Set<PropertyGraph> getUncalculatedGraphs() throws AccessDeniedForUserException,
			DatabaseDoesNotExistException, ConnectionFailedException, TablesNotAsExpectedException {
		try {
			return this.graphTable.getUncalculatedGraphs();
		} catch (AccessDeniedForUserException e) {
			throw new AccessDeniedForUserException();
		} catch (DatabaseDoesNotExistException e) {
			throw new DatabaseDoesNotExistException();
		} catch (ConnectionFailedException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new TablesNotAsExpectedException();
		}
	}

	@Override //TODO: löschen
	public Set<PropertyGraph> getGraphsByVertex(int vertices) throws Exception {
		return null;
	}

}

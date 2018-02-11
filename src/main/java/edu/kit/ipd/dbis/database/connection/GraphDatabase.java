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

	@Override
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

	@Override
	public void deleteGraph(int id) throws ConnectionFailedException {
		try {
			this.graphTable.switchState(id);
		} catch (SQLSyntaxErrorException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new ConnectionFailedException(e.getMessage());
		}
	}

	@Override
	public void deleteFilter(int id) throws ConnectionFailedException {
		try {
			this.filterTable.delete(id);
		} catch (SQLSyntaxErrorException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new ConnectionFailedException(e.getMessage());
		}
	}

	@Override
	public void restoreGraph(int id) throws ConnectionFailedException {

		try {
			this.graphTable.switchState(id);
		} catch (SQLSyntaxErrorException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new ConnectionFailedException(e.getMessage());
		}
	}

	@Override
	public void changeStateOfFilter(int id) throws ConnectionFailedException {
		try {
			this.filterTable.switchState(id);
		} catch (SQLSyntaxErrorException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new ConnectionFailedException(e.getMessage());
		}
	}

	@Override
	public void permanentlyDeleteGraphs() throws ConnectionFailedException {
		try {
			this.graphTable.deleteAll();
		} catch (SQLSyntaxErrorException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new ConnectionFailedException();
		}
	}

	@Override
	public void permanentlyDeleteGraph(int id) throws ConnectionFailedException {
		try {
			this.graphTable.delete(id);
		} catch (SQLSyntaxErrorException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new ConnectionFailedException(e.getMessage());
		}
	}

	@Override
	public void replaceGraph(int id, PropertyGraph<Integer, Integer> graph)
			throws ConnectionFailedException, InsertionFailedException, UnexpectedObjectException {
		this.permanentlyDeleteGraph(id);
		graph.setId(id);
		this.addGraph(graph);
	}

	@Override
	public void replaceFilter(int id, Filtersegment filter)
			throws ConnectionFailedException, UnexpectedObjectException, InsertionFailedException {
		this.deleteFilter(id);
		this.addFilter(filter);
	}

	@Override
	public void merge(GraphDatabase database) throws ConnectionFailedException {
		try {
			this.graphTable.merge(database.getGraphTable());
		} catch (SQLSyntaxErrorException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new ConnectionFailedException(e.getMessage());
		}
	}

	@Override
	public boolean graphExists(PropertyGraph<Integer, Integer> graph) throws ConnectionFailedException {
		try {
			return this.graphTable.graphExists(graph);
		} catch (SQLSyntaxErrorException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new ConnectionFailedException(e.getMessage());
		}
	}

	@Override
	public LinkedList<Filtersegment> getFilters() throws ConnectionFailedException {
		try {
			return this.filterTable.getContent();
		} catch (SQLSyntaxErrorException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new ConnectionFailedException(e.getMessage());
		}
	}

	@Override
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

	@Override
	public ResultSet getGraphs(String[][] filters, String column, boolean ascending) throws ConnectionFailedException {
		try {
			return this.graphTable.getContent(filters, column, ascending);
		} catch (SQLSyntaxErrorException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new ConnectionFailedException(e.getMessage());
		}
	}

	@Override
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

	@Override
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

	@Override
	public boolean hasUncalculatedGraphs() throws ConnectionFailedException {
		try {
			return this.graphTable.hasUncalculated();
		} catch (SQLSyntaxErrorException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new ConnectionFailedException(e.getMessage());
		}
	}

	@Override
	public LinkedList<Double> getValues(String[][] filters, String column) throws ConnectionFailedException {
		try {
			return this.graphTable.getValues(filters, column);
		} catch (SQLSyntaxErrorException e) {
			throw new ConnectionFailedException();
		} catch (SQLException e) {
			throw new ConnectionFailedException(e.getMessage());
		}
	}
}

package edu.kit.ipd.dbis.database.connection.tables;

import edu.kit.ipd.dbis.database.exceptions.sql.AccessDeniedForUserException;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.DatabaseDoesNotExistException;
import edu.kit.ipd.dbis.database.exceptions.sql.UnexpectedObjectException;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.DoubleProperty;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.IntegerProperty;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.BfsCode;

import java.io.IOException;
import java.io.Serializable;
import java.sql.*;
import java.util.*;

/**
 * This class represents a MySQL-Table where PropertyGraph-Objects will be stored.
 */
public class GraphTable extends Table {

	/**
	 * Creates a new GraphTable.
	 * @param url location of the MySQL-Database that contains the MySQLTable which is represented by a subclass of Table.
	 * @param user username of the MySQL-Database user.
	 * @param password password of the user.
	 * @param name name of the MySQL-Table which is represented by a subclass of Table.
	 * @throws SQLException
	 * @throws DatabaseDoesNotExistException
	 * @throws AccessDeniedForUserException
	 * @throws ConnectionFailedException
	 */
	public GraphTable(String url, String user, String password, String name)
			throws SQLException, DatabaseDoesNotExistException, AccessDeniedForUserException,
			ConnectionFailedException {
		super(url, user, password, name);
	}

	@Override
	public PropertyGraph getContent(int id)
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException,
			SQLException, IOException, ClassNotFoundException, UnexpectedObjectException {

		Connection connection = this.getConnection();
		String sql = "SELECT graph FROM " + this.name + " WHERE id = " + id;
		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet result = statement.executeQuery();

		if (result.next()) {
			Object o = this.byteArrayToObject(result.getBytes("graph"));
			return this.getInstanceOf(o);
		}
		return null;
	}

	/**
	 * Sorts the represented MySQL-Table
	 * @param column the column to sort by
	 * @param ascending determines if the table should be sorted ascending or descending
	 * @throws AccessDeniedForUserException
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 * @throws SQLException
	 */
	public void sortTable(String column, boolean ascending)
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException,
			SQLException {
		if (column.equals("BfsCode")) return;
		Connection connection = this.getConnection();
		String s = (ascending) ? ("ASC") : ("DESC");
		String sql = "ALTER TABLE tablename ORDER BY columnname " + s;
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.executeUpdate();
	}

	/**
	 * Sorts a List of PropertyGraphs by BfsCode
	 * @param graphs the list that should be sorted
	 * @param ascending determines if the table should be sorted ascending or descending
	 * @throws AccessDeniedForUserException
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 * @throws SQLException
	 */
	public void sortByBfsCode(LinkedList<PropertyGraph> graphs, boolean ascending) {
		//TODO: 2 Methoden
		if (ascending) {
			Collections.sort(graphs, new Comparator<PropertyGraph>() {
				@Override
				public int compare(PropertyGraph o1, PropertyGraph o2) {
					BfsCodeAlgorithm.BfsCodeImpl bfs1 = (BfsCodeAlgorithm.BfsCodeImpl) o1.getProperty(BfsCode.class);
					BfsCodeAlgorithm.BfsCodeImpl bfs2 = (BfsCodeAlgorithm.BfsCodeImpl) o2.getProperty(BfsCode.class);
					return bfs1.compareTo(bfs2);
				}
			});
		} else {
			Collections.sort(graphs, new Comparator<PropertyGraph>() {
				@Override
				public int compare(PropertyGraph o1, PropertyGraph o2) {
					BfsCodeAlgorithm.BfsCodeImpl bfs1 = (BfsCodeAlgorithm.BfsCodeImpl) o1.getProperty(BfsCode.class);
					BfsCodeAlgorithm.BfsCodeImpl bfs2 = (BfsCodeAlgorithm.BfsCodeImpl) o2.getProperty(BfsCode.class);
					return bfs2.compareTo(bfs1);
				}
			});
		}

	}

	/**
	 * Returns all PropertyGraph-Objects that fulfill the current filters.
	 * @param filters determines how the GraphTable should be filtered
	 * @param column determines how the GraphTable should be sorted
	 * @param ascending determines if the GraphTable should be sorted ascending or descending
	 * @return all PropertyGraph-Objects in the MySQL-Database that fulfill the current filters.
	 * @throws AccessDeniedForUserException
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 * @throws SQLException
	 */
	public LinkedList<PropertyGraph> getContent(String[][] filters, String column, boolean ascending)
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException,
			SQLException {
		Connection connection = this.getConnection();
		this.sortTable(column, ascending);

		String sql = "SELECT graph FROM " + this.name + " WHERE";

		for (int i = 0; i < filters.length; i++) {
			for (int j = 0; j < filters[i].length; j++) {
				sql += " " + filters[i][j];
			}
			if (i != filters.length - 1) sql += " AND";
		}

		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet result = statement.executeQuery();
		LinkedList<PropertyGraph> graphs = new LinkedList<>();
		while (result.next()) {
			try {
				graphs.add((PropertyGraph) this.byteArrayToObject(result.getBytes("graph")));
			} catch(Exception e) {

			}
		}
		if (column.equals("BfsCode")) this.sortByBfsCode(graphs, ascending);
		return graphs;
	}

	@Override
	public void insert(Serializable object)
			throws DatabaseDoesNotExistException, SQLException, AccessDeniedForUserException,
			ConnectionFailedException, IOException, UnexpectedObjectException {
		PropertyGraph graph = getInstanceOf(object);
		Collection<Property> properties = graph.getProperties();
		String columns = "(";
		String values = "(";

		for (Property property : properties) {
			if (property.getClass().getSuperclass() == IntegerProperty.class) {
				columns += property.toString() + ", ";
				values += (int) property.getValue() + ", ";
			} else if (property.getClass().getSuperclass() == DoubleProperty.class) {
				columns += property.toString() + ", ";
				values += (double) property.getValue() + ", ";
			}
		}

		graph.setId(this.getId());
		columns += "graph, id, BfsCode, state, isCalculated, nothing)";
		values += "?, ?, ?, ?, ?, ?)";

		String sql = "INSERT INTO " + this.name + " " + columns + " VALUES " + values;
		PreparedStatement statement = this.getConnection().prepareStatement(sql);
		statement.setObject(1, this.objectToByteArray(graph));
		statement.setObject(2, graph.getId());
		statement.setObject(3, this.minimalBfsCodeToString(graph));
		statement.setObject(4, false);
		statement.setObject(5, this.isCalculated(graph));
		statement.setObject(6, 0);
		statement.executeUpdate();
	}

	@Override
	protected PropertyGraph getInstanceOf(Object object) throws UnexpectedObjectException {
		if (object instanceof PropertyGraph) {
			return (PropertyGraph) object;
		}
		throw new UnexpectedObjectException();
	}

	@Override
	protected void createTable()
			throws SQLException, AccessDeniedForUserException, DatabaseDoesNotExistException,
			ConnectionFailedException {

		String sql = "CREATE TABLE IF NOT EXISTS "
				+ this.name +" ("
				+ "graph longblob, "
				+ "id int NOT NULL, "
				+ "BfsCode VARCHAR(255), "
				+ "state boolean, "
				+ "isCalculated boolean";
		PropertyGraph graph = new PropertyGraph();
		Collection<Property> properties = graph.getProperties();

		for (Property property : properties) {
			if (property.getClass().getSuperclass() == IntegerProperty.class) {
				sql += ", " + property.toString() + " int";
			} else if (property.getClass().getSuperclass() == DoubleProperty.class) {
				sql += ", " + property.toString() + " double";
			}
		}

		sql += ", nothing int, PRIMARY KEY(id))";
		Connection connection = this.getConnection();
		PreparedStatement create = connection.prepareStatement(sql);
		create.executeUpdate();
	}

	/**
	 * The given content will be inserted into the represented MySQL-Table.
	 * @param table content of another MySQL-Table.
	 * @throws DatabaseDoesNotExistException
	 * @throws SQLException
	 * @throws AccessDeniedForUserException
	 * @throws ConnectionFailedException
	 */
	public void merge(GraphTable table)
			throws DatabaseDoesNotExistException, SQLException, AccessDeniedForUserException,
			ConnectionFailedException {
		LinkedList<Integer> ids = table.getIds();
		for (int i : ids) {
			try {
				if (!this.graphExists(table.getContent(i))) {
					PropertyGraph graph = table.getContent(i);
					graph.setId(this.getId());
					this.insert(graph);
				}
			} catch (Exception e) {

			}
		}
	}

	//TODO: löschen
	private boolean isMergeableWith(GraphTable table) throws Exception {
		Connection current = this.getConnection();
		Connection other = table.getConnection();

		String sql = "SHOW COLUMNS FROM ";
		ResultSet currentResult = current.prepareStatement(sql + this.name).executeQuery();

		while (currentResult.next()) {
			ResultSet otherResult = other.prepareStatement(sql + table.getName()).executeQuery();
			boolean found = false;
			while ((otherResult.next()) && (!found)) {
				found = (currentResult.getString(1).equals(otherResult.getString(1))) ?
						(true) : (false);
			}
			if (!found) return false;
		}
		return true;
	}

	/**
	 * All PropertyGraph-Objects that are marked as deleted will be removed from
	 * the represented MySQL-Table.
	 * @throws AccessDeniedForUserException
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 * @throws SQLException
	 */
	public void deleteAll()
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException,
			SQLException {
		Connection connection = this.getConnection();
		String sql = "DELETE * FROM " + this.name + " WHERE state = true";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.executeUpdate();
	}

	/**
	 * Parses a BfsCode of a PropertyGraph
	 * @param graph a PropertyGraph-object
	 * @return the BfsCode of the given graph as String
	 */
	private String minimalBfsCodeToString(PropertyGraph graph) {
		String s = "";
		BfsCode bfs = (BfsCode) graph.getProperty(BfsCode.class);
		int[] bfsCode = (int[]) bfs.getValue();

		for (int i = 0; i < bfsCode.length; i++) {
			s += (i != bfsCode.length - 1) ? (bfsCode[i] + ";") : (bfsCode[i]);
		}
		return s;
	}

	/**
	 * Checks if a PropertyGraph-Object already Exists.
	 * @param graph PropertyGaph-Object.
	 * @return true if the given graph already exists in the represented MySQLTable.
	 * @throws AccessDeniedForUserException
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 * @throws SQLException
	 */
	public boolean graphExists(PropertyGraph graph)
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException,
			SQLException {
		Connection connection = this.getConnection();
		String sql = "SELECT * FROM " + this.name + " WHERE BfsCode = " + this.minimalBfsCodeToString(graph);
		ResultSet result = connection.prepareStatement(sql).executeQuery();
		return result.next();
	}

	/**
	 * Checks if the given graph is calculated
	 * @param graph PropertyGraph-objcet
	 * @return true if all properties of graph have already been calculated
	 */
	private boolean isCalculated(PropertyGraph graph) {
		Collection<Property> properties = graph.getProperties();
		for (Property property : properties) {
			if (property.getValue() == null) return false;
		}
		return true;
	}

	/**
	 *@return all PropertyGraph-Objects in the represented MySQL-Table that are marked as uncalculated.
	 * @throws AccessDeniedForUserException
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 * @throws SQLException
	 */
	public LinkedList<PropertyGraph> getUncalculatedGraphs( ) throws AccessDeniedForUserException,
			DatabaseDoesNotExistException, ConnectionFailedException, SQLException {
		Connection connection = this.getConnection();
		String sql = "SELECT graph FROM " + this.name + " WHERE isCalculated = false";
		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet result = statement.executeQuery();
		LinkedList<PropertyGraph> graphs = new LinkedList<>();
		while (result.next()) {
			try {
				graphs.add((PropertyGraph) this.byteArrayToObject(result.getBytes("graph")));
			} catch(Exception e) {

			}
		}
		return graphs;

	}

}

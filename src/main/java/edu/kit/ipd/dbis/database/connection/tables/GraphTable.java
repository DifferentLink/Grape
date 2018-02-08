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
	 * @throws SQLException if the connection to the database fails
	 * @throws ConnectionFailedException if a database connection could not be established
	 * @throws AccessDeniedForUserException if the username or the password is invalid
	 * @throws DatabaseDoesNotExistException if the database does not exist
	 */
	public GraphTable(String url, String user, String password, String name)
			throws SQLException, DatabaseDoesNotExistException, AccessDeniedForUserException,
			ConnectionFailedException {
		super(url, user, password, name);
	}

	@Override
	protected void createTable() throws SQLException {

		String sql = "CREATE TABLE IF NOT EXISTS "
				+ this.name +" ("
				+ "graph longblob, "
				+ "id int NOT NULL, "
				+ "bfscode VARCHAR(255), "
				+ "state boolean, "
				+ "iscalculated boolean";

		PropertyGraph<Integer, Integer> graph = new PropertyGraph<>();
		Collection<Property> properties = graph.getProperties();

		for (Property property : properties) {
			if (property.getClass().getSuperclass() == IntegerProperty.class) {
				sql += ", " + property.getClass().getSimpleName().toLowerCase() + " int";
			} else if (property.getClass().getSuperclass() == DoubleProperty.class) {
				sql += ", " + property.getClass().getSimpleName().toLowerCase() + " double";
			}
		}

		sql += ", PRIMARY KEY(id))";
		this.connection.prepareStatement(sql).executeUpdate();

	}

	@Override
	protected PropertyGraph<Integer, Integer> getInstanceOf(Object object) throws UnexpectedObjectException {
		try {
			return (PropertyGraph<Integer, Integer>) object;
		} catch (ClassCastException e) {
			throw new UnexpectedObjectException("The given object is not a PropertyGraph-object");
		}
	}

	@Override
	public void insert(Serializable object) throws SQLException, IOException, UnexpectedObjectException {

		PropertyGraph<Integer, Integer> graph = getInstanceOf(object);
		if (this.graphExists(graph)) return;
		Collection<Property> properties = graph.getProperties();
		String columns = "(";
		String values = "(";

		boolean calculated = true;
		for (Property property : properties) {
			if (property.isCalculated()) {
				if (property.getClass().getSuperclass() == IntegerProperty.class) {
					columns += property.getClass().getSimpleName() + ", ";
					values += (int) property.getValue() + ", ";
				} else if (property.getClass().getSuperclass() == DoubleProperty.class) {
					columns += property.getClass().getSimpleName() + ", ";
					values += (double) property.getValue() + ", ";
				}
			} else {
				calculated = false;
			}
		}

		this.setIdGraph(graph);
		columns += "graph, id, bfscode, state, iscalculated)";
		values += "?, " + graph.getId()
				+ ", '" + this.minimalBfsCodeToString(graph) + "'"
				+ ", " + false
				+ ", " + calculated + ")";

		String sql = "INSERT INTO " + this.name + " " + columns + " VALUES " + values;
		PreparedStatement statement = this.connection.prepareStatement(sql);
		statement.setObject(1, this.objectToByteArray(graph));
		statement.executeUpdate();

	}

	@Override
	public PropertyGraph<Integer, Integer> getContent(int id)
			throws SQLException, IOException, ClassNotFoundException, UnexpectedObjectException {

		String sql = "SELECT graph FROM " + this.name + " WHERE id = " + id;
		ResultSet result = this.connection.prepareStatement(sql).executeQuery();
		if (result.next()) {
			Object object = this.byteArrayToObject(result.getBytes("graph"));
			return this.getInstanceOf(object);
		}
		return null;
	}


	/**
	 * Returns all PropertyGraph-Objects that fulfill the current filters.
	 * @param filters determines how the GraphTable should be filtered
	 * @param column determines how the GraphTable should be sorted
	 * @param ascending determines if the GraphTable should be sorted ascending or descending
	 * @return all PropertyGraph-Objects in the MySQL-Database that fulfill the current filters.
	 * @throws SQLException if the connection to the database fails
	 */
	public ResultSet getContent(String[][] filters, String column, boolean ascending) throws SQLException {

		String sql = this.getFilteredTableQuery(filters, column, ascending);
		ResultSet result = this.connection.prepareStatement(sql).executeQuery();

		return result;
	}

	/**
	 * @return a PropertyGraph-Object in the represented MySQL-Table that is marked as uncalculated.
	 * @throws SQLException if the connection to the database fails
	 * @throws IOException if the serialization of the object fails
	 * @throws ClassNotFoundException if the serialization of the object fails
	 * @throws UnexpectedObjectException if the given object is not as expected
	 */
	public PropertyGraph<Integer, Integer> getUncalculatedGraph()
			throws SQLException, IOException, ClassNotFoundException, UnexpectedObjectException {

		String sql = "SELECT graph FROM " + this.name + " WHERE iscalculated = false LIMIT 1";
		ResultSet result = this.connection.prepareStatement(sql).executeQuery();

		if (result.next()) {
			return this.getInstanceOf(this.byteArrayToObject(result.getBytes("graph")));
		}
		return null;

	}

	/**
	 * Returns the values of a certain column of every graph that matches the filter criteria
	 * @param filters determines how the database should be filters
	 * @param column the column
	 * @return the value of every given column
	 * @throws SQLException if the connection to the database fails
	 */
	public LinkedList<Double> getValues(String[][] filters, String column) throws SQLException {

		String sql = this.getValuesQuery(filters, column);
		ResultSet result = this.connection.prepareStatement(sql).executeQuery();
		LinkedList<Double> values = new LinkedList<>();
		while (result.next()) {
			try {
				double value = (double) result.getObject(column);
				values.add(value);
			} catch (SQLException e) {

			}
		}
		return values;
	}

	/**
	 * All PropertyGraph-Objects that are marked as deleted will be removed from
	 * the represented MySQL-Table.
	 * @throws SQLException if the connection to the database fails
	 */
	public void deleteAll() throws SQLException {

		String sql = "DELETE * FROM " + this.name + " WHERE state = true";
		this.connection.prepareStatement(sql).executeUpdate();

	}

	/**
	 * The given content will be inserted into the represented MySQL-Table.
	 * @param table content of another MySQL-Table.
	 * @throws SQLException if the connection to the database fails
	 */
	public void merge(GraphTable table) throws SQLException {

		LinkedList<Integer> ids = table.getIds();
		for (int i : ids) {
			try {
				if (!this.graphExists(table.getContent(i))) {
					PropertyGraph<Integer, Integer> graph = table.getContent(i);
					graph.setId(this.getId());
					this.insert(graph);
				}
			} catch (IOException | UnexpectedObjectException | ClassNotFoundException e) {

			}
		}

	}

	/**
	 * Checks if a PropertyGraph-Object already Exists.
	 * @param graph PropertyGaph-Object.
	 * @return true if the given graph already exists in the represented MySQLTable.
	 * @throws SQLException if the connection to the database fails
	 */
	public boolean graphExists(PropertyGraph<Integer, Integer> graph) throws SQLException {

		String sql = "SELECT * FROM " + this.name + " WHERE BfsCode = '" + this.minimalBfsCodeToString(graph) + "'";
		ResultSet result = this.connection.prepareStatement(sql).executeQuery();
		if (result.next()) {
			return this.minimalBfsCodeToString(graph).equals(result.getString("bfscode"));
		}
		return false;

	}

	/**
	 * Determines if there are uncalculated graphs in the database.
	 * @return true if there are
	 * @throws SQLException if the connection to the database fails
	 */
	public boolean hasUncalculated() throws SQLException {
		String sql = "SELECT graph FROM " + this.name + " WHERE iscalculated = false LIMIT 1";
		return this.connection.prepareStatement(sql).executeQuery().next();

	}

	/**
	 * Generates the MySQL-Query necessary to filter and sort the represented MySQL-table
	 * @param filters determines how the MySQL-table should be filtered
	 * @param column determines how the MySQL-table should be sorted
	 * @param ascending determines whether the MySQL-table should be sorted ascending or descending
	 * @return the according MySQL-Query
	 * @throws SQLException if the connection to the database fails
	 */
	private String getFilteredTableQuery(String[][] filters, String column, boolean ascending) throws SQLException {

		String sql = "SELECT " + this.getPropertyColumns() + " FROM " + this.name
				+ " WHERE iscalculated = true AND state = false";
		String order = (ascending) ? ("ASC") : ("DESC");
		sql += this.filtersToQuery(filters);

		if (!column.equals("bfscode")) {
			sql += " ORDER BY " + column + " " + order;
		} else {
			sql += " ORDER BY bfscode, CHAR_LENGTH(bfscode) " + order;
		}
		return sql;
	}

	/**
	 * Generates the MySQL-Query necessary to get the values of a certain column
	 * @param filters determines how the MySQL-table should be filtered
	 * @param column determines which values should be returned
	 * @return the according MySQL-Query
	 */
	private String getValuesQuery(String[][] filters, String column) {
		String sql = "SELECT " + column + " FROM " + this.name + " WHERE 0 = 0";
		sql += this.filtersToQuery(filters);
		return sql;
	}

	/**
	 * Generates the part of a MySQL-Query that is responsible for filtering
	 * @param filters determines how the MySQL-table should be filtered
	 * @return the according part of a MySQL-Query
	 */
	private String filtersToQuery(String[][] filters) {
		String sql = "";
		for (int i = 0; i < filters.length; i++) {
			sql += " AND ";
			for (int j = 0; j < filters[i].length; j++) {
				sql += filters[i][j];
			}
		}
		return sql;
	}

	/**
	 * Returns the string of the BfsCode of a PropertyGraph by using its toString()-method
	 * @param graph a PropertyGraph-object
	 * @return the BfsCode of the given graph as String
	 */
	private String minimalBfsCodeToString(PropertyGraph<Integer, Integer> graph) {
		BfsCode bfs = (BfsCode) graph.getProperty(BfsCode.class);
		BfsCodeAlgorithm.BfsCode code = (BfsCodeAlgorithm.BfsCode) bfs.getValue();
		return code.toString();

	}

	/**
	 * Sets the id of a graph
	 * @param graph the PropertyGraph-object
	 * @throws SQLException if the connection to the database fails
	 */
	private void setIdGraph(PropertyGraph<Integer, Integer> graph) throws SQLException {
		try {
			if (this.getIds().contains(graph.getId())) {
				graph.setId(this.getId());
			}
		} catch (NullPointerException e) {
			graph.setId(this.getId());
		}
	}

}

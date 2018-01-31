package edu.kit.ipd.dbis.database.connection.tables;

import edu.kit.ipd.dbis.database.exceptions.sql.AccessDeniedForUserException;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.DatabaseDoesNotExistException;
import edu.kit.ipd.dbis.database.exceptions.sql.UnexpectedObjectException;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.DoubleProperty;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.IntegerProperty;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.BfsCode;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

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
	public PropertyGraph<Integer, Integer> getContent(int id)
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException,
			SQLException, IOException, ClassNotFoundException, UnexpectedObjectException {

		String sql = "SELECT graph FROM " + this.name + " WHERE id = " + id;
		ResultSet result = this.getConnection().prepareStatement(sql).executeQuery();
		if (result.next()) {
			Object object = this.byteArrayToObject(result.getBytes("graph"));
			return this.getInstanceOf(object);
		}
		return null;
	}

	private String filtersToString(String[][] filters, String column, boolean ascending) {

		String sql = "SELECT graph FROM " + this.name + " WHERE iscalculated = true";
		String order = (ascending) ? ("ASC") : ("DESC");

		for (int i = 0; i < filters.length; i++) {
			for (int j = 0; j < filters[i].length; j++) {
				sql += " AND" + filters[i][j];
			}
		}

		if (!column.equals("bfscode")) {
			sql += " ORDER BY " + column + " " + order;
		} else {
			//TODO: zuerst der Länge nach sortieren?
			sql += " ORDER BY bfscode, CHAR_LENGTH(bfscode) " + order;
		}
		return sql;
	}

	private String filtersToString(String[][] filters, String column) {
		String sql = "SELECT " + column + " FROM " + this.name + " WHERE 0 = 0";

		for (int i = 0; i < filters.length; i++) {
			for (int j = 0; j < filters[i].length; j++) {
				sql += " AND" + filters[i][j];
			}
		}
		return sql;
	}

	public LinkedList<Double> getValues(String[][] filters, String column) throws AccessDeniedForUserException,
			DatabaseDoesNotExistException, ConnectionFailedException, SQLException {

		String sql = this.filtersToString(filters, column);
		ResultSet result = this.getConnection().prepareStatement(sql).executeQuery();
		LinkedList<Double> values = new LinkedList<>();
		while (result.next()) {
			try {
				double value = (double) result.getObject(column);
				values.add(value);
			} catch(Exception e) {

			}
		}
		return values;
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
	public LinkedList<PropertyGraph<Integer, Integer>> getContent(String[][] filters, String column, boolean ascending)
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException,
			SQLException {

		String sql = this.filtersToString(filters, column, ascending);
		ResultSet result = this.getConnection().prepareStatement(sql).executeQuery();
		LinkedList<PropertyGraph<Integer, Integer>> graphs = new LinkedList<>();
		while (result.next()) {
			try {
				graphs.add((PropertyGraph) this.byteArrayToObject(result.getBytes("graph")));
			} catch(Exception e) {

			}
		}
		return graphs;
	}

	@Override
	public void insert(Serializable object) throws DatabaseDoesNotExistException, SQLException,
			AccessDeniedForUserException, ConnectionFailedException, IOException, UnexpectedObjectException {

		PropertyGraph graph = getInstanceOf(object);
		if (this.graphExists(graph)) return;
		Collection<Property> properties = graph.getProperties();
		String columns = "(";
		String values = "(";

		//TODO: getValue != null ?
		for (Property property : properties) {
			if (property.getClass().getSuperclass() == IntegerProperty.class) {
				if (property.getValue() != null) {
					columns += property.getClass().getSimpleName() + ", ";
					values += (int) property.getValue() + ", ";
				}
			} else if (property.getClass().getSuperclass() == DoubleProperty.class) {
				if (property.getValue() != null) {
					columns += property.getClass().getSimpleName() + ", ";
					values += (double) property.getValue() + ", ";
				}
			}
		}

		graph.setId(this.getId());
		columns += "graph, id, bfscode, state, iscalculated)";
		values += "?, " + graph.getId()
				+ ", '" + this.minimalBfsCodeToString(graph) + "'"
				+ ", " + false
				+ ", " + this.isCalculated(graph) + ")";

		String sql = "INSERT INTO " + this.name + " " + columns + " VALUES " + values;
		PreparedStatement statement = this.getConnection().prepareStatement(sql);
		statement.setObject(1, this.objectToByteArray(graph));
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
				+ "bfscode VARCHAR(255), "
				+ "state boolean, "
				+ "iscalculated boolean";

		PropertyGraph graph = new PropertyGraph();
		Collection<Property> properties = graph.getProperties();

		for (Property property : properties) {
			if (property.getClass().getSuperclass() == IntegerProperty.class) {
				sql += ", " + property.getClass().getSimpleName().toLowerCase() + " int";
			} else if (property.getClass().getSuperclass() == DoubleProperty.class) {
				sql += ", " + property.getClass().getSimpleName().toLowerCase() + " double";
			}
		}

		sql += ", PRIMARY KEY(id))";
		this.getConnection().prepareStatement(sql).executeUpdate();

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
				e.printStackTrace();
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
				found = (currentResult.getString(1).equals(otherResult.getString(1)));
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

		String sql = "DELETE * FROM " + this.name + " WHERE state = true";
		this.getConnection().prepareStatement(sql).executeUpdate();

	}

	/**
	 * Parses a BfsCode of a PropertyGraph
	 * @param graph a PropertyGraph-object
	 * @return the BfsCode of the given graph as String
	 */
	private String minimalBfsCodeToString(PropertyGraph graph) {

		String s = "";
		BfsCode bfs = (BfsCode) graph.getProperty(BfsCode.class);
		BfsCodeAlgorithm.BfsCode code = (BfsCodeAlgorithm.BfsCode) bfs.getValue();
		int[] bfsCode = code.getCode();

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

		String sql = "SELECT * FROM " + this.name + " WHERE BfsCode = '" + this.minimalBfsCodeToString(graph) + "'";
		ResultSet result = this.getConnection().prepareStatement(sql).executeQuery();
		if (result.next()) {
			return this.minimalBfsCodeToString(graph).equals(result.getString("bfscode"));
		}
		return false;

	}

	/**
	 * Checks if the given graph is calculated
	 * @param graph PropertyGraph-objcet
	 * @return true if all properties of graph have already been calculated
	 */
	private boolean isCalculated(PropertyGraph graph) {

		//TODO: eine methode die prüft, ob ein graph berechnet ist
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
	public LinkedList<PropertyGraph<Integer, Integer>> getUncalculatedGraphs() throws AccessDeniedForUserException,
			DatabaseDoesNotExistException, ConnectionFailedException, SQLException {

		String sql = "SELECT graph FROM " + this.name + " WHERE iscalculated = false";
		ResultSet result = this.getConnection().prepareStatement(sql).executeQuery();
		LinkedList<PropertyGraph<Integer, Integer>> graphs = new LinkedList<>();

		while (result.next()) {
			try {
				graphs.add((PropertyGraph<Integer, Integer>) this.byteArrayToObject(result.getBytes("graph")));
			} catch (Exception e) {

			}
		}
		return graphs;

	}

}

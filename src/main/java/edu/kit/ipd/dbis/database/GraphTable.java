package edu.kit.ipd.dbis.database;

import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;
import edu.kit.ipd.dbis.database.Exceptions.AccessDeniedForUserException;
import edu.kit.ipd.dbis.database.Exceptions.ConnectionFailedException;
import edu.kit.ipd.dbis.database.Exceptions.DatabaseDoesNotExistException;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.BfsCode;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.sql.*;
import java.util.*;

public class GraphTable extends Table {

	/**
	 * @param url
	 * @param user
	 * @param password
	 * @param name
	 */
	public GraphTable(String url, String user, String password, String name)
			throws SQLException, DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException {
		super(url, user, password, name);
	}

	@Override
	public PropertyGraph getContent(int id) throws Exception {
		Connection connection = this.getConnection();
		String sql = "SELECT graph FROM " + this.name + " WHERE id = " + id;
		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet result = statement.executeQuery();

		if (result.next()) {
			ByteArrayInputStream byteInput = new ByteArrayInputStream(result.getBytes("graph"));
			ObjectInputStream objectInput = new ObjectInputStream(byteInput);
			PropertyGraph graph = this.getInstanceOf(objectInput.readObject());
			byteInput.close();
			objectInput.close();
			return graph;
		}
		return null;
	}

	public void sortTable(String column, boolean ascending) throws Exception {
		if (column.equals("BfsCode")) return;
		Connection connection = this.getConnection();
		String s = (ascending) ? ("ASC") : ("DESC");
		String sql = "ALTER TABLE tablename ORDER BY columnname " + s;
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.executeUpdate();
	}

	public void sortByBfsCode(LinkedList<PropertyGraph> graphs, boolean ascending) {
		//TODO: warte auf Graphenpaket
	}

	public LinkedList<PropertyGraph> getContent(String[][] filters, String column, boolean ascending) throws Exception {
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
	public void insert(Serializable object) throws Exception {
		PropertyGraph graph = (PropertyGraph) object;
		Collection<Property> properties = graph.getProperties();
		String columns = "(";
		String values = "(";

		for (Property property : properties) {
			if (property.getClass().getSuperclass().getName().equals("IntegerProperty")
					|| property.getClass().getSuperclass().getName().equals("DoubleProperty")) {
				columns += property.getClass().getName() + ", ";
				values += property.getValue() + ", ";
			}
		}

		int id = this.getId();
		graph.setId(id);
		columns += "graph, id, BfsCode, state, isCalculated, nothing)";
		values += "?, ?, ?, ?, ?, ?)";

		String sql = "INSERT INTO " + this.name + " " + columns + " VALUES " + values;
		PreparedStatement statement = this.getConnection().prepareStatement(sql);
		statement.setObject(1, this.objectToByteArray(graph));
		statement.setObject(2, id);
		statement.setObject(3, this.minimalBfsCodeToString(graph));
		statement.setObject(4, false);
		statement.setObject(5, this.isCalculated(graph));
		statement.setObject(6, 0);
		statement.executeUpdate();
	}

	@Override
	protected PropertyGraph getInstanceOf(Object object) throws Exception {
		PropertyGraph graph = (object instanceof  PropertyGraph) ? (PropertyGraph) object : null;
		return graph;
	}

	@Override
	protected void createTable()
			throws SQLException, AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException {

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
			if (property.getClass().getSuperclass().getName().equals("IntegerProperty")) {
				sql += ", " + property.getClass().getName() + " int";
			} else if (property.getClass().getSuperclass().getName().equals("DoubleProperty")) {
				sql += ", " + property.getClass().getName() + " double";
			}
		}

		sql += ", nothing int, PRIMARY KEY(id))";
		Connection connection = this.getConnection();
		PreparedStatement create = connection.prepareStatement(sql);
		create.executeUpdate();
	}

	/**
	 *
	 * @param table
	 */
	public void merge(GraphTable table) throws Exception {
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
				found = (currentResult.getString(1) == (otherResult.getString(1))) ?
						(true) : (false);
			}
			if (!found) return false;
		}
		return true;
	}

	/**
	 *
	 */
	public void deleteAll() throws Exception {
		Connection connection = this.getConnection();
		String sql = "DELETE * FROM " + this.name + " WHERE state = true";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.executeUpdate();
	}

	/**
	 *
	 * @param graph
	 * @return
	 */
	private String minimalBfsCodeToString(PropertyGraph graph) {
		String s = "";
		int[] bfsCode = new int[0];
		Collection<Property> properties = graph.getProperties();

		for (Property property : properties) {
			if (property.getClass().getName().equals("BfsCode")) {
				bfsCode = (int[]) property.getValue();
			}
		}

		for (int i = 0; i < bfsCode.length; i++) {
			s += (i != bfsCode.length - 1) ? (bfsCode[i] + ";") : (bfsCode[i]);
		}
		return s;
	}

	/**
	 *
	 * @return
	 */
	public boolean graphExists(PropertyGraph graph) throws Exception {
		Connection connection = this.getConnection();
		String sql = "SELECT * FROM " + this.name + " WHERE BfsCode = " + this.minimalBfsCodeToString(graph);
		ResultSet result = connection.prepareStatement(sql).executeQuery();
		return result.next();
	}

	/**
	 *
	 * @return
	 */
	private boolean isCalculated(PropertyGraph graph) {
		Collection<Property> properties = graph.getProperties();
		for (Property property : properties) {
			if (property.getValue() == null) return false;
		}
		return true;
	}

}

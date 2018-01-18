package edu.kit.ipd.dbis.database;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class GraphTable extends Table {

	/**
	 * @param url
	 * @param user
	 * @param password
	 * @param name
	 */
	public GraphTable(String url, String user, String password, String name) throws Exception {
		super(url, user, password, name);
	}

	@Override
	public PropertyGraph getContent(int id) throws Exception {
		Connection connection = this.getConnection();
		String sql = "SELECT graph FROM " + this.name + " WHERE id = " + id;
		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet result = statement.executeQuery();
		ByteArrayInputStream byteInput;
		ObjectInputStream objectInput;
		if (result.next()) {
			byteInput = new ByteArrayInputStream(result.getBytes("graph"));
			objectInput = new ObjectInputStream(byteInput);
			return (PropertyGraph) objectInput.readObject();
		}
		return null;
	}


	public Set<PropertyGraph> getContent() throws Exception {
		Connection connection = this.getConnection();
		String sql = "SELECT graph FROM " + this.name;
		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet result = statement.executeQuery();
		Set<PropertyGraph> graphs = new HashSet<>();
		ByteArrayInputStream byteInput;
		ObjectInputStream objectInput;
		while (result.next()) {
			byteInput = new ByteArrayInputStream(result.getBytes("graph"));
			objectInput = new ObjectInputStream(byteInput);
			try {
				graphs.add((PropertyGraph) objectInput.readObject());
			} catch(Exception e) {

			}
		}
		return graphs;
	}

	@Override
	public void insert(Serializable object) throws Exception {
		//TODO: warte auf das Graphenpaket
	}

	@Override
	protected PropertyGraph getInstanceOf(Serializable object) throws Exception {
		PropertyGraph graph = (object instanceof  PropertyGraph) ? (PropertyGraph) object : null;
		if (graph != null) graph.setId(this.getId());
		return graph;
	}

	@Override
	protected void createTable() throws Exception {

		String sql = "CREATE TABLE IF NOT EXISTS "
				+ this.name +" ("
				+ "graph longblob, "
				+ "id int NOT NULL";
		PropertyGraph graph = new PropertyGraph();
		HashMap<Class<?>, Property> map = (HashMap) graph.getProperties();

		for (HashMap.Entry<Class<?>, Property> entry : map.entrySet()) {
			if (entry.getKey().getSuperclass().toString().equals("IntegerProperty")) {
				sql += ", " + entry.getKey().toString() + " int";
			} else if (entry.getKey().getSuperclass().toString().equals("DoubleProperty")) {
				sql += ", " + entry.getKey().toString() + " double";
			}
		}

		sql += ", nothing int, PRIMARY KEY(id))";
		Connection connection = this.getConnection();
		PreparedStatement create = connection.prepareStatement(sql);
		create.executeUpdate();
	}

	/**
	 *
	 * @param vertices
	 * @return
	 */
	public Set<PropertyGraph> getGraphsByVertex(int vertices) {
		//TODO: Gereon und Felix fragen, ob diese Methode gebraucht wird
		return null;
	}

	/**
	 *
	 * @param graphTable
	 */
	public void merge(GraphTable table) {

	}

	/**
	 *
	 */
	public void deleteAll() {

	}

	/**
	 *
	 * @return
	 */
	public LinkedList getIds() {
		return null;
	}

	/**
	 *
	 * @param graph
	 * @return
	 */
	private String minimalBfsCodeToString(PropertyGraph graph) {
		String s = "";
		int[] array = graph.getBfsCode().getValue();
		for (int i = 0; i < array.length; i++) {
			s += (i != array.length - 1) ? (array[i] + ";") : (array[i]);
		}
		return s;
	}

	/**
	 *
	 * @return
	 */
	public boolean graphExists(PropertyGraph graph) {
		return true;
	}

	/**
	 *
	 * @return
	 */
	private boolean isCalculated(PropertyGraph graph) {
		return true;
	}

}

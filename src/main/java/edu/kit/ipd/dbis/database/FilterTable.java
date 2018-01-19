package edu.kit.ipd.dbis.database;

import edu.kit.ipd.dbis.Controller.Filter.Filtersegment;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

public class FilterTable extends Table {

	/**
	 * @param url
	 * @param user
	 * @param password
	 * @param name
	 */
	public FilterTable(String url, String user, String password, String name) throws Exception {
		super(url, user, password, name);
	}

	@Override
	public Filtersegment getContent(int id) throws Exception {
		Connection connection = this.getConnection();
		String sql = "SELECT filter FROM " + this.name + " WHERE id = " + id;
		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet result = statement.executeQuery();
		ByteArrayInputStream byteInput;
		ObjectInputStream objectInput;
		if (result.next()) {
			byteInput = new ByteArrayInputStream(result.getBytes("filter"));
			objectInput = new ObjectInputStream(byteInput);
			return (Filtersegment) objectInput.readObject();
		}
		return null;
	}

	public Set<Filtersegment> getContent() throws Exception{
		Connection connection = this.getConnection();
		String sql = "SELECT filter FROM " + this.name;
		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet result = statement.executeQuery();
		Set<Filtersegment> filters = new HashSet<>();
		ByteArrayInputStream byteInput;
		ObjectInputStream objectInput;

		while (result.next()) {
			byteInput = new ByteArrayInputStream(result.getBytes("filter"));
			objectInput = new ObjectInputStream(byteInput);
			try {
				filters.add((Filtersegment) objectInput.readObject());
			} catch(Exception e) {

			}
		}
		return filters;
	}

	@Override
	public void insert(Serializable object) throws Exception {
		Filtersegment filter = this.getInstanceOf(object);
		String sql = "INSERT INTO " + this.name + " (filter, id, isActivated) VALUES (?, ?, ?)";
		PreparedStatement statement = this.getConnection().prepareStatement(sql);
		statement.setObject(1, this.objectToByteArray(filter));
		statement.setObject(2, filter.getID());
		statement.setObject(3, filter.getIsActivated());
		statement.executeUpdate();
	}

	@Override
	protected Filtersegment getInstanceOf(Object object) throws Exception {
		Filtersegment filter = (object instanceof  PropertyGraph) ? (Filtersegment) object : null;
		return filter;
	}

	@Override
	protected void createTable() throws Exception{
		Connection connection = this.getConnection();
		String sql = "CREATE TABLE IF NOT EXISTS "
				+ this.name +" ("
				+ "filter longblob, "
				+ "id int NOT NULL, "
				+ "isActivated boolean, "
				+ "PRIMARY KEY(id))";
		PreparedStatement create = connection.prepareStatement(sql);
		create.executeUpdate();
	}

}

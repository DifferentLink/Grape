package edu.kit.ipd.dbis.database;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Set;

public class FilterTable extends Table {

	/**
	 * @param url
	 * @param user
	 * @param password
	 * @param name
	 */
	public FilterTable(String url, String user, String password, String name) {
		super(url, user, password, name);
	}

	@Override
	public FilterSegment getContent(int id) throws Exception {
		Connection connection = this.getConnection();
		String sql = "SELECT filter FROM " + this.name + " WHERE id = " + id;
		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet result = statement.executeQuery();
		ByteArrayInputStream byteInput;
		ObjectInputStream objectInput;
		if (result.next()) {
			byteInput = new ByteArrayInputStream(result.getBytes("filter"));
			objectInput = new ObjectInputStream(byteInput);
			return (FilterSegment) objectInput.readObject();
		}
		return null;
	}

	@Override
	public Set<FilterSegment> getContent() throws Exception{
		Connection connection = this.getConnection();
		String sql = "SELECT filter FROM " + this.name;
		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet result = statement.executeQuery();
		Set<FilterSegment> filters = new Set<>();
		ByteArrayInputStream byteInput;
		ObjectInputStream objectInput;

		while (result.next()) {
			byteInput = new ByteArrayInputStream(result.getBytes("filter"));
			objectInput = new ObjectInputStream(byteInput);
			try {
				filters.add((FilterSegment) objectInput.readObject());
			} catch(Exception e) {

			}
		}
		return filters;
	}

	@Override
	public void insert(Serializable object) throws Exception {
		FilterSegment filter = this.getInstanceOf(object);
		String sql = "INSERT INTO " + this.name + " (filter, id, isActivated) VALUES (?, ?, ?)";
		PreparedStatement statement = this.getConnection().prepareStatement(sql);
		statement.setObject(1, this.objectToByteArray(filter));
		statement.setObject(2, filter.getId());
		statement.setObject(3, filter.getIsActivated());
		statement.executeUpdate();
	}

	@Override
	protected FilterSegment getInstanceOf(Serializable object) throws Exception {
		FilterSegment filter = (object instanceof  PropertyGraph) ? (FilterSegment) object : null;
		filter.setId(this.getId());
		return filter;
	}

	@Override
	protected void createTable() {

	}

}

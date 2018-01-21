package edu.kit.ipd.dbis.database;

import edu.kit.ipd.dbis.Filter.Filtersegment;
import edu.kit.ipd.dbis.database.Exceptions.AccessDeniedForUserException;
import edu.kit.ipd.dbis.database.Exceptions.ConnectionFailedException;
import edu.kit.ipd.dbis.database.Exceptions.DatabaseDoesNotExistException;
import edu.kit.ipd.dbis.database.Exceptions.UnexpectedObjectException;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class FilterTable extends Table {

	/**
	 * @param url
	 * @param user
	 * @param password
	 * @param name
	 */
	public FilterTable(String url, String user, String password, String name)
			throws DatabaseDoesNotExistException, SQLException, AccessDeniedForUserException,
			ConnectionFailedException {
		super(url, user, password, name);
	}

	@Override
	public Filtersegment getContent(int id)
			throws AccessDeniedForUserException, ConnectionFailedException, DatabaseDoesNotExistException,
			SQLException, IOException, ClassNotFoundException, UnexpectedObjectException {
		Connection connection = this.getConnection();
		String sql = "SELECT filter FROM " + this.name + " WHERE id = " + id;
		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet result = statement.executeQuery();
		if (result.next()) {
			return this.getInstanceOf(this.byteArrayToObject(result.getBytes("filter")));
		}
		throw new UnexpectedObjectException();
	}

	public Set<Filtersegment> getContent()
			throws AccessDeniedForUserException, ConnectionFailedException, DatabaseDoesNotExistException,
			SQLException {

		Connection connection = this.getConnection();
		String sql = "SELECT filter FROM " + this.name;
		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet result = statement.executeQuery();
		Set<Filtersegment> filters = new HashSet<>();

		while (result.next()) {
			try {
				filters.add(this.getInstanceOf(this.byteArrayToObject(result.getBytes("filter"))));
			} catch(Exception e) {

			}
		}
		return filters;
	}

	@Override
	public void insert(Serializable object)
			throws AccessDeniedForUserException, ConnectionFailedException, DatabaseDoesNotExistException,
			SQLException, UnexpectedObjectException, IOException {
		Filtersegment filter = this.getInstanceOf(object);
		String sql = "INSERT INTO " + this.name + " (filter, id, state) VALUES (?, ?, ?)";
		PreparedStatement statement = this.getConnection().prepareStatement(sql);
		statement.setObject(1, this.objectToByteArray(filter));
		statement.setObject(2, filter.getID());
		statement.setObject(3, filter.getIsActivated());
		statement.executeUpdate();
	}

	@Override
	protected Filtersegment getInstanceOf(Object object) throws UnexpectedObjectException {
		if (object instanceof Filtersegment) {
			return (Filtersegment) object;
		}
		throw new UnexpectedObjectException();
	}

	@Override
	protected void createTable()
			throws SQLException, AccessDeniedForUserException, DatabaseDoesNotExistException,
			ConnectionFailedException {
		Connection connection = this.getConnection();
		String sql = "CREATE TABLE IF NOT EXISTS "
				+ this.name +" ("
				+ "filter longblob, "
				+ "id int NOT NULL, "
				+ "state boolean, "
				+ "PRIMARY KEY(id))";
		PreparedStatement create = connection.prepareStatement(sql);
		create.executeUpdate();
	}

}

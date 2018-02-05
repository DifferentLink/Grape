package edu.kit.ipd.dbis.database.connection.tables;

import edu.kit.ipd.dbis.filter.Filtersegment;
import edu.kit.ipd.dbis.database.exceptions.sql.AccessDeniedForUserException;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.DatabaseDoesNotExistException;
import edu.kit.ipd.dbis.database.exceptions.sql.UnexpectedObjectException;

import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * This class represents a MySQL-Table where FilterSegment-Objects will be stored.
 */
public class FilterTable extends Table {

	/**
	 * Creates a new FilterTable.
	 * @param url location of the MySQL-Database that contains the MySQLTable which is represented by a subclass of Table.
	 * @param user username of the MySQL-Database user.
	 * @param password password of the user.
	 * @param name name of the MySQL-Table which is represented by a subclass of Table.
	 * @throws SQLException
	 * @throws DatabaseDoesNotExistException
	 * @throws AccessDeniedForUserException
	 * @throws ConnectionFailedException
	 */
	public FilterTable(String url, String user, String password, String name)
			throws DatabaseDoesNotExistException, SQLException, AccessDeniedForUserException,
			ConnectionFailedException {
		super(url, user, password, name);
	}

	@Override
	protected void createTable()
			throws SQLException, AccessDeniedForUserException, DatabaseDoesNotExistException,
			ConnectionFailedException {

		String sql = "CREATE TABLE IF NOT EXISTS "
				+ this.name +" ("
				+ "filter longblob, "
				+ "id int NOT NULL, "
				+ "state boolean, "
				+ "PRIMARY KEY(id))";
		this.getConnection().prepareStatement(sql).executeUpdate();
	}

	@Override
	protected Filtersegment getInstanceOf(Object object) throws UnexpectedObjectException {
		if (object instanceof Filtersegment) {
			return (Filtersegment) object;
		}
		throw new UnexpectedObjectException();
	}

	@Override
	public void insert(Serializable object)
			throws AccessDeniedForUserException, ConnectionFailedException, DatabaseDoesNotExistException,
			SQLException, UnexpectedObjectException, IOException {

		Filtersegment filter = this.getInstanceOf(object);
		String sql = "INSERT INTO " + this.name + " (filter, id, state) VALUES (?, "
				+ filter.getID() + ", "
				+ filter.getIsActivated() + ")";

		PreparedStatement statement = this.getConnection().prepareStatement(sql);
		statement.setObject(1, this.objectToByteArray(filter));
		statement.executeUpdate();
	}

	@Override
	public Filtersegment getContent(int id)
			throws AccessDeniedForUserException, ConnectionFailedException, DatabaseDoesNotExistException,
			SQLException, IOException, ClassNotFoundException, UnexpectedObjectException {

		String sql = "SELECT filter FROM " + this.name + " WHERE id = " + id;
		ResultSet result = this.getConnection().prepareStatement(sql).executeQuery();

		if (result.next()) {
			return this.getInstanceOf(this.byteArrayToObject(result.getBytes("filter")));
		}
		return null;
	}

	/**
	 * @return every FilterSegment-Object in the represented MySQL-Table.
	 * @throws AccessDeniedForUserException
	 * @throws ConnectionFailedException
	 * @throws DatabaseDoesNotExistException
	 * @throws SQLException
	 */
	public LinkedList<Filtersegment> getContent()
			throws AccessDeniedForUserException, ConnectionFailedException, DatabaseDoesNotExistException,
			SQLException {

		String sql = "SELECT filter FROM " + this.name;
		ResultSet result = this.getConnection().prepareStatement(sql).executeQuery();
		LinkedList<Filtersegment> filters = new LinkedList<>();

		while (result.next()) {
			try {
				filters.add(this.getInstanceOf(this.byteArrayToObject(result.getBytes("filter"))));
			} catch(Exception e) {

			}
		}
		return filters;
	}

}

package edu.kit.ipd.dbis.database;

import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;
import edu.kit.ipd.dbis.database.Exceptions.AccessDeniedForUserException;
import edu.kit.ipd.dbis.database.Exceptions.ConnectionFailedException;
import edu.kit.ipd.dbis.database.Exceptions.DatabaseDoesNotExistException;

import java.io.*;
import java.sql.*;
import java.util.*;

public abstract class Table {

	protected String url;
	protected String user;
	protected String password;
	protected String name;
	protected final String DRIVER = "com.mysql.jdbc.Driver";

	/**
	 *
	 * @param url
	 * @param user
	 * @param password
	 * @param name
	 */
	public Table(String url, String user, String password, String name)
			throws SQLException, DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException {
		this.url = url;
		this.user = user;
		this.password = password;
		this.name = name;
		this.createTable();
	}

	public String getUrl() {
		return this.url;
	}

	public String getUser() {
		return this.user;
	}

	public String getPassword() {
		return this.password;
	}

	public String getName() {
		return this.name;
	}

	/**
	 *
	 * @param id
	 */
	public void delete(int id)
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException,
			SQLException {
		Connection connection = this.getConnection();
		String sql = "DELETE * FROM " + this.name + " WHERE id = " + id;
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.executeUpdate();
	}

	/**
	 *
	 * @return
	 */
	public int getId()
			throws DatabaseDoesNotExistException, SQLException, AccessDeniedForUserException,
			ConnectionFailedException {
		LinkedList<Integer> ids = this.getIds();
		for (int i = 0; i < ids.size(); i++) {
			if (!ids.contains(i)) return i;
		}
		return ids.size() + 1;
	}

	/**
	 *
	 * @return
	 */
	public LinkedList<Integer> getIds()
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException,
			SQLException {
		Connection connection = this.getConnection();
		String sql = "SELECT id FROM " + this.name;
		ResultSet result = connection.prepareStatement(sql).executeQuery();
		LinkedList<Integer> ids = new LinkedList<>();
		while (result.next()) {
			ids.add(result.getInt("id"));
		}
		return ids;
	}

	/**
	 *
	 * @return
	 */
	public Connection getConnection()
			throws ConnectionFailedException, AccessDeniedForUserException, DatabaseDoesNotExistException {

		try {
			Class.forName(this.DRIVER);
			Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
			return connection;
		} catch (MySQLSyntaxErrorException e) {
			throw new DatabaseDoesNotExistException();
		} catch (SQLException e) {
			throw new AccessDeniedForUserException();
		} catch (Exception e) {
			throw new ConnectionFailedException();
		}
	}

	/**
	 *
	 * @param id
	 */
	public void switchState(int id)
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException,
			SQLException {
		Connection connection= this.getConnection();
		String sql = "SELECT state FROM " + this.name + " WHERE id = " + id;
		ResultSet result = connection.prepareStatement(sql).executeQuery();

		if (result.next()) {
			boolean value = (result.getInt("state") == 1) ? (false) : (true);
			sql = "UPDATE " + this.name + " SET state = " + value + " WHERE id = " + id;
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.executeUpdate();
		}

	}

	/**
	 *
	 * @param object
	 */
	public byte[] objectToByteArray(Serializable object) throws IOException {
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
		ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);
		objectOutput.writeObject(object);
		byte[] bytes = byteOutput.toByteArray();

		byteOutput.close();
		objectOutput.close();
		return bytes;
	}

	public Object byteArrayToObject(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream byteInput = new ByteArrayInputStream(bytes);
		ObjectInputStream objectInput = new ObjectInputStream(byteInput);
		Object object = objectInput.readObject();
		byteInput.close();
		objectInput.close();
		return object;
	}

	public HashSet<String> getColumns()
			throws AccessDeniedForUserException, ConnectionFailedException, DatabaseDoesNotExistException,
			SQLException {
		Connection connection = this.getConnection();
		String sql = "SHOW COLUMNS FROM " + this.name;
		ResultSet result = connection.prepareStatement(sql).executeQuery();
		HashSet<String> columns = new HashSet<>();
		while (result.next()) {
			columns.add(result.getString(1));
		}
		return columns;
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	public abstract Serializable getContent(int id) throws Exception;

	/**
	 *
	 * @param object
	 */
	public abstract void insert(Serializable object) throws Exception;

	/**
	 *
	 * @return
	 * @throws Exception
	 */
	protected abstract Serializable getInstanceOf(Object object) throws Exception;

	protected abstract void createTable()
			throws SQLException, AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException;

}

package edu.kit.ipd.dbis.database.connection.tables;

import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;
import edu.kit.ipd.dbis.database.exceptions.sql.AccessDeniedForUserException;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.DatabaseDoesNotExistException;
import edu.kit.ipd.dbis.database.exceptions.sql.UnexpectedObjectException;

import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * This class represents a MySQL-Table.
 */
public abstract class Table {

	protected String url;
	protected String user;
	protected String password;
	protected String name;
	protected final String DRIVER = "com.mysql.jdbc.Driver";
	protected Connection connection;

	/**
	 * Creates a new Table.
	 * @param url location of the MySQL-Database that contains the MySQLTable which is represented by a subclass of Table.
	 * @param user username of the MySQL-Database user.
	 * @param password password of the user.
	 * @param name name of the MySQL-Table which is represented by a subclass of Table.
	 * @throws SQLException
	 * @throws DatabaseDoesNotExistException
	 * @throws AccessDeniedForUserException
	 * @throws ConnectionFailedException
	 */
	public Table(String url, String user, String password, String name)
			throws SQLException, DatabaseDoesNotExistException, AccessDeniedForUserException,
			ConnectionFailedException {
		this.url = url + "?autoReconnect=true&useSSL=false";
		this.user = user;
		this.password = password;
		this.name = name;
		this.connection = null;
		this.createTable();
	}

	/**
	 * getter-method
	 * @return the url
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * getter-method
	 * @return the user
	 */
	public String getUser() {
		return this.user;
	}

	/**
	 * getter-method
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * getter-method
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Deletes the identified row from the represented MySQL-Table.
	 * @param id identifies a row in the represented MySQL-Table.
	 * @throws AccessDeniedForUserException
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 * @throws SQLException
	 */
	public void delete(int id)
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException,
			SQLException {

		String sql = "DELETE FROM " + this.name + " WHERE id = " + id;
		this.getConnection().prepareStatement(sql).executeUpdate();
	}

	/**
	 *
	 * @return Value of the next free id.
	 * @throws DatabaseDoesNotExistException
	 * @throws SQLException
	 * @throws AccessDeniedForUserException
	 * @throws ConnectionFailedException
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
	 * @return all ids in the represented MySQL-Table
	 * @throws AccessDeniedForUserException
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 * @throws SQLException
	 */
	public LinkedList<Integer> getIds()
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException,
			SQLException {

		String sql = "SELECT id FROM " + this.name;
		ResultSet result = this.getConnection().prepareStatement(sql).executeQuery();
		LinkedList<Integer> ids = new LinkedList<>();
		while (result.next()) {
			ids.add(result.getInt("id"));
		}
		return ids;
	}

	/**
	 * Creates and returns a Connection-Objcect.
	 * @return the Connection to the MySQL-Database.
	 * @throws ConnectionFailedException
	 * @throws AccessDeniedForUserException
	 * @throws DatabaseDoesNotExistException
	 */
	public Connection getConnection()
			throws ConnectionFailedException, AccessDeniedForUserException, DatabaseDoesNotExistException {
		try {
			if (this.connection == null) {
				Class.forName(this.DRIVER);
				this.connection = DriverManager.getConnection(this.url, this.user, this.password);
				return this.connection;
			} else {
				return this.connection;
			}

		} catch (MySQLSyntaxErrorException e) {
			throw new DatabaseDoesNotExistException();
		} catch (SQLException e) {
			throw new AccessDeniedForUserException();
		} catch (Exception e) {
			throw new ConnectionFailedException();
		}
	}

	/**
	 * Functionality will be described in the subclasses.
	 * @param id identifies a row in the represented MySQL-Table.
	 * @throws AccessDeniedForUserException
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 * @throws SQLException
	 */
	public void switchState(int id)
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException,
			SQLException {

		String sql = "SELECT state FROM " + this.name + " WHERE id = " + id;
		ResultSet result = this.getConnection().prepareStatement(sql).executeQuery();
		if (result.next()) {
			boolean value = (result.getInt("state") != 1);
			sql = "UPDATE " + this.name + " SET state = " + value + " WHERE id = " + id;
			this.getConnection().prepareStatement(sql).executeUpdate();
		}

	}

	/**
	 * converts a object into a byte-array
	 * @param object the object that should be converted
	 * @return the byte-array
	 * @throws IOException
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

	/**
	 * converts a byte-array into a object
	 * @param bytes the byte-array that should be converted
	 * @return the object
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Object byteArrayToObject(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream byteInput = new ByteArrayInputStream(bytes);
		ObjectInputStream objectInput = new ObjectInputStream(byteInput);
		Object object = objectInput.readObject();
		byteInput.close();
		objectInput.close();
		return object;
	}

	/**
	 *
	 * @return all columns of the MySQL-Table represented by this object
	 * @throws AccessDeniedForUserException
	 * @throws ConnectionFailedException
	 * @throws DatabaseDoesNotExistException
	 * @throws SQLException
	 */
	public LinkedList<String> getColumns()
			throws AccessDeniedForUserException, ConnectionFailedException, DatabaseDoesNotExistException,
			SQLException {

		String sql = "SHOW COLUMNS FROM " + this.name;
		ResultSet result = this.getConnection().prepareStatement(sql).executeQuery();
		LinkedList<String> columns = new LinkedList<>();
		while (result.next()) {
			columns.add(result.getString(1));
		}
		return columns;
	}

	/**
	 * returns the Object identified by the given id.
	 * @param id identifies the object
	 * @return every object in the represented MySQL-Table
	 * @throws AccessDeniedForUserException
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 * @throws SQLException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws UnexpectedObjectException
	 */
	public abstract Serializable getContent(int id)
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException,
			SQLException, IOException, ClassNotFoundException, UnexpectedObjectException;

	/**
	 * Inserts the given object into the MySQL-Table.
	 * @param object Java object that should be inserted into the represented MySQL-Table.
	 * @throws DatabaseDoesNotExistException
	 * @throws SQLException
	 * @throws AccessDeniedForUserException
	 * @throws ConnectionFailedException
	 * @throws IOException
	 * @throws UnexpectedObjectException
	 */
	public abstract void insert(Serializable object)
			throws DatabaseDoesNotExistException, SQLException, AccessDeniedForUserException,
			ConnectionFailedException, IOException, UnexpectedObjectException;

	/**
	 * decides if a object is a Filtersegment or a PropertyGraph
	 * @param object the given object
	 * @return a Filtersegment or a PropertyGraph
	 * @throws UnexpectedObjectException
	 */
	protected abstract Serializable getInstanceOf(Object object) throws UnexpectedObjectException;

	/**
	 * Creates the MySQL-Table that should be represented by this Class
	 * @throws SQLException
	 * @throws AccessDeniedForUserException
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 */
	protected abstract void createTable()
			throws SQLException, AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException;

}

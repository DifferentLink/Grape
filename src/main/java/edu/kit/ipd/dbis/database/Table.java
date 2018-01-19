package edu.kit.ipd.dbis.database;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
	public Table(String url, String user, String password, String name) throws Exception {
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
	public void delete(int id) {

	}

	/**
	 *
	 * @return
	 */
	public int getId() {
		return 1;
	}

	/**
	 *
	 * @return
	 */
	public Connection getConnection() throws Exception {
		Class.forName(this.DRIVER);
		Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
		return connection;
	}

	/**
	 *
	 * @param id
	 */
	public void switchState(int id) {

	}

	/**
	 *
	 * @param object
	 */
	public byte[] objectToByteArray(Serializable object) throws Exception {
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
		ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);
		objectOutput.writeObject(object);
		return byteOutput.toByteArray();
	}

	public HashSet<String> getColumns() throws Exception {
		Connection connection = this.getConnection();
		String sql = "SHOW COLUMNS FROM " + this.name;
		ResultSet result = connection.prepareStatement(sql).executeQuery();
		HashSet<String> columns = new HashSet<>();
		while (result.next()) {
			columns.add(result.toString());
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
	protected abstract Serializable getInstanceOf(Serializable object) throws Exception;

	protected abstract void createTable() throws Exception;

}

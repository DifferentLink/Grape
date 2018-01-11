package edu.kit.ipd.dbis.database;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;

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
	public Table(String url, String user, String password, String name) {
		this.url = url;
		this.user = user;
		this.password = password;
		this.name = name;
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
	public void insert(Serializable object) throws Exception {

	}

	/**
	 *
	 * @param id
	 * @return
	 */
	public abstract Serializable getContent(int id);

	/**
	 *
	 * @param object
	 */
	public abstract void insertColumns(Serializable object);

	/**
	 *
	 * @return
	 * @throws Exception
	 */
	protected abstract Serializable getInstanceOf(Serializable object) throws Exception;


}

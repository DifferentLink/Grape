package edu.kit.ipd.dbis.database;

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
	 * @return
	 */
	public Connection getConnection() throws Exception {
		Class.forName(this.DRIVER);
		Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
		return connection;
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
	 * @param id
	 */
	public void delete(int id) {

	}

	/**
	 *
	 * @throws Exception
	 */
	protected abstract void createTable() throws Exception;

}

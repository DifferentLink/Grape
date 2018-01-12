package edu.kit.ipd.dbis.database;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
	public Table(String url, String user, String password, String name) {
		this.url = url;
		this.user = user;
		this.password = password;
		this.name = name;
		this.createTable();
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

	/**
	 *
	 * @param id
	 * @return
	 */
	public abstract Serializable getContent(int id);

	public abstract Set<Serializable> getContent() throws Exception;

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

	protected abstract void createTable();

}

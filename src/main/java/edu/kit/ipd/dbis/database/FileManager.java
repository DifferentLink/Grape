package edu.kit.ipd.dbis.database;

import java.sql.Connection;

public class FileManager implements Connector {

	/**
	 *
	 */
	public FileManager() {

	}

	@Override
	public GraphDatabase createGraphDatabase(String url, String user, String password, String name) throws Exception {
		return null;
	}

	@Override
	public void saveGraphDatabase(String directory, GraphDatabase database) throws Exception {

	}

	@Override
	public GraphDatabase loadGraphDatabase(String directory) throws Exception {
		return null;
	}

	@Override
	public void deleteGraphDatabase(GraphDatabase database) {

	}

	/**
	 *
	 * @param connection
	 * @param name
	 * @return
	 */
	private boolean tableExists(Connection connection, String name) {
		return true;
	}

	/**
	 *
	 * @param url
	 * @param user
	 * @param password
	 * @return
	 */
	private Connection getConnection(String url, String user, String password) {
		return null;
	}

	/**
	 *
	 * @param connection
	 * @param name
	 * @return
	 */
	private String getValidFilterTableName(Connection connection, String name) {
		return null;
	}

}

package edu.kit.ipd.dbis.database;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class FileManager implements Connector {

	/**
	 *
	 */
	public FileManager() {

	}

	@Override
	public GraphDatabase createGraphDatabase(String url, String user, String password, String name) throws Exception {
		Connection connection = getConnection(url, user, password);
		if (tableExists(connection, name)) {
			throw new Exception();
		}
		FilterTable filterTable = new FilterTable(url, user, password, name);
		GraphTable graphTable = new GraphTable(url, user, password, getValidFilterTableName(connection, name));
		return new GraphDatabase(graphTable, filterTable);
	}

	@Override
	public void saveGraphDatabase(String directory, GraphDatabase database) throws Exception {
		File file = new File(directory);
		if (database.getDirectory() != null) {
			throw new Exception();
		} else if (file.exists()) {
			throw new Exception();
		} else {
			Parser save = new SaveParser(database);
			save.parse();
			Files.write(Paths.get(directory), save.getInformation().getBytes());
		}
	}

	@Override
	public GraphDatabase loadGraphDatabase(String directory) throws Exception {
		FileReader file = new FileReader(directory);
		BufferedReader reader = new BufferedReader(file);
		Parser load = new LoadParser(reader.readLine());
		load.parse();
		file.close();
		reader.close();
		return load.getDatabase();
	}

	@Override
	public void deleteGraphDatabase(GraphDatabase database) {
		//TODO: implementieren?
	}

	/**
	 *
	 * @param connection
	 * @param name
	 * @return
	 */
	private boolean tableExists(Connection connection, String name) throws Exception {
		DatabaseMetaData meta = connection.getMetaData();
		ResultSet resultSet = meta.getTables(null, null, null, null);
		while (resultSet.next()) {
			if (resultSet.getString(3).equals(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 *
	 * @param url
	 * @param user
	 * @param password
	 * @return
	 */
	private Connection getConnection(String url, String user, String password) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection(url, user, password);
		return connection;
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

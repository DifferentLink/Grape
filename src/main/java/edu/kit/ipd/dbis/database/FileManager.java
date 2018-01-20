package edu.kit.ipd.dbis.database;

import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;
import edu.kit.ipd.dbis.database.Exceptions.*;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;

public class FileManager implements Connector {

	/**
	 *
	 */
	public FileManager() {

	}

	@Override
	public GraphDatabase createGraphDatabase(String url, String user, String password, String name)
			throws TableAlreadyExistsException, MySQLDatabaseDoesNotExistException, AccessDeniedForUserException,
			ConnectionFailedException, SQLException {

		Connection connection = getConnection(url, user, password);
		if (tableExists(connection, name)) {
			throw new TableAlreadyExistsException();
		}
		GraphTable graphTable = new GraphTable(url, user, password, name);
		FilterTable filterTable = new FilterTable(url, user, password, getValidFilterTableName(connection, name));
		return new GraphDatabase(graphTable, filterTable);

	}

	@Override
	public void saveGraphDatabase(String directory, GraphDatabase database)
			throws GraphDatabaseAlreadySavedException, FileNameAlreadyTakenException, FileCouldNotBeSavedException {
		File file = new File(directory);
		if (database.getDirectory() != null) {
			throw new GraphDatabaseAlreadySavedException();
		} else if (file.exists()) {
			throw new FileNameAlreadyTakenException();
		} else {
			SaveParser save = new SaveParser(database);
			save.parse();
			try {
				Files.write(Paths.get(directory), save.getInformation().getBytes());
			} catch (Exception e) {
				throw new FileCouldNotBeSavedException();
			}
		}
	}

	@Override
	public GraphDatabase loadGraphDatabase(String directory)
			throws FileNotFoundException, FileContentNotAsExpectedException, SQLException, AccessDeniedForUserException,
			ConnectionFailedException, MySQLDatabaseDoesNotExistException, FileContentCouldNotBeReadException,
			TablesNotAsExpectedException {

		FileReader file = new FileReader(directory);
		BufferedReader reader = new BufferedReader(file);
		LoadParser load = null;
		try {
			load = new LoadParser(reader.readLine());
			file.close();
			reader.close();
		} catch (Exception e) {
			throw new FileContentCouldNotBeReadException();
		}

		load.parse();
		GraphDatabase database = load.getDatabase();
		if (this.validGraphTable(database.getGraphTable()) && this.validFilterTable(database.getFilterTable())) {
			database.setDirectory(directory);
			return database;
		}
		throw new TablesNotAsExpectedException();
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
	private boolean tableExists(Connection connection, String name) throws SQLException {
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
	private Connection getConnection(String url, String user, String password)
			throws AccessDeniedForUserException, MySQLDatabaseDoesNotExistException, ConnectionFailedException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(url, user, password);
			return connection;
		} catch (MySQLSyntaxErrorException e) {
			throw new MySQLDatabaseDoesNotExistException();
		} catch (SQLException e) {
			throw new AccessDeniedForUserException();
		} catch (Exception e) {
			throw new ConnectionFailedException();
		}
	}

	/**
	 *
	 * @param connection
	 * @param name
	 * @return
	 */
	private String getValidFilterTableName(Connection connection, String name) throws SQLException {
		String filterTable = name + "Filters";
		String s = filterTable;
		int i = 0;

		while (tableExists(connection, s)) {
			s  = filterTable + i;
			i++;
		}
		return s;
	}

	private boolean validFilterTable(FilterTable filterTable)
			throws SQLException, AccessDeniedForUserException, ConnectionFailedException, MySQLDatabaseDoesNotExistException {

		HashSet<String> columns = filterTable.getColumns();
		HashSet<String> names = new HashSet<>();
		names.add("id");
		names.add("state");
		names.add("filter");
		if (columns.containsAll(names) && names.containsAll(names)) {
			return true;
		}
		return false;
	}

	private boolean validGraphTable(GraphTable graphTable)
			throws SQLException, AccessDeniedForUserException, ConnectionFailedException, MySQLDatabaseDoesNotExistException {
		//TODO: columns
		HashSet<String> columns = graphTable.getColumns();
		HashSet<String> names = new HashSet<>();
		names.add("id");
		names.add("graph");
		names.add("BfsCode");
		names.add("nothing");
		names.add("state");
		names.add("isCalculated");

		PropertyGraph graph = new PropertyGraph();
		HashMap<Class<?>, Property> map = (HashMap) graph.getProperties();

		for (HashMap.Entry<Class<?>, Property> entry : map.entrySet()) {
			if (entry.getKey().getSuperclass().toString().equals("IntegerProperty")) {
				names.add(entry.getKey().toString());
			} else if (entry.getKey().getSuperclass().toString().equals("DoubleProperty")) {
				names.add(entry.getKey().toString());
			}
		}

		if (columns.containsAll(names) && names.containsAll(columns)) {
			return true;
		}
		return false;
	}

}

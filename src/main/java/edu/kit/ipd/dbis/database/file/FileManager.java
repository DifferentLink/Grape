package edu.kit.ipd.dbis.database.file;

import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;
import edu.kit.ipd.dbis.database.exceptions.files.*;
import edu.kit.ipd.dbis.database.exceptions.sql.*;
import edu.kit.ipd.dbis.database.file.parsers.LoadParser;
import edu.kit.ipd.dbis.database.file.parsers.SaveParser;
import edu.kit.ipd.dbis.database.connection.tables.FilterTable;
import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.connection.tables.GraphTable;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Collection;
import java.util.HashSet;

public class FileManager implements Connector {

	/**
	 * Constructor
	 */
	public FileManager() {

	}

	@Override
	public GraphDatabase createGraphDatabase(String url, String user, String password, String name)
			throws TableAlreadyExistsException, DatabaseDoesNotExistException, AccessDeniedForUserException,
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
			ConnectionFailedException, DatabaseDoesNotExistException, FileContentCouldNotBeReadException,
			TablesNotAsExpectedException {

		FileReader file = new FileReader(directory);
		BufferedReader reader = new BufferedReader(file);
		LoadParser load;
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
	 * Checks if a MySQL-Table with the given name already exists.
	 * @param connection the Connection to a MySQL-Database.
	 * @param name name of a MySQL-Table.
	 * @return true if there already is a MySQL-Table with the given name
	 * @throws SQLException
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
	 * Creates and returns a Connection-Objcect.
	 * @param url localizes the MySQL-Database in which the data should be stored.
	 * @param user username of the MySQL-Database user.
	 * @param password password of the user.
	 * @return the Connection to the MySQL-Database.
	 * @throws AccessDeniedForUserException
	 * @throws DatabaseDoesNotExistException
	 * @throws ConnectionFailedException
	 */
	private Connection getConnection(String url, String user, String password)
			throws AccessDeniedForUserException, DatabaseDoesNotExistException, ConnectionFailedException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(url, user, password);
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
	 * Returns a name for a MySQL-Table that does not already exist.
	 * @param name name of the GraphTable-Object.
	 * @return a Valid name that can be used to create a FilterTable-Object.
	 * @throws SQLException
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

	/**
	 * Determines whether the given FilterTable-Object has the required columns
	 * @param filterTable the FilterTable-Object
	 * @return true if filterTable is valid
	 * @throws SQLException
	 * @throws AccessDeniedForUserException
	 * @throws ConnectionFailedException
	 * @throws DatabaseDoesNotExistException
	 */
	private boolean validFilterTable(FilterTable filterTable)
			throws SQLException, AccessDeniedForUserException, ConnectionFailedException,
			DatabaseDoesNotExistException {

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

	/**
	 * Determines whether the given GraphTable-Object has the required columns
	 * @param graphTable the GraphTable-Object
	 * @return true if filterTable is valid
	 * @throws SQLException
	 * @throws AccessDeniedForUserException
	 * @throws ConnectionFailedException
	 * @throws DatabaseDoesNotExistException
	 */
	private boolean validGraphTable(GraphTable graphTable)
			throws SQLException, AccessDeniedForUserException, ConnectionFailedException,
			DatabaseDoesNotExistException {

		HashSet<String> columns = graphTable.getColumns();
		HashSet<String> names = new HashSet<>();
		names.add("id");
		names.add("graph");
		names.add("BfsCode");
		names.add("nothing");
		names.add("state");
		names.add("isCalculated");

		PropertyGraph graph = new PropertyGraph();
		Collection<Property> properties = graph.getProperties();

		for (Property property : properties) {
			if (property.getClass().getSuperclass().getSimpleName().equals("IntegerProperty")) {
				names.add(property.toString());
			} else if (property.getClass().getSuperclass().getSimpleName().equals("DoubleProperty")) {
				names.add(property.toString());
			}
		}

		if (columns.containsAll(names) && names.containsAll(columns)) {
			return true;
		}
		return false;
	}

}

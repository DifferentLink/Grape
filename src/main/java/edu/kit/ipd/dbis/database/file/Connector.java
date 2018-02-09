package edu.kit.ipd.dbis.database.file;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.files.*;
import edu.kit.ipd.dbis.database.exceptions.sql.*;

import java.io.FileNotFoundException;
import java.sql.SQLException;

/**
 *
 */
public interface Connector {

	/**
	 *
	 * Creates and returns a new GraphDatabase-Object.
	 * @param url localizes the MySQL-Database in which the data should be stored.
	 * @param user username of the MySQL-Database user.
	 * @param password password of the user.
	 * @param name determines the name of the MySQL-Table in which the Graphs will be stored.
	 * @return GraphDatabase-Object will be created and returned.
	 * @throws DatabaseDoesNotExistException if the MySQL-database does not exist
	 * @throws AccessDeniedForUserException if the username or the password is invalid
	 * @throws ConnectionFailedException if the selected table does not contain the required columns
	 */
	GraphDatabase createGraphDatabase(String url, String user, String password, String name)
			throws DatabaseDoesNotExistException, AccessDeniedForUserException,
			ConnectionFailedException;

	/**
	 * Saves the information of the given GraphDatabase-Object in a text file.
	 * @param directory localizes where the text file will be saved.
	 * @param database GraphDatabase that will be saved as text file.
	 * @throws FileNameAlreadyTakenException if the given directory already exists
	 * @throws FileCouldNotBeSavedException if saving the file fails
	 */
	void saveGraphDatabase(String directory, GraphDatabase database)
			throws FileNameAlreadyTakenException, FileCouldNotBeSavedException;

	/**
	 *
	 * Creates and returns a GraphDatabse-Object from the information contained by the given file.
	 * @param directory localizes the text file from which a GraphDatabase-Object can be created.
	 * @return GraphDatabase-Object that contains the connection to an already used MySQL-Database.
	 * @throws FileContentNotAsExpectedException if the given file does not contain the required information
	 * @throws FileContentCouldNotBeReadException if the file content is not accessible
	 * @throws ConnectionFailedException if the selected table does not contain the required columns
	 * @throws FileNotFoundException if the directory is invalid
	 */
	GraphDatabase loadGraphDatabase(String directory)
			throws FileNotFoundException, FileContentNotAsExpectedException, ConnectionFailedException,
			FileContentCouldNotBeReadException;

	/**
	 * Deletes given database by deleting its MySQL-Tables and the text file that belongs to it.
	 * @param database GraphDatabase-Object that should be deleted.
	 */
	void deleteGraphDatabase(GraphDatabase database) 
			throws DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException;

}

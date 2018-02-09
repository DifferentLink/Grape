package edu.kit.ipd.dbis.database.file.parsers;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.files.FileContentNotAsExpectedException;

/**
 *
 */
public abstract class Parser {

	protected String information;
	protected GraphDatabase database;

	/**
	 * creates a GraphDatabase-Object or the content of the text file that should be created by the FileManager
	 * @throws FileContentNotAsExpectedException if no GraphDatabase-object could not be created
	 *                                           using the content of the given file
	 */
	public abstract void parse() throws FileContentNotAsExpectedException;

	/**
	 * getter-method for information
	 * @return parsed information
	 */
	public String getInformation() {
		return information;
	}

	/**
	 * getter-method for graphDatabase
	 * @return parsed graphDatabase
	 */
	public GraphDatabase getDatabase() {
		return database;
	}
}

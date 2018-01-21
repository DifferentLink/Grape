package edu.kit.ipd.dbis.database.file.parsers;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;

/**
 *
 */
public abstract class Parser {

	protected String information;
	protected GraphDatabase database;

	/**
	 *
	 */
	public abstract void parse() throws Exception;

	public String getInformation() {
		return information;
	}

	public GraphDatabase getDatabase() {
		return database;
	}
}

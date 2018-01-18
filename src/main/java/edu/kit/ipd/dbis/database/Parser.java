package edu.kit.ipd.dbis.database;

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

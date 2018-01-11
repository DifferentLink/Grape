package edu.kit.ipd.dbis.database;

/**
 *
 */
public interface Connector {

	/**
	 *
	 * @param url
	 * @param user
	 * @param password
	 * @param name
	 * @return
	 */
	GraphDatabase createGraphDatabase(String url, String user, String password, String name) throws Exception;

	/**
	 *
	 * @param directory
	 * @param database
	 */
	void saveGraphDatabase(String directory, GraphDatabase database) throws Exception;

	/**
	 *
	 * @param directory
	 * @return
	 */
	GraphDatabase loadGraphDatabase(String directory) throws Exception;

	/**
	 *
	 * @param database
	 */
	void deleteGraphDatabase(GraphDatabase database);

}

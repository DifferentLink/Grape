package edu.kit.ipd.dbis.controller;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.file.Connector;
import edu.kit.ipd.dbis.database.file.FileManager;

import java.util.List;

public class DatabaseController {

	private GenerateController generate;
	private CalculationController calculation;
	private GraphEditorController editor;
	private FilterController filter;

	private Connector connector;
	private GraphDatabase database;


	public DatabaseController() {
		generate = GenerateController.getInstance();
		calculation = CalculationController.getInstance();
		editor = GraphEditorController.getInstance();
		// filter = FilterController.getInstance();
		connector = new FileManager();
	}

	/**
	 * Triggers the database to open a new database table.
	 */
	public void newDatabase(String url, String user, String password, String name) throws Exception {
		database = connector.createGraphDatabase(url, user, password, name);
		this.updateDatabases();
	}

	/**
	 * Triggers the database to open the database table at the given file path.
	 *
	 * @param filepath the file path of the database.
	 */
	public void loadDatabase(String filepath) throws Exception {
		database = connector.loadGraphDatabase(filepath);
		this.updateDatabases();
	}

	/**
	 * Triggers the database to merge the database table at the given path with the current database table.
	 *
	 * @param filepath the file path of the Database.
	 */
	public void mergeDatabase(String filepath) throws Exception {
		GraphDatabase mergeDatabase;
		mergeDatabase = connector.loadGraphDatabase(filepath);
		database.merge(mergeDatabase);
		this.updateDatabases();
	}

	/**
	 * Triggers the database to save the current database table at the given file path.
	 *
	 * @param filepath the file path of the Database.
	 */
	public void saveDatabase(String filepath) {
		database.setDirectory(filepath);
	}

	/**
	 * Triggers the database to save the current selected graphs in the table at the given path.
	 *
	 * @param filepath the fille path of the Database.
	 * @param graphIDs the GraphIDs to save.
	 */
	public void saveSelection(String filepath, List<Integer> graphIDs) {

	}

	/**
	 * Sets the database for all controller classes that have a database connection
	 */
	private void updateDatabases() throws Exception {
		generate.setDatabase(database);
		calculation.setDatabase(database);
		editor.setDatabase(database);
		//filter.setDatabase(database);
	}

}

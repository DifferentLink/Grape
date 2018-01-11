package edu.kit.ipd.dbis.Controller;

import edu.kit.ipd.dbis.database.Connector;
import edu.kit.ipd.dbis.database.FileManager;
import edu.kit.ipd.dbis.database.GraphDatabase;

import java.util.List;

public class DatabaseController {

	private GenerateController generate;
	private CalculationController calculation;
	private GraphEditorController editor;


	private Connector connector;
	private GraphDatabase database;


	public DatabaseController() {
		generate = GenerateController.getInstance();
		calculation = CalculationController.getInstance();
		editor = GraphEditorController.getInstance();
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
	public void newDatabase(String filepath) {

	}

	/**
	 * Triggers the database to merge the database table at the given path with the current database table.
	 *
	 * @param filepath the file path of the Database.
	 */
	public void mergeDatabase(String filepath) {

	}

	/**
	 * Triggers the database to save the current database table at the given file path.
	 *
	 * @param filepath the file path of the Database.
	 */
	public void saveDatabase(String filepath) {

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
	private void updateDatabases() {
		generate.setDatabase(database);
		calculation.setDatabase(database);
		editor.setDatabase(database);
	}

}

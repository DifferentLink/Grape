package edu.kit.ipd.dbis.Controller;

public class DatabaseController {

	private GenerateController generate = GenerateController.getInstance();
	private CalculationController calculation = CalculationController.getInstance();
	private GraphEditorController editor = GraphEditorController.getInstance();

	private Database database;

	/**
	 * Triggers the database to open a new database table.
	 */
	public void newDatabase() {

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
	public void saveSelection(String filepath, List<Int> graphIDs) {

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

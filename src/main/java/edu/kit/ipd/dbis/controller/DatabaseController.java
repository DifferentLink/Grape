package edu.kit.ipd.dbis.controller;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.files.FileContentCouldNotBeReadException;
import edu.kit.ipd.dbis.database.exceptions.files.FileContentNotAsExpectedException;
import edu.kit.ipd.dbis.database.exceptions.files.FileCouldNotBeSavedException;
import edu.kit.ipd.dbis.database.exceptions.files.FileNameAlreadyTakenException;
import edu.kit.ipd.dbis.database.exceptions.sql.AccessDeniedForUserException;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.DatabaseDoesNotExistException;
import edu.kit.ipd.dbis.database.file.Connector;
import edu.kit.ipd.dbis.database.file.FileManager;
import edu.kit.ipd.dbis.gui.NonEditableTableModel;
import edu.kit.ipd.dbis.log.Event;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static edu.kit.ipd.dbis.log.EventType.MESSAGE;

/**
 * The type Database controller.
 */
public class DatabaseController {

	private GenerateController generate;
	private CalculationController calculation;
	private GraphEditorController editor;
	private FilterController filter;
	private StatusbarController log;
	private CorrelationController correlation;

	private NonEditableTableModel tableModel;
	private Connector connector;
	private GraphDatabase database;

	/**
	 * Instantiates a new Database controller.
	 */
	public DatabaseController() {
		this.generate = GenerateController.getInstance();
		this.calculation = CalculationController.getInstance();
		this.editor = GraphEditorController.getInstance();
		this.filter = FilterController.getInstance();
		this.log = StatusbarController.getInstance();
		this.correlation = CorrelationController.getInstance();
		this.connector = new FileManager();
	}

	/**
	 * Sets table model.
	 *
	 * @param tableModel the table model
	 */
// TODO: Instance of TableModel
	public void setTableModel(NonEditableTableModel tableModel) {
		this.tableModel = tableModel;
	}

	/**
	 * Triggers the database to open a new database table.
	 *
	 * @param url      the url
	 * @param user     the user
	 * @param password the password
	 * @param name     the name
	 */
	public void newDatabase(String url, String user, String password, String name) {
		try {
			database = connector.createGraphDatabase(url, user, password, name);
			this.updateDatabases();
			this.tableModel.update(filter.getFilteredAndSortedGraphs());
		} catch (SQLException | DatabaseDoesNotExistException
				| ConnectionFailedException | AccessDeniedForUserException e) {
			log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.EMPTY_SET));
		}
	}

	/**
	 * Triggers the database to open the database table at the given file path.
	 *
	 * @param filepath the file path of the database.
	 */
	public void loadDatabase(String filepath) {
		try {
			database = connector.loadGraphDatabase(filepath);
			this.updateDatabases();
			this.tableModel.update(filter.getFilteredAndSortedGraphs());
		} catch (FileNotFoundException | FileContentNotAsExpectedException | AccessDeniedForUserException
				| SQLException | FileContentCouldNotBeReadException
				| ConnectionFailedException | DatabaseDoesNotExistException e) {
			log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.EMPTY_SET));
		}
	}

	/**
	 * Triggers the database to merge the database table at the given path with the current database table.
	 *
	 * @param filepath the file path of the Database.
	 */
	public void mergeDatabase(String filepath) {
		GraphDatabase mergeDatabase = null;
		try {
			mergeDatabase = connector.loadGraphDatabase(filepath);
			database.merge(mergeDatabase);
			this.updateDatabases();
			this.tableModel.update(filter.getFilteredAndSortedGraphs());
		} catch (FileNotFoundException | FileContentNotAsExpectedException | AccessDeniedForUserException
				| SQLException | DatabaseDoesNotExistException | ConnectionFailedException | FileContentCouldNotBeReadException e) {
			log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.EMPTY_SET));
		}
	}

	/**
	 * Triggers the database to save the current database table at the given file path.
	 *
	 * @param filepath the file path of the Database.
	 */
	public void saveDatabase(String filepath) {
		try {
			connector.saveGraphDatabase(filepath, database);
		} catch (FileNameAlreadyTakenException | FileCouldNotBeSavedException e) {
			log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.EMPTY_SET));
		}
	}

	/**
	 * Triggers the database to save the current selected graphs in the table at the given path.
	 *
	 * @param filepath the file path of the Database.
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
		filter.setDatabase(database);
		correlation.setDatabase(database);
		log.setDatabase(database);
	}
}

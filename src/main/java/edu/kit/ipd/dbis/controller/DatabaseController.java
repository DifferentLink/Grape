package edu.kit.ipd.dbis.controller;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.files.FileContentCouldNotBeReadException;
import edu.kit.ipd.dbis.database.exceptions.files.FileContentNotAsExpectedException;
import edu.kit.ipd.dbis.database.exceptions.files.FileCouldNotBeSavedException;
import edu.kit.ipd.dbis.database.exceptions.files.FileNameAlreadyTakenException;
import edu.kit.ipd.dbis.database.exceptions.sql.*;
import edu.kit.ipd.dbis.database.file.Connector;
import edu.kit.ipd.dbis.database.file.FileManager;
import edu.kit.ipd.dbis.gui.GrapeUI;
import edu.kit.ipd.dbis.gui.StatusbarUI;
import edu.kit.ipd.dbis.log.History;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * The type Database controller.
 */
public class DatabaseController {

	private GenerateController generate;
	private CalculationController calculation;
	private GraphEditorController editor;
	private FilterController filter;
	private StatusbarController statusbar;
	private CorrelationController correlation;

	private Connector connector;
	private GraphDatabase database;
	private GrapeUI grapeUI;
	private StatusbarUI statusbarUI;


	/**
	 * Instantiates a new Database controller.
	 */
	public DatabaseController() {
		this.generate = GenerateController.getInstance();
		this.calculation = CalculationController.getInstance();
		this.editor = GraphEditorController.getInstance();
		this.filter = FilterController.getInstance();
		this.statusbar = StatusbarController.getInstance();
		this.correlation = CorrelationController.getInstance();
		this.connector = new FileManager();
	}

	/**
	 * Sets grape ui.
	 *
	 * @param grapeUI the grape ui
	 */
	public void setGrapeUI(GrapeUI grapeUI) {
		this.grapeUI = grapeUI;
	}

	/**
	 * Triggers the database to open a new database table.
	 *
	 * @param url the url
	 * @param user the user
	 * @param password the password
	 * @param name the name
	 */
	public void newDatabase(String url, String user, String password, String name) {
		try {
			database = connector.createGraphDatabase(url, user, password, name);
			this.updateDatabases();
			this.statusbarUI.setDatabaseInfo(name, this.database.getNumberOfGraphs());
			this.statusbarUI.setRemainingCalculations(0);
			this.filter.updateFilters();
			this.grapeUI.updateTable();
			this.statusbar.setHistory(new History(50));
			this.statusbar.addMessage("Database \"" + name + "\" opened.");
		} catch (DatabaseDoesNotExistException | ConnectionFailedException | AccessDeniedForUserException e) {
			statusbar.addMessage(e.getMessage());
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
			this.filter.updateFilters();
			this.grapeUI.updateTable();
			this.statusbar.setHistory(new History(50));
			this.statusbar.addMessage("Database loaded.");
		} catch (FileNotFoundException | FileContentNotAsExpectedException | FileContentCouldNotBeReadException
				| ConnectionFailedException e) {
			statusbar.addMessage(e.getMessage());
		}
	}

	/**
	 * Triggers the database to merge the database table at the given path with the current database table.
	 *
	 * @param filepath the file path of the Database.
	 */
	public void mergeDatabase(String filepath) {
		GraphDatabase mergeDatabase;
		try {
			mergeDatabase = connector.loadGraphDatabase(filepath);
			database.merge(mergeDatabase);
			this.updateDatabases();
			this.filter.updateFilters();
			this.grapeUI.updateTable();
			this.statusbar.setHistory(new History(50));
			this.statusbar.addMessage("Databases merged");
		} catch (FileNotFoundException | FileContentNotAsExpectedException | ConnectionFailedException | FileContentCouldNotBeReadException e) {
			statusbar.addMessage(e.getMessage());
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
			statusbar.addMessage("Database saved");
		} catch (FileNameAlreadyTakenException | FileCouldNotBeSavedException e) {
			statusbar.addMessage(e.getMessage());
		}
	}

	/**
	 * Triggers the database to save the current selected graphs in the table at the given path.
	 *
	 * @param url      the url
	 * @param user     the user
	 * @param password the password
	 * @param name     the name
	 * @param filepath the file path of the Database.
	 * @param graphIDs the GraphIDs to save.
	 */
	public void saveSelection(String url, String user, String password, String name, String filepath, List<Integer>
			graphIDs) {
		GraphDatabase selectionDatabase;
		try {
			selectionDatabase = connector.createGraphDatabase(url, user, password, name);
			for (int id : graphIDs) {
				selectionDatabase.addGraph(this.database.getGraphById(id));
			}
			connector.saveGraphDatabase(filepath, selectionDatabase);
			this.statusbar.addMessage("Selection saved");
		} catch (DatabaseDoesNotExistException | ConnectionFailedException | AccessDeniedForUserException
				| InsertionFailedException | UnexpectedObjectException | FileCouldNotBeSavedException
				| FileNameAlreadyTakenException e) {
			statusbar.addMessage(e.getMessage());
		}
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
		statusbar.setDatabase(database);
	}

	/**
	 * Sets statusbar ui.
	 *
	 * @param statusbarUI the statusbar ui
	 */
	public void setStatusbarUI(StatusbarUI statusbarUI) {
		this.statusbarUI = statusbarUI;
	}

	public boolean isDatabaseLoaded() {
		return database != null;
	}
}

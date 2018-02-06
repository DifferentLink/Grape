package edu.kit.ipd.dbis.controller;


import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.*;
import edu.kit.ipd.dbis.gui.NonEditableTableModel;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.sql.SQLException;

/**
 * The type Calculation controller.
 */
public class CalculationController implements Runnable {

	private Boolean isCalculating;
	private StatusbarController log;
	private GraphDatabase database;
	private FilterController filter;
	private NonEditableTableModel tableModel;

	//TODO: Singleton pattern
	private static CalculationController calculation;

	private CalculationController() {
		this.log = StatusbarController.getInstance();
		this.filter = FilterController.getInstance();
		this.isCalculating = true;
	}

	/**
	 * Gets instance.
	 *
	 * @return the instance
	 */
	public static CalculationController getInstance() {
		if (calculation == null) {
			calculation = new CalculationController();
		}
		return calculation;
	}

	/**
	 * Replaces the old database with the given database.
	 *
	 * @param database the current database
	 */
	public void setDatabase(GraphDatabase database) {
		this.database = database;
	}

	// TODO: Instance of TableModel
	public void setTableModel(NonEditableTableModel tableModel) {
		this.tableModel = tableModel;
	}

	/**
	 * induces the calculation of all properties of PropertyGraph<V,E> in the graphlist
	 * of the database and induces their saving in the database.
	 */
	public void run() {
		if (isCalculating) {
			try {
				if (database.hasUncalculatedGraphs()) {
					PropertyGraph<Integer, Integer> graph = database.getUncalculatedGraph();
					graph.calculateProperties();
					database.replaceGraph(graph.getId(), graph);
				}
			} catch (ConnectionFailedException | InsertionFailedException | UnexpectedObjectException e) {
				e.printStackTrace();
			}
			try {
				tableModel.update(filter.getFilteredAndSortedGraphs());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// start recursion
			try {
				if (database.hasUncalculatedGraphs()) {
					run();
				}
			} catch (ConnectionFailedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Gets number not calculated graphs.
	 *
	 * @return the length of the graphlist of CalculationController.
	 */
	public int getNumberNotCalculatedGraphs() { //Todo: perhaps remove method
		int numberGraphs = 0;
		//numberGraphs = database.getUncalculatedGraph().size();
		return numberGraphs;
	}

	/**
	 * checks if the current calculation is running.
	 *
	 * @return true if the calculation is running.
	 */
	public Boolean getCalcStatus() {
		return isCalculating;
	}

	/**
	 * pauses the method calculateGraphProperties().
	 */
	public void pauseCalculation() {
		isCalculating = false;
	}

	/**
	 * continues the method calculateGraphProperties().
	 */
	public synchronized void continueCalculation() {
		isCalculating = true;
		run();
	}

}

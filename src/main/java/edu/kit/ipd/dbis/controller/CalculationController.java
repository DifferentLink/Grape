package edu.kit.ipd.dbis.controller;


import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.*;
import edu.kit.ipd.dbis.gui.NonEditableTableModel;
import edu.kit.ipd.dbis.log.Event;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.util.Collections;
import java.util.LinkedList;

import static edu.kit.ipd.dbis.log.EventType.MESSAGE;

/**
 * The type Calculation controller.
 */
public class CalculationController implements Runnable {

	private Boolean calculationStatus;
	private StatusbarController log;
	private GraphDatabase database;
	private FilterController filter;
	private NonEditableTableModel table; // todo initialize table

	//TODO: Singleton pattern
	private static CalculationController calculation;

	private CalculationController() {
		this.log = StatusbarController.getInstance();
		this.filter = FilterController.getInstance();
		this.calculationStatus = false;
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

	/**
	 * induces the calculation of all properties of PropertyGraph<V,E> in the graphlist
	 * of the database and induces their saving in the database.
	 */
	public void run() {
		LinkedList<PropertyGraph<Integer, Integer>> graphs = null;
		try {
			graphs = database.getUncalculatedGraphs();
		} catch (AccessDeniedForUserException | DatabaseDoesNotExistException | TablesNotAsExpectedException
				| ConnectionFailedException e) {
			log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.EMPTY_SET));
		}
		if (graphs == null) {
			return;
		}
		// Trigger Graph calculation

		for (PropertyGraph<Integer, Integer> graph : graphs) {
			while (calculationStatus) {
				graph.calculateProperties();
				// Replacing graphs
				try {
					database.replaceGraph(graph.getId(), graph);
				} catch (TablesNotAsExpectedException | DatabaseDoesNotExistException | ConnectionFailedException
						| AccessDeniedForUserException | InsertionFailedException | UnexpectedObjectException e) {
					log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.EMPTY_SET));
				}
				table.update(filter.getFilteredAndSortedGraphs()); // todo implement calculatedGraphProperties()
			}
		}

	}

	/**
	 * Gets number not calculated graphs.
	 *
	 * @return the length of the graphlist of CalculationController.
	 */
	public int getNumberNotCalculatedGraphs() {
		int numberGraphs = 0;
		try {
			numberGraphs = database.getUncalculatedGraphs().size();
		} catch (AccessDeniedForUserException | DatabaseDoesNotExistException | TablesNotAsExpectedException
				| ConnectionFailedException e) {
			log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.EMPTY_SET));
		}
		return numberGraphs;
	}

	/**
	 * checks if the current calculation is running.
	 *
	 * @return true if the calculation is running.
	 */
	public Boolean getCalcStatus() {
		return calculationStatus;
	}


	/**
	 * pauses the method calculateGraphProperties().
	 */
	public void pauseCalculation() {
		calculationStatus = false;
	}

	/**
	 * continues the method calculateGraphProperties().
	 */
	public void continueCalculation() {
		calculationStatus = true;
	}

}

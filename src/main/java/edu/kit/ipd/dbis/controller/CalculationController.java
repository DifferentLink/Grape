package edu.kit.ipd.dbis.controller;


import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.*;
import edu.kit.ipd.dbis.gui.NonEditableTableModel;
import edu.kit.ipd.dbis.log.Event;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static edu.kit.ipd.dbis.log.EventType.MESSAGE;

public class CalculationController {

	private Boolean calculationStatus = false;
	private StatusbarController log;
	private GraphDatabase database;
	private NonEditableTableModel table; // todo initialize table

	//TODO: Singleton pattern
	private static CalculationController calculation;

	private CalculationController() {
	}

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
	private void calculateGraphProperties() {
		List<PropertyGraph> graphs = null;
		try {
			graphs = database.getUncalculatedGraphs();
		} catch (AccessDeniedForUserException | DatabaseDoesNotExistException | TablesNotAsExpectedException | ConnectionFailedException e) {
			log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.EMPTY_SET));
		}
		// Trigger Graph calculation
		for (PropertyGraph<Integer, Integer> graph : graphs) {
			if (calculationStatus == true) {
				graph.calculateProperties();
				// Replacing graphs
				try {
					database.replaceGraph(graph.getId(), graph);
				} catch (TablesNotAsExpectedException | DatabaseDoesNotExistException | ConnectionFailedException | AccessDeniedForUserException | InsertionFailedException | UnexpectedObjectException e) {
					log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.EMPTY_SET));
				}
				table.update(null); // todo implement calculatedGraphProperties()
			} else {
				return;
			}
		}
	}

	/**
	 * @return the length of the graphlist of CalculationController.
	 */
	public int getNumberNotCalculatedGraphs() {
		int numberGraphs = 0;
		try {
			numberGraphs = database.getUncalculatedGraphs().size();
		} catch (AccessDeniedForUserException | DatabaseDoesNotExistException | TablesNotAsExpectedException | ConnectionFailedException e) {
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
		this.calculateGraphProperties();
	}

}

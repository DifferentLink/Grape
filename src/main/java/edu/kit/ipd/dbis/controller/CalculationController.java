package edu.kit.ipd.dbis.controller;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.InsertionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.UnexpectedObjectException;
import edu.kit.ipd.dbis.gui.GrapeUI;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

/**
 * The type Calculation controller.
 */
public class CalculationController implements Runnable {

	private Boolean isCalculating;
	private StatusbarController statusbarController;
	private GraphDatabase database;
	private GrapeUI grapeUI;
	private static CalculationController calculation;

	/**
	 * @param grapeUI the grape ui
	 */
	public void setGrapeUI(GrapeUI grapeUI) {
		this.grapeUI = grapeUI;
	}

	private CalculationController() {
		this.statusbarController = StatusbarController.getInstance();
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
					grapeUI.updateTable();
				}
			} catch (ConnectionFailedException | InsertionFailedException | UnexpectedObjectException e) {
				statusbarController.addMessage(e.getMessage());
			}
			// start recursion
			try {
				if (database.hasUncalculatedGraphs()) {
					run();
				}
			} catch (ConnectionFailedException e) {
				statusbarController.addMessage(e.getMessage());
			}
		}
	}

	/**
	 * Start calculation of properties of graphs in database which have uncalculated properties
	 */
	public synchronized void startCalculation() {
		isCalculating = true;
		run();
	}

}

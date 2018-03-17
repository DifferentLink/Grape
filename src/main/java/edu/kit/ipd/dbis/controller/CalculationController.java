package edu.kit.ipd.dbis.controller;

import edu.kit.ipd.dbis.controller.util.CalculationMaster;
import edu.kit.ipd.dbis.controller.util.CalculationWorker;
import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.UnexpectedObjectException;
import edu.kit.ipd.dbis.gui.GrapeUI;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.util.LinkedList;
import java.util.List;

/**
 * The type Calculation controller.
 */
public class CalculationController {

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
	 * Start calculation of properties of graphs in database which have uncalculated properties
	 */
	public synchronized void startCalculation() {
		List<PropertyGraph<Integer, Integer>> uncalculatedGraphs = new LinkedList<>();
		try {
			while (database.hasUncalculatedGraphs()) {
				uncalculatedGraphs.add(database.getUncalculatedGraph());
			}
		} catch (ConnectionFailedException | UnexpectedObjectException e) {
			statusbarController.addMessage(e.getMessage());
		}

		List<Thread> jobs = new LinkedList<>();
		for (PropertyGraph<Integer, Integer> graph : uncalculatedGraphs) {
			jobs.add(new CalculationWorker(graph, database));
		}
		CalculationMaster.executeCalculation(jobs);
		grapeUI.updateTable();
		statusbarController.setRemainingCalculations();
	}

}

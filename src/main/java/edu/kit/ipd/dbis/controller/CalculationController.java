package edu.kit.ipd.dbis.controller;

import edu.kit.ipd.dbis.database.GraphDatabase;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.util.HashSet;
import java.util.Set;

public class CalculationController {

	private Boolean calculationStatus = false;

	private GraphDatabase database;

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
	private void calculateGraphProperties() throws Exception {
		Set<PropertyGraph<Integer, Integer>> graphs = new HashSet<PropertyGraph<Integer, Integer>>();
		//graphs = database.getUncalculatedGraphs();

		// Trigger Graph calculation
		for (PropertyGraph<Integer, Integer> graph : graphs) {
			if (calculationStatus == true) {
				graph.calculateRemainingProperties();
				// Deleting graphs from list of not calculated graphs
				//database.deleteGraphFromList(graph.getId());
			} else {
				return;
			}
		}
	}

	/**
	 * @return the length of the graphlist of CalculationController.
	 */
	public int getNumberNotCalculatedGraphs() {
		Set<PropertyGraph<Integer, Integer>> graphs = new HashSet<PropertyGraph<Integer, Integer>>();
		//graphs = database.getUncalculatedGraphs();
		return graphs.size();
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
	public void continueCalculation() throws Exception {
		calculationStatus = true;
		this.calculateGraphProperties();
	}


}

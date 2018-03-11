package edu.kit.ipd.dbis.controller;


import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.InsertionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.UnexpectedObjectException;
import edu.kit.ipd.dbis.gui.GrapeUI;
import edu.kit.ipd.dbis.log.EventType;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

/**
 * The type Calculation controller.
 */
public class CalculationController implements Runnable {

	private Boolean isCalculating;
	private StatusbarController statusbar;
	private GraphDatabase database;

	private GrapeUI grapeUI;

	/**
	 * Sets grape ui.
	 *
	 * @param grapeUI the grape ui
	 */
	public void setGrapeUI(GrapeUI grapeUI) {
		this.grapeUI = grapeUI;
	}

	private static CalculationController calculation;

	private CalculationController() {
		this.statusbar = StatusbarController.getInstance();
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
				statusbar.addMessage(e.getMessage());
			}
			// start recursion
			try {
				if (database.hasUncalculatedGraphs()) {
					run();
				}
			} catch (ConnectionFailedException e) {
				statusbar.addMessage(e.getMessage());
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

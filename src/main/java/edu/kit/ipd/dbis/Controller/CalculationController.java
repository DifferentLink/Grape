package edu.kit.ipd.dbis.Controller;

public class CalculationController {

	private Boolean calculationStatus = false;

	private Database;

	private static CalculationController calculation;

	private CalculationController(){}

	public static CalculationController getInstance() {
		if(calculation == null) {
			calculation = new CalculationController();
		}
		return calculation;
	}

	/**
	 * Replaces the old database with the given database.
	 *
	 * @param database the current database
	 */
	public void setDatabase(Database database) {
		this.database = Database;
	}

	/**
	 * induces the calculation of all properties of PropertyGraph<V,E> in the graphlist
	 * of the database and induces their saving in the database.
	 */
	private void calculateGraphProperties() {

	}

	/**
	 * @return the length of the graphlist of CalculationController.
	 */
	public int getNumberNotCalculatedGraphs() {
		return 9999;
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

	}

	/**
	 * continues the method calculateGraphProperties().
	 */
	public void continueCalculation() {

	}


}

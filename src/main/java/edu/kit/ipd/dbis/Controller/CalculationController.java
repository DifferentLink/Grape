package edu.kit.ipd.dbis.Controller;

public class CalculationController {

	public void setDatabase(database: Database): void
	replaces the old database with the given database.
	@param database the current database.
			private void calculateGraphProperties(): void
	induces the calculation of all properties of PropertyGraph<V,E> in the graphlist
	of the database and induces their saving in the database.

	public int getNumberNotCalculatedGraphs(){

	}

	@return the length of the graphlist of CalculationController.

	public Boolean getCalcStatus(){

	}

	checks if the current calculation is running.
	@return true if the calculation is running.

	public void pauseCalculation(): void
	pauses the method calculateGraphProperties().

	public void continueCalculation(){}
	continues the method calculateGraphProperties().

}

package edu.kit.ipd.dbis.controller;

import edu.kit.ipd.dbis.correlation.CorrelationOutput;
import edu.kit.ipd.dbis.correlation.CorrelationRequest;
import edu.kit.ipd.dbis.correlation.exceptions.InvalidCorrelationInputException;
import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.AccessDeniedForUserException;
import edu.kit.ipd.dbis.database.exceptions.sql.DatabaseDoesNotExistException;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.log.EventType;

import java.util.List;

/**
 * The type Correlation controller.
 */
public class CorrelationController {

	private StatusbarController statusbar;
	private GraphDatabase database;

	//TODO: Singleton pattern
	private static CorrelationController correlationController;

	private CorrelationController() {
		this.statusbar = StatusbarController.getInstance();
	}

	/**
	 * Gets instance.
	 *
	 * @return the instance
	 */
	public static CorrelationController getInstance() {
		if (correlationController == null) {
			correlationController = new CorrelationController();
		}
		return correlationController;
	}

	/**
	 * Replaces the old database with the given in the filter package.
	 *
	 * @param database the current database
	 */
	public void setDatabase(GraphDatabase database) {
		this.database = database;
	}

	/**
	 * gets all filtered and sorted graphs, checks the input for the correlation, creates a new instance
	 * of CorrelationRequest and finally executes use() on the CorrelationRequest instance with the graph list.
	 *
	 * @param input the input for the correlation.
	 * @return a list of CorrelationOutput.
	 * @throws InvalidCorrelationInputException the invalid correlation input exception
	 */
	public List<CorrelationOutput> addNewCorrelation(String input) throws InvalidCorrelationInputException {
		List<CorrelationOutput> output = null;
		CorrelationRequest request = new CorrelationRequest(input, this.database);
		try {
			System.out.println("Ich war da");
			output = request.applyCorrelation();
			System.out.println("Auweia! Eine NullPointerException :(");
			for (CorrelationOutput current: output) {
				System.out.println("Hallo! Ich bin die Korrelation " + current.getFirstProperty() + " "
				+ current.getSecondProperty() + " " + current.getOutputNumber());
			}
		} catch (DatabaseDoesNotExistException | AccessDeniedForUserException | ConnectionFailedException e) {
			System.out.println("Ein Fehler ist aufgetreten");
			e.printStackTrace();
			statusbar.addMessage(e.getMessage());
		}
		return output;
	}
}

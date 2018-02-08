package edu.kit.ipd.dbis.controller;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.AccessDeniedForUserException;
import edu.kit.ipd.dbis.database.exceptions.sql.DatabaseDoesNotExistException;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.log.Event;
import edu.kit.ipd.dbis.log.EventType;
import edu.kit.ipd.dbis.log.History;
import edu.kit.ipd.dbis.log.Log;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static edu.kit.ipd.dbis.log.EventType.MESSAGE;

/**
 * The type Statusbar controller.
 */
//TODO:
public class StatusbarController {

	private Log log;
	private CalculationController calculation;

	//TODO: Singleton pattern
	private static StatusbarController statusbar;

	private StatusbarController() {
		this.log = new Log(100);
	}

	/**
	 * Gets instance.
	 *
	 * @return the instance
	 */
	public static StatusbarController getInstance() {
		if (statusbar == null) {
			statusbar = new StatusbarController();
		}
		return statusbar;
	}

	public void setDatabase(GraphDatabase database) {
		log.setDatabase(database);
	}

	private void setCalculation() {
		this.calculation = CalculationController.getInstance();
	}

	/**
	 * Gets as string.
	 *
	 * @return the as string
	 */
	public String getAsString() {
		return log.getAsString();
	}

	/**
	 * Removes all events of the type MESSAGE from the current history
	 */
	public void removeMessages() {
		log.removeMessages();
	}

	/**
	 * Move backward in the history
	 */
	public void undo() {
		try {
			log.undo();
		} catch (DatabaseDoesNotExistException | AccessDeniedForUserException | ConnectionFailedException e) {
			this.addMessage(e.getMessage());
		}
	}

	/**
	 * Move forward in the history
	 */
	public void redo() {
		try {
			log.redo();
		} catch (DatabaseDoesNotExistException | AccessDeniedForUserException | ConnectionFailedException e) {
			this.addMessage(e.getMessage());
		}
	}

	/**
	 * Gets the history of the log instance.
	 *
	 * @return the history
	 */
	public History getHistory() {
		return log.getHistory();
	}

	/**
	 * Sets history.
	 *
	 * @param history the history
	 */
	public void setHistory(History history) {
		log.setHistory(history);
	}

	/**
	 * Add event.
	 *
	 * @param event the event to add
	 */
	public void addEvent(Event event) {
		log.addEvent(event);
	}

	/**
	 * Add event.
	 *
	 * @param type the EventType
	 * @param id   the id of the graph
	 */
	public void addEvent(EventType type, int id) {
		Set<Integer> changedGraph = new HashSet<Integer>();
		changedGraph.add(id);
		Event event;
		String message;
		if (type.equals(EventType.ADD)) {
			message = "New graph added.";
		} else if (type.equals(EventType.REMOVE)) {
			message = "graph removed.";
		} else {
			return;
		}
		event = new Event(type, message, changedGraph);
		log.addEvent(event);
	}

	/**
	 *
	 */
	public void addMessage(String message) {
		Set<Integer> empty = new HashSet<Integer>();
		Event event;
		event = new Event(EventType.MESSAGE, message, empty);
		log.addEvent(event);
	}

	/**
	 * pauses the method calculateGraphProperties().
	 */
	public void pauseCalculation() {
		if (calculation == null) {
			setCalculation();
		}
		calculation.pauseCalculation();
	}

	/**
	 * continues the method calculateGraphProperties().
	 */
	public void continueCalculation() {
		if (calculation == null) {
			setCalculation();
		}
		calculation.continueCalculation();
	}
}


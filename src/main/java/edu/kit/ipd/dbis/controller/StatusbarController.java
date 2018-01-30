package edu.kit.ipd.dbis.controller;

import edu.kit.ipd.dbis.database.exceptions.sql.AccessDeniedForUserException;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.DatabaseDoesNotExistException;
import edu.kit.ipd.dbis.database.exceptions.sql.TablesNotAsExpectedException;
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

	/**
	 * Gets as string.
	 *
	 * @return the as string
	 */
	public String getAsString() {
		return log.getAsString();
	}

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
		} catch (DatabaseDoesNotExistException | AccessDeniedForUserException | TablesNotAsExpectedException
				| ConnectionFailedException e) {
			log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.emptySet()));
		}
	}

	/**
	 * Move forward in the history
	 */
	public void redo() {
		try {
			log.redo();
		} catch (DatabaseDoesNotExistException | AccessDeniedForUserException | TablesNotAsExpectedException
				| ConnectionFailedException e) {
			log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.emptySet()));
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
	public void addMessage(EventType type, String message) {
		Set<Integer> empty = new HashSet<Integer>();
		Event event;
		if (!type.equals(EventType.REMOVE)) {
			return;
		}
		event = new Event(type, message, empty);
		// TODO: change to addMessage
		log.addEvent(event);
	}

	/**
	 * pauses the method calculateGraphProperties().
	 */
	public void pauseCalculation() { //TODO

	}

	/**
	 * continues the method calculateGraphProperties().
	 */
	public void continueCalculation() { //TODO

	}

}

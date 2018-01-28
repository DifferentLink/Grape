package edu.kit.ipd.dbis.controller;

import edu.kit.ipd.dbis.database.exceptions.sql.AccessDeniedForUserException;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.DatabaseDoesNotExistException;
import edu.kit.ipd.dbis.database.exceptions.sql.TablesNotAsExpectedException;
import edu.kit.ipd.dbis.log.Event;
import edu.kit.ipd.dbis.log.History;
import edu.kit.ipd.dbis.log.Log;

import java.util.Collections;

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
	 * @param event the event
	 */
	public void addEvent(Event event) {
		log.addEvent(event);
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


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

//TODO:
public class StatusbarController {

	private Log log;

	public String getAsString() {
		return log.getAsString();
	}


	/**
	 * Parses a given String to the current history object with the given events.
	 *
	 * @param historyString the string of the history.
	 */
	private void parseHistory(String historyString) {

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
		} catch (DatabaseDoesNotExistException e) {
			log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.emptySet()));
		} catch (AccessDeniedForUserException e) {
			log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.emptySet()));
		} catch (ConnectionFailedException e) {
			log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.emptySet()));
		} catch (TablesNotAsExpectedException e) {
			log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.emptySet()));
		}
	}

	/**
	 * Move forward in the history
	 */
	public void redo() {
		try {
			log.redo();
		} catch (DatabaseDoesNotExistException e) {
			log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.emptySet()));
		} catch (AccessDeniedForUserException e) {
			log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.emptySet()));
		} catch (ConnectionFailedException e) {
			log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.emptySet()));
		} catch (TablesNotAsExpectedException e) {
			log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.emptySet()));
		}
	}

	public History getHistory() {
		return log.getHistory();
	}

	public void setHistory(History history) {
		log.setHistory(history);
	}

}


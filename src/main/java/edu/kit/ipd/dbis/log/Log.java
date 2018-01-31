package edu.kit.ipd.dbis.log;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.AccessDeniedForUserException;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.DatabaseDoesNotExistException;
import edu.kit.ipd.dbis.database.exceptions.sql.TablesNotAsExpectedException;

/**
 * The type Log.
 */
public class Log {

	private GraphDatabase database;
	private History history;

	/**
	 * Instantiates a new Log.
	 *
	 * @param maxHistorySize the history size
	 */
	public Log(int maxHistorySize) {
		this.history = new History(maxHistorySize);
	}

	/**
	 * Sets database.
	 *
	 * @param database the database
	 */
	public void setDatabase(GraphDatabase database) {
		this.database = database;
	}

	/**
	 * Gets as string.
	 *
	 * @return a string with the format [EventType] Event Message"GraphID-1, GraphID- 2, ... \n Therefore each line of
	 * the string corresponds to one log entry.
	 */
	public String getAsString() {
		String historyEntries = "";
		if (history == null || history.getEvents().size() == 0) {
			return "";
		}
		//Building String
		for (Event event : history.getEvents()) {
			String changedGraphs = "";
			for (int id : event.getChangedGraphs()) {
				changedGraphs += id + ", ";
			}
			changedGraphs = changedGraphs.substring(0, changedGraphs.length() - 1);
			historyEntries += "[" + event.getType() + "] " + event.getMessage() + " (" + changedGraphs + ")\n";
		}
		historyEntries = historyEntries.substring(0, historyEntries.length() - 2);
		return historyEntries;
	}

	/**
	 * Removes all events of the type MESSAGE from the current history
	 */
	public void removeMessages() {
		for (Event event : history.getEvents()) {
			if (event.getType().equals(EventType.MESSAGE)) {
				history.removeEvent(event);
			}
		}
	}

	/**
	 * Add event to the log history.
	 *
	 * @param event the event
	 */
	public void addEvent(Event event) {
		this.history.addEvent(event);
	}

	/**
	 * Move backward in the history
	 *
	 * @throws DatabaseDoesNotExistException the database does not exist exception
	 * @throws AccessDeniedForUserException  the access denied for user exception
	 * @throws ConnectionFailedException     the connection failed exception
	 * @throws TablesNotAsExpectedException  the tables not as expected exception
	 */
	public void undo() throws DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException,
			TablesNotAsExpectedException {
		Event action = history.getActiveState();
		history.moveBackward();
		action.getType().undo(this.database, action.getChangedGraphs());
	}

	/**
	 * Move forward in the history
	 *
	 * @throws DatabaseDoesNotExistException the database does not exist exception
	 * @throws AccessDeniedForUserException  the access denied for user exception
	 * @throws ConnectionFailedException     the connection failed exception
	 * @throws TablesNotAsExpectedException  the tables not as expected exception
	 */
	public void redo() throws DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException,
			TablesNotAsExpectedException {
		Event action = history.getActiveState();
		history.moveForward();
		action.getType().redo(this.database, action.getChangedGraphs());
	}

	/**
	 * Gets history.
	 *
	 * @return the history
	 */
	public History getHistory() {
		return history;
	}

	/**
	 * Sets history.
	 *
	 * @param history the history
	 */
	public void setHistory(History history) {
		this.history = history;
	}

}

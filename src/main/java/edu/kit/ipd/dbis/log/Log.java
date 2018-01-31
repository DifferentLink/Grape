package edu.kit.ipd.dbis.log;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.AccessDeniedForUserException;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.DatabaseDoesNotExistException;
import edu.kit.ipd.dbis.database.exceptions.sql.TablesNotAsExpectedException;

import java.util.List;

public class Log {

	// TODO: How to change/set Database?
	private GraphDatabase database;
	private History history;

	// TODO: Constructor?
	public Log(int maxHistorySize) {
		history = new History(maxHistorySize);
	}

	/**
	 * @return a string with the format [EventType] Event Message"GraphID-1, GraphID-
	 * 2, ... \n
	 * Therefore each line of the string corresponds to one log entry.
	 */
	public String getAsString() {
		String historyEntries = "";
		List<Event> events;
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
		for (Event event : history.getEvents()) {
			if (event.getType().equals(EventType.MESSAGE)) {
				history.removeEvent(event);
			}
		}
	}

	public void addEvent(Event event) {
		this.history.addEvent(event);
	}

	/**
	 * Move backward in the history
	 */
	public void undo() throws DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException, TablesNotAsExpectedException { // todo catch
		Event action = history.getActiveState();
		history.moveBackward();
		action.getType().undo(this.database, action.getChangedGraphs());
	}

	/**
	 * Move forward in the history
	 */
	public void redo() throws DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException, TablesNotAsExpectedException { // todo catch
		Event action = history.getActiveState();
		history.moveForward();
		action.getType().redo(this.database, action.getChangedGraphs());
	}

	public History getHistory() {
		return history;
	}

	public void setHistory(History history) {
		this.history = history;
	}

	public void loadHistory(String file) {

	}

}

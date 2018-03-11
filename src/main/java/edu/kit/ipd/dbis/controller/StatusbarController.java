package edu.kit.ipd.dbis.controller;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.AccessDeniedForUserException;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.DatabaseDoesNotExistException;
import edu.kit.ipd.dbis.gui.GrapeUI;
import edu.kit.ipd.dbis.gui.StatusbarUI;
import edu.kit.ipd.dbis.log.Event;
import edu.kit.ipd.dbis.log.EventType;
import edu.kit.ipd.dbis.log.History;
import edu.kit.ipd.dbis.log.Log;

import java.util.HashSet;
import java.util.Set;

/**
 * The type Statusbar controller.
 */
public class StatusbarController {

	private Log log;
	private CalculationController calculation;
	private GrapeUI grapeUI;
	private GraphDatabase database;
	private static StatusbarController statusbar;
	private StatusbarUI statusbarUI;

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
	 * Sets database.
	 *
	 * @param database the database
	 */
	public void setDatabase(GraphDatabase database) {
		this.database = database;
		log.setDatabase(database);
	}

	/**
	 * Sets grape ui.
	 *
	 * @param grapeUI the grape ui
	 */
	public void setGrapeUI(GrapeUI grapeUI) {
		this.grapeUI = grapeUI;
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
			grapeUI.updateTable();
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
			grapeUI.updateTable();
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
		this.statusbarUI.setLastLogentry(statusbar.getHistory().getLastEvent());
	}

	/**
	 * Add event.
	 *
	 * @param type the EventType
	 * @param id   the id of the graph
	 */
	public void addEvent(EventType type, int id) {
		Set<Integer> changedGraph = new HashSet<>();
		changedGraph.add(id);
		Event event;
		String message;
		if (type.equals(EventType.ADD)) {
			message = "New graph added";
		} else if (type.equals(EventType.REMOVE)) {
			message = "Graph deleted";
		} else {
			return;
		}
		event = new Event(type, message, changedGraph);
		this.addEvent(event);
	}

	/**
	 * Add event.
	 *
	 * @param type the EventType
	 * @param id   the id of the graph
	 */
	public void addEvent(EventType type, int id, String message) {
		Set<Integer> changedGraph = new HashSet<>();
		changedGraph.add(id);
		Event event;
		event = new Event(type, message, changedGraph);
		this.addEvent(event);
	}

	/**
	 * Add message.
	 *
	 * @param message the message
	 */
	public void addMessage(String message) {
		Set<Integer> empty = new HashSet<>();
		Event event;
		event = new Event(EventType.MESSAGE, message, empty);
		this.addEvent(event);
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
		this.setRemainingCalculations();
	}

	public void setRemainingCalculations() {
		try {
			this.statusbarUI.setRemainingCalculations(this.database.getNumberOfUncalculatedGraphs());
		} catch (ConnectionFailedException e) {
			this.addMessage(e.getMessage());
		}
	}

	public void setNumberOfGraphs() {
		try {
			this.statusbarUI.setNumberOfGraphs(this.database.getNumberOfGraphs());
		} catch (ConnectionFailedException e) {
			this.addMessage(e.getMessage());
		}
	}

	/**
	 * Sets statusbar ui.
	 *
	 * @param statusbarUI the statusbar ui
	 */
	public void setStatusbarUI(StatusbarUI statusbarUI) {
		this.statusbarUI = statusbarUI;
	}
}


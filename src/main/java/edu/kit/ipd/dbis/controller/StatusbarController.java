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

import java.util.LinkedList;
import java.util.List;

/**
 * The type Statusbar controller.
 */
public class StatusbarController {

	private Log log;
	private CalculationController calculationController;
	private GrapeUI grapeUI;

	private static StatusbarController statusbarController;
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
		if (statusbarController == null) {
			statusbarController = new StatusbarController();
		}
		return statusbarController;
	}

	/**
	 * Sets database.
	 *
	 * @param database the database
	 */
	public void setDatabase(GraphDatabase database) {
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
		this.calculationController = CalculationController.getInstance();
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
		this.statusbarUI.setLastLogEntry(statusbarController.getHistory().getLastEvent());
	}

	/**
	 * Add event.
	 *
	 * @param type the EventType
	 * @param ids the ids of the graph
	 */
	public void addEvent(EventType type, List<Integer> ids) {
		Event event;
		String message;
		if (type.equals(EventType.ADD)) {
			message = "New graph added";
		} else if (type.equals(EventType.REMOVE)) {
			message = "Graph deleted";
		} else {
			return;
		}
		event = new Event(type, message, ids);
		this.addEvent(event);
	}

	/**
	 * Add event.
	 *
	 * @param type the EventType
	 * @param ids   the ids of the graph
	 */
	public void addEvent(EventType type, List<Integer> ids, String message) {
		this.addEvent(new Event(type, message, ids));
	}

	public void addEvent(EventType eventType, int id, String message) {
		List<Integer> ids = new LinkedList<>();
		ids.add(id);
		this.addEvent(new Event(eventType, message, ids));
	}

	/**
	 * Add message.
	 *
	 * @param message the message
	 */
	public void addMessage(String message) {
		this.addEvent(new Event(EventType.MESSAGE, message, new LinkedList<>()));
	}

	/**
	 * pauses the method calculateGraphProperties().
	 */
	public void pauseCalculation() {
		if (calculationController == null) {
			setCalculation();
		}
		calculationController.pauseCalculation();
	}

	/**
	 * continues the method calculateGraphProperties().
	 */
	public void continueCalculation() {
		if (calculationController == null) {
			setCalculation();
		}
		calculationController.startCalculation();
	}

	/**
	 * @return the number uncalculated graphs
	 */
	public int getNumberUncalculatedGraphs() {
		return 0;
	}

	/**
	 * @param statusbarUI the statusbarController ui
	 */
	public void setStatusbarUI(StatusbarUI statusbarUI) {
		this.statusbarUI = statusbarUI;
	}
}


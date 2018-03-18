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
	private GraphDatabase database;
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
		this.calculationController = CalculationController.getInstance();
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
	 * @param ids  the ids of the graph
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
	 * continues the method calculateGraphProperties().
	 */
	public void continueCalculation() {
		if (calculationController == null) {
			setCalculation();
		}
		calculationController.startCalculation();
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
	 * @param statusbarUI the statusbarController ui
	 */
	public void setStatusbarUI(StatusbarUI statusbarUI) {
		this.statusbarUI = statusbarUI;
	}

}


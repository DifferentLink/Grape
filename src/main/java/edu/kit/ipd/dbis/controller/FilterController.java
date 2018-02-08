package edu.kit.ipd.dbis.controller;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.*;
import edu.kit.ipd.dbis.filter.Filtermanagement;
import edu.kit.ipd.dbis.filter.exceptions.InvalidInputException;
import edu.kit.ipd.dbis.gui.GrapeUI;
import edu.kit.ipd.dbis.gui.NonEditableTableModel;
import edu.kit.ipd.dbis.log.Event;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

import static edu.kit.ipd.dbis.log.EventType.MESSAGE;

/**
 * The type Filter controller.
 */
public class FilterController {

	private Filtermanagement filter;
	private StatusbarController statusbar;
	private NonEditableTableModel tableModel;

	private GrapeUI grapeUI;

	public void setGrapeUI(GrapeUI grapeUI) {
		this.grapeUI = grapeUI;
	}

	//TODO: Singleton pattern
	private static FilterController filterController;

	private FilterController() {
		this.statusbar = StatusbarController.getInstance();
		this.filter = new Filtermanagement();
	}

	/**
	 * Gets instance.
	 *
	 * @return the instance
	 */
	public static FilterController getInstance() {
		if (filterController == null) {
			filterController = new FilterController();
		}
		return filterController;
	}

	// TODO: Instance of TableModel
	public void setTableModel(NonEditableTableModel tableModel) {
		this.tableModel = tableModel;
	}

	/**
	 * Replaces the old database with the given in the filter package.
	 *
	 * @param database the current database
	 */
	public void setDatabase(GraphDatabase database) {
		try {
			filter.setDatabase(database);
		} catch (ConnectionFailedException e) {
			statusbar.addMessage(e.getMessage());
		}
	}

	/**
	 * Update filter.
	 *
	 * @param filterInput the filter input
	 * @param id          the id of the filter
	 * @throws InvalidInputException the invalid input exception
	 */
	public void updateFilter(String filterInput, int id) throws InvalidInputException {
		try {
			filter.updateFilter(filterInput, id);
			tableModel.update(this.getFilteredAndSortedGraphs());
		} catch (ConnectionFailedException
				| InsertionFailedException | UnexpectedObjectException
				| SQLException e) {
			statusbar.addMessage(e.getMessage());
		}
	}

	/**
	 * Add filter to group.
	 *
	 * @param filterInput the filter input
	 * @param filterId    the filter id
	 * @param groupId     the group id
	 * @throws InvalidInputException the invalid input exception
	 */
	public void addFilterToGroup(String filterInput, int filterId, int groupId) throws InvalidInputException {
		try {
			filter.addFilterToGroup(filterInput, filterId, groupId);
		} catch (ConnectionFailedException | InsertionFailedException | UnexpectedObjectException e) {
			statusbar.addMessage(e.getMessage());
		}
	}

	/**
	 * Update filter group.
	 *
	 * @param filterInput the filter input
	 * @param id          the id
	 * @throws InvalidInputException the invalid input exception
	 */
	public void updateFilterGroup(String filterInput, int id) throws InvalidInputException {
		try {
			filter.updateFiltergroup(filterInput, id);
			tableModel.update(this.getFilteredAndSortedGraphs());
		} catch (ConnectionFailedException | UnexpectedObjectException | InsertionFailedException | SQLException e) {
			statusbar.addMessage(e.getMessage());
		}
	}

	/**
	 * Remove filtersegment.
	 *
	 * @param id the id
	 */
	public void removeFiltersegment(int id) {
		try {
			filter.removeFiltersegment(id);
			tableModel.update(this.getFilteredAndSortedGraphs());
		} catch (ConnectionFailedException | UnexpectedObjectException | InsertionFailedException
				| SQLException e) {
			statusbar.addMessage(e.getMessage());
		}
	}

	/**
	 * Activate.
	 *
	 * @param id the id
	 */
	public void activate(int id) {
		try {
			filter.activate(id);
			tableModel.update(this.getFilteredAndSortedGraphs());
		} catch (UnexpectedObjectException
				| InsertionFailedException | ConnectionFailedException
				| SQLException e) {
			statusbar.addMessage(e.getMessage());
		}
	}

	/**
	 * Deactivate.
	 *
	 * @param id the id
	 */
	public void deactivate(int id) {
		try {
			filter.deactivate(id);
			tableModel.update(this.getFilteredAndSortedGraphs());
		} catch (UnexpectedObjectException
				| InsertionFailedException | ConnectionFailedException
				| SQLException e) {
			statusbar.addMessage(e.getMessage());
		}
	}

	/**
	 * gets all graphs that fulfill the filter requirements and sorts these graphs by graphID.
	 *
	 * @return a list of PropertyGraph<V,E>.
	 */
	public ResultSet getFilteredAndSortedGraphs() {
		try {
			return filter.getFilteredAndSortedGraphs();
		} catch (ConnectionFailedException e) {
			statusbar.addMessage(e.getMessage());
		}
		return null;
	}

	/**
	 * gets all graphs that fulfill the filter requirements and sorts these graphs ascending
	 * by a specific attribute
	 *
	 * @param property the property to sort after.
	 * @return a list of PropertyGraph<V,E>.
	 */
	public ResultSet getFilteredAndAscendingSortedGraphs(Property property) {
		try {
			return filter.getFilteredAndAscendingSortedGraphs(property);
		} catch (ConnectionFailedException e) {
			statusbar.addMessage(e.getMessage());
		}
		return null;
	}

	/**
	 * gets all graphs that fulfill the filter requirements and sorts these graphs descending by a specific attribute
	 *
	 * @param property the property to sort after.
	 * @return a list of PropertyGraph<V,E>.
	 */
	public ResultSet getFilteredAndDescendingSortedGraphs(Property property) {
		try {
			return filter.getFilteredAndDescendingSortedGraphs(property);
		} catch (ConnectionFailedException e) {
			statusbar.addMessage(e.getMessage());
		}
		return null;
	}
}


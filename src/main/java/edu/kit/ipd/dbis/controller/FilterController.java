package edu.kit.ipd.dbis.controller;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.*;
import edu.kit.ipd.dbis.filter.Filter;
import edu.kit.ipd.dbis.filter.Filtergroup;
import edu.kit.ipd.dbis.filter.Filtermanagement;
import edu.kit.ipd.dbis.filter.exceptions.InvalidInputException;
import edu.kit.ipd.dbis.gui.GrapeUI;
import edu.kit.ipd.dbis.gui.filter.FilterGroup;
import edu.kit.ipd.dbis.gui.filter.FilterUI;
import edu.kit.ipd.dbis.gui.filter.SimpleFilter;
import edu.kit.ipd.dbis.gui.filter.UIFilterManager;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * The type Filter controller.
 */
public class FilterController {

	private Filtermanagement filtermanagement;
	private StatusbarController statusbar;
	private GrapeUI grapeUI;
	private UIFilterManager uiFilterManager;
	private FilterUI filterUI;

	private static FilterController filterController;

	private FilterController() {
		this.statusbar = StatusbarController.getInstance();
		this.filtermanagement = new Filtermanagement();
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

	/**
	 * Sets grape ui.
	 *
	 * @param grapeUI the grape ui
	 */
	public void setGrapeUI(GrapeUI grapeUI) {
		this.grapeUI = grapeUI;
	}

	/**
	 * Sets filter ui.
	 *
	 * @param filterUI the filter ui
	 */
	public void setFilterUI(FilterUI filterUI) {
		this.filterUI = filterUI;
	}

	/**
	 * Sets filter manager.
	 *
	 * @param uiFilterManager the filter manager
	 */
	public void setUIFilterManager(UIFilterManager uiFilterManager) {
		this.uiFilterManager = uiFilterManager;
	}

	/**
	 * Replaces the old database with the given in the filter package.
	 *
	 * @param database the current database
	 */
	public void setDatabase(GraphDatabase database) {
		try {
			filtermanagement.setDatabase(database);
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
			filtermanagement.updateFilter(filterInput, id);
			this.grapeUI.updateTable();
		} catch (ConnectionFailedException
				| InsertionFailedException | UnexpectedObjectException e) {
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
	public void updateFilter(String filterInput, int filterId, int groupId) throws InvalidInputException {
		try {
			filtermanagement.updateFilter(filterInput, filterId, groupId);
			this.grapeUI.updateTable();
		} catch (ConnectionFailedException | InsertionFailedException | UnexpectedObjectException e) {
			statusbar.addMessage(e.getMessage());
		}
	}

	/**
	 * Update filter group.
	 *
	 * @param filterInput the filter input
	 * @param id          the id
	 */
	public void updateFilterGroup(String filterInput, int id) {
		try {
			filtermanagement.updateFiltergroup(filterInput, id);
			this.grapeUI.updateTable();
		} catch (ConnectionFailedException | UnexpectedObjectException | InsertionFailedException e) {
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
			filtermanagement.removeFiltersegment(id);
			this.grapeUI.updateTable();
		} catch (ConnectionFailedException | UnexpectedObjectException | InsertionFailedException e) {
			statusbar.addMessage(e.getMessage());
		}
	}

	/**
	 * .
	 *
	 * @param id the id
	 */
	public void activate(int id) {
		try {
			filtermanagement.activate(id);
			this.grapeUI.updateTable();
		} catch (UnexpectedObjectException
				| InsertionFailedException | ConnectionFailedException e) {
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
			filtermanagement.deactivate(id);
			this.grapeUI.updateTable();
		} catch (UnexpectedObjectException
				| InsertionFailedException | ConnectionFailedException e) {
			statusbar.addMessage(e.getMessage());
		}
	}

	/**
	 * Displays every filter that the current database contains.
	 */
	public void updateFilters() {
		this.uiFilterManager.clearFilters();
		Set<Filter> filterSet = filtermanagement.getAvailableFilters();
		Set<Filtergroup> filtergroupSet = filtermanagement.getAvailableFilterGroups();
		List<Integer> newId = new LinkedList<>();
		newId.add(0);

		for (Filter f : filterSet) {
			SimpleFilter simpleFilter = new SimpleFilter(f.getID(), f.getName());
			uiFilterManager.addNewSimpleFilter(simpleFilter);
			newId.add(f.getID());
		}
		for (Filtergroup f : filtergroupSet) {
			FilterGroup filterGroup = new FilterGroup(f.getID(), f.getName());
			for (Filter filter : f.getAvailableFilters()) {
				SimpleFilter simpleFilter = new SimpleFilter(filter.getID(), filter.getName());
				filterGroup.add(simpleFilter);
				newId.add(filter.getID());
			}
			uiFilterManager.addNewFilterGroup(filterGroup);
			newId.add(f.getID());
		}
		uiFilterManager.setNextUniqueID(Collections.max(newId) + 1);
		this.filterUI.update();

	}

	/**
	 * gets all graphs that fulfill the filter requirements and sorts these graphs by graphID.
	 *
	 * @return a list of PropertyGraph<V,E>.
	 */
	public ResultSet getFilteredAndSortedGraphs() {
		try {
			return filtermanagement.getFilteredAndSortedGraphs();
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
			return filtermanagement.getFilteredAndAscendingSortedGraphs(property);
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
			return filtermanagement.getFilteredAndDescendingSortedGraphs(property);
		} catch (ConnectionFailedException e) {
			statusbar.addMessage(e.getMessage());
		}
		return null;
	}
}


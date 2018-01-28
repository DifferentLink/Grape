package edu.kit.ipd.dbis.controller;

import edu.kit.ipd.dbis.database.exceptions.sql.*;
import edu.kit.ipd.dbis.filter.Filtermanagement;
import edu.kit.ipd.dbis.filter.InvalidInputException;
import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.log.Event;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static edu.kit.ipd.dbis.log.EventType.MESSAGE;

//TODO:
public class FilterController {

	private Filtermanagement filter;
	private StatusbarController log;

	//TODO: Singleton pattern
	private static FilterController filterController;

	private FilterController() {
		this.log = StatusbarController.getInstance();
		this.filter = new Filtermanagement();
	}

	public static FilterController getInstance() {
		if (filterController == null) {
			filterController = new FilterController();
		}
		return filterController;
	}

	/**
	 * Replaces the old database with the given in the filter package.
	 *
	 * @param database the current database
	 */
	public void setDatabase(GraphDatabase database) {
		try {
			filter.setDatabase(database);
		} catch (DatabaseDoesNotExistException | AccessDeniedForUserException | TablesNotAsExpectedException | ConnectionFailedException e) {
			log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.EMPTY_SET));
		}
	}

	public void updateFilter(String filterInput, int id) throws InvalidInputException {
		try {
			filter.updateFilter(filterInput, id);
		} catch (TablesNotAsExpectedException | ConnectionFailedException | AccessDeniedForUserException | InsertionFailedException | DatabaseDoesNotExistException | UnexpectedObjectException e) {
			log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.EMPTY_SET));
		}
	}

	public void addFilterToGroup(String filterInput, int filterId, int groupId) throws InvalidInputException {
		try {
			filter.addFilterToGroup(filterInput, filterId, groupId);
		} catch (TablesNotAsExpectedException | ConnectionFailedException | AccessDeniedForUserException | InsertionFailedException | UnexpectedObjectException | DatabaseDoesNotExistException e) {
			log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.EMPTY_SET));
		}
	}

	public void updateFilterGroup(String filterInput, int id) throws InvalidInputException {
		filter.updateFiltergroup(filterInput, id);
	}

	public void removeFiltersegment(int id) {
		try {
			filter.removeFiltersegment(id);
		} catch (DatabaseDoesNotExistException | AccessDeniedForUserException | ConnectionFailedException | TablesNotAsExpectedException | UnexpectedObjectException | InsertionFailedException e) {
			log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.EMPTY_SET));
		}
	}

	public void activate(int id) {
		try {
			filter.activate(id);
		} catch (TablesNotAsExpectedException | DatabaseDoesNotExistException | UnexpectedObjectException | AccessDeniedForUserException | InsertionFailedException | ConnectionFailedException e) {
			log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.EMPTY_SET));
		}
	}

	public void deactivate(int id) {
		try {
			filter.deactivate(id);
		} catch (TablesNotAsExpectedException | DatabaseDoesNotExistException | UnexpectedObjectException | AccessDeniedForUserException | InsertionFailedException | ConnectionFailedException e) {
			log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.EMPTY_SET));
		}
	}

	/**
	 * gets all graphs that fulfill the filter requirements and sorts these graphs by graphID.
	 *
	 * @return a list of PropertyGraph<V,E>.
	 */
	public List<PropertyGraph> getFilteredAndSortedGraphs() {
		List<PropertyGraph> graphs = new LinkedList<PropertyGraph>();
		try {
			graphs = filter.getFilteredAndSortedGraphs();
		} catch (DatabaseDoesNotExistException | TablesNotAsExpectedException | ConnectionFailedException | AccessDeniedForUserException e) {
			log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.EMPTY_SET));
		}
		return graphs;
	}

	/**
	 * gets all graphs that fulfill the filter requirements and sorts these graphs ascending
	 * by a specific attribute
	 *
	 * @param property the property to sort after.
	 * @return a list of PropertyGraph<V,E>.
	 */
	public List<PropertyGraph> getFilteredAndAscendingSortedGraphs(Property property) {
		List<PropertyGraph> graphs = new LinkedList<PropertyGraph>();
		try {
			graphs = filter.getFilteredAndAscendingSortedGraphs(property);
		} catch (DatabaseDoesNotExistException | AccessDeniedForUserException | ConnectionFailedException | TablesNotAsExpectedException e) {
			log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.EMPTY_SET));
		}
		return graphs;
	}

	/**
	 * gets all graphs that fulfill the filter requirements and sorts these graphs descending by a specific attribute
	 *
	 * @param property the property to sort after.
	 * @return a list of PropertyGraph<V,E>.
	 */
	public List<PropertyGraph> getFilteredAndDescendingSortedGraphs(Property property) {
		List<PropertyGraph> graphs = new LinkedList<PropertyGraph>();
		try {
			graphs = filter.getFilteredAndDescendingSortedGraphs(property);
		} catch (DatabaseDoesNotExistException | AccessDeniedForUserException | ConnectionFailedException | TablesNotAsExpectedException e) {
			log.addEvent(new Event(MESSAGE, e.getMessage(), Collections.EMPTY_SET));
		}
		return graphs;
	}
}


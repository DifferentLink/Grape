package edu.kit.ipd.dbis.controller;

import edu.kit.ipd.dbis.filter.Filtermanagement;
import edu.kit.ipd.dbis.filter.InvalidInputException;
import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.util.ArrayList;
import java.util.List;

//TODO:
public class FilterController {

	private Filtermanagement filter;

	//TODO: Singleton pattern
	private static FilterController filterController;

	private FilterController() {
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
	public void setDatabase(GraphDatabase database) throws Exception {
		filter.setDatabase(database);
	}

	public void addFilter(String filterInput, int id) throws Exception, InvalidInputException {
		filter.addFilter(filterInput, id);
	}

	public void addFilterToGroup(String filterInput, int filterId, int groupId) throws Exception, InvalidInputException {
		filter.addFilterToGroup(filterInput, filterId, groupId);
	}

	public void addFiltergroup(String filterInput, int id) throws Exception, InvalidInputException {
		filter.addFiltergroup(filterInput, id);
	}

	public void removeFiltersegment(int id) throws Exception {
		filter.removeFiltersegment(id);
	}

	public void activate(int id) throws Exception {
		filter.activate(id);
	}

	public void deactivate(int id) throws Exception {
		filter.deactivate(id);
	}

	/**
	 * gets all graphs that fulfill the filter requirements and sorts these graphs by graphID.
	 *
	 * @return a list of PropertyGraph<V,E>.
	 */
	public List<PropertyGraph> getFilteredAndSortedGraphs() {
		List<PropertyGraph> graphs = new ArrayList<PropertyGraph>();
		return graphs;
	}

	/**
	 * gets all graphs that fulfill the filter requirements and sorts these graphs ascending
	 * by a specific attribute
	 *
	 * @param property the property to sort after.
	 * @return a list of PropertyGraph<V,E>.
	 */
	public List<PropertyGraph> getFilteredAndAscendingSortedGraphs(Property property) throws Exception {
		return filter.getFilteredAndAscendingSortedGraphs(property);
	}

	/**
	 * gets all graphs that fulfill the filter requirements and sorts these graphs descending by a specific attribute
	 *
	 * @param property the property to sort after.
	 * @return a list of PropertyGraph<V,E>.
	 */
	public List<PropertyGraph> getFilteredAndDescendingSortedGraphs(Property property) throws Exception {
		return filter.getFilteredAndDescendingSortedGraphs(property);
	}
}


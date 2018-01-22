package edu.kit.ipd.dbis.controller;

import edu.kit.ipd.dbis.Filter.Filtermanagement;
import edu.kit.ipd.dbis.database.GraphDatabase;
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

	public void addFilter(String filterInput) throws Exception {
		filter.addFilter(filterInput);
	}

	public void addFilterToGroup(String filterInput, int groupId) throws Exception {
		filter.addFilterToGroup(filterInput, groupId);
	}

	public void addFiltergroup(String filterInput) throws Exception {
		filter.addFilterGroup(filterInput);
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
	public List<PropertyGraph<Integer, Integer>> getFilteredAndSortedGraphs() {
		List<PropertyGraph<Integer, Integer>> graphs = new ArrayList<PropertyGraph<Integer, Integer>>();

		return graphs;
	}

	/**
	 * gets all graphs that fulfill the filter requirements and sorts these graphs ascending
	 * by a specific attribute
	 *
	 * @param property the property to sort after.
	 * @return a list of PropertyGraph<V,E>.
	 */
	public List<PropertyGraph<Integer, Integer>> getFilteredAndAscendingSortedGraphs(Property property) {
		return null;
	}

	/**
	 * gets all graphs that fulfill the filter requirements and sorts these graphs descending by a specific attribute
	 *
	 * @param property the property to sort after.
	 * @return a list of PropertyGraph<V,E>.
	 */
	public List<PropertyGraph<Integer, Integer>> getFilteredAndDescendingSortedGraphs(Property property) {
		return null;
	}
}


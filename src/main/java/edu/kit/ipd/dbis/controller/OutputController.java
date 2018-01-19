package edu.kit.ipd.dbis.controller;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.util.ArrayList;
import java.util.List;

public class OutputController {

/*	FilterController filter;
	TableRequest table;*/

	public OutputController() {
		/*filter = new FilterController();
		table = new TableRequest();*/
	}

	/**
	 * gets all graphs that fulfill the filter requirements and sorts these graphs by graphID.
	 *
	 * @return a list of PropertyGraph<V,E>.
	 */
	public List<PropertyGraph<Integer, Integer>> getFilteredAndSortedGraphs() {

		List<PropertyGraph<Integer,Integer>> graphs = new ArrayList<PropertyGraph<Integer,Integer>>();

		/*
		while(filter.hasNextValidGraph()) {
			graphs.add(filter.getNextValidGraph());
		}
		// Liste sortieren
		table.sortAscending(graphs);
		 */

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

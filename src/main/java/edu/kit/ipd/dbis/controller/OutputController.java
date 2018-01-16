package edu.kit.ipd.dbis.controller;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.util.ArrayList;
import java.util.List;

public class OutputController {

	/**
	 * gets all filtered and sorted graphs, checks the input for the correlation, creates a new instance
	 * of CorrelationRequest and finally executes use() on the CorrelationRequest instance with the graph list.
	 *
	 * @param input the input for the correlation.
	 * @return a list of CorrelationOutput.
	 */
//	public List<CorrelationOutput> newCorrelation(String input) {
//		return null;
//	}

	/**
	 * gets all graphs that fulfill the filter requirements and sorts these graphs by graphID.
	 *
	 * @return a list of PropertyGraph<V,E>.
	 */
	public List<PropertyGraph<Integer, Integer>> getFilteredAndSortedGraphs() {

		List<PropertyGraph<Integer,Integer>> graphs = new ArrayList<PropertyGraph<Integer,Integer>>();

		/*
		Filtermanagement filter = new Filtermanagement();
		TableRequest sortionRequest = new TableRequest(ID);

		while(filter.hasNextValidGraph()) {
			graphs.add(filter.getNextValidGraph());
		}
		// Liste sortieren
		sortionRequest.sortDescending(graphs);
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
	public List<PropertyGraph<Integer, Integer>> getFilteredAndAscendingSortedGraphs(Enum property) {
		return null;
	}

	/**
	 * gets all graphs that fulfill the filter requirements and sorts these graphs descending by a specific attribute
	 *
	 * @param property the property to sort after.
	 * @return a list of PropertyGraph<V,E>.
	 */
	public List<PropertyGraph<Integer, Integer>> getFilteredAndDescendingSortedGraphs(Enum property) {
		return null;
	}

}

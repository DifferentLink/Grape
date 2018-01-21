package edu.kit.ipd.dbis.database;

import edu.kit.ipd.dbis.Filter.Filtersegment;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.util.LinkedList;
import java.util.Set;

/**
 *
 */
public interface DatabaseManager {

	/**
	 *
	 * @param graph
	 */
	void addGraph(PropertyGraph graph) throws Exception;

	/**
	 *
	 * @param filter
	 */
	void addFilter(Filtersegment filter) throws Exception;

	/**
	 *
	 * @param id
	 */
	void deleteGraph(int id) throws Exception;

	/**
	 *
	 * @param id
	 */
	void deleteFilter(int id) throws Exception;

	/**
	 *
	 * @param id
	 */
	void restoreGraph(int id) throws Exception;

	/**
	 *
	 * @param id
	 */
	void changeStateOfFilter(int id) throws Exception;

	/**
	 *
	 */
	void permanentlyDeleteGraphs() throws Exception;

	/**
	 *
	 * @param id
	 * @param graph
	 */
	void replaceGraph(int id, PropertyGraph graph) throws Exception;

	/**
	 *
	 * @param id
	 * @param filter
	 */
	void replaceFilter(int id, Filtersegment filter) throws Exception;

	/**
	 *
	 * @param database
	 */
	void merge(GraphDatabase database) throws Exception;

	/**
	 *
	 * @param graph
	 * @return
	 */
	boolean graphExists(PropertyGraph graph) throws Exception;

	/**
	 *
	 * @return
	 */
	Set<Filtersegment> getFilters() throws Exception;

	/**
	 *
	 * @param id
	 * @return
	 */
	Filtersegment getFilterById(int id) throws Exception;

	/**
	 *
	 * @return
	 */
	Set<Filtersegment> getActivatedFilters() throws Exception;

	/**
	 *
	 * @return
	 */
	LinkedList<PropertyGraph> getGraphs(String[][] filters, String column, boolean ascending) throws Exception;

	/**
	 *
	 * @param id
	 * @return
	 */
	PropertyGraph getGraphById(int id) throws Exception;

	/**
	 *
	 * @return
	 */
	Set<PropertyGraph> getCalculatedGraphs() throws Exception;

	/**
	 *
	 * @return
	 */
	Set<PropertyGraph> getUncalculatedGraphs() throws Exception;

	/**
	 *
	 * @param vertices
	 * @return
	 */
	Set<PropertyGraph> getGraphsByVertex(int vertices) throws Exception;

}


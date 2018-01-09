package edu.kit.ipd.dbis.database;

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
	void addFilter(FilterSegment filter) throws Exception;

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
	void repleaceGraph(int id, PropertyGraph graph) throws Exception;

	/**
	 *
	 * @param id
	 * @param filter
	 */
	void repleaceFilter(int id, FilterSegment filter) throws Exception;

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
	Set<FilterSegment> getFilters() throws Exception;

	/**
	 *
	 * @param id
	 * @return
	 */
	FilterSegment getFilterById(int id) throws Exception;

	/**
	 *
	 * @return
	 */
	Set<FilterSegment> getActivatedFilters() throws Exception;

	/**
	 *
	 * @return
	 */
	Set<PropertyGraph> getGraphs() throws Exception;

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


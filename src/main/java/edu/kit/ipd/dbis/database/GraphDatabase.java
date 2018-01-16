package edu.kit.ipd.dbis.database;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.Controller.Filter.Filtersegment;
import java.util.Set;

public class GraphDatabase implements DatabaseManager {

	private String directory;
	private GraphTable graphTable;
	private FilterTable filterTable;

	/**
	 *
	 */
	public GraphDatabase(GraphTable graphTable, FilterTable filterTable) {
		this.graphTable = graphTable;
		this.filterTable = filterTable;
	}

	/**
	 *
	 * @return
	 */
	public GraphTable getGraphTable() {
		return this.graphTable;
	}

	/**
	 *
	 * @return
	 */
	public FilterTable getFilterTable() {
		return this.filterTable;
	}

	/**
	 *
	 */
	public void setDirectory(String directory) {
		this.directory = directory;
	}

	@Override
	public void addGraph(PropertyGraph graph) throws Exception {

	}

	@Override
	public void addFilter(Filtersegment filter) throws Exception {

	}

	@Override
	public void deleteGraph(int id) throws Exception {

	}

	@Override
	public void deleteFilter(int id) throws Exception {

	}

	@Override
	public void restoreGraph(int id) throws Exception {

	}

	@Override
	public void changeStateOfFilter(int id) throws Exception {

	}

	@Override
	public void permanentlyDeleteGraphs() throws Exception {

	}

	@Override
	public void repleaceGraph(int id, PropertyGraph graph) throws Exception {

	}

	@Override
	public void repleaceFilter(int id, Filtersegment filter) throws Exception {

	}

	@Override
	public void merge(GraphDatabase database) throws Exception {

	}

	@Override
	public boolean graphExists(PropertyGraph graph) throws Exception {
		return false;
	}

	@Override
	public Set<Filtersegment> getFilters() throws Exception {
		return null;
	}

	@Override
	public Filtersegment getFilterById(int id) throws Exception {
		return null;
	}

	@Override
	public Set<Filtersegment> getActivatedFilters() throws Exception {
		return null;
	}

	@Override
	public Set<PropertyGraph> getGraphs() throws Exception {
		return null;
	}

	@Override
	public PropertyGraph getGraphById(int id) throws Exception {
		return null;
	}

	@Override
	public Set<PropertyGraph> getCalculatedGraphs() throws Exception {
		return null;
	}

	@Override
	public Set<PropertyGraph> getUncalculatedGraphs() throws Exception {
		return null;
	}

	@Override
	public Set<PropertyGraph> getGraphsByVertex(int vertices) throws Exception {
		return null;
	}

	/**
	 *
	 * @param graph
	 * @return
	 */
	private boolean isCalculated(PropertyGraph graph) {
		return true;
	}


}

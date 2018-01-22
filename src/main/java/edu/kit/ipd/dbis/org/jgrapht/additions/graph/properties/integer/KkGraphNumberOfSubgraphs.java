package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.IntegerProperty;
import org.kohsuke.MetaInfServices;

/**
 * TODO: design change
 * Calculates a number of subgraphs of a graph's Kk-graph.
 */
@MetaInfServices
public class KkGraphNumberOfSubgraphs extends IntegerProperty {
	/**
	 * Standard constructor
	 *
	 * @param graph
	 */
	public KkGraphNumberOfSubgraphs(PropertyGraph graph) {
		super(graph);
	}

	@Override
	protected Integer calculateAlgorithm(PropertyGraph graph) {
		return null;
	}
}

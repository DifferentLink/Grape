package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.KkGraphAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.IntegerProperty;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.KkGraph;
import org.kohsuke.MetaInfServices;

/**
 * Calculates a number of subgraphs of a graph's Kk-graph.
 */
@MetaInfServices
public class KkGraphNumberOfSubgraphs extends IntegerProperty {
	/**
	 * Standard constructor
	 *
	 * @param graph the input graph
	 */
	public KkGraphNumberOfSubgraphs(PropertyGraph graph) {
		super(graph);
	}

	@Override
	protected Integer calculateAlgorithm(PropertyGraph graph) {
		return ((KkGraphAlgorithm.KkGraph) graph.getProperty(KkGraph.class).getValue()).getNumberOfSubgraphs();
	}
}

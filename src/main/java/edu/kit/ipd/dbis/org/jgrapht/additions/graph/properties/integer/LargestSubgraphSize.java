package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.KkGraphAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.IntegerProperty;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.KkGraph;

import java.util.List;
import java.util.Set;

/**
 * the largest subgraph size property
 */
public class LargestSubgraphSize extends IntegerProperty {
	/**
	 * Standard constructor
	 *
	 * @param graph the input graph
	 */
	public LargestSubgraphSize(PropertyGraph graph) {
		super(graph);
	}

	@Override
	protected Integer calculateAlgorithm(PropertyGraph graph) {
		int largestSubgraphSize = 0;
		for (Set<Object> s : (List<Set<Object>>)
				((KkGraphAlgorithm.KkGraph) graph.getProperty(KkGraph.class).getValue()).getSubgraphs()) {
			if (s.size() > largestSubgraphSize) {
				largestSubgraphSize = s.size();
			}
		}
		return largestSubgraphSize;
	}
}

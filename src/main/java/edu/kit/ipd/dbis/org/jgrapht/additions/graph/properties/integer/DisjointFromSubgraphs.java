package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.IntegerProperty;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.KkGraph;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Returns the number of vertices which are disjoint from the
 * calculated subgraphs.
 */
public class DisjointFromSubgraphs extends IntegerProperty {
	/**
	 * Standard constructor
	 *
	 * @param graph
	 */
	public DisjointFromSubgraphs(PropertyGraph graph) {
		super(graph);
	}

	@Override
	protected Integer calculateAlgorithm(PropertyGraph graph) {
		List<Set<Object>> subgraphs = (List<Set<Object>>) graph.getProperty(KkGraph.class).getValue();
		Set<Object> subgraphVertices = new HashSet<>();
		for (Set<Object> s : subgraphs) {
			subgraphVertices.addAll(s);
		}
		Set<Object> tmp = new HashSet<>(graph.vertexSet());
		tmp.removeAll(subgraphVertices);
		return tmp.size();
	}
}

package edu.kit.ipd.dbis.org.jgrapht.additions.alg.density;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.NumberDensityAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.BfsCode;

import java.util.List;
import java.util.Objects;

/**
 The proportion density algorithm.
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 */
public class NodeSpecificDensityAlgorithm<V, E> implements NumberDensityAlgorithm {
	/**
	 * The input graph
	 */
	protected final PropertyGraph<V, E> graph;

	/**
	 * Construct a new proportion density algorithm.
	 *
	 * @param graph the input graph
	 */
	public NodeSpecificDensityAlgorithm(PropertyGraph<V, E> graph) {
		this.graph = Objects.requireNonNull(graph, "Graph cannot be null");
	}

	@Override
	public double getDensity() {
		int maxEdges = 0;
		for (int i = graph.vertexSet().size() - 1; i > 0; i--) {
			maxEdges += i;
		}
		return maxEdges / graph.edgeSet().size();
	}
}
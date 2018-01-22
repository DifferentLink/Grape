package edu.kit.ipd.dbis.org.jgrapht.additions.alg.kkgraph;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.KkGraphAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.util.Objects;

/**
 *The kk-graph generator. It generates the kk-graph for the input graph depending on the Hadwiger Conjecture.
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 */
public class KkGraphGenerator<V, E> implements KkGraphAlgorithm {
	/**
	 * The input graph
	 */
	protected final PropertyGraph<V, E> graph;

	/**
	 * Construct a new proportion density algorithm.
	 *
	 * @param graph the input graph
	 */
	public KkGraphGenerator(PropertyGraph<V, E> graph) {
		this.graph = Objects.requireNonNull(graph, "Graph cannot be null");
	}

	@Override
	public KkGraph getKkGraph() {
		//TODO: implement me
		return null;
	}
}

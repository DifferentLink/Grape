package edu.kit.ipd.dbis.org.jgrapht.additions.alg.density;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import org.jgrapht.Graph;

import java.util.Objects;

/**
 The minimal bfs code algorithm.
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 */
public class MinimalBfsCodeAlgorithm<V, E> implements BfsCodeAlgorithm {
	/**
	 * The input graph
	 */
	protected final Graph<V, E> graph;

	/**
	 * Construct a new minimal bfs code algorithm.
	 *
	 * @param graph the input graph
	 */
	public MinimalBfsCodeAlgorithm(Graph<V, E> graph) {
		this.graph = Objects.requireNonNull(graph, "Graph cannot be null");
	}

	@Override
	public BfsCode getBfsCode() {
		//Import the bfs code algorithm
		return null;
	}
}

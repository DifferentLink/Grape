package edu.kit.ipd.dbis.org.jgrapht.additions.alg.density;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.NumberDensityAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.util.Objects;

/**
 The proportion density algorithm.
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 */
public class ProportionDensityAlgorithm<V, E> implements NumberDensityAlgorithm {
	/**
	 * The input graph
	 */
	protected final PropertyGraph<V, E> graph;

	/**
	 * Construct a new proportion density algorithm.
	 *
	 * @param graph the input graph
	 */
	public ProportionDensityAlgorithm(PropertyGraph<V, E> graph) {
		this.graph = Objects.requireNonNull(graph, "Graph cannot be null");
	}

	private int factorial(int n) {
		if (n < 0) {
			throw new IllegalArgumentException("n has to be positive!");
		}
		if (n == 0) {
			return 1;
		}
		int result = 1;
		for (int i = n; i > 0; i--) {
			result = result * i;
		}
		return result;
	}

	@Override
	public double getDensity() {
		int maxEdge = factorial(graph.vertexSet().size() - 1); //Computes the max edge number
		return graph.edgeSet().size() / maxEdge;
	}
}

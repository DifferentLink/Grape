package edu.kit.ipd.dbis.org.jgrapht.additions.alg.density;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.NumberDensityAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.util.Objects;

/**
The binomial density algorithm.
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 */
public class BinomialDensityAlgorithm<V, E> implements NumberDensityAlgorithm {
	/**
	 * The input graph
	 */
	protected final PropertyGraph<V, E> graph;

	/**
	 * Construct a new binomial density algorithm.
	 *
	 * @param graph the input graph
	 */
	public BinomialDensityAlgorithm(PropertyGraph<V, E> graph) {
		this.graph = Objects.requireNonNull(graph, "Graph cannot be null");
	}

	private int binomialCoefficient(int n, int k) {
		int result = 1;
		//formula for the binomial coefficient
		for (int j = 1; j <= k; j++) {
			result = result * ((n + 1 - j) / j);
		}
		return result;
	}
	@Override
	public double getDensity() {
		int k = graph.vertexSet().size();
		int n = graph.edgeSet().size();
		return (double)n / binomialCoefficient(k, 2);
	}
}

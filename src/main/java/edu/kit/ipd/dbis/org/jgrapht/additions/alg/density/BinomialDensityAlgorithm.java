package edu.kit.ipd.dbis.org.jgrapht.additions.alg.density;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.NumberDensityAlgorithm;
import org.jgrapht.Graph;

import java.util.Objects;

/**
The binomial density algorithm.
 *
 * @param <V>
 * @param <E>
 */
public class BinomialDensityAlgorithm<V, E> implements NumberDensityAlgorithm {
	/**
	 * The input graph
	 */
	protected final Graph<V, E> graph;

	/**
	 * Construct a new binomial density algorithm.
	 *
	 * @param graph the input graph
	 */
	public BinomialDensityAlgorithm(Graph<V, E> graph) {
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
		return n / binomialCoefficient(k, 2);
	}
}

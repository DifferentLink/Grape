package edu.kit.ipd.dbis.org.jgrapht.additions.alg.density;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.ProfileDensityAlgorithm;
import org.jgrapht.Graph;

import java.util.Objects;

/**
 The minimal profile algorithm.
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 */
public class MinimalProfileAlgorithm<V, E> implements ProfileDensityAlgorithm {

	/**
	 * The input graph
	 */
	protected final Graph<V, E> graph;

	/**
	 * Construct a new minimal profile algorithm.
	 *
	 * @param graph the input graph
	 */
	public MinimalProfileAlgorithm(Graph<V, E> graph) {
		this.graph = Objects.requireNonNull(graph, "Graph cannot be null");
	}

	@Override
	public Profile getProfile() {
		//Import profile algorithm
		return null;
	}
}

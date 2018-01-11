package edu.kit.ipd.dbis.org.jgrapht.additions.alg.density;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.NextDensityAlgorithm;
import org.jgrapht.Graph;

import java.util.Objects;

/**
 The next denser graph algorithm.
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 */
public class NextDenserGraphFinder<V, E> implements NextDensityAlgorithm {

	/**
	 * The input graph
	 */
	protected final Graph<V, E> graph;

	/**
	 * Construct a new next denser graph finder.
	 *
	 * @param graph the input graph
	 */
	public NextDenserGraphFinder(Graph<V, E> graph) {
		this.graph = Objects.requireNonNull(graph, "Graph cannot be null");
	}

	@Override
	public Graph getNextDensityGraph() {
		//Import next denser graph algorithm
		return null;
	}
}

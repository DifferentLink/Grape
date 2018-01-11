package edu.kit.ipd.dbis.org.jgrapht.additions.alg.color;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.TotalColoringAlgorithm;
import org.jgrapht.Graph;
import java.util.Objects;

/**
 * The minimal coloring algorithm.
 *
 * <p>
 * Description of the algorithm
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 *
 */
public class MinimalTotalColoring<V, E> implements TotalColoringAlgorithm<V, E> {
	/**
	 * The input graph
	 */
	protected final Graph<V, E> graph;

	/**
	 * Constructs a new minimal total coloring algorithm
	 *
	 * @param graph the input graph
	 */
	public MinimalTotalColoring(Graph<V, E> graph) {
		this.graph = Objects.requireNonNull(graph, "Graph cannot be null");
	}

	@Override
	public TotalColoring getColoring() {
		//Compute Coloring
		return null;
	}
}

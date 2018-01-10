package edu.kit.ipd.dbis.org.jgrapht.additions.alg.color;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.TotalColoringAlgorithm;
import org.jgrapht.Graph;
import java.util.Objects;

/**
 *
 * @param <V>
 * @param <E>
 */
public class MinimalTotalColoring<V, E> implements TotalColoringAlgorithm<V, E> {

	protected final Graph<V, E> graph;

	/**
	 * A minimal total coloring
	 *
	 * @param graph the corresponding graph
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

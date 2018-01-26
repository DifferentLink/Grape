package edu.kit.ipd.dbis.org.jgrapht.additions.alg.color;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.TotalColoringAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;

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
	protected final PropertyGraph<V, E> graph;

	/**
	 * Constructs a new minimal total coloring algorithm
	 *
	 * @param graph the input graph
	 */
	public MinimalTotalColoring(PropertyGraph<V, E> graph) {
		this.graph = Objects.requireNonNull(graph, "Graph cannot be null");
	}

	@Override
	public TotalColoring getColoring() {
		PropertyGraph edgeToVertexGraph = this.makeEdgesToVertices();
		VertexColoringAlgorithm vertexColoringAlgorithm = new MinimalVertexColoring(edgeToVertexGraph);
		VertexColoringAlgorithm.Coloring coloring = vertexColoringAlgorithm.getColoring();
		// parse back
		return null;
	}

	private PropertyGraph makeEdgesToVertices() {
		//TODO: implement me
		return null;
	}
}

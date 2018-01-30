package edu.kit.ipd.dbis.org.jgrapht.additions.alg.color;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.TotalColoringAlgorithm;
import org.jgrapht.Graph;
import org.jgrapht.VertexFactory;
import org.jgrapht.graph.ClassBasedVertexFactory;

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
	private final Graph<V, E> graph;
	private VertexFactory<V> vertexFactory;

	/**
	 * Constructs a new minimal total coloring algorithm.
	 * The vertex class is needed in order to create
	 * new vertices in the input graph.
	 *
	 * @param graph the input graph
	 * @param vertexClass the vertices' class
	 */
	public MinimalTotalColoring(Graph<V, E> graph, Class<? extends V> vertexClass) {
		this.graph = Objects.requireNonNull(graph, "Graph cannot be null");
		this.vertexFactory = new ClassBasedVertexFactory<>(vertexClass);
	}

	/**
	 * Constructs a new minimal total coloring algorithm.
	 *
	 * @param graph         the input graph
	 * @param vertexFactory the graph's vertex factory
	 */
	public MinimalTotalColoring(Graph<V, E> graph, VertexFactory vertexFactory) {
		this.graph = Objects.requireNonNull(graph, "Graph cannot be null");
		this.vertexFactory = Objects.requireNonNull(vertexFactory, "VertexFactory cannot be null");
	}

	@Override
	public TotalColoring getColoring() {
		Graph edgeToVertexGraph = this.makeEdgesToVertices();
		return null;
	}

	private Graph makeEdgesToVertices() {
		Graph edgeToVertexGraph = this.graph;
		// iterate over vertices
		for (Object v : this.graph.vertexSet()) {
			// iterate over vertex v's edges
			for (Object e : this.graph.outgoingEdgesOf((V) v)) {
				// make edge to vertex by creating new
				// vertex that is situated inbetween
				// v and the edges' target and creating
				// edges from the new vertex to those two
				// vertices.
				V edgeToVertex = this.vertexFactory.createVertex();
				edgeToVertexGraph.addVertex(edgeToVertex);
				edgeToVertexGraph.addEdge(v, edgeToVertex);
				edgeToVertexGraph.addEdge(edgeToVertex, edgeToVertexGraph.getEdgeTarget(e));
			}
		}
		return edgeToVertexGraph;
	}
}

package edu.kit.ipd.dbis.org.jgrapht.additions.alg.color;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.TotalColoringAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.jgrapht.Graph;
import org.jgrapht.VertexFactory;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;
import org.jgrapht.alg.util.IntegerVertexFactory;

import java.util.*;

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
	private Map<Integer, V> integerVMap;
	private Map<Integer, E> integerEMap;

	/**
	 * Constructs a new minimal total coloring algorithm.
	 *
	 * @param graph         the input graph
	 */
	public MinimalTotalColoring(Graph<V, E> graph) {
		this.graph = Objects.requireNonNull(graph, "Graph cannot be null");
	}

	@Override
	public TotalColoring getColoring() {
		Graph edgeToVertexGraph = this.makeEdgesToVertices();
		VertexColoringAlgorithm.Coloring coloring = new MinimalVertexColoring(edgeToVertexGraph).getColoring();
		return null;
	}

	private Graph makeEdgesToVertices() {
		this.integerVMap = new HashMap<>();
		this.integerEMap = new HashMap<>();
		Graph<Integer, Integer> edgeToVertexGraph = new PropertyGraph();
		VertexFactory vertexFactory = new IntegerVertexFactory();
		Set<Object> transformedEdges = new HashSet<>();

		// iterate over vertices
		for (Object v : this.graph.vertexSet()) {
			Object newV = vertexFactory.createVertex();
			if (!edgeToVertexGraph.containsVertex((Integer) newV)) {
				edgeToVertexGraph.addVertex((Integer) newV);
				integerVMap.put((Integer) newV, (V) v);
			}
			// iterate over vertex v's edges
			for (Object e : this.graph.outgoingEdgesOf((V) v)) {
				if (!transformedEdges.contains(e)) {
					// make edge to vertex by creating new
					// vertex that is situated inbetween
					// v and the edges' target and creating
					// edges from the new vertex to those two
					// vertices.
					transformedEdges.add(e);
					Object targetVertex = this.graph.getEdgeTarget((E) e);
					Object newTargetVertex = vertexFactory.createVertex();
					if (!edgeToVertexGraph.containsVertex((Integer) newTargetVertex)) {
						edgeToVertexGraph.addVertex((Integer) newTargetVertex);
					}

					Object edgeToVertex = vertexFactory.createVertex();
					edgeToVertexGraph.addVertex((Integer) edgeToVertex);
					integerEMap.put((Integer) edgeToVertex, (E) e);

					edgeToVertexGraph.addEdge((Integer) newV, (Integer) edgeToVertex);
					edgeToVertexGraph.addEdge((Integer) edgeToVertex, (Integer) newTargetVertex);
				}
			}
		}
		return edgeToVertexGraph;
	}
}

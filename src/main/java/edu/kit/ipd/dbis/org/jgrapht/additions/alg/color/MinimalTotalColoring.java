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
	 * @param graph the input graph
	 */
	public MinimalTotalColoring(Graph<V, E> graph) {
		this.graph = Objects.requireNonNull(graph, "Graph cannot be null");
	}

	/**
	 * Determines all minimal total colorings.
	 *
	 * @return List of minimal total colorings
	 */
	public List<TotalColoring> getAllColorings() {
		return null;
	}
	@Override
	public TotalColoring getColoring() {
		Graph edgeToVertexGraph = this.makeEdgesToVertices();
		VertexColoringAlgorithm.Coloring coloring = new MinimalVertexColoring(edgeToVertexGraph).getColoring();
		return this.createTotalColoringObject(coloring);
	}

	private Graph makeEdgesToVertices() {
		this.integerVMap = new HashMap<>();
		this.integerEMap = new HashMap<>();

		// quick hack. there probably exists a better solution.
		Map<V, Integer> vIntegerMap = new HashMap<>();

		Graph<Integer, Integer> edgeToVertexGraph = new PropertyGraph();
		VertexFactory vertexFactory = new IntegerVertexFactory();
		Set<String> transformedEdges = new HashSet<>();

		Set<Integer> vEdgeToVertexSet = new HashSet<>();

		// iterate over vertices
		for (Object v : this.graph.vertexSet()) {
			Object newV;
			if (!integerVMap.values().contains(v)) {
				newV = vertexFactory.createVertex();
				edgeToVertexGraph.addVertex((Integer) newV);
				integerVMap.put((Integer) newV, (V) v);
				vIntegerMap.put((V) v, (Integer) newV);
			} else {
				newV = vIntegerMap.get(v);
			}

			// iterate over vertex v's edges
			for (Object e : this.graph.outgoingEdgesOf((V) v)) {
				if (!transformedEdges.contains(e.toString())) {
					// make edge to vertex by creating new
					// vertex that is situated inbetween
					// v and the edges' target and creating
					// edges from the new vertex to those two
					// vertices.
					Object edgeTarget = this.graph.getEdgeTarget((E) e);

					transformedEdges.add((e.toString()));
					transformedEdges.add("(" + edgeTarget.toString() + " : " + v.toString() + ")");

					Object newTargetVertex;
					if (vIntegerMap.containsKey((V) edgeTarget)) {
						newTargetVertex = vIntegerMap.get((V) edgeTarget);
					} else {
						newTargetVertex = vertexFactory.createVertex();
					}

					Object edgeToVertex = vertexFactory.createVertex();

					edgeToVertexGraph.addVertex((Integer) newTargetVertex);
					edgeToVertexGraph.addVertex((Integer) edgeToVertex);
					edgeToVertexGraph.addEdge((Integer) newV, (Integer) edgeToVertex);
					edgeToVertexGraph.addEdge((Integer) edgeToVertex, (Integer) newTargetVertex);
					edgeToVertexGraph.addEdge((Integer) newV, (Integer) newTargetVertex);

					integerEMap.put((Integer) edgeToVertex, (E) e);
					integerVMap.put((Integer) newTargetVertex, (V) edgeTarget);

					vIntegerMap.put((V) edgeTarget, (Integer) newTargetVertex);

					vEdgeToVertexSet.add((Integer) edgeToVertex);
				}
			}
		}
		Set<String> addedEdges = new HashSet<>();
		// create edges between all vertices that
		// were created from edges and share at
		// least one target vertex of their edges.
		for (Integer i1 : vEdgeToVertexSet) {
			for (Integer i2 : vEdgeToVertexSet) {
				if (!i1.equals(i2)
						&& !edgeToVertexGraph.containsEdge(i1, i2)
						&& !edgeToVertexGraph.containsEdge(i2, i1)
						&& shareVertex(i1, i2, edgeToVertexGraph, addedEdges)) {
					edgeToVertexGraph.addEdge(i1, i2);
					addedEdges.add(edgeToString(i1, i2));
					addedEdges.add(edgeToString(i2, i1));
				}
			}
		}

		return edgeToVertexGraph;
	}

	private boolean shareVertex(Integer v1, Integer v2, Graph<Integer, Integer> graph, Set<String> addedEdges) {
		Set<String> v1TargetVertices = new HashSet<>();
		for (Object e : graph.outgoingEdgesOf(v1)) {
			if (!addedEdges.contains(e.toString())) {
				if (!("" + e.toString().charAt(1)).equals(v1.toString())) {
					v1TargetVertices.add("" + e.toString().charAt(1));
				} else {
					v1TargetVertices.add(("" + e.toString().charAt(5)));
				}
			}
		}

		for (Object e : graph.outgoingEdgesOf(v2)) {
			if (!addedEdges.contains(e.toString())) {
				String curr;
				if (!("" + e.toString().charAt(1)).equals(v2.toString())) {
					curr = "" + e.toString().charAt(1);
				} else {
					curr = "" + e.toString().charAt(5);
				}
				if (v1TargetVertices.contains(curr)) {
					return true;
				}
			}
		}

		return false;
	}

	private String edgeToString(Object o1, Object o2) {
		return "(" + o1.toString() + " : " + o2.toString() + ")";
	}

	private TotalColoring createTotalColoringObject(VertexColoringAlgorithm.Coloring coloring) {
		Map<V, Integer> vertexColors = new HashMap<>();
		Map<E, Integer> edgeColors = new HashMap<>();
		int numberColors = coloring.getNumberColors();

		Map colors = coloring.getColors();
		for (Object o : colors.keySet()) {
			if (this.integerVMap.containsKey(o)) {
				vertexColors.put(this.integerVMap.get(o), (Integer) colors.get(o));
			} else if (this.integerEMap.containsKey(o)) {
				edgeColors.put(this.integerEMap.get(o), (Integer) colors.get(o));
			}
		}
		return new TotalColoringImpl(vertexColors, edgeColors, numberColors);
	}
}

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
	private List<TotalColoring<V, E>> totalColorings;

	/**
	 * Constructs a new minimal total coloring algorithm.
	 *
	 * @param graph the input graph
	 */
	public MinimalTotalColoring(Graph<V, E> graph) {
		this.graph = Objects.requireNonNull(graph, "Graph cannot be null");
		this.totalColorings = new ArrayList<>();
	}

	/**
	 * Determines all minimal total colorings.
	 *
	 * @return List of minimal total colorings
	 */
	public List<TotalColoring<V, E>> getAllColorings() {
		if (!this.totalColorings.isEmpty()) {
			return this.totalColorings;
		}
		Graph<Integer, Integer> edgeToVertexGraph = this.makeEdgesToVertices();
		List<VertexColoringAlgorithm.Coloring<Integer>> colorings = new MinimalVertexColoring<>(edgeToVertexGraph).getAllColorings();
		for (VertexColoringAlgorithm.Coloring<Integer> c : colorings) {
			this.totalColorings.add(this.createTotalColoringObject(c));
		}
		return this.totalColorings;
	}

	@Override
	public TotalColoring getColoring() {
		if (this.totalColorings.isEmpty()) {
			return this.getAllColorings().get(0);
		} else {
			return this.totalColorings.get(0);
		}
	}

	private Graph<Integer, Integer> makeEdgesToVertices() {
		this.integerVMap = new HashMap<>();
		this.integerEMap = new HashMap<>();

		// quick hack. there probably exists a better solution.
		Map<V, Integer> vIntegerMap = new HashMap<>();

		Graph<Integer, Integer> edgeToVertexGraph = new PropertyGraph<>();
		VertexFactory vertexFactory = new IntegerVertexFactory();
		Set<String> transformedEdges = new HashSet<>();

		Set<Integer> vEdgeToVertexSet = new HashSet<>();

		// iterate over vertices
		for (V v : this.graph.vertexSet()) {
			Integer newV;
			if (!integerVMap.values().contains(v)) {
				newV = (Integer) vertexFactory.createVertex();
				edgeToVertexGraph.addVertex(newV);
				integerVMap.put(newV, v);
				vIntegerMap.put(v, newV);
			} else {
				newV = vIntegerMap.get(v);
			}

			// iterate over vertex v's edges
			for (E e : this.graph.outgoingEdgesOf(v)) {
				if (!transformedEdges.contains(e.toString())) {
					// make edge to vertex by creating new
					// vertex that is situated inbetween
					// v and the edges' target and creating
					// edges from the new vertex to those two
					// vertices.
					V edgeTarget = this.graph.getEdgeTarget(e);

					transformedEdges.add((e.toString()));
					transformedEdges.add("(" + edgeTarget.toString() + " : " + v.toString() + ")");

					Integer newTargetVertex;
					if (vIntegerMap.containsKey(edgeTarget)) {
						newTargetVertex = (Integer) vIntegerMap.get(edgeTarget);
					} else {
						newTargetVertex = (Integer) vertexFactory.createVertex();
					}

					Integer edgeToVertex = (Integer) vertexFactory.createVertex();

					edgeToVertexGraph.addVertex(newTargetVertex);
					edgeToVertexGraph.addVertex(edgeToVertex);
					edgeToVertexGraph.addEdge(newV, edgeToVertex);
					edgeToVertexGraph.addEdge(edgeToVertex, newTargetVertex);
					edgeToVertexGraph.addEdge(newV, newTargetVertex);

					integerEMap.put(edgeToVertex, e);
					integerVMap.put(newTargetVertex, edgeTarget);

					vIntegerMap.put(edgeTarget, newTargetVertex);

					vEdgeToVertexSet.add(edgeToVertex);
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

	private TotalColoring<V, E> createTotalColoringObject(VertexColoringAlgorithm.Coloring<Integer> coloring) {
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
		return new TotalColoringImpl<>(vertexColors, edgeColors, numberColors);
	}
}

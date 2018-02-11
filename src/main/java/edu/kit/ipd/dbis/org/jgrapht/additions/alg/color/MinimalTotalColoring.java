package edu.kit.ipd.dbis.org.jgrapht.additions.alg.color;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.TotalColoringAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.jgrapht.Graph;
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
	private final PropertyGraph<V, E> graph;
	private Map<Integer, V> integerVMap;
	private Map<Integer, E> integerEMap;
	private List<TotalColoring<V, E>> totalColorings;

	/**
	 * Constructs a new minimal total coloring algorithm.
	 *
	 * @param graph the input graph
	 */
	public MinimalTotalColoring(PropertyGraph<V, E> graph) {
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
		PropertyGraph<Integer, Integer> edgeToVertexGraph = this.makeEdgesToVertices();
		List<VertexColoringAlgorithm.Coloring<Integer>> colorings =
				new MinimalVertexColoring<>(edgeToVertexGraph).getAllColorings();
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

	private PropertyGraph<Integer, Integer> makeEdgesToVertices() {
		this.integerVMap = new HashMap<>();
		this.integerEMap = new HashMap<>();

		// quick hack. there probably exists a better solution.
		Map<V, Integer> vIntegerMap = new HashMap<>();

		PropertyGraph<Integer, Integer> edgeToVertexGraph = new PropertyGraph<>();
		IntegerVertexFactory vertexFactory = new IntegerVertexFactory();
		Set<String> transformedEdges = new HashSet<>();

		Set<Integer> vEdgeToVertexSet = new HashSet<>();

		// iterate over vertices
		for (Object v : this.graph.vertexSet()) {
			Integer createdV;
			if (!integerVMap.values().contains(v)) {
				createdV = vertexFactory.createVertex();
				edgeToVertexGraph.addVertex(createdV);
				integerVMap.put(createdV, (V) v);
				vIntegerMap.put((V) v, createdV);
			} else {
				createdV = vIntegerMap.get(v);
			}

			// iterate over vertex v's edges
			for (Object e : this.graph.outgoingEdgesOf(v)) {
				V edgeTarget = (V) this.graph.getEdgeTarget(e);
				if (!transformedEdges.contains((e).toString())
						&& !v.equals(edgeTarget)) {
					// make edge to vertex by creating new
					// vertex that is situated inbetween
					// v and the edges' target and creating
					// edges from the new vertex to those two
					// vertices.

					transformedEdges.add((e.toString()));
					transformedEdges.add("(" + edgeTarget.toString() + " : " + v.toString() + ")");

					Integer createdEdgeTarget;
					if (vIntegerMap.containsKey(edgeTarget)) {
						createdEdgeTarget = vIntegerMap.get(edgeTarget);
					} else {
						createdEdgeTarget = vertexFactory.createVertex();
					}

					Integer createdEdgeToVertex = vertexFactory.createVertex();

					edgeToVertexGraph.addVertex(createdEdgeTarget);
					edgeToVertexGraph.addVertex(createdEdgeToVertex);
					edgeToVertexGraph.addEdge(createdV, createdEdgeToVertex);
					edgeToVertexGraph.addEdge(createdEdgeToVertex, createdEdgeTarget);
					edgeToVertexGraph.addEdge(createdV, createdEdgeTarget);

					integerEMap.put(createdEdgeToVertex, (E) e);
					integerVMap.put(createdEdgeTarget, edgeTarget);

					vIntegerMap.put(edgeTarget, createdEdgeTarget);

					vEdgeToVertexSet.add(createdEdgeToVertex);
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

	private boolean shareVertex(
			Integer edgeToVertex1,
			Integer edgeToVertex2,
			Graph<Integer, Integer> graph,
			Set<String> addedEdges) {
		Set<String> v1TargetVertices = new HashSet<>();
		for (Object e : graph.outgoingEdgesOf(edgeToVertex1)) {
			if (!addedEdges.contains(e.toString())) {
				if (!(extractSourceVertex(e.toString())).equals(edgeToVertex1.toString())) {
					v1TargetVertices.add(extractSourceVertex(e.toString()));
				} else {
					v1TargetVertices.add(extractTargetVertex(e.toString()));
				}
			}
		}

		for (Object e : graph.outgoingEdgesOf(edgeToVertex2)) {
			if (!addedEdges.contains(e.toString())) {
				String curr;
				if (!(extractSourceVertex(e.toString())).equals(edgeToVertex2.toString())) {
					curr = extractSourceVertex(e.toString());
				} else {
					curr = extractTargetVertex(e.toString());
				}
				if (v1TargetVertices.contains(curr)) {
					return true;
				}
			}
		}

		return false;
	}

	private String extractSourceVertex(String edgeString) {
		String[] vertices = edgeString.split(" : ");
		String source = vertices[0];
		source = source.split("\\(")[1];
		return source;
	}

	private String extractTargetVertex(String edgeString) {
		String[] vertices = edgeString.split(" : ");
		String target = vertices[1];
		target = target.split("\\)")[0];
		return target;
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

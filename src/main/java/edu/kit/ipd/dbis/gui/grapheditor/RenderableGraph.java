package edu.kit.ipd.dbis.gui.grapheditor;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.KkGraphAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.TotalColoringAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;
import org.jgrapht.alg.util.IntegerVertexFactory;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.util.*;

/**
 * The graph representation used in the graph editor.
 */
public class RenderableGraph {

	private Set<Vertex> vertices;
	private Set<Edge> edges;
	private int id;
	private Set<Set<Vertex>> subgraphs;

	/**
	 * Creates an empty graph
	 *
	 */
	public RenderableGraph() {
		vertices = new HashSet<>();
		edges = new HashSet<>();
		subgraphs = new HashSet<>();
	}

	/**
	 * Creates a graph with the given edges and vertices and assigns an ID.
	 *
	 * @param vertices the graph's vertices
	 * @param edges the graph's edges
	 * @param id the graph's ID
	 */
	public RenderableGraph(Set<Vertex> vertices, Set<Edge> edges, int id) {
		this.vertices = vertices;
		this.edges = edges;
		this.subgraphs = new HashSet<>();
		this.id = id;
	}

	/**
	 * Creates a graph with the given subgraphs, edges and vertices and assigns an ID.
	 *
	 * @param vertices the graph's vertices
	 * @param edges the graph's edges
	 * @param id the graph's ID
	 * @param subgraphs the graph's subgraphs
	 */
	public RenderableGraph(Set<Vertex> vertices, Set<Edge> edges, int id, Set<Set<Vertex>> subgraphs) {
		this.vertices = vertices;
		this.edges = edges;
		this.id = id;
		this.subgraphs = subgraphs;
	}

	/**
	 * Takes a PropertyGraph as an input and parses it into a RenderableGraph.
	 *
	 * @param propertyGraph the input graph
	 */
	public RenderableGraph(PropertyGraph<Integer, Integer> propertyGraph, VertexFactory factory) {
		this.edges = new HashSet<>();
		this.vertices = new HashSet<>();
		this.subgraphs = new HashSet<>();
		this.id = propertyGraph.getId();
		Map<Object, Vertex> objectVertexMap = new HashMap<>();
		Set<Object> addedEdges = new HashSet<>();

		// iterate over vertices
		for (Object v : propertyGraph.vertexSet()) {
			// check if vertex was already added as
			// 'edgeTarget' in loop below
			if (!objectVertexMap.containsKey(v)) {
				Vertex vertex1 = factory.createVertex();
				this.vertices.add(vertex1);
				objectVertexMap.put(v, vertex1);
			}

			// iterate over vertex v's edges
			for (Object e : propertyGraph.outgoingEdgesOf(v)) {
				if (addedEdges.contains(e)) {
					continue;
				}

				Object edgeTarget = propertyGraph.getEdgeTarget(e);
				if (v.equals(edgeTarget)) {
					edgeTarget = propertyGraph.getEdgeSource(e);
				}

				addedEdges.add(e);
				addedEdges.add(propertyGraph.getEdgeFactory().createEdge(edgeTarget, v));

				// check if vertex was already added
				if (!objectVertexMap.containsKey(edgeTarget)) {
					Vertex vertex2 = factory.createVertex();
					this.vertices.add(vertex2);
					objectVertexMap.put(edgeTarget, vertex2);
				}

				Edge sourceTargetEdge = new Edge(objectVertexMap.get(v), objectVertexMap.get(edgeTarget));
				this.edges.add(sourceTargetEdge);
			}
		}
	}

	/**
	 * Converts a RenderableGraph to a PropertyGraph.
	 *
	 * @return the PropertyGraph
	 */
	public PropertyGraph<Integer, Integer> asPropertyGraph() {
		PropertyGraph<Integer, Integer> graph = new PropertyGraph<>();
		Map<Vertex, Integer> vertexIntegerMap = new HashMap<>();
		IntegerVertexFactory vertexFactory = new IntegerVertexFactory();

		// iterate over vertices
		for (Vertex vertex : new TreeSet<>(this.vertices)) {
			Integer newVertex = vertexFactory.createVertex();
			vertexIntegerMap.put(vertex, newVertex);
			graph.addVertex(newVertex);
		}

		// iterate over edges
		for (Edge edge : this.edges) {
			// check if edge was already added
			if (!graph.containsEdge(graph.getEdgeFactory().createEdge(
					vertexIntegerMap.get(edge.getStart()),
					vertexIntegerMap.get(edge.getEnd())))
					&& !graph.containsEdge(graph.getEdgeFactory().createEdge(
					vertexIntegerMap.get(edge.getEnd()),
					vertexIntegerMap.get(edge.getStart())))
					&& !edge.getStart().equals(edge.getEnd())) {
				graph.addEdge(
						vertexIntegerMap.get(edge.getStart()),
						vertexIntegerMap.get(edge.getEnd())
				);
			}
		}
		return graph;
	}

	/**
	 * Converts a PropertyGraph to a RenderableGraph with colored vertices.
	 *
	 * @param propertyGraph the input graph
	 * @param coloring the abstract vertex coloring
	 * @param <V> the type representing vertices
	 * @param <E> the type representing edges
	 */
	public <V, E> RenderableGraph(PropertyGraph<V, E> propertyGraph, VertexColoringAlgorithm.Coloring<V> coloring, VertexFactory factory) {
		this.edges = new HashSet<>();
		this.vertices = new HashSet<>();
		this.subgraphs = new HashSet<>();
		this.id = propertyGraph.getId();

		Color[] colorArray = GraphLook.spreadColors(coloring.getNumberColors());
		Map<Integer, Color> colorsToColorObjectMap = new HashMap<>();
		Map<V, Integer> colors = coloring.getColors();


		KkGraphAlgorithm.KkGraph kkGraph = (KkGraphAlgorithm.KkGraph) propertyGraph.getProperty(KkGraph.class).getValue();
		Map<V, Integer> subgraphs = kkGraph.getKkGraph();


		// associate integer value of colorings
		// with Color object
		int i = 0;
		for (V key : coloring.getColors().keySet()) {
			colorsToColorObjectMap.put(colors.get(key), colorArray[i]);
			if (i + 1 < colorArray.length) {
				i++;
			} else {
				break;
			}
		}

		Map<Object, Vertex> objectVertexMap = new HashMap<>();
		Set<Object> addedEdges = new HashSet<>();

		Map<Vertex, Integer> kksubgraphs = new HashMap<>();

		// iterate over vertices
		for (Object v : propertyGraph.vertexSet()) {
			// check if vertex was already added as
			// 'edgeTarget' in loop below
			if (!objectVertexMap.containsKey(v)) {
				Vertex vertex1 = factory.createVertex();
				vertex1.setFillColor(colorsToColorObjectMap.get(colors.get(v)));

				kksubgraphs.put(vertex1, subgraphs.get(v));

				this.vertices.add(vertex1);
				objectVertexMap.put(v, vertex1);
			}

			// iterate over vertex v's edges
			for (Object e : propertyGraph.outgoingEdgesOf(v)) {
				if (addedEdges.contains(e)) {
					continue;
				}

				Object edgeTarget = propertyGraph.getEdgeTarget(e);
				if (v.equals(edgeTarget)) {
					edgeTarget = propertyGraph.getEdgeSource(e);
				}

				addedEdges.add(e);
				addedEdges.add(propertyGraph.getEdgeFactory().createEdge(edgeTarget, v));

				// check if vertex was already added
				if (!objectVertexMap.containsKey(edgeTarget)) {
					Vertex vertex2 = factory.createVertex();

					kksubgraphs.put(vertex2, subgraphs.get(edgeTarget));

					vertex2.setFillColor(colorsToColorObjectMap.get(colors.get(edgeTarget)));
					this.vertices.add(vertex2);
					objectVertexMap.put(edgeTarget, vertex2);
				}

				Edge sourceTargetEdge = new Edge(objectVertexMap.get(v), objectVertexMap.get(edgeTarget));
				this.edges.add(sourceTargetEdge);
			}
		}


		Map<Integer, Set<Vertex>> groups = new HashMap<>();
		kksubgraphs.forEach((v, subgraph) -> {
			Set<Vertex> g = groups.get(subgraph);
			if (g == null) {
				g = new HashSet<>();
				groups.put(subgraph, g);
			}
			g.add(v);
		});
		Set<Set<Vertex>> classes = new HashSet<>(kkGraph.getNumberOfSubgraphs());
		for (Set<Vertex> c : groups.values()) {
			classes.add(c);
		}
		this.subgraphs = classes;

	}

	/**
	 * Converts a PropertyGraph to a RenderableGraph with colored vertices and edges.
	 *
	 * @param propertyGraph the input graph
	 * @param coloring the abstract vertex coloring
	 * @param <V> the type representing vertices
	 * @param <E> the type representing edges
	 */
	public <V, E> RenderableGraph(PropertyGraph<V, E> propertyGraph, TotalColoringAlgorithm.TotalColoring coloring, VertexFactory factory) {
		this.edges = new HashSet<>();
		this.vertices = new HashSet<>();
		this.subgraphs = new HashSet<>();
		this.id = propertyGraph.getId();

		Color[] colorArray = GraphLook.spreadColors(coloring.getNumberColors());
		Map<Integer, Color> vertexColorsToColorMap = new HashMap<>();
		Map<Integer, Color> edgeColorsToColorMap = new HashMap<>();
		Map<V, Integer> vertexColors = coloring.getVertexColors();
		Map<E, Integer> edgeColors = coloring.getEdgeColors();

		// associate integer value of colorings
		// with Color object
		int i = 0;
		for (V key : vertexColors.keySet()) {
			if (!vertexColorsToColorMap.containsKey(vertexColors.get(key))) {
				vertexColorsToColorMap.put(vertexColors.get(key), colorArray[i]);
				if (i + 1 < colorArray.length) {
					i++;
				} else {
					break;
				}
			}
		}

		for (E key : edgeColors.keySet()) {
			if (!edgeColorsToColorMap.containsKey(edgeColors.get(key))) {
				int integerColorValue = edgeColors.get(key);
				if (vertexColorsToColorMap.containsKey(integerColorValue)) {
					edgeColorsToColorMap.put(integerColorValue, vertexColorsToColorMap.get(integerColorValue));
				} else if (i + 1 == colorArray.length) {
					edgeColorsToColorMap.put(edgeColors.get(key), colorArray[i]);
				} else if (i + 1 < colorArray.length) {
					edgeColorsToColorMap.put(edgeColors.get(key), colorArray[i]);
					i++;
				}
			}
		}

		Map<Object, Vertex> objectVertexMap = new HashMap<>();
		Set<Object> addedEdges = new HashSet<>();

		// iterate over vertices
		for (Object v : propertyGraph.vertexSet()) {
			// check if vertex was already added as
			// 'edgeTarget' in loop below
			if (!objectVertexMap.containsKey(v)) {
				Vertex vertex1 = factory.createVertex();
				vertex1.setFillColor(vertexColorsToColorMap.get(vertexColors.get(v)));
				this.vertices.add(vertex1);
				objectVertexMap.put(v, vertex1);
			}

			// iterate over vertex v's edges
			for (Object e : propertyGraph.outgoingEdgesOf(v)) {
				if (addedEdges.contains(e)) {
					continue;
				}

				Object edgeTarget = propertyGraph.getEdgeTarget(e);
				if (v.equals(edgeTarget)) {
					edgeTarget = propertyGraph.getEdgeSource(e);
				}

				addedEdges.add(e);
				addedEdges.add(propertyGraph.getEdgeFactory().createEdge(edgeTarget, v));

				// check if vertex was already added
				if (!objectVertexMap.containsKey(edgeTarget)) {
					Vertex vertex2 = factory.createVertex();
					vertex2.setFillColor(vertexColorsToColorMap.get(vertexColors.get(edgeTarget)));
					this.vertices.add(vertex2);
					objectVertexMap.put(edgeTarget, vertex2);
				}

				Edge sourceTargetEdge = new Edge(objectVertexMap.get(v), objectVertexMap.get(edgeTarget));
				sourceTargetEdge.setColor(edgeColorsToColorMap.get(edgeColors.get((e))));
				this.edges.add(sourceTargetEdge);
			}
		}
	}

	/**
	 * Moves the vertices of a graph
	 * @param delta the delta to move vertices by
	 */
	public void move(Point delta) {
		for (Vertex vertex : vertices) {
			vertex.move(delta);
		}
	}

	/**
	 * @param vertex the vertex to add to the graph
	 */
	public void add(Vertex vertex) {
		if (getVertexAt(vertex.getPosition()) == null) {
			vertices.add(vertex);
		}
	}

	/**
	 * @param vertex the vertex to remove from the graph
	 */
	public void remove(Vertex vertex) {
		Set<Edge> edgesRemove = new HashSet<>();
		for (Edge edge : edges) {
			if (edge.isIncidentTo(vertex)) {
				edgesRemove.add(edge);
			}
		}
		vertices.remove(vertex);
		edges.removeAll(edgesRemove);
	}

	/**
	 * Removes the first vertex that covers the given point
	 * @param point where to look for the vertex
	 */
	public void remove(Point point) {
		Vertex foundVertex = getVertexAt(point);
		if (foundVertex != null) {
			remove(foundVertex);
		}
	}

	/**
	 * @param edge the edge to add to the graph
	 */
	public void add(Edge edge) {

		Vertex start = getVertexAt(edge.getStart().getPosition());
		Vertex end = getVertexAt(edge.getEnd().getPosition());

		if (start == null) {
			vertices.add(edge.getStart());
		} else {
			edge.setStart(start);
		}
		if (end == null) {
			vertices.add(edge.getEnd());
		} else {
			edge.setEnd(end);
		}

		if (!areConnected(edge.getStart(), edge.getEnd())) {
			edges.add(edge);
		}
	}

	/**
	 * @param start the start vertex
	 * @param end the end vertex
	 * @return whether the vertices start and end are connected by an edge
	 */
	public boolean areConnected(Vertex start, Vertex end) {
		for (Edge edge : edges) {
			if ((edge.getStart() == start && edge.getEnd() == end)
					|| (edge.getEnd() == start && edge.getStart() == end)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @param edge the edge to remove from the graph
	 */
	public void remove(Edge edge) {
		edges.remove(edge);
	}

	/**
	 * @param vertex the vertex which' degree to check
	 * @return the degree of the given vertex
	 */
	public int getDegree(final Vertex vertex) {
		if (!vertices.contains(vertex)) {
			return -1;
		}

		int vertexDegree = 0;
		for (Edge edge : edges) {
			if (edge.getStart() == vertex || edge.getEnd() == vertex) {
				vertexDegree++;
			}
		}
		return vertexDegree;
	}

	/**
	 * @return the highest vertex degree
	 */
	public int getMaxDegree() {
		int maxDegree = 0;
		for (final Vertex vertex : vertices) {
			final int vertexDegree = getDegree(vertex);
			maxDegree = (vertexDegree > maxDegree) ? vertexDegree : maxDegree;
		}
		return maxDegree;
	}

	/**
	 * Returns the first vertex under the given point
	 * @param position where to look for the vertex
	 * @return the found vertex or null
	 */
	public Vertex getVertexAt(final Point position) {
		for (Vertex vertex : vertices) {
			if (vertex.containsPoint(position.x, position.y)) {
				return vertex;
			}
		}

		return null;
	}

	/**
	 * @return the graph's vertices
	 */
	public Set<Vertex> getVertices() {
		return vertices;
	}

	/**
	 * @param vertices the graph's new vertices
	 */
	public void setVertices(Set<Vertex> vertices) {
		this.vertices = vertices;
	}

	/**
	 * @return the graph's edges
	 */
	public Set<Edge> getEdges() {
		return edges;
	}

	/**
	 * @param edges the graph's new edges
	 */
	public void setEdges(Set<Edge> edges) {
		this.edges = edges;
	}

	/**
	 * @return the graph's ID
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the graph's new ID
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the graph's subgraphs
	 */
	public Set<Set<Vertex>> getSubgraphs() {
		return subgraphs;
	}

	/**
	 * @param subgraphs the graph's new subgraphs
	 */
	public void setSubgraphs(Set<Set<Vertex>> subgraphs) {
		this.subgraphs = subgraphs;
	}

	/**
	 * Find the position of the vertex which is furthest right and down
	 * @param vertices the vertices to check
	 * @return the lower right position of the lower right vertex or (0, 0)
	 */
	public static Point getLowerRight(Set<Vertex> vertices) {
		Iterator<Vertex> iterator = vertices.iterator();
		if (iterator.hasNext()) {
			Vertex startVertex = iterator.next();
			Vertex lowerrightVertex = new Vertex(startVertex.x, startVertex.y);
			Vertex vertex;
			while (iterator.hasNext()) {
				vertex = iterator.next();
				if (vertex.x >= lowerrightVertex.x) {
					lowerrightVertex.x = vertex.x;
				}
				if (vertex.y >= lowerrightVertex.y) {
					lowerrightVertex.y = vertex.y;
				}
			}
			return new Point(lowerrightVertex.x, lowerrightVertex.y);
		}
		return new Point(0, 0);
	}

	/**
	 * Find the position of the vertex which is furthest left and up
	 * @param vertices the vertices to check
	 * @return the uppper left position of the upper left vertex or (0, 0)
	 */
	public static Point getUpperLeft(Set<Vertex> vertices) {
		Iterator<Vertex> iterator = vertices.iterator();
		if (iterator.hasNext()) {
			Vertex startVertex = iterator.next();
			Vertex upperleftVertex = new Vertex(startVertex.x, startVertex.y);
			Vertex vertex;
			while (iterator.hasNext()) {
				vertex = iterator.next();
				if (vertex.x <= upperleftVertex.x) {
					upperleftVertex.x = vertex.x;
				}
				if (vertex.y <= upperleftVertex.y) {
					upperleftVertex.y = vertex.y;
				}
			}
			return new Point(upperleftVertex.x, upperleftVertex.y);
		}
		return new Point(0, 0);
	}

	/**
	 * Draws a line around the given vertices
	 * @param vertices the vertices to draw a line around
	 * @return the outline
	 */
	public static Shape outline(Set<Vertex> vertices) {
		final Point upperleft = getUpperLeft(vertices);
		final Point lowerright = getLowerRight(vertices);
		final float margin = 1.025f;
		return new RoundRectangle2D.Double(
				upperleft.x - GraphLook.VERTEX_DIAMETER * margin,
				upperleft.y - GraphLook.VERTEX_DIAMETER * margin,
				lowerright.x - upperleft.x + 2 * GraphLook.VERTEX_DIAMETER * margin,
				lowerright.y - upperleft.y + 2 * GraphLook.VERTEX_DIAMETER * margin,
				15, 15);
	}

	/**
	 * Find the vertices in the graph with position (0, 0)
	 * @return all the found vertices
	 */
	public Set<Vertex> getUnpositionedVertices() {
		Set<Vertex> unpositionedVertices = new HashSet<>();
		vertices.forEach(vertex -> {
			if (vertex.x == 0 && vertex.y == 0) {
				unpositionedVertices.add(vertex);
			}
		});
		return unpositionedVertices;
	}

	/**
	 * @return all vertices in the graph which aren't contained in any of it's subgraphs
	 */
	public Set<Vertex> getVerticesNotContainedInSubgraphs() {
		Set<Vertex> notContainedVertices = new HashSet<>();
		for (Vertex vertex : vertices) {
			boolean contained = false;
			for (Set<Vertex> subgraph : subgraphs) {
				if (subgraph.contains(vertex)) {
					contained = true;
				}
			}
			if (!contained) {
				notContainedVertices.add(vertex);
			}
		}
		return notContainedVertices;
	}

	/**
	 * @return a deep copy of the given graph
	 */
	public RenderableGraph deepCopy() {
		Set<Vertex> newVertices = (this.vertices == null) ? new HashSet<>() : new HashSet<>(vertices);
		Set<Edge> newEdges = (this.edges == null) ? new HashSet<>() : new HashSet<>(edges);
		Set<Set<Vertex>> newSubgraphs = (this.subgraphs == null) ? new HashSet<>() : new HashSet<>(subgraphs);
		return new RenderableGraph(newVertices, newEdges, id, newSubgraphs);
	}
}

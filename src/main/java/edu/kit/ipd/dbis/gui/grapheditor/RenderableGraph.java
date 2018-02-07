/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.grapheditor;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.TotalColoringAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.*;

public class RenderableGraph {

	private Set<Vertex> vertices;
	private Set<Edge> edges;
	private int id;
	private Set<RenderableGraph> subgraphs;

	public RenderableGraph() {
		vertices = new HashSet<>();
		edges = new HashSet<>();
		subgraphs = new HashSet<>();
	}

	public RenderableGraph(Set<Vertex> vertices, Set<Edge> edges, int id) {
		this.vertices = vertices;
		this.edges = edges;
		this.id = id;
	}

	public RenderableGraph(Set<Vertex> vertices, Set<Edge> edges, int id, Set<RenderableGraph> subgraphs) {
		this.vertices = vertices;
		this.edges = edges;
		this.id = id;
		this.subgraphs = subgraphs;
	}

	public RenderableGraph(PropertyGraph propertyGraph) {
		this.edges = new HashSet<>();
		this.vertices = new HashSet<>();
		this.id = propertyGraph.getId();

		Map<Object, Vertex> objectVertexMap = new HashMap<>();
		Set addedEdges = new HashSet();

		// iterate over vertices
		for (Object v : propertyGraph.vertexSet()) {
			// check if vertex was already added as
			// 'edgeTarget' in loop below
			if (!objectVertexMap.containsKey(v)) {
				Vertex vertex1 = new Vertex(0, 0);
				this.vertices.add(vertex1);
				objectVertexMap.put(v, vertex1);
			}

			// iterate over vertex v's edges
			for (Object e : propertyGraph.outgoingEdgesOf(v)) {
				Object edgeTarget = propertyGraph.getEdgeTarget(e);

				// check if vertex was already added
				if (!objectVertexMap.containsKey(edgeTarget)) {
					Vertex vertex2 = new Vertex(0, 0);
					this.vertices.add(vertex2);
					objectVertexMap.put(edgeTarget, vertex2);
				}

				// check if edge was already added
				if (!addedEdges.contains(e)
						&& !addedEdges.contains(propertyGraph.getEdgeFactory().createEdge(edgeTarget, v))) {
					addedEdges.add(e);
					this.edges.add(new Edge(objectVertexMap.get(v), objectVertexMap.get(edgeTarget)));
				}
			}
		}
	}

	public <V, E> RenderableGraph(PropertyGraph<V, E> propertyGraph, VertexColoringAlgorithm.Coloring<V> coloring) {
		this.edges = new HashSet<>();
		this.vertices = new HashSet<>();
		this.id = propertyGraph.getId();

		Color[] colorArray = GraphLook.spreadColors(coloring.getNumberColors());
		Map<Integer, Color> colorsToColorObjectMap = new HashMap<>();
		Map<V, Integer> colors = coloring.getColors();

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
		Set addedEdges = new HashSet();

		// iterate over vertices
		for (Object v : propertyGraph.vertexSet()) {
			// check if vertex was already added as
			// 'edgeTarget' in loop below
			if (!objectVertexMap.containsKey(v)) {
				Vertex vertex1 = new Vertex(0, 0);
				vertex1.setFillColor(colorsToColorObjectMap.get(colors.get(v)));
				this.vertices.add(vertex1);
				objectVertexMap.put(v, vertex1);
			}

			// iterate over vertex v's edges
			for (Object e : propertyGraph.outgoingEdgesOf(v)) {
				Object edgeTarget = propertyGraph.getEdgeTarget(e);

				// check if vertex was already added
				if (!objectVertexMap.containsKey(edgeTarget)) {
					Vertex vertex2 = new Vertex(0, 0);
					vertex2.setFillColor(colorsToColorObjectMap.get(colors.get(edgeTarget)));
					this.vertices.add(vertex2);
					objectVertexMap.put(edgeTarget, vertex2);
				}

				// check if edge was already added
				if (!addedEdges.contains(e)
						&& !addedEdges.contains(propertyGraph.getEdgeFactory().createEdge(edgeTarget, v))) {
					addedEdges.add(e);
					this.edges.add(new Edge(objectVertexMap.get(v), objectVertexMap.get(edgeTarget)));
				}
			}
		}
	}

	public <V, E> RenderableGraph(PropertyGraph<V, E> propertyGraph, TotalColoringAlgorithm.TotalColoring coloring) {
		this.edges = new HashSet<>();
		this.vertices = new HashSet<>();
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
			vertexColorsToColorMap.put(vertexColors.get(key), colorArray[i]);
			if (i + 1 < colorArray.length) {
				i++;
			} else {
				break;
			}
		}

		for (E key : edgeColors.keySet()) {
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

		Map<Object, Vertex> objectVertexMap = new HashMap<>();
		Set addedEdges = new HashSet();

		// iterate over vertices
		for (Object v : propertyGraph.vertexSet()) {
			// check if vertex was already added as
			// 'edgeTarget' in loop below
			if (!objectVertexMap.containsKey(v)) {
				Vertex vertex1 = new Vertex(0, 0);
				vertex1.setFillColor(vertexColorsToColorMap.get(vertexColors.get(v)));
				this.vertices.add(vertex1);
				objectVertexMap.put(v, vertex1);
			}

			// iterate over vertex v's edges
			for (Object e : propertyGraph.outgoingEdgesOf(v)) {
				Object edgeTarget = propertyGraph.getEdgeTarget(e);

				// check if vertex was already added
				if (!objectVertexMap.containsKey(edgeTarget)) {
					Vertex vertex2 = new Vertex(0, 0);
					vertex2.setFillColor(vertexColorsToColorMap.get(vertexColors.get(edgeTarget)));
					this.vertices.add(vertex2);
					objectVertexMap.put(edgeTarget, vertex2);
				}

				// check if edge was already added
				if (!addedEdges.contains(e)
						&& !addedEdges.contains(propertyGraph.getEdgeFactory().createEdge(edgeTarget, v))) {
					addedEdges.add(e);
					Edge edge = new Edge(objectVertexMap.get(v), objectVertexMap.get(edgeTarget));
					edge.setColor(edgeColorsToColorMap.get(edgeColors.get((e))));
					this.edges.add(edge);
				}
			}
		}
	}


	public PropertyGraph asPropertyGraph() {
		PropertyGraph graph = new PropertyGraph();
		Map<Vertex, Integer> vertexIntegerMap = new HashMap<>();
		int vertexName = 0;

		// iterate over vertices and assign
		// integer value to each one
		for (Vertex vertex : this.vertices) {
			vertexIntegerMap.put(vertex, vertexName);
			graph.addVertex(vertexName);
			vertexName++;
		}

		// iterate over edges
		for (Edge edge : this.edges) {
			// check if edge was already added
			if (!graph.containsEdge(graph.getEdgeFactory().createEdge(
					vertexIntegerMap.get(edge.getStart()),
					vertexIntegerMap.get(edge.getEnd())))
					&& !graph.containsEdge(graph.getEdgeFactory().createEdge(
					vertexIntegerMap.get(edge.getEnd()),
					vertexIntegerMap.get(edge.getStart())))) {
				graph.addEdge(
						vertexIntegerMap.get(edge.getStart()),
						vertexIntegerMap.get(edge.getEnd())
				);
			}
		}
		return graph;
	}

	public void move(Point delta) {
		for (Vertex vertex : vertices) {
			vertex.move(delta);
		}
	}

	public void add(Vertex vertex) {
		if (getVertexAt(vertex.getPosition()) == null) {
			vertices.add(vertex);
		}
	}

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

	public void remove(Point point) {
		Vertex foundVertex = getVertexAt(point);
		if (foundVertex != null) {
			remove(foundVertex);
		}
	}

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

	public boolean areConnected(Vertex start, Vertex end) {
		for (Edge edge : edges) {
			if ((edge.getStart() == start && edge.getEnd() == end)
					|| (edge.getEnd() == start && edge.getStart() == end)) {
				return true;
			}
		}

		return false;
	}

	public void remove(Edge edge) {
		edges.remove(edge);
	}

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

	public int getMaxDegree() {
		int maxDegree = 0;
		for (final Vertex vertex : vertices) {
			final int vertexDegree = getDegree(vertex);
			maxDegree = (vertexDegree > maxDegree)? vertexDegree : maxDegree;
		}
		return maxDegree;
	}

	public boolean isConnected() { // todo implement isConnected()
		return true;
	}

	public void makeConnected() { // todo implement makeConnected() (only if sensible choice) by removing smallest disconnected graph

	}

	public Vertex getVertexAt(final Point position) {
		for (Vertex vertex : vertices) {
			if (vertex.containsPoint(position.x, position.y)) {
				return vertex;
			}
		}

		return null;
	}

	public Set<Vertex> getVertices() {
		return vertices;
	}

	public void setVertices(Set<Vertex> vertices) {
		this.vertices = vertices;
	}

	public Set<Edge> getEdges() {
		return edges;
	}

	public void setEdges(Set<Edge> edges) {
		this.edges = edges;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Set<RenderableGraph> getSubgraphs() {
		return subgraphs;
	}

	public void setSubgraphs(Set<RenderableGraph> subgraphs) {
		this.subgraphs = subgraphs;
	}

	public Point getUpperLeft() {
		Vertex upperleftVertex = new Vertex(0, 0);
		Vertex vertex;
		for (Iterator<Vertex> iterator = vertices.iterator(); iterator.hasNext(); ) {
			vertex = iterator.next();
			if (vertex.x <= upperleftVertex.x && vertex.y >= upperleftVertex.y) {
				upperleftVertex = vertex;
			}
		}
		return new Point(upperleftVertex.x, upperleftVertex.y);
	}

	public Point getLowerRight() {
		Vertex upperleftVertex = new Vertex(0, 0);
		Vertex vertex;
		for (Iterator<Vertex> iterator = vertices.iterator(); iterator.hasNext(); ) {
			vertex = iterator.next();
			if (vertex.x >= upperleftVertex.x && vertex.y <= upperleftVertex.y) {
				upperleftVertex = vertex;
			}
		}
		return new Point(upperleftVertex.x, upperleftVertex.y);
	}

	public Shape outline() {
		final Point upperleft = getUpperLeft();
		final Point lowerright = getLowerRight();
		return new RoundRectangle2D.Double(upperleft.x, upperleft.y, lowerright.x, lowerright.y, 1 , 2);
	}

	public RenderableGraph deepCopy() {
		Set<Vertex> newVertices = (this.vertices == null) ? new HashSet<>() : new HashSet<>(vertices);
		Set<Edge> newEdges = (this.edges == null) ? new HashSet<>() : new HashSet<>(edges);
		Set<RenderableGraph> newSubgraphs = (this.subgraphs == null) ? new HashSet<>() : new HashSet<>(subgraphs);
		return new RenderableGraph(newVertices, newEdges, id, newSubgraphs);
	}
}

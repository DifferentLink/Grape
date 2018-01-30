/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.grapheditor;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

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

		for (Object v : propertyGraph.vertexSet()) {
			if (!objectVertexMap.containsKey(v)) {
				Vertex vertex1 = new Vertex(0, 0);
				this.vertices.add(vertex1);
				objectVertexMap.put(v, vertex1);
			}
			for (Object e : propertyGraph.outgoingEdgesOf(v)) {
				Object edgeTarget = propertyGraph.getEdgeTarget(e);
				if (!objectVertexMap.containsKey(edgeTarget)) {
					Vertex vertex2 = new Vertex(0, 0);
					this.vertices.add(vertex2);
					objectVertexMap.put(edgeTarget, vertex2);
				}
				if (!addedEdges.contains(e)
						&& !addedEdges.contains(propertyGraph.getEdgeFactory().createEdge(edgeTarget, v))) {
					addedEdges.add(e);
					this.edges.add(new Edge(objectVertexMap.get(v), objectVertexMap.get(edgeTarget)));
				}
			}
		}
	}

	public PropertyGraph asPropertyGraph() {
		return new PropertyGraph();
	} // todo implement asPropertyGraph() now that PropertyGraph class is available

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
		Set<Edge> edgesRemove = new HashSet();
		for (Edge edge : edges) {
			if (edge.isIncidentTo(vertex)) {
				edgesRemove.add(edge);
			}
		}
		vertices.remove(vertex);
		edges.remove(edgesRemove);
	}

	public void remove(Point point) {
		Vertex foundVertex = getVertexAt(point);
		if (foundVertex != null) {
			vertices.remove(foundVertex);
		}
	}

	public void add(Edge edge) {
		vertices.add(edge.getStart());
		vertices.add(edge.getEnd());
		edges.add(edge);
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

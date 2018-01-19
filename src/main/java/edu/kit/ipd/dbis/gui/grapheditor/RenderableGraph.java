/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.grapheditor;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class RenderableGraph {

	private Set<Vertex> vertices;
	private Set<Edge> edges;
	private int id;
	private Set<RenderableGraph> subgraphs;

	public RenderableGraph() {
		vertices = new HashSet<>();
		edges = new HashSet<>();
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

	}

	public PropertyGraph asPropertyGraph() {
		return new PropertyGraph();
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

	public void makeConnected() { // todo implement makeConnected() by removing smallest disconnected graph

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

	public RenderableGraph deepCopy() {
		Set<Vertex> newVertices = (this.vertices == null) ? new HashSet<>() : new HashSet<>(vertices);
		Set<Edge> newEdges = (this.edges == null) ? new HashSet<>() : new HashSet<>(edges);
		Set<RenderableGraph> newSubgraphs = (this.subgraphs == null) ? new HashSet<>() : new HashSet<>(subgraphs);
		return new RenderableGraph(newVertices, newEdges, id, newSubgraphs);
	}
}

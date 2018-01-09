/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.grapheditor;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class RenderableGraph {

	private Set<Vertex> vertices;
	private Set<Edge> edges;
	private int id;
	private Set<RenderableGraph> subgraphs;

	public RenderableGraph(Set<Vertex> vertices, Set<Edge> edges, int id) {
		this.vertices = vertices;
		this.edges = edges;
		this.id = id;
	}

	public RenderableGraph(PropertyGraph propertyGraph) {

	}

	public PropertyGraph asPropertyGraph() {
		return new PropertyGraph();
	}

	public void removeVertex(Vertex vertex) {
		Set<Edge> edgesRemove = new HashSet();
		for (Edge edge : edges) {
			if (edge.isIncidentTo(vertex)) {
				edgesRemove.add(edge);
			}
		}
		vertices.remove(vertex);
		edges.remove(edgesRemove);
	}

	public void removeVertex(Point point) {
		Vertex foundVertex = getVertexAt(point);
		if (foundVertex != null) {
			vertices.remove(foundVertex);
		}
	}

	public Vertex getVertexAt(Point position) {
		for (Vertex vertex : vertices) {
			if (vertex.containsPoint(position)) {
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
}

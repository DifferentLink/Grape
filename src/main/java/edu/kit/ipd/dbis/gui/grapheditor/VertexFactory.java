package edu.kit.ipd.dbis.gui.grapheditor;

import java.awt.*;

/**
 * Vertex factory which creates vertices
 * and assigns them ids.
 *
 */
public class VertexFactory {
	private int idCount = 0;

	/**
	 * Creates a vertex.
	 *
	 * @return the vertex
	 */
	public Vertex createVertex() {
		Vertex vertex = new Vertex(0, 0);
		vertex.setId(idCount);
		idCount++;
		return vertex;
	}

	/**
	 * Creates a Vertex at a certain location.
	 *
	 * @param x x axis
	 * @param y y axis
	 * @return the vertex
	 */
	public Vertex createVertex(int x, int y) {
		Vertex vertex = new Vertex(x, y);
		vertex.setId(idCount);
		idCount++;
		return vertex;
	}

	/**
	 * Creates a Vertex at a point.
	 *
	 * @param point the point
	 * @return the vertex
	 */
	public Vertex createVertex(Point point) {
		Vertex vertex = new Vertex(point);
		vertex.setId(idCount);
		idCount++;
		return vertex;
	}
}

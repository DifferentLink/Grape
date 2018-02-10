package edu.kit.ipd.dbis.gui.grapheditor;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Line2D;

/**
 * An edge that can be rendered in the graph editor
 */
public class Edge {
	private Vertex start;
	private Vertex end;

	private Color color = GraphLook.EDGE_COLOR;
	private int thickness = GraphLook.EDGE_THICKNESS;

	/**
	 * @param start the start vertex of the edge
	 * @param end the end vertex of the edge
	 */
	public Edge(Vertex start, Vertex end) {
		this.start = start;
		this.end = end;
	}

	/**
	 * @param start the start vertex of the edge
	 * @param end the end vertex of the edge
	 * @param color the edge's color
	 */
	public Edge(Vertex start, Vertex end, Color color) {
		this.start = start;
		this.end = end;
		this.color = color;
	}

	/**
	 * @param start the start vertex of the edge
	 * @param end the end vertex of the edge
	 * @param color the edge's color
	 * @param thickness the edge's thickness
	 */
	public Edge(Vertex start, Vertex end, Color color, int thickness) {
		this.start = start;
		this.end = end;
		this.color = color;
		this.thickness = thickness;
	}

	/**
	 * @param vertex the vertex to check
	 * @return whether the edge is incident to the given vertex
	 */
	public boolean isIncidentTo(Vertex vertex) {
		return start == vertex || end == vertex;
	}

	/**
	 * @return a shape that can be rendered in the graph editor
	 */
	public Shape draw() {
		return new Line2D.Double(start.x, start.y, end.x, end.y);
	}

	/**
	 * @return the edge's start vertex
	 */
	public Vertex getStart() {
		return this.start;
	}

	/**
	 * @param vertex the edge's new start vertex
	 */
	public void setStart(Vertex vertex) {
		this.start = vertex;
	}

	/**
	 * @param vertex the edge's new end vertex
	 */
	public void setEnd(Vertex vertex) {
		this.end = vertex;
	}

	/**
	 * @return the edge's end vertex
	 */
	public Vertex getEnd() {
		return end;
	}

	/**
	 * @return the edge's color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color the edge's new color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * @return the edge's thickness
	 */
	public int getThickness() {
		return thickness;
	}

	/**
	 * @param thickness the edge's new thickness
	 */
	public void setThickness(int thickness) {
		this.thickness = thickness;
	}
}

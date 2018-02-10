package edu.kit.ipd.dbis.gui.grapheditor;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

/**
 * A vertex that can be rendered in the graph editor.
 */
public class Vertex implements Comparable {
	static int idCount = 0;
	private int id;

	public int x;
	public int y;
	private float diameter = GraphLook.vertexDiameter;
	private Color fillColor = GraphLook.vertexFillColor;
	private Color outlineColor = GraphLook.vertexOutlineColor;
	private float outlineThickness = GraphLook.vertexOutLineThickness;

	/**
	 * A new vertex at position (x, y)
	 * @param x the vertex' x coordinate
	 * @param y the vertex' y coordinate
	 */
	public Vertex(int x, int y) {
		this.id = idCount;
		idCount++;
		this.x = x;
		this.y = y;
	}

	/**
	 * A new vertex at position (x, y) which has the given color
	 * @param x the vertex' x coordinate
	 * @param y the vertex' y coordinate
	 * @param fillColor the vertex' fill color
	 */
	public Vertex(int x, int y, Color fillColor) {
		this.id = idCount;
		idCount++;
		this.x = x;
		this.y = y;
		this.fillColor = fillColor;
	}

	/**
	 * A new vertex at the given point
	 * @param point the vertex' location
	 */
	public Vertex(Point point) {
		this.id = idCount;
		idCount++;
		this.x = point.x;
		this.y = point.y;
	}

	/**
	 * Moves a vertex by a given delta
	 * @param delta the delta to move the vertex by
	 */
	public void move(Point delta) {
		this.x += delta.x;
		this.y += delta.y;
	}

	/**
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return whether the point lies on the vertex
	 */
	public boolean containsPoint(int x, int y) {
		int deltaX = x - this.x;
		int deltaY = y - this.y;

		return Math.sqrt(deltaX*deltaX + deltaY*deltaY) <= diameter/2;
	}

	/**
	 * Draws a vertex
	 * @return the drawn vertex
	 */
	public Shape draw() {
		return new Ellipse2D.Double(x - diameter /2, y - diameter /2, diameter, diameter);
	}

	/**
	 * @return the vertex' fill color
	 */
	public Color getFillColor() {
		return fillColor;
	}

	/**
	 * @param fillColor the vertex' new fill color
	 */
	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	/**
	 * @return the vertex' outline color
	 */
	public Color getOutlineColor() {
		return outlineColor;
	}

	/**
	 * @param outlineColor the vertex' new outline color
	 */
	public void setOutlineColor(Color outlineColor) {
		this.outlineColor = outlineColor;
	}

	/**
	 * @return the vertex' diameter
	 */
	public float getDiameter() {
		return diameter;
	}

	/**
	 * @param diameter the vertex' new diameter
	 */
	public void setDiameter(float diameter) {
		this.diameter = diameter;
	}

	/**
	 * @return the vertex' outline thickness
	 */
	public float getOutlineThickness() {
		return outlineThickness;
	}

	/**
	 * @param outlineThickness the vertex' new outline thickness
	 */
	public void setOutlineThickness(float outlineThickness) {
		this.outlineThickness = outlineThickness;
	}

	/**
	 * @return the vertex' position
	 */
	public Point getPosition() {
		return new Point(x, y);
	}

	/**
	 * @param x the vertex' new x coordinate
	 * @param y the vertex' new y coordinate
	 */
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @return the vertex' ID
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @param o the object to compare the vertex to
	 * @return -1 if the other vertex has a bigger ID, 0 if they are equal, 1 if the ID of the vertex is smaller
	 */
	@Override
	public int compareTo(Object o) {
		return Integer.compare(this.getId(), ((Vertex) o).getId());
	}
}

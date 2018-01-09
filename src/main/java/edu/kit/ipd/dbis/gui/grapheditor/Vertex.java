/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.grapheditor;

import java.awt.*;

public class Vertex {
	private Point position;
	private float diameter = GraphLook.vertexDiameter;
	private Color fillColor = GraphLook.vertexFillColor;
	private Color outlineColor = GraphLook.vertexOutlineColor;

	public Vertex(int x, int y) {
		position.x = x;
		position.y = y;
	}

	public Vertex(Point position) {
		this.position = position;
	}

	public Vertex(Point position, Color fillColor) {
		this.position = position;
		this.fillColor = fillColor;
	}

	public Vertex(int x, int y, Color fillColor) {
		position.x = x;
		position.y = y;
		this.fillColor = fillColor;
	}

	public void move(Point delta) {
		position.x += delta.x;
		position.y += delta.y;
	}

	public boolean containsPoint(Point point) {
		int deltaX = point.x - position.x;
		int deltaY = point.y - position.y;

		return Math.sqrt(deltaX*deltaX + deltaY*deltaY) <= diameter/2;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public Color getFillColor() {
		return fillColor;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	public Color getOutlineColor() {
		return outlineColor;
	}

	public void setOutlineColor(Color outlineColor) {
		this.outlineColor = outlineColor;
	}

	public float getDiameter() {
		return diameter;
	}

	public void setDiameter(float diameter) {
		this.diameter = diameter;
	}
}

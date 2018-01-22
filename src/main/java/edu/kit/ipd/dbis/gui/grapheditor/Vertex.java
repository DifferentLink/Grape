/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.grapheditor;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Vertex {
	public int x;
	public int y;
	private float diameter = GraphLook.vertexDiameter;
	private Color fillColor = GraphLook.vertexFillColor;
	private Color outlineColor = GraphLook.vertexOutlineColor;
	private float outlineThickness = GraphLook.vertexOutLineThickness;

	public Vertex(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Vertex(int x, int y, Color fillColor) {
		this.x = x;
		this.y = y;
		this.fillColor = fillColor;
	}

	public Vertex(Point point) {
		this.x = point.x;
		this.y = point.y;
	}

	public void move(Point delta) {
		this.x += delta.x;
		this.y += delta.y;
	}

	public boolean containsPoint(int x, int y) {
		int deltaX = x - this.x;
		int deltaY = y - this.y;

		return Math.sqrt(deltaX*deltaX + deltaY*deltaY) <= diameter/2;
	}

	public Shape draw() {
		return new Ellipse2D.Double(x - diameter /2, y - diameter /2, diameter, diameter);
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

	public float getOutlineThickness() {
		return outlineThickness;
	}

	public void setOutlineThickness(float outlineThickness) {
		this.outlineThickness = outlineThickness;
	}

	public Point getPosition() {
		return new Point(x, y);
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
}

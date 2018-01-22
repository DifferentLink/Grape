/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui.grapheditor;

import java.awt.*;
import java.awt.geom.Line2D;

public class Edge {
	private final Vertex start;
	private final Vertex end;

	private Color color = GraphLook.edgeColor;
	private int thickness = GraphLook.edgeThickness;

	public Edge(Vertex start, Vertex end) {
		this.start = start;
		this.end = end;
	}

	public Edge(Vertex start, Vertex end, Color color) {
		this.start = start;
		this.end = end;
		this.color = color;
	}

	public Edge(Vertex start, Vertex end, Color color, int thickness) {
		this.start = start;
		this.end = end;
		this.color = color;
		this.thickness = thickness;
	}

	public boolean isIncidentTo(Vertex vertex) {
		return start == vertex || end == vertex;
	}

	public Shape draw() {
		return new Line2D.Double(start.x, start.y, end.x, end.y);
	}

	public Vertex getStart() {
		return this.start;
	}

	public Vertex getEnd() {
		return end;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getThickness() {
		return thickness;
	}

	public void setThickness(int thickness) {
		this.thickness = thickness;
	}
}

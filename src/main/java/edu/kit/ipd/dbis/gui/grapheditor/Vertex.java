/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.grapheditor;

import java.awt.*;

public class Vertex {
	private Coordinate position;
	private Color fillColor = GraphLook.vertexFillColor;
	private Color outlineColor = GraphLook.vertexOutlineColor;

	public Vertex(Coordinate position) {
		this.position = position;
	}

	public Vertex(Coordinate position, Color fillColor) {
		this.position = position;
		this.fillColor = fillColor;
	}
}

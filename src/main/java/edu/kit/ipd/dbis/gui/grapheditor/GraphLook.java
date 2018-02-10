/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.grapheditor;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class simply lists default render values for RenderableGraphs.
 */
public class GraphLook {
	public static final int vertexDiameter = 25;
	public static final Color vertexFillColor = Color.WHITE;
	public static final int vertexOutLineThickness = 1;
	public static final Color vertexOutlineColor = Color.BLACK;

	public static final int edgeThickness = 2;
	public static final Color edgeColor = Color.BLACK;

	/**
	 * This method takes n input colors and spreads these on the colorwheel
	 * so that two respective colors have maximum contrast.
	 * @param numColors number of colors to spread
	 * @return an array with n Colors
	 */
	public static Color[] spreadColors(final int numColors) {
		Color[] colors = new Color[numColors];

		final float step = 1 / (float) numColors;

		for (int color = 0; color < numColors; color++) {
			colors[color] = Color.getHSBColor(step * color, .75f, .8f);
		}

		return colors;
	}

	public static void arrangeInCircle(Set<Vertex> vertices, Point upperLeft, Point lowerRight) {

		final Point center =
				new Point(Math.abs(upperLeft.x - lowerRight.x) / 2, Math.abs(upperLeft.y - lowerRight.y) / 2);
		final double radius = Math.min(center.x, center.y) * .75;
		final double angle = Math.toRadians(360d / (double) vertices.size());
		int i = 0;

		for (Vertex vertex : new ArrayList<>(new TreeSet<>(vertices))) {
			vertex.setPosition(
					(int) (radius * Math.cos(i * angle) + center.x),
					(int) (radius * Math.sin(i * angle) + center.y));
			i++;
		}
	}

	public static void arrangeInGrid(Set<Set<Vertex>> subgraphs, Set<Vertex> otherVertices,
	                                 Point upperLeft, Point lowerRight) {

		final int numberGridcells = subgraphs.size() + 1;
		final int xCells = (int) Math.ceil(Math.sqrt(numberGridcells));
		final int xStepsize = (lowerRight.x - upperLeft.x) / xCells;
		final int yCells = (int) Math.floor(Math.sqrt(numberGridcells));
		final int yStepsize = (lowerRight.y - upperLeft.y) / yCells;
		Iterator<Set<Vertex>> iterator = subgraphs.iterator();

		for (int y = 0; y < yCells; y++) {
			for (int x = 0; x < xCells; x++) {
				if (iterator.hasNext()) {
					arrangeInCircle(iterator.next(),
							new Point(x * xStepsize, y * yStepsize),
							new Point(x * xStepsize + xStepsize, y * yStepsize + yStepsize));
				} else {
					arrangeInCircle(otherVertices,
							new Point(x * xStepsize, y * yStepsize),
							new Point(x * xStepsize + xStepsize, y * yStepsize + yStepsize));
					return;
				}
			}
		}
	}
}

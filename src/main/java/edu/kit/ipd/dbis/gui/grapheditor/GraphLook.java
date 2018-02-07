/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.grapheditor;

import java.awt.*;
import java.util.Set;

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

		final float step = (float) 1 / numColors;

		for (int color = 0; color < numColors; color++) {
			colors[color] = Color.getHSBColor(step * color, 1f, 1f);
		}

		return colors;
	}

	public static void arrangeInCircle(Set<Vertex> vertices, Point upperLeft, Point lowerRight) { // todo implement dummy method

		final Point center =
				new Point(Math.abs(upperLeft.x - lowerRight.x) / 2, Math.abs(upperLeft.y - lowerRight.y) / 2);
		final double radius = Math.min(center.x, center.y) * .75;
		final double angle = Math.toRadians(360d / (double) vertices.size());
		int i = 0;

		for (Vertex vertex : vertices) {
			vertex.setPosition(
					(int) (radius * Math.cos(i * angle) + center.x),
					(int) (radius * Math.sin(i * angle) + center.y));
			i++;
		}
	}
}

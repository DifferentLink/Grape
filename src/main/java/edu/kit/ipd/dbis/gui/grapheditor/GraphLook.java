/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.grapheditor;

import java.awt.*;

/**
 * This class simply lists default render values for RenderableGraphs.
 */
public class GraphLook {
	public static final int vertexDiameter = 25;
	public static final Color vertexFillColor = Color.WHITE;
	public static final int vertexOutLineThickness = 1;
	public static final Color vertexOutlineColor = Color.BLACK;

	public static final int edgeThickness = 3;
	public static final Color edgeColor = Color.BLACK;

	/**
	 * This method takes n input colors and spreads these on the colorwheel
	 * so that two respective colors have maximum contrast.
	 * @param numColors number of colors to spread
	 * @return an array with n Colors
	 */
	public Color[] spreadColors(final int numColors) {
		Color[] colors = new Color[numColors];

		final float step = 1/numColors;

		for (int color = 0; color < numColors; color++) {
			colors[color] = Color.getHSBColor(step * color, 1f, 1f);
		}

		return colors;
	}

	public void arrangeInCircle(Vertex[] vertices) { // todo implement dummy method

	}
}

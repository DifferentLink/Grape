package edu.kit.ipd.dbis.gui.grapheditor;

import java.awt.Color;
import java.awt.Point;
import java.util.*;

/**
 * This class simply lists default render values for RenderableGraphs.
 */
public class GraphLook {
	public static final int VERTEX_DIAMETER = 25;
	public static final Color VERTEX_FILL_COLOR = Color.WHITE;
	public static final int VERTEX_OUT_LINE_THICKNESS = 1;
	public static final Color VERTEX_OUTLINE_COLOR = Color.BLACK;

	public static final int EDGE_THICKNESS = 2;
	public static final Color EDGE_COLOR = Color.BLACK;

	private GraphLook() { }

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

	/**
	 * Arrange the given vertices in a circle in the area defined by two points.
	 * @param vertices the vertices to arrange
	 * @param upperLeft the upper left corner of the area
	 * @param lowerRight the lower right corner of the area
	 */
	public static void arrangeInCircle(Set<Vertex> vertices, Point upperLeft, Point lowerRight) {

		final Point center =
				new Point((lowerRight.x - upperLeft.x) / 2 + upperLeft.x, (lowerRight.y - upperLeft.y) / 2 + upperLeft.y);
		final double radius = ((Math.min(center.x - upperLeft.x, center.y - lowerRight.y)) * .75);
		final double angle = Math.toRadians(360d / (double) vertices.size());
		int i = 0;

		if (vertices.size() == 1) {
			vertices.iterator().next().setPosition(center.x, center.y);
		} else {
			for (Vertex vertex : new ArrayList<>(new TreeSet<>(vertices))) {
				vertex.setPosition(
						(int) (radius * Math.cos(i * angle) + center.x),
						(int) (radius * Math.sin(i * angle) + center.y));
				i++;
			}
		}
	}
	/**
	 * This method splits a the area defined by the upper left and lower right corner into a grid
	 * with at most n + 1 many cells, where n is the number of subgraphs given. It arranges the subgraphs
	 * in a circle, each in it's own cell in the grid. The vertices not contained in any subgraph are also
	 * placed in a circle in the last cell in the grid.
	 * @param subgraphs the subgraphs to position separately
	 * @param otherVertices the vertices not contained in any subgraph
	 * @param upperLeft the upper left corner of the area which is split into a grid
	 * @param lowerRight the lower right corner of the area which is split into a grid
	 */
	public static void arrangeInGrid(Set<Set<Vertex>> subgraphs, Set<Vertex> otherVertices,
	                                 Point upperLeft, Point lowerRight) {

		int numberGridcells;
		final int cellMargin = 5;
		if (subgraphs.size() > 0) {
			numberGridcells = subgraphs.size() + otherVertices.size();
		} else {
			numberGridcells = otherVertices.size();
		}
		if ((subgraphs.size() + otherVertices.size()) % 2 == 1) {
			numberGridcells = subgraphs.size() + otherVertices.size() + 1;
		}

		int xCells = (int) Math.ceil(Math.sqrt(numberGridcells));
		int yCells = (int) Math.floor(Math.sqrt(numberGridcells));
		if (xCells * yCells < numberGridcells) {
			if (xCells <= yCells) {
				xCells += 1;
			} else {
				yCells += 1;
			}

		}
		final int xStepsize = (lowerRight.x - upperLeft.x) / xCells;
		final int yStepsize = (lowerRight.y - upperLeft.y) / yCells;
		Iterator<Set<Vertex>> iterator = subgraphs.iterator();
		Iterator<Vertex> iteratorOther = otherVertices.iterator();
		for (int x = 0; x < xCells; x++) {
			for (int y = 0; y < yCells; y++) {
				Point upperLeftGrid = new Point(upperLeft.x + x * xStepsize + cellMargin,
						upperLeft.y + y * yStepsize + cellMargin);
				Point lowerRightGrid = new Point(upperLeft.x + x * xStepsize + xStepsize - cellMargin,
						upperLeft.y + y * yStepsize + yStepsize - cellMargin);
				if (iterator.hasNext()) {
					arrangeInCircle(iterator.next(), upperLeftGrid, lowerRightGrid);
				} else if (iteratorOther.hasNext()) {
					Set<Vertex> set = new HashSet<>();
					set.add(iteratorOther.next());
					arrangeInCircle(set, upperLeftGrid, lowerRightGrid);
				}
			}
		}
	}
	/**
	 * Arrange the given vertices in a circle in the area defined by two points.
	 * @param subgraphs the kk subgraphs
	 * @param otherVertices vertices that are not contained in the kkgraph
	 * @param upperLeft the upper left corner of the area
	 * @param lowerRight the lower right corner of the area
	 */
	public static void arrangeInCircle(Set<Set<Vertex>> subgraphs, Set<Vertex> otherVertices,
									   Point upperLeft, Point lowerRight) {

		final Point center =
				new Point((lowerRight.x - upperLeft.x) / 2 + upperLeft.x, (lowerRight.y - upperLeft.y) / 2 + upperLeft.y);
		final double radius = ((Math.min(center.x - upperLeft.x, center.y - lowerRight.y)) * .75);

		int numberOfVertices = otherVertices.size();
		for (Set<Vertex> subgraph : subgraphs) {
			numberOfVertices += subgraph.size();
		}
		final double angle = Math.toRadians(360d / (double) (numberOfVertices));
		int i = 0;

		if (subgraphs.size() == 0 && otherVertices.size() == 1) {
			otherVertices.iterator().next().setPosition(center.x, center.y);
		} else {
			for (Vertex vertex : new ArrayList<>(new TreeSet<>(otherVertices))) {
				vertex.setPosition(
						(int) (radius * Math.cos(i * angle) + center.x),
						(int) (radius * Math.sin(i * angle) + center.y));
				i++;
			}
			for (Set<Vertex> subgraph : subgraphs) {
				/*
				int distance = Math.abs((int) ((radius * Math.cos(0 * angle) + center.x) - (radius * Math.cos(1 * angle) + center.x)));
				Point upLeft = new Point(Math.max((int) (radius * Math.cos(i * angle) + center.x)
						- distance, 5), Math.max ((int) (radius * Math.sin(i * angle) + center.y) - distance, 5));
				Point lowRight = new Point(Math.min((int) (radius * Math.cos(i * angle) + center.x)
						+ distance, lowerRight.x) , Math.min((int) (radius * Math.sin(i * angle) + center.y) + distance, lowerRight.y));
				arrangeInCircle(subgraph, upLeft, lowRight);

				i++;
				*/
				for (Vertex vertex : subgraph) {
					vertex.setPosition(
							(int) (radius * Math.cos(i * angle) + center.x),
							(int) (radius * Math.sin(i * angle) + center.y));
					i++;
				}
			}
		}
	}
}

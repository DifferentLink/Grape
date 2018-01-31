package edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces;

import java.util.*;

/**
 * An algorithm which computes a graph total coloring.
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 */
public interface TotalColoringAlgorithm<V, E> {
	/**
	 * Get a total coloring.
	 *
	 * @return a total coloring
	 */
	TotalColoring getColoring();

	/**
	 * A total coloring.
	 *
	 * @param <V> the graph vertex type
	 * @param <E> the graph edge type
	 */
	interface TotalColoring<V, E> {
		/**
		 * Get the number of colors.
		 *
		 * @return the number of colors
		 */
		int getNumberColors();

		/**
		 * Get the vertices' color map.
		 *
		 * @return the color map
		 */
		Map<V, Integer> getVertexColors();

		/**
		 * Get the edges' color map.
		 *
		 * @return the color map
		 */
		Map<E, Integer> getEdgeColors();

		/**
		 * Get the vertices' color classes.
		 *
		 * @return list of color classes
		 */
		List<Set<V>> getVertexColorClasses();

		/**
		 * Get the edges' color classes.
		 *
		 * @return list of color classes
		 */
		List<Set<E>> getEdgeColorClasses();
	}

	/**
	 * Default implementation of the total coloring interface
	 *
	 * @param <V> the graph vertex type
	 * @param <E> the graph edge type
	 */
	class TotalColoringImpl<V, E> implements TotalColoring<V, E> {
		private final int numberColors;
		private final Map<V, Integer> vertexColors;
		private final Map<E, Integer> edgeColors;

		/**
		 * Construct a new total coloring.
		 *
		 * @param vertexColors the vertex color map
		 * @param edgeColors   the edge color map
		 * @param numberColors the number of colors
		 */
		public TotalColoringImpl(Map<V, Integer> vertexColors, Map<E, Integer> edgeColors, int numberColors) {
			this.vertexColors = vertexColors;
			this.edgeColors = edgeColors;
			this.numberColors = numberColors;
		}

		@Override
		public int getNumberColors() {
			return this.numberColors;
		}

		@Override
		public Map<V, Integer> getVertexColors() {
			return this.vertexColors;
		}

		@Override
		public Map<E, Integer> getEdgeColors() {
			return this.edgeColors;
		}

		@Override
		public List<Set<V>> getVertexColorClasses() {
			Map<Integer, Set<V>> groups = new HashMap<>();
			vertexColors.forEach((v, color) -> {
				Set<V> g = groups.get(color);
				if (g == null) {
					g = new HashSet<>();
					groups.put(color, g);
				}
				g.add(v);
			});
			List<Set<V>> classes = new ArrayList<>(numberColors);
			for (Set<V> c : groups.values()) {
				classes.add(c);
			}
			return classes;
		}

		@Override
		public List<Set<E>> getEdgeColorClasses() {
			Map<Integer, Set<E>> groups = new HashMap<>();
			edgeColors.forEach((e, color) -> {
				Set<E> g = groups.get(color);
				if (g == null) {
					g = new HashSet<>();
					groups.put(color, g);
				}
				g.add(e);
			});
			List<Set<E>> classes = new ArrayList<>(numberColors);
			for (Set<E> c : groups.values()) {
				classes.add(c);
			}
			return classes;
		}

		@Override
		public String toString() {
			return "Total Coloring [number-of-colors="
					+ numberColors + ", vertexColors="
					+ vertexColors + ", edgeColors=" + edgeColors
					+ "]";
		}
	}
}

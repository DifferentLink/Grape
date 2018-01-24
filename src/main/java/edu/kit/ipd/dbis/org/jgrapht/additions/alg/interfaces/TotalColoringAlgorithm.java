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
		 * Get the vertex color map.
		 *
		 * @return the vertex color map
		 */
		Map<V, Integer> getVertexColors();

		/**
		 * Get the edge color map.
		 *
		 * @return the edge color map
		 */
		Map<E, Integer> getEdgeColors();

		/**
		 * Get the color classes with both edges and vertices.
		 *
		 * @return List of Sets of Objects with the same color
		 */
		List<Set<Object>> getColorClasses();

		/**
		 * Get the vertices' color classes.
		 *
		 * @return the color classes
		 */
		List<Set<V>> getVertexColorClasses();

		/**
		 * Get the edges' color classes.
		 *
		 * @return the color classes
		 */
		List<Set<E>> getEdgeColorClasses();
	}

	/**
	 * Default implementation of the total coloring interface
	 *
	 * @param <V> the graph vertex type
	 * @param <E> the graph edge type
	 */
	class TotalColoringImplementation<V, E> implements TotalColoring<V, E> {
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
		public TotalColoringImplementation(Map<V, Integer> vertexColors, Map<E, Integer> edgeColors, int numberColors) {
			this.vertexColors = vertexColors;
			this.edgeColors = edgeColors;
			this.numberColors = numberColors;
		}

		@Override
		public int getNumberColors() {
			return 0;
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
		public List<Set<Object>> getColorClasses() {
			Map<Integer, Set<Object>> groups = new HashMap<>();

			List<Set<Object>> classes = new ArrayList<>();
			for (Set<Object> c : groups.values()) {
				classes.add(c);
			}
			return classes;
		}

		@Override
		public List<Set<V>> getVertexColorClasses() {
			// TODO: remove duplicate code. maybe create a new class in alg?
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
	}
}

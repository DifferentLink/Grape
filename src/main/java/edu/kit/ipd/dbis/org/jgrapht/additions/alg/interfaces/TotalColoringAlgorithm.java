package edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces;

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
	}

	/**
	 * Default implementation of the total coloring interface
	 *
	 * @param <V> the graph vertex type
	 * @param <E> the graph edge type
	 */
	class TotalColoringImpl<V, E> implements TotalColoring<V, E> {

		@Override
		public int getNumberColors() {
			return 0;
		}
	}
}

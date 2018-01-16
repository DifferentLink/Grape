package edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces;


import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.util.List;

/**
 * An algorithm which computes a bfs code of a graph.
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 */
public interface BfsCodeAlgorithm<V, E> {
	/**
	 * Get the bfs code
	 *
	 * @return the bfs code
	 */
	BfsCode<V, E> getBfsCode(PropertyGraph graph);

	/**
	 * A bfs code. A bfs code is an array of integer values.
	 *
	 * @param <V> the graph vertex type
	 * @param <E> the graph edge type
	 */
	interface BfsCode<V, E> extends Comparable {
		/**
		 * Get the length of the bfs code
		 * @return the length of the bfs code
		 */
		int getLength();

		/**
		 * Get the bfs code
		 * @return the bfs code
		 */
		int[] getCode();

		/**
		 * Determines the number of backward edges
		 * @return number of backward edges
		 */
		int getNumberOfBackwardEdges();

		/**
		 * Determines the backward edges
		 * @return set of backward edges
		 */
		List<E> getBackwardEdges();


	}
	/**
	 * Default implementation of the bfs code.
	 *
	 * @param <V> the graph vertex type
	 * @param <E> the graph edge type
	 */
	class BfsCodeImpl<V, E> implements BfsCode<V, E> {

		private final int[] code;

		/**
		 * Construct a new bfs code.
		 *
		 * @param code the bfs code
		 */
		public BfsCodeImpl(int[] code) {
			this.code = code;
		}

		@Override
		public int getLength() {
			return code.length;
		}

		@Override
		public int[] getCode() {
			return code;
		}

		@Override
		public int getNumberOfBackwardEdges() {
			return 0;
		}

		@Override
		public List<E> getBackwardEdges() {
			// TODO: implement me
			return null;
		}

		/**
		 *
		 * @param o other BfsCode
		 * @return -1, 0, 1 if this is less than, equal to, or greater than o.
		 */
		@Override
		public int compareTo(Object o) {
			int[] b2 = ((BfsCode) o).getCode();
			for (int i = 0; i < Math.min(this.code.length, b2.length); i++) {
				if (this.code[i] < b2[i]) {
					return -1;
				} else if (this.code[i] > b2[i]) {
					return 1;
				}
			}

			return Integer.compare(this.code.length, b2.length);
		}
	}
}

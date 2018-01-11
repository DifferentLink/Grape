package edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces;


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
	BfsCode<V, E> getBfsCode();

	/**
	 * A bfs code. A bfs code is an array of integer values.
	 *
	 * @param <V> the graph vertex type
	 * @param <E> the graph edge type
	 */
	interface BfsCode<V, E> {
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
			// TODO: implement me
			return 0;
		}
	}
}

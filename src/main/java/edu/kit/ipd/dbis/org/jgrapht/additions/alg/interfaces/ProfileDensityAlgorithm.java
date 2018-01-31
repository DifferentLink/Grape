package edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * An algorithm that computes a profile of a graph.
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 */
public interface ProfileDensityAlgorithm<V, E> extends Serializable {
	/**
	 * Get the profile.
	 *
	 * @param graph the input graph
	 * @return the profile
	 */
	Profile getProfile(PropertyGraph<V, E> graph);

	/**
	 * A profile. The profile is list of bfs codes.
	 *
	 * @param <V> the graph vertex type
	 * @param <E> the graph edge type
	 */
	interface Profile<V, E> extends Comparable {
		/**
		 * Get the profile matrix.
		 *
		 * @return the profile matrix
		 */
		int[][] getMatrix();

		LinkedList<BfsCodeAlgorithm.BfsCode> getBfsList();
	}

	/**
	 * Default implementation of the profile interface.
	 *
	 * @param <V> the graph vertex type
	 * @param <E> the graph edge type
	 */
	class ProfileImpl<V, E> implements Profile<V, E> {
		private int[][] profile;
		private LinkedList<BfsCodeAlgorithm.BfsCode> bfsList;

		/**
		 * constructs a new profile
		 *
		 * @param profile the profile matrix
		 */

		public ProfileImpl(int[][] profile) {
			this.profile = profile;
		}

		public ProfileImpl( LinkedList<BfsCodeAlgorithm.BfsCode> bfsList) {
			this.profile = getMatrix(bfsList);
			this.bfsList = bfsList;
		}

		@Override
		public int[][] getMatrix() {
			return this.profile;
		}

		@Override
		public LinkedList<BfsCodeAlgorithm.BfsCode> getBfsList() {
			return this.bfsList;
		}
		/**
		 *
		 * @param o other BfsCode
		 * @return -1, 0, 1 if this is less than, equal to, or greater than o.
		 */
		@Override
		public int compareTo(Object o) {
			int[][] p2 = ((ProfileImpl) o).getMatrix();
			if (this.getMatrix().length == 0 && p2.length == 0) {
				return 0;
			} else if (this.getMatrix()[0].length == 0 && p2[0].length == 0) {
				return 0;
			} else if (this.getMatrix().length == 0) {
				return -1;
			} else if (p2.length == 0) {
				return 1;
			}

			for (int i = 0; i < Math.min(this.getMatrix()[0].length, p2[0].length); i++) {
				for (int j = 0; j < Math.min(this.getMatrix().length, p2.length); j++) {
					if (this.profile[j][i] < p2[j][i]) {
						return -1;
					} else if (this.profile[j][i] > p2[j][i]) {
						return 1;
					}
				}
			}
			return 0;
		}

		/**
		 * get the profile matrix of the list of bfs codes -> on position 0 is the minimal bfs code
		 * @param list the list
		 * @return the profile
		 */
		private int[][] getMatrix(LinkedList<BfsCodeAlgorithm.BfsCode> list) {
			if (list.size() == 0) {
				return new int[0][0];
			}
			int[][] result = new int[list.size()][];
			int i = 0;
			Iterator<BfsCodeAlgorithm.BfsCode> iterator = list.iterator();
			while (iterator.hasNext()) {
				int[] code = iterator.next().getCode();
				result[i] = code;
				i++;
			}
			return result;
		}
	}
}

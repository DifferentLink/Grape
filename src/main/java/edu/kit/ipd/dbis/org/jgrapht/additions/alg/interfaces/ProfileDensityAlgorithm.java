package edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.util.List;

/**
 * An algorithm that computes a profile of a graph.
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 */
public interface ProfileDensityAlgorithm<V, E> {
	/**
	 * Get the profile.
	 *
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
		 * Get the profile.
		 *
		 * @return the profile
		 */
		int[][] getProfile();
	}

	/**
	 * Default implementation of the profile interface.
	 *
	 * @param <V> the graph vertex type
	 * @param <E> the graph edge type
	 */
	class ProfileImpl<V, E> implements Profile<V, E> {
		private int[][] profile;

		public ProfileImpl(int[][] profile) {
			this.profile = profile;
		}

		@Override
		public int[][] getProfile() {
			return this.profile;
		}

		/**
		 *
		 * @param o other BfsCode
		 * @return -1, 0, 1 if this is less than, equal to, or greater than o.
		 */
		@Override
		public int compareTo(Object o) {
			//TODO: implement me
			int[][] p2 = ((ProfileImpl) o).getProfile();
			if (this.getProfile().length == 0 && p2.length == 0) {
				return 0;
			} else if (this.getProfile()[0].length == 0 && p2[0].length == 0) {
				return 0;
			} else if (this.getProfile().length == 0) {
				return -1;
			} else if (p2.length == 0) {
				return 1;
			}

			for (int i = 0; i < Math.min(this.getProfile()[0].length, p2[0].length); i++) {
				for (int j = 0; j < Math.min(this.getProfile().length, p2.length); j++) {
					if (this.profile[j][i] < p2[j][i]) {
						return -1;
					} else if (this.profile[j][i] > p2[j][i]) {
						return 1;
					}
				}
			}
			return 0;
		}
	}
}

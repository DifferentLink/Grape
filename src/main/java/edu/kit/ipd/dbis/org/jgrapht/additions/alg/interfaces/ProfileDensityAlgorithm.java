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
	interface Profile<V, E> {
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
	}
}

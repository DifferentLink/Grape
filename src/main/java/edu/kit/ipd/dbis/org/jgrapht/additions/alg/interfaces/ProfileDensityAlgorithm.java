package edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces;

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
	Profile getProfile();

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
		List<BfsCodeAlgorithm.BfsCode> getProfile();
	}

	/**
	 * Default implementation of the profile interface.
	 *
	 * @param <V> the graph vertex type
	 * @param <E> the graph edge type
	 */
	class ProfileImpl<V, E> implements Profile<V, E> {

		@Override
		public List<BfsCodeAlgorithm.BfsCode> getProfile() {
			return null;
		}
	}
}

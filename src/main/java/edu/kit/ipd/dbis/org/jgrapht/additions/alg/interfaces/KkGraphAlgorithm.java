package edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A algorithm that computes a kk-graph of a given graph based on the Hadwiger Conjecture
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 */
public interface KkGraphAlgorithm<V, E> {
	/**
	 * Get the kk-graph.
	 * @return the kk-graph
	 */
	KkGraph getKkGraph();

	/**
	 * A kk-graph.
	 *
	 * @param <V> the graph vertex type
	 * @param <E> the graph edge type
	 */
	interface KkGraph<V, E> {
		/**
		 * Get the subgraphs of the kk-graph.
		 * @return the subgraphs
		 */
		List<Set<V>> getSubgraphs();

		/**
		 * Get the kk-graph.
		 *
		 * @return the kk-graph
		 */
		Map<V, Integer> getKkGraph();

		/**
		 * Get the number of subgraphs.
		 *
		 * @return the number of subgraphs
		 */
		int getNumberOfSubgraphs();
	}

	/**
	 * Default implementation of the kk-graph interface.
	 *
	 * @param <V> the graph vertex type
	 * @param <E> the graph edge type
	 */
	class KkGraphImpl<V, E> implements KkGraph<V, E> {

		private final int numberOfSubgraphs;
		private final Map<V, Integer> kkGraph;

		/**
		 * Constructs a new kk-graph
		 *
		 * @param kkGraph the kk-graph map
		 * @param numberOfSubgraphs the number of subgraphs
		 */
		public KkGraphImpl(Map<V, Integer> kkGraph, int numberOfSubgraphs) {
			this.numberOfSubgraphs = numberOfSubgraphs;
			this.kkGraph = kkGraph;
		}
		@Override
		public List<Set<V>> getSubgraphs() {
			//Implementation of the specific algorithm
			return null;
		}

		@Override
		public Map<V, Integer> getKkGraph() {
			return this.kkGraph;
		}

		@Override
		public int getNumberOfSubgraphs() {
			return this.numberOfSubgraphs;
		}
	}
}

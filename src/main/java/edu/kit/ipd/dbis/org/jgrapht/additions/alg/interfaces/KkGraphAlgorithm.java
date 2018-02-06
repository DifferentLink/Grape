package edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.kkgraph.NoKkGraphException;

import java.io.Serializable;
import java.util.*;

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
	 * @throws NoKkGraphException if the kk graph can't be calculated
	 */
	KkGraph getKkGraph() throws NoKkGraphException;

	/**
	 * A kk-graph.
	 *
	 * @param <V> the graph vertex type
	 * @param <E> the graph edge type
	 */
	interface KkGraph<V, E> extends Serializable {
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
			Map<Integer, Set<V>> groups = new HashMap<>();
			kkGraph.forEach((v, kkgraph) -> {
				Set<V> g = groups.get(kkgraph);
				if (g == null) {
					g = new HashSet<>();
					groups.put(kkgraph, g);
				}
				g.add(v);
			});
			List<Set<V>> classes = new ArrayList<>(numberOfSubgraphs);
			for (Set<V> c : groups.values()) {
				classes.add(c);
			}
			return classes;
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

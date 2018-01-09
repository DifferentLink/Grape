package edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface KkGraphAlgorithm<V,E> {
	KkGraph getKkGraph();

	public interface KkGraph<V,E> {
		List<Set<V>> getSubgraphs();
		Map<V,Integer> getKkGraph();
		int getNumberOfSubgraphs();
	}

	public class KkGraphImplementation<V,E> implements  KkGraph {

		@Override
		public List<Set> getSubgraphs() {
			return null;
		}

		@Override
		public Map getKkGraph() {
			return null;
		}

		@Override
		public int getNumberOfSubgraphs() {
			return 0;
		}
	}
}

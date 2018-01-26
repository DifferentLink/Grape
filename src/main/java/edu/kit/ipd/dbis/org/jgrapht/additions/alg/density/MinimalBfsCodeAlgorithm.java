package edu.kit.ipd.dbis.org.jgrapht.additions.alg.density;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

/**
 The minimal bfs code algorithm.
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 */
@Deprecated
public class MinimalBfsCodeAlgorithm<V, E> implements BfsCodeAlgorithm {

	@Override
	public BfsCode getBfsCode(PropertyGraph graph) {
		int[] worstCode = new int[graph.edgeSet().size() * 3];
		for (int i = 0; i < worstCode.length; i++) {
			worstCode[i] = graph.vertexSet().size() + 1;
		}
		BfsCode bestCode = new BfsCodeImpl(worstCode);

		for (Object v : graph.vertexSet()) {
			LocalBfsCodeAlgorithm local = new LocalBfsCodeAlgorithm((V) v);
			BfsCode localCode = local.getBfsCode(graph);
			if (localCode.compareTo(bestCode) == -1) {
				bestCode = localCode;
			}
		}
		return bestCode;
	}
}

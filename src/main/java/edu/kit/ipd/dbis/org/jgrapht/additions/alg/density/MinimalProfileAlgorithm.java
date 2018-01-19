package edu.kit.ipd.dbis.org.jgrapht.additions.alg.density;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.ProfileDensityAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.util.*;

/**
 The minimal profile algorithm.
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 */
public class MinimalProfileAlgorithm<V, E> implements ProfileDensityAlgorithm {

	@Override
	public Profile getProfile(PropertyGraph graph) {
		ArrayList<BfsCodeAlgorithm.BfsCode> allLocalCodes = new ArrayList<>();
		for (Object v : graph.vertexSet()) {
			LocalBfsCodeAlgorithm local = new LocalBfsCodeAlgorithm((V) v);
			BfsCodeAlgorithm.BfsCode localCode = local.getBfsCode(graph);
			allLocalCodes.add(localCode);
		}
		return new ProfileImpl(getProfile(allLocalCodes));
	}

	/**
	 * get the profil of the list of bfs codes -> on position 0 is the minimal bfs code
	 * @param list the list
	 * @return the profil
	 */
	public int[][] getProfile(ArrayList<BfsCodeAlgorithm.BfsCode> list) {
		return null;
	}
}

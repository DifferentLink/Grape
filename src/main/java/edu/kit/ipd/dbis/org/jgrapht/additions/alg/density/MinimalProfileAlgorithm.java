package edu.kit.ipd.dbis.org.jgrapht.additions.alg.density;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.ProfileDensityAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.BfsCode;

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
		LinkedList<BfsCodeAlgorithm.BfsCode> allLocalCodes = new LinkedList<>(); //is sorted at the end
		for (Object v : graph.vertexSet()) {
			LocalBfsCodeAlgorithm local = new LocalBfsCodeAlgorithm((V) v);
			BfsCodeAlgorithm.BfsCode localCode = local.getBfsCode(graph);

			//adds the localCode at the right sorted order
			Iterator<BfsCodeAlgorithm.BfsCode> iterator = allLocalCodes.iterator();
			int index = 0;
			while (iterator.hasNext()) {
				BfsCodeAlgorithm.BfsCode code = iterator.next();
				if (code.compareTo(localCode) < 0) {
					index++;
				}
			}
			allLocalCodes.add(index, localCode);
		}
		return new ProfileImpl(getProfile(allLocalCodes));
	}

	/**
	 * get the profil of the list of bfs codes -> on position 0 is the minimal bfs code
	 * @param list the list
	 * @return the profil
	 */
	public int[][] getProfile(LinkedList<BfsCodeAlgorithm.BfsCode> list) {
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

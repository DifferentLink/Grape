package edu.kit.ipd.dbis.org.jgrapht.additions.alg.density;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.ProfileDensityAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.util.LinkedList;
import java.util.Iterator;

/**
 The minimal profile algorithm.
 *
 * @param <V> the graph vertex type
 */
public class MinimalProfileAlgorithm<V> implements ProfileDensityAlgorithm {

	@Override
	public Profile getProfile(PropertyGraph graph) {
		LinkedList<BfsCodeAlgorithm.BfsCode> allLocalCodes = new LinkedList<>(); //is sorted at the end
		for (Object v : graph.vertexSet()) {
			LocalBfsCodeAlgorithm local = new LocalBfsCodeAlgorithm(v);
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
		return new ProfileImpl(allLocalCodes);
	}
}

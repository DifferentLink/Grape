package edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.density.NoDenserGraphException;
import org.jgrapht.Graph;

/**
 * An algorithm that computes the closest new graph which has a higher density.
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 */
public interface NextDensityAlgorithm<V, E> {
	/**
	 * Get the closest graph with a higher density.
	 *
	 * @return the closest graph with a higher density
	 * @throws NoDenserGraphException if there is no denser graph
	 */
	Graph<V, E> getNextDenserGraph() throws NoDenserGraphException;
}


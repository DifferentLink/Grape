package edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces;

/**
 * An algorithm that computes a density of a graph with a number value.
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 */
public interface NumberDensityAlgorithm<V, E> {
	/**
	 * Get the density.
	 *
	 * @return the density
	 */
	double getDensity();
}

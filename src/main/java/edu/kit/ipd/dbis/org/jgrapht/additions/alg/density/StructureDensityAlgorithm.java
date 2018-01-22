package edu.kit.ipd.dbis.org.jgrapht.additions.alg.density;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.NumberDensityAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.util.Objects;


/**
 The structure density algorithm. It computes the density of a given graph by using the s4 density defined in our
 *Pflichtenheft.
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 */
public class StructureDensityAlgorithm<V, E> implements NumberDensityAlgorithm {
	/**
	 * The input graph
	 */
	protected final PropertyGraph<V, E> graph;

	/**
	 * Construct a new proportion density algorithm.
	 *
	 * @param graph the input graph
	 */
	public StructureDensityAlgorithm(PropertyGraph<V, E> graph) {
		this.graph = Objects.requireNonNull(graph, "Graph cannot be null");
	}

	@Override
	public double getDensity() {
		return 0.0;
	}
}

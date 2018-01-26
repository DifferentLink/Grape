package edu.kit.ipd.dbis.org.jgrapht.additions.alg.density;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.NumberDensityAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.BfsCode;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 The proportion density algorithm.
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 */
public class NodeSpecificDensityAlgorithm<V, E> implements NumberDensityAlgorithm {
	/**
	 * The input graph
	 */
	protected final PropertyGraph<V, E> graph;

	/**
	 * Construct a new proportion density algorithm.
	 *
	 * @param graph the input graph
	 */
	public NodeSpecificDensityAlgorithm(PropertyGraph<V, E> graph) {
		this.graph = Objects.requireNonNull(graph, "Graph cannot be null");
	}

	@Override
	public double getDensity() {
		BfsCodeAlgorithm.BfsCodeImpl bfsCode = new BfsCodeAlgorithm.BfsCodeImpl((int[]) graph.getProperty(BfsCode.class).getValue());
		List<int[]> backwardEdges = bfsCode.getBackwardEdges();
		double numerator = 0 ;
		for (int[] edge : backwardEdges) {
			numerator += (1 / (Math.abs(edge[0] - edge[1])));
		}
		double denumerator = 0;
		int k = graph.vertexSet().size(); //number of vertices
		for (int i = 1; i <= k - 2; i++) {
			denumerator += (i * (1 / (k - 1 - i)));
		}
		return numerator / denumerator;
	}
}
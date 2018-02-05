package edu.kit.ipd.dbis.org.jgrapht.additions.alg.density;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.NumberDensityAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.BfsCode;

import java.util.Objects;

/**
 The proportion density algorithm.
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 */
public class ProportionDensityAlgorithm<V, E> implements NumberDensityAlgorithm {
	/**
	 * The input graph
	 */
	protected final PropertyGraph<V, E> graph;

	/**
	 * Construct a new proportion density algorithm.
	 *
	 * @param graph the input graph
	 */
	public ProportionDensityAlgorithm(PropertyGraph<V, E> graph) {
		this.graph = Objects.requireNonNull(graph, "Graph cannot be null");
	}

	private int getNumberOfBackwardEdges(int[] code) {
		int cnt = 0;
		for (int i = 0; i < code.length; i++) {
			if (code[i] == -1) {
				cnt++;
			}
		}
		return cnt;
	}

	@Override
	public double getDensity() {
		int[] code = ((BfsCodeAlgorithm.BfsCode) graph.getProperty(BfsCode.class).getValue()).getCode();
		int b = getNumberOfBackwardEdges(code);
		return  (double)b / graph.edgeSet().size();
	}
}

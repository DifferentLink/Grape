package edu.kit.ipd.dbis.org.jgrapht.additions.alg.density;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.NumberDensityAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyType;

import java.util.Objects;

/**
 * Calculates a graph's density based on the node specific
 * density formula.
 * @param <V>
 * @param <E>
 */
public class NodeSpecificDensityAlgorithm<V,E> implements NumberDensityAlgorithm {
	private PropertyGraph graph;

	/**
	 * Standard constructor
	 * @param graph target graph
	 */
	public NodeSpecificDensityAlgorithm(PropertyGraph<V, E> graph) {
		this.graph = Objects.requireNonNull(graph, "Graph cannot be null");
	}

	@Override
	public double getDensity() {
		BfsCodeAlgorithm.BfsCode bfsCode = (BfsCodeAlgorithm.BfsCode) graph.getProperty(PropertyType.BFSCODE).getValue();
		double numberOfBackwardEdges = bfsCode.getNumberOfBackwardEdges();
		int numEdges = graph.edgeSet().size();
		return numberOfBackwardEdges / numEdges;
	}
}

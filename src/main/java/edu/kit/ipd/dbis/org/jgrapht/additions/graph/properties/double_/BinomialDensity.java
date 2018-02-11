package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.density.BinomialDensityAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.DoubleProperty;

/**
 * Represents the binomial density
 * in a graph.
 */
public class BinomialDensity extends DoubleProperty {
	/**
	 * Standard constructor
	 *
	 * @param graph the input graph
	 */
	public BinomialDensity(PropertyGraph graph) {
		super(graph);
	}

	@Override
	protected Double calculateAlgorithm(PropertyGraph graph) {
		BinomialDensityAlgorithm alg = new BinomialDensityAlgorithm(graph);
		return alg.getDensity();
	}
}
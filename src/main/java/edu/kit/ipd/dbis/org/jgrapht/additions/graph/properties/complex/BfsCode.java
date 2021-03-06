package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.ProfileDensityAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.ComplexProperty;

/**
 * the bfs code property
 */
public class BfsCode extends ComplexProperty {
	/**
	 * Standard constructor
	 *
	 * @param graph the input graph
	 */
	public BfsCode(PropertyGraph graph) {
		super(graph);
	}

	@Override
	protected BfsCodeAlgorithm.BfsCode calculateAlgorithm(PropertyGraph graph) {
		return (((ProfileDensityAlgorithm.ProfileImpl) graph.getProperty(Profile.class).getValue()).getMinBfsCode());
	}
}

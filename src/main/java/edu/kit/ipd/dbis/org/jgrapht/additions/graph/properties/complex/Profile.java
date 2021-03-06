package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.density.MinimalProfileAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.ProfileDensityAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.ComplexProperty;

/**
 * the profile property
 */
public class Profile extends ComplexProperty {
	/**
	 * Standard constructor
	 *
	 * @param graph the input graph
	 */
	public Profile(PropertyGraph graph) {
		super(graph);
	}

	@Override
	protected ProfileDensityAlgorithm.Profile calculateAlgorithm(PropertyGraph graph) {
		MinimalProfileAlgorithm alg = new MinimalProfileAlgorithm();
		return alg.getProfile(graph);
	}
}

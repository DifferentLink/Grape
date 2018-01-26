package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.density.ProportionDensityAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.DoubleProperty;
import org.kohsuke.MetaInfServices;

/**
 * the proportion density property
 */
@MetaInfServices
public class ProportionDensity extends DoubleProperty {
	/**
	 * Standard constructor
	 *
	 * @param graph  the input graph
	 */
	public ProportionDensity(PropertyGraph graph) {
		super(graph);
	}

	@Override
	protected Double calculateAlgorithm(PropertyGraph graph) {
		ProportionDensityAlgorithm alg = new ProportionDensityAlgorithm(graph);
		return alg.getDensity();
	}
}

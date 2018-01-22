package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.degree.AverageDegreeFinder;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.density.NodeSpecificDensityAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.DoubleProperty;
import org.kohsuke.MetaInfServices;

/**
 * Represents the average degree of all nodes
 * in a graph.
 */
@MetaInfServices
public class NodeSpecificDensity extends DoubleProperty {
	/**
	 * Standard constructor
	 *
	 * @param graph
	 */
	public NodeSpecificDensity(PropertyGraph graph) {
		super(graph);
	}

	@Override
	protected Double calculateAlgorithm(PropertyGraph graph) {
		NodeSpecificDensityAlgorithm alg = new NodeSpecificDensityAlgorithm(graph);
		return alg.getDensity();
	}
}
package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.degree.AverageDegreeFinder;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.DoubleProperty;
import org.kohsuke.MetaInfServices;

/**
 * Represents the average degree of all nodes
 * in a graph.
 */
@MetaInfServices
public class AverageDegree extends DoubleProperty {
	@Override
	protected Double calculateAlgorithm(PropertyGraph graph) {
		AverageDegreeFinder finder = new AverageDegreeFinder(graph);
		return finder.getDegree();
	}
}

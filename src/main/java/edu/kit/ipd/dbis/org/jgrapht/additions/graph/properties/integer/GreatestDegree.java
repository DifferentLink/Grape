package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.degree.MaxDegreeFinder;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.IntegerProperty;
import org.kohsuke.MetaInfServices;

/**
 * Calculates the greatest degree of all nodes
 * in a graph.
 */
@MetaInfServices
public class GreatestDegree extends IntegerProperty {
	/**
	 * Standard constructor
	 *
	 * @param graph
	 */
	public GreatestDegree(PropertyGraph graph) {
		super(graph);
	}

	@Override
	protected Integer calculateAlgorithm(PropertyGraph graph) {
		MaxDegreeFinder finder = new MaxDegreeFinder(graph);
		return finder.getDegree();
	}
}

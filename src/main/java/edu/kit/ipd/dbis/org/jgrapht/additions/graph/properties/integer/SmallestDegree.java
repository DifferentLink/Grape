package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.degree.MinDegreeFinder;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.IntegerProperty;
import org.kohsuke.MetaInfServices;

/**
 * Calculates the smallest degree of all nodes
 * in a graph.
 */
@MetaInfServices
public class SmallestDegree extends IntegerProperty {
	/**
	 * Standard constructor
	 *
	 * @param graph
	 */
	public SmallestDegree(PropertyGraph graph) {
		super(graph);
	}

	@Override
	protected Integer calculateAlgorithm(PropertyGraph graph) {
		MinDegreeFinder finder = new MinDegreeFinder(graph);
		return finder.getDegree();
	}
}

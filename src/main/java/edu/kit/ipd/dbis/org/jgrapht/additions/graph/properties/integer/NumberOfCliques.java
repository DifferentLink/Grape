package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.IntegerProperty;
import org.kohsuke.MetaInfServices;

/**
 * TODO: design change
 * Calculates the number of cliques.
 */
@MetaInfServices
public class NumberOfCliques extends IntegerProperty {
	/**
	 * Standard constructor
	 *
	 * @param graph
	 */
	public NumberOfCliques(PropertyGraph graph) {
		super(graph);
	}

	@Override
	protected Integer calculateAlgorithm(PropertyGraph graph) {
		return null;
	}
}

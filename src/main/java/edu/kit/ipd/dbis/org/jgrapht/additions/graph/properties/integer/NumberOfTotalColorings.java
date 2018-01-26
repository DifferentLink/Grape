package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.IntegerProperty;
import org.kohsuke.MetaInfServices;

/**
 * Calculates the number of total colorings
 */
@MetaInfServices
public class NumberOfTotalColorings extends IntegerProperty {
	/**
	 * Standard constructor
	 *
	 * @param graph the input graph
	 */
	public NumberOfTotalColorings(PropertyGraph graph) {
		super(graph);
	}

	@Override
	protected Integer calculateAlgorithm(PropertyGraph graph) {
		return null;
	}
}

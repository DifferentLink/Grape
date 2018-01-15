package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.IntegerProperty;
import org.kohsuke.MetaInfServices;

/**
 * Calculates the number of total colorings
 */
@MetaInfServices
public class NumberOfTotalColorings extends IntegerProperty {
	@Override
	protected Integer calculateAlgorithm(PropertyGraph graph) {
		return null;
	}
}

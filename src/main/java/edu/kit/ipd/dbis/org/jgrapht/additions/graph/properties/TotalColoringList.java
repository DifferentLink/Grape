package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.TotalColoringAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.util.List;

/**
 * Represents the possible total colorings
 * of a graph.
 */
public class TotalColoringList extends Property {
	@Override
	protected List<TotalColoringAlgorithm.TotalColoring> calculationAlgorithm(PropertyGraph graph) {
		return null;
	}
}

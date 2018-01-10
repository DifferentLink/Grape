package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.util.List;
import java.util.Set;

/**
 * Represents all maximal cliques of a graph.
 */
public class MaxCliques<V> extends Property {
	@Override
	protected List<Set<V>> calculationAlgorithm(PropertyGraph graph) {
		return null;
	}
}

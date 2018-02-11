package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.IntegerProperty;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.Cliques;

import java.util.List;
import java.util.Set;

/**
 * Calculates the number of cliques.
 */
public class NumberOfCliques extends IntegerProperty {
	/**
	 * Standard constructor
	 *
	 * @param graph the input graph
	 */
	public NumberOfCliques(PropertyGraph graph) {
		super(graph);
	}

	@Override
	protected Integer calculateAlgorithm(PropertyGraph graph) {
		return ((List<Set<Object>>) graph.getProperty(Cliques.class).getValue()).size();
	}
}

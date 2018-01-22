package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.IntegerProperty;
import org.kohsuke.MetaInfServices;

/**
 * TODO: design change
 * Calculates the number of vertex colorings.
 */
@MetaInfServices
public class NumberOfVertexColorings extends IntegerProperty {
	/**
	 * Standard constructor
	 *
	 * @param graph
	 */
	public NumberOfVertexColorings(PropertyGraph graph) {
		super(graph);
	}

	@Override
	protected Integer calculateAlgorithm(PropertyGraph graph) {
		return null;
	}
}

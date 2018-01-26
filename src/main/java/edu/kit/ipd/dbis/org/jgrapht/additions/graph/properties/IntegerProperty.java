package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public abstract class IntegerProperty extends Property {
	/**
	 * Standard constructor
	 *
	 * @param graph
	 */
	public IntegerProperty(PropertyGraph graph) {
		super(graph);
	}

	@Override
	protected abstract Integer calculateAlgorithm(PropertyGraph graph);
}

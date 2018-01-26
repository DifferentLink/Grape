package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.kohsuke.MetaInfServices;

/**
 * This class represents a complex property, whose value
 * can only be represented as an object which cannot be sorted.
 */
@MetaInfServices
public abstract class ComplexProperty extends Property {
	/**
	 * Standard constructor
	 *
	 * @param graph the input graph
	 */
	public ComplexProperty(PropertyGraph graph) {
		super(graph);
	}

	@Override
	protected abstract Object calculateAlgorithm(PropertyGraph graph);
}

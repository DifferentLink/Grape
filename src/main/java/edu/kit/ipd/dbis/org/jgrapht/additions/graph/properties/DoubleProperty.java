package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

/**
 * the double property class. This class is a superclass for all properties that returns a double value
 */
public abstract class DoubleProperty extends Property {
	/**
	 * Standard constructor
	 *
	 * @param graph the input graph
	 */
	public DoubleProperty(PropertyGraph graph) {
		super(graph);
	}

	@Override
	protected abstract Double calculateAlgorithm(PropertyGraph graph);
}

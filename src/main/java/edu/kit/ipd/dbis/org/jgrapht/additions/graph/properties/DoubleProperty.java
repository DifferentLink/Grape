package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public abstract class DoubleProperty extends Property {
	/**
	 * Standard constructor
	 *
	 * @param graph
	 */
	public DoubleProperty(PropertyGraph graph) {
		super(graph);
	}

	@Override
	protected abstract Double calculateAlgorithm(PropertyGraph graph);
}

package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public abstract class DoubleProperty extends Property {
	@Override
	protected abstract Double calculateAlgorithm(PropertyGraph graph);
}

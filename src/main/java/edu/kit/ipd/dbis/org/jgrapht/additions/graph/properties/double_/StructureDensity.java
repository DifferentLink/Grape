package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.DoubleProperty;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class StructureDensity extends DoubleProperty {
	/**
	 * Standard constructor
	 *
	 * @param graph
	 */
	public StructureDensity(PropertyGraph graph) {
		super(graph);
	}

	@Override
	protected Double calculateAlgorithm(PropertyGraph graph) {
		return null;
	}
}

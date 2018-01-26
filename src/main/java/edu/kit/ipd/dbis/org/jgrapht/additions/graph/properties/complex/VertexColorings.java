package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.ComplexProperty;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class VertexColorings extends ComplexProperty {
	/**
	 * Standard constructor
	 *
	 * @param graph
	 */
	public VertexColorings(PropertyGraph graph) {
		super(graph);
	}

	@Override
	protected Object calculateAlgorithm(PropertyGraph graph) {
		// TODO: Need method calculateAllColorings()
		return null;
	}
}

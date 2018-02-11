package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.density.StructureDensityAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.DoubleProperty;

/**
 * the structure density property
 */
public class StructureDensity extends DoubleProperty {
	/**
	 * Standard constructor
	 *
	 * @param graph the input graph
	 */
	public StructureDensity(PropertyGraph graph) {
		super(graph);
	}

	@Override
	protected Double calculateAlgorithm(PropertyGraph graph) {
		StructureDensityAlgorithm alg = new StructureDensityAlgorithm(graph);
		return alg.getDensity();
	}
}

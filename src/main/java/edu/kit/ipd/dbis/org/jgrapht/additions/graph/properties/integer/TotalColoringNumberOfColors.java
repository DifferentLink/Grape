package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.TotalColoringAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.IntegerProperty;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.TotalColoring;

import java.util.List;

public class TotalColoringNumberOfColors extends IntegerProperty {
	/**
	 * Standard constructor
	 *
	 * @param graph
	 */
	public TotalColoringNumberOfColors(PropertyGraph graph) {
		super(graph);
	}

	@Override
	protected Integer calculateAlgorithm(PropertyGraph graph) {
		return ((TotalColoringAlgorithm.TotalColoring)
				((List<TotalColoring>) graph.getProperty(TotalColoring.class).getValue()).get(0))
				.getNumberColors();
	}
}

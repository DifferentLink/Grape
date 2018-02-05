package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.IntegerProperty;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.VertexColoring;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;

import java.util.List;

public class VertexColoringNumberOfColors extends IntegerProperty {
	/**
	 * Standard constructor
	 *
	 * @param graph
	 */
	public VertexColoringNumberOfColors(PropertyGraph graph) {
		super(graph);
	}

	@Override
	protected Integer calculateAlgorithm(PropertyGraph graph) {
		return ((VertexColoringAlgorithm.Coloring)
				((List<VertexColoring>) graph.getProperty(VertexColoring.class).getValue()).get(0))
				.getNumberColors();
	}
}

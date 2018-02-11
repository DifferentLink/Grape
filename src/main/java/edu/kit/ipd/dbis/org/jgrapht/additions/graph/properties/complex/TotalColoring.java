package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.color.MinimalTotalColoring;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.TotalColoringAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.ComplexProperty;

import java.util.List;


/**
 * the total coloring property
 */
public class TotalColoring<V> extends ComplexProperty {
	/**
	 * Standard constructor
	 *
	 * @param graph the input graph
	 */
	public TotalColoring(PropertyGraph graph) {
		super(graph);
	}

	@Override
	protected List<TotalColoringAlgorithm.TotalColoring> calculateAlgorithm(PropertyGraph graph) {
		MinimalTotalColoring alg = new MinimalTotalColoring(graph);
		return alg.getAllColorings();
	}
}

package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.color.MinimalTotalColoring;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.TotalColoringAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.ComplexProperty;
import org.jgrapht.VertexFactory;
import org.kohsuke.MetaInfServices;


/**
 * the total coloring property
 */
@MetaInfServices
public class TotalColoring<V> extends ComplexProperty {
	private VertexFactory vertexFactory;
	/**
	 * Standard constructor
	 *
	 * @param graph the input graph
	 */
	public TotalColoring(PropertyGraph graph, VertexFactory vertexFactory) {
		super(graph);
		this.vertexFactory = vertexFactory;
	}

	@Override
	protected TotalColoringAlgorithm.TotalColoring calculateAlgorithm(PropertyGraph graph) {
		MinimalTotalColoring alg = new MinimalTotalColoring(graph, vertexFactory);
		return alg.getColoring();
	}
}

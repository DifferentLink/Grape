package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.color.MinimalVertexColoring;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.ComplexProperty;

import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;
import org.kohsuke.MetaInfServices;



/**
 * the vertex coloring property
 */
@MetaInfServices
public class VertexColoring extends ComplexProperty {
	/**
	 * Standard constructor
	 *
	 * @param graph  the input graph
	 */
	public VertexColoring(PropertyGraph graph) {
		super(graph);
	}

	@Override
	protected VertexColoringAlgorithm.Coloring calculateAlgorithm(PropertyGraph graph) {
		MinimalVertexColoring alg = new MinimalVertexColoring(graph);
		return alg.getColoring();
	}
}

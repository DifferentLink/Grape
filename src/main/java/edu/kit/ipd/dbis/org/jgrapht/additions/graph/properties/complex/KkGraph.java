package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.KkGraphAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.kkgraph.KkGraphGenerator;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.ComplexProperty;
import org.kohsuke.MetaInfServices;

/**
 * the kk graph property
 */
@MetaInfServices
public class KkGraph extends ComplexProperty {
	/**
	 * Standard constructor
	 *
	 * @param graph the input graph
	 */
	public KkGraph(PropertyGraph graph) {
		super(graph);
	}

	@Override
	protected KkGraphAlgorithm.KkGraph calculateAlgorithm(PropertyGraph graph) {
		KkGraphGenerator gen = new KkGraphGenerator(graph);
		return gen.getKkGraph();
	}
}

package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.KkGraphAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.IntegerProperty;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.KkGraph;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NumberOfDisjointVerticesFromKkGraph extends IntegerProperty {
	/**
	 * Standard constructor
	 *
	 * @param graph input graph
	 */
	public NumberOfDisjointVerticesFromKkGraph(PropertyGraph graph) {
		super(graph);
	}

	@Override
	protected Integer calculateAlgorithm(PropertyGraph graph) {
		List<Set<Object>> subgraphs = (List<Set<Object>>) ((KkGraphAlgorithm.KkGraph) graph.getProperty(KkGraph.class).getValue()).getSubgraphs();
		Set<Object> subgraphVertices = new HashSet<>();
		for (Set<Object> s : subgraphs) {
			subgraphVertices.addAll(s);
		}
		Set<Object> tmp = new HashSet<>(graph.vertexSet());
		tmp.removeAll(subgraphVertices);
		return tmp.size();
	}
}

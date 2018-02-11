package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.KkGraphAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.IntegerProperty;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.KkGraph;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NumberOfDisjointEdgesFromKkGraph extends IntegerProperty {
	/**
	 * Standard constructor
	 *
	 * @param graph input graph
	 */
	public NumberOfDisjointEdgesFromKkGraph(PropertyGraph graph) {
		super(graph);
	}

	protected Integer calculateAlgorithm(PropertyGraph graph) {
		Map<Object, Integer> kkGraph = (Map<Object, Integer>) ((KkGraphAlgorithm.KkGraph) graph.getProperty(KkGraph.class).getValue()).getKkGraph();
		Set<Object> subgraphEdges = new HashSet<>();
		for (Object v : kkGraph.keySet()) {
			Set<Object> outgoingedges = graph.outgoingEdgesOf(v);
			for (Object o : outgoingedges) {
				if (kkGraph.keySet().contains(graph.getEdgeTarget(o)) && kkGraph.keySet().contains(graph.getEdgeSource(o))) {
					subgraphEdges.add(o);
				}
			}
			Set<Object> incomingedges = graph.incomingEdgesOf(v);
			for (Object o : incomingedges) {
				if (kkGraph.keySet().contains(graph.getEdgeTarget(o)) && kkGraph.keySet().contains(graph.getEdgeSource(o))) {
					subgraphEdges.add(o);
				}
			}
		}
		Set<Object> tmp = new HashSet<>(graph.edgeSet());
		tmp.removeAll(subgraphEdges);
		return tmp.size();
	}
}

package edu.kit.ipd.dbis.org.jgrapht.additions.alg.density;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.util.*;

/**
 The minimal bfs code algorithm.
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 */
public class MinimalBfsCodeAlgorithm<V, E> implements BfsCodeAlgorithm {

	@Override
	public BfsCode getBfsCode(PropertyGraph graph) {
		int[] worstCode = new int[graph.edgeSet().size() * 3];
		for (int i : worstCode) {
			i = graph.vertexSet().size() + 1;
		}
		BfsCode bestCode = new BfsCodeImpl(worstCode);

		for (Object v : graph.vertexSet()) {
			BfsCode localCode = this.getLocalBfsCode(graph, (V) v);
			// TODO: create Profile object and add to graph
			if (localCode.compareTo(bestCode) < 0) {
				bestCode = localCode;
			}
		}

		return bestCode;
	}

	/**
	 * Determine the BFS-Code starting
	 * from the specified start node.
	 * @param startNode
	 * @return
	 */
	public BfsCode getLocalBfsCode(PropertyGraph graph, V startNode) {
		int[] code = new int[graph.edgeSet().size() * 3];
		Set<E> adjacentEdges = graph.outgoingEdgesOf(startNode);
		Set<V> adjacentVertices = new HashSet<>();
		for (E edge : adjacentEdges) {
			adjacentVertices.add((V) graph.getEdgeTarget(edge));
		}

		return null;
	}

	private int[] getAdjacentBfsCode(PropertyGraph graph, V startNode) {
		return null;
	}
}

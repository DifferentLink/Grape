package edu.kit.ipd.dbis.org.jgrapht.additions.alg.density;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.util.Set;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

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
		for (int i = 0; i < worstCode.length; i++) {
			worstCode[i] = graph.vertexSet().size() + 1;
		}
		BfsCode bestCode = new BfsCodeImpl(worstCode);

		for (Object v : graph.vertexSet()) {
			LocalBfsCodeAlgorithm local = new LocalBfsCodeAlgorithm((V) v);
			BfsCode localCode = local.getBfsCode(graph);
			if (localCode.compareTo(bestCode) == -1) {
				bestCode = localCode;
			}
		}
		return bestCode;
	}

	/**
	 * Generates an adjacency matrix for a given graph
	 * @return the adjacency matrix
	 */
	protected List<int[]> getAdjacencyMatrix(PropertyGraph graph) {
		List<int[]> matrix = new ArrayList<>();
		List<Object> sortedVertices = new ArrayList<>(new TreeSet<>(graph.vertexSet()));
		for (Object v1 : sortedVertices) {
			int[] line = new int[sortedVertices.size()];
			Set<E> outgoingEdges = graph.outgoingEdgesOf((V) v1);
			for (Object e : outgoingEdges) {
				V edgeTarget = (V) graph.getEdgeTarget(e);

				// problem with JGraphT implementation:
				// edges are treated as they should be in
				// an undirected graph. But depending on
				// how they were added, getEdgeTarget(e)
				// actually returns the sourceNode, as
				// this is how it is stored in JGraphT.
				if (edgeTarget.equals(v1)) {
					edgeTarget = (V) graph.getEdgeSource(e);
				}
				int index = sortedVertices.indexOf(edgeTarget);
				line[index] = 1;
			}
			matrix.add(line);
		}
		return matrix;
	}
}

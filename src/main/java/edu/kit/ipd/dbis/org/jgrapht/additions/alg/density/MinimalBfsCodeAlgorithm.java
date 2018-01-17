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
			// BfsCode localCode = this.getLocalBfsCode(graph, (V) v);
			// TODO: create Profile object and add to graph
			//if (localCode.compareTo(bestCode) < 0) {
			//	bestCode = localCode;
			//}
		}


		return bestCode;
	}

	/**
	 * Determine the BFS-Code starting
	 * from the specified start node.
	 * @param startNode
	 * @return
	 */
	public BfsCode getLocalBfsCode(PropertyGraph graph, V startNode, int nodeCounter, Map<V,Integer> bfsIds) {
		nodeCounter++;
		BfsCode target = new BfsCodeImpl(new int[0]);
		if (nodeCounter >= graph.vertexSet().size()) {
			return new BfsCodeImpl(calculateBFS(graph, bfsIds));
		}
		List<V> adjacentNodes = new ArrayList<>();
		for (Object e : graph.outgoingEdgesOf(startNode)) {
			if (!bfsIds.containsValue(graph.getEdgeTarget(e))) {
				adjacentNodes.add((V) graph.getEdgeTarget(e));
			}
		}

		List<List<V>> bestPermutations = new ArrayList<>();
		int[] bestCode = new int[3 * graph.edgeSet().size()];
		for (int i : bestCode) {
			i = Integer.MAX_VALUE;
		}
		//calculate bfs code for the subgraph of adjacentNodes + nodes from bfsIds;
		for (List<V> perm : getPermutations(adjacentNodes)) {
			//new map for subgraph
			Map<V, Integer> subgraphMap = new HashMap<>();
			subgraphMap.putAll(bfsIds);
			for(int i = 0; i < perm.size(); i++) {
				subgraphMap.put(perm.get(i), i + 1 + nodeCounter);
			}
			int[] bfsCode = calculateBFS(graph, subgraphMap);
			if (compareLocal(bfsCode, bestCode) == 1) {
				bestCode = bfsCode;
				bestPermutations.clear();
				bestPermutations.add(perm);
			} else if (compareLocal(bfsCode, bestCode) == 0) {
				bestPermutations.add(perm);
			}
		}

		for (List<V> bestperm : bestPermutations) {
			Map<V, Integer> subgraphMap = new HashMap<>();
			subgraphMap.putAll(bfsIds);
			for(int i = 0; i < bestperm.size(); i++) {
				subgraphMap.put(bestperm.get(i), i + 1 + nodeCounter);
			}
			if (bfsIds.size() >= graph.vertexSet().size()) {
				return new BfsCodeImpl(calculateBFS(graph, subgraphMap));
			}

			target = getLocalBfsCode(graph, bestperm.get(0), nodeCounter,subgraphMap);
		}

		return target;
	}

	private int compareLocal(int[] bfs1, int[] bfs2) {
		for (int i = 0; i < Math.min(bfs1.length, bfs2.length); i++) {
			if (bfs1[i] < bfs2[i]) {
				return -1;
			} else if (bfs1[i] > bfs2[i]) {
				return 1;
			}
		}

		return Integer.compare(bfs1.length, bfs2.length);
	}


	private int[] calculateBFS(PropertyGraph graph, Map<V,Integer> bfsIds) {
		return null;
	}

	private Set<List<V>> getPermutations(List<V> list) {
		return null;
	}

	private int[][] getAdjacencyMatrix(PropertyGraph graph) {
		return null;
	}
}

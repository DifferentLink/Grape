package edu.kit.ipd.dbis.org.jgrapht.additions.alg.density;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
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
		for (int i = 1; i < worstCode.length; i++) {
			worstCode[i] = graph.vertexSet().size() + 1;
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
	 * @return the local bfs code
	 */
	public BfsCodeImpl getLocalBfsCode(PropertyGraph graph, V startNode) {
		int nodeCnt = 0; //Von welchem Knoten berechnet man grade Permutation (index im perm Array)
		Map<Integer,V> bfsIds = new HashMap<>();
		bfsIds.put(1, startNode);
		Set<V> startNodes = new HashSet<>();
		startNodes.add(startNode);

		List<V[]> startPermutations = new LinkedList<>();
		Object[] start = {startNode};
		startPermutations.add((V[]) start);

		List<V[]> bestPermutationsEnd = new ArrayList<>();


		while(startPermutations.get(0).length < graph.vertexSet().size()) {
			List<V[]> nextPerms = new LinkedList<>();
			for(V[] perm : startPermutations) {

				ArrayList<V> adjacentNotCheckedNodes = new ArrayList<>();
				//computes the adjacent nodes that are not checked yet

				for (Object e : graph.outgoingEdgesOf(perm[nodeCnt])) {
					Object adjacentNode1 = graph.getEdgeTarget(e);
					Object adjacentNode2 = graph.getEdgeSource(e);
					if (adjacentNode1 != perm[nodeCnt]) {
						boolean checked = false;
						for (int i = 0; i < perm.length; i++) {
							if (perm[i] == adjacentNode1) {
								checked = true;
							}
						}
						if (!checked) {
							adjacentNotCheckedNodes.add((V) adjacentNode1);
						}
					} else {
						boolean checked = false;
						for (int i = 0; i < perm.length; i++) {
							if (perm[i] == adjacentNode1) {
								checked = true;
							}
						}
						if (!checked) {
							adjacentNotCheckedNodes.add((V) adjacentNode2);
						}
					}
				}
				List<V[]> bestPermutations = new ArrayList<>();

				int[] bestCode = new int[3 * graph.edgeSet().size()];
				for (int i = 0; i < bestCode.length; i++) {
					bestCode[i] = graph.vertexSet().size();
				}
				//calculate bfs code for the subgraph of adjacentNodes + nodes from bfsIds;

				Set<V[]> allPermutations = getPermutations(adjacentNotCheckedNodes);
				for (V[] p : allPermutations) {
					Object[] completePerm = new Object[perm.length + p.length];
					for (int i = 0; i < completePerm.length; i++) {
						if (i < perm.length) {
							completePerm[i] = perm[i];
						} else {
							completePerm[i] = p[i - perm.length];
						}
					}
					int[] bfsCode = calculateBFS(graph, (V[]) completePerm, perm.length + 1); //calculate BFS code



					if (compareLocal(bfsCode, bestCode) == 1) {
						bestCode = bfsCode;
						bestPermutations.clear();
						bestPermutations.add((V[]) completePerm);
					} else if (compareLocal(bfsCode, bestCode) == 0) {
						bestPermutations.add((V[]) completePerm);
					}
				}
				for (V[] p : bestPermutations) {
					nextPerms.add(p);
				}

				//bestPermutationsEnd vergleichen mit bestPermutations
				if (bestPermutationsEnd.size() == 0) {
					bestPermutationsEnd = bestPermutations;
				} else if(bestPermutations.size() > 0) {
					if (compareLocal(calculateBFS(graph, bestPermutationsEnd.get(0), 1), calculateBFS(graph, bestPermutations.get(0), 1)) <= 0) {
						bestPermutationsEnd = bestPermutations;
					}
				}
			}
			startPermutations = nextPerms;
			nodeCnt++;
		}
		return new BfsCodeImpl(calculateBFS(graph, bestPermutationsEnd.get(0), 1));
	}


	public int compareLocal(int[] bfs1, int[] bfs2) {
		for (int i = 0; i < Math.min(bfs1.length, bfs2.length); i++) {
			if (bfs1[i] < bfs2[i]) {
				return 1;
			} else if (bfs1[i] > bfs2[i]) {
				return -1;
			}
		}
		return Integer.compare(bfs1.length, bfs2.length);
	}

	/**
	 *
	 * @param graph the graph
	 * @param permutation the permutation for the bfs code
	 * @param index start of Bfs -> if index = 4 -> starts with edges 14,24,34,15,...
	 * @return
	 */
	public int[] calculateBFS(PropertyGraph graph, V[] permutation, int index) {

		if (index < 0) {
			throw new IllegalArgumentException();
		} else if (index >= graph.vertexSet().size()) {
			return new int[0];
		}
		int cnt = 0;
		int vertexSetSize = permutation.length;
		int[] result = new int[graph.edgeSet().size() * 3];
		for (int i = index; i <= vertexSetSize; i++) {
			boolean backward = false;
			for (int j = 1; j < i; j++) {
				if (graph.containsEdge(permutation[i - 1], permutation[j - 1]) ||
						graph.containsEdge(permutation[j - 1], permutation[i - 1])) {
					if(backward) {
						result[cnt] = -1;
					} else {
						result[cnt] = 1;
						backward = true;
					}
					result[cnt + 1] = j;
					result[cnt + 2] = i;
					cnt = cnt + 3;
				}
			}
		}
		int[] endResult = new int[cnt];
		for (int i = 0; i < endResult.length; i++) {
			endResult[i] = result[i];
		}
		return endResult;
	}

	/**
	 * calculates all permutations from the list
	 * @param list
	 * @return a set of all permutations
	 */
	public Set<V[]> getPermutations(ArrayList<V> list) {
		Set<V[]> result = new HashSet<>();
		if (list.size() == 0) {
			return result;
		}
		int size = list.size();
		//Heap algorithm
		Object[] A = new Object[size]; //Array of any
		for (int i = 0; i < size; i++) {
			A[i] = list.get(i);
		}

		int n = A.length;

		int[] c = new int[n];
		for (int i = 0; i < n; i++) {
			c[i] = 0;
		}
		result.add((V[]) A.clone());

		int i = 0;
		while (i < n) {
			if (c[i] < i) {
				if ((i % 2) == 0) {
					//swap(A[0], A[i])
					Object a = A[0];
					A[0] = A[i];
					A[i] = a;
				} else {
					//swap(A[c[i]], A[i])
					Object a = A[c[i]];
					A[c[i]] = A[i];
					A[i] = a;
				}
				result.add((V[]) A.clone());
				c[i] = c[i] + 1;
				i = 0;
			} else {
				c[i] = 0;
				i += 1;
			}
		}
		return result;
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

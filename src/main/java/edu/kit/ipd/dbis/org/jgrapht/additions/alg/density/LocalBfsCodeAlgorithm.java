package edu.kit.ipd.dbis.org.jgrapht.additions.alg.density;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.util.*;

/**
 * The local bfs code algorithm.
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 */
public class LocalBfsCodeAlgorithm<V, E> implements BfsCodeAlgorithm {
	/**
	 * The start vertex
	 */
	protected final V startNode;

	/**
	 * Construct a new minimal profile algorithm.
	 *
	 * @param startNode the start vertex
	 */
	public LocalBfsCodeAlgorithm(V startNode) {
		this.startNode = startNode;
	}

	@Override
	public BfsCode getBfsCode(PropertyGraph graph) {

		int nodeCnt = 0; //the actual nod from which the algorithm searches permutations
		List<V[]> startPermutations = new LinkedList<>(); //actual permutations with best bfs code
		Object[] start = {this.startNode};
		startPermutations.add((V[]) start);

		while (startPermutations.get(0).length < graph.vertexSet().size()) {
			List<V[]> nextPerms = new LinkedList<>(); //actual best permutations for this loop
			for (V[] perm : startPermutations) {
				ArrayList<V> adjacentNotCheckedNodes = new ArrayList<>();

				//computes the adjacent nodes that are not checked yet
				for (Object e : graph.outgoingEdgesOf(perm[nodeCnt])) {
					Object adjacentNode1 = graph.getEdgeTarget(e);
					Object adjacentNode2 = graph.getEdgeSource(e);
					if (adjacentNode1 != perm[nodeCnt]) {
						boolean checked = false;
						for (V v : perm) {
							if (v == adjacentNode1) {
								checked = true;
							}
						}
						if (!checked) {
							adjacentNotCheckedNodes.add((V) adjacentNode1);
						}
					} else {
						boolean checked = false;
						for (V v : perm) {
							if (v == adjacentNode2) {
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

				Set<V[]> allPermutations = getPermutations(adjacentNotCheckedNodes);
				//calculate bfs code for the subgraph of adjacent nodes + previous nodes;
				for (V[] p : allPermutations) {
					Object[] completePerm = new Object[perm.length + p.length];
					for (int i = 0; i < completePerm.length; i++) {
						if (i < perm.length) {
							completePerm[i] = perm[i];
						} else {
							completePerm[i] = p[i - perm.length];
						}
					}
					int[] bfsCode = calculateBFS(graph, (V[]) completePerm, perm.length); //calculate BFS code
					if (compareLocal(bfsCode, bestCode) == 1) {
						bestCode = bfsCode;
						bestPermutations.clear();
						bestPermutations.add((V[]) completePerm);
					} else if (compareLocal(bfsCode, bestCode) == 0) {
						bestPermutations.add((V[]) completePerm);
					}
				}
				//Set the nextPerms
				if (bestPermutations.size() > 0) {
					if (nextPerms.size() == 0) {
						nextPerms.addAll(bestPermutations);
					} else {
						int comp = compareLocal(calculateBFS(graph, nextPerms.get(0), 1), calculateBFS(graph,
								bestPermutations.get(0), 1));
						if (comp < 0) {
							nextPerms.clear();
							nextPerms.addAll(bestPermutations);
						} else if (comp == 0) {
							nextPerms.addAll(bestPermutations);
						}
					}
				}
			}
			if (nextPerms.size() > 0) {
				startPermutations = nextPerms;
			}
			nodeCnt++;
		}

		HashMap<Integer, V> bfsMap = new HashMap<>();
		for (int i = 0; i < startPermutations.get(0).length; i++) {
			bfsMap.put(i + 1, startPermutations.get(0)[i]);
		}
		return new BfsCodeImpl(calculateBFS(graph, startPermutations.get(0), 1), bfsMap);
	}

	/**
	 * compares two bfs arrays for the algorithm
	 * @param bfs1 first array
	 * @param bfs2 second array
	 * @return -1, 0, 1 if bfs1 is worse than, equal to, or better than bfs2.
	 */
	private int compareLocal(int[] bfs1, int[] bfs2) {
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
	 * @return the bfs array of the permutation on the graph
	 */
	private int[] calculateBFS(PropertyGraph graph, V[] permutation, int index) {

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
				if (graph.containsEdge(permutation[i - 1], permutation[j - 1])
						|| graph.containsEdge(permutation[j - 1], permutation[i - 1])) {
					if (backward) {
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
		int[] endResult = result.clone();
		return endResult;
	}

	/**
	 * calculates all permutations from the list
	 * @param list list of nodes
	 * @return a set of all permutations
	 */
	private Set<V[]> getPermutations(ArrayList<V> list) {
		Set<V[]> result = new HashSet<>();
		if (list.size() == 0) {
			return result;
		}
		int size = list.size();
		//Heap algorithm
		Object[] array = new Object[size]; //Array of any
		for (int i = 0; i < size; i++) {
			array[i] = list.get(i);
		}

		int n = array.length;

		int[] c = new int[n];
		for (int i = 0; i < n; i++) {
			c[i] = 0;
		}

		result.add((V[]) array.clone());

		int i = 0;
		while (i < n) {
			if (result.size() >= Integer.MAX_VALUE - 4) {
				throw new IllegalArgumentException("Too many permutations");
			}
			if (c[i] < i) {
				if ((i % 2) == 0) {
					//swap(A[0], A[i])
					Object a = array[0];
					array[0] = array[i];
					array[i] = a;
				} else {
					//swap(A[c[i]], A[i])
					Object a = array[c[i]];
					array[c[i]] = array[i];
					array[i] = a;
				}

				result.add((V[]) array.clone());

				c[i] = c[i] + 1;
				i = 0;
			} else {
				c[i] = 0;
				i += 1;
			}
		}
		return result;
	}
}


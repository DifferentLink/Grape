package edu.kit.ipd.dbis.org.jgrapht.additions.alg.kkgraph;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.KkGraphAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.BfsCode;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.VertexColoring;
import org.jgrapht.alg.clique.BronKerboschCliqueFinder;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;

import java.util.*;

/**
 *The kk-graph generator. It generates the kk-graph for the input graph depending on the Hadwiger Conjecture.
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 */
public class KkGraphGenerator<V, E> implements KkGraphAlgorithm {
	/**
	 * The input graph
	 */
	protected final PropertyGraph<V, E> graph;

	/**
	 * Construct a new proportion density algorithm.
	 *
	 * @param graph the input graph
	 */
	public KkGraphGenerator(PropertyGraph<V, E> graph) {
		this.graph = Objects.requireNonNull(graph, "Graph cannot be null");
	}

	@Override
	public KkGraph getKkGraph() {
		VertexColoringAlgorithm.ColoringImpl vertexColoring = (VertexColoringAlgorithm.ColoringImpl)
				((List<VertexColoringAlgorithm.Coloring>) graph.getProperty(VertexColoring.class).getValue()).get(0);
		int numberOfColors = vertexColoring.getNumberColors();

		//this is the number of edges that have to be contract to get the kk graph
		int numberOfContractEdges = graph.vertexSet().size() - numberOfColors;
		int maxNumberOfContractEdges = graph.vertexSet().size() - numberOfColors;

		boolean found = false;
		Map<V, Integer> graphMap = new HashMap<>();
		for (int j = 0; j <= maxNumberOfContractEdges; j++) {
			//allocates the endComb and the actualComb f.e 5 Edges, numberOfContractEdges = 3 -> actualComb = (1,1,1,0,0)
			//endComb = (0,0,1,1,1)
			int[] actualComb = new int[graph.edgeSet().size()];
			int[] endComb = new int[graph.edgeSet().size()];
			for (int i = 0; i < actualComb.length; i++) {
				if (i < j) {
					actualComb[i] = 1;
				} else {
					actualComb[i] = 0;
				}
				if (i < endComb.length - numberOfContractEdges) {
					endComb[i] = 0;
				} else {
					endComb[i] = 1;
				}
			}

			BfsCodeAlgorithm.BfsCodeImpl bfsCode =
					(BfsCodeAlgorithm.BfsCodeImpl) graph.getProperty(BfsCode.class).getValue();
			int[] bfsArray = bfsCode.getCode();
			ArrayList<int[]> edges = this.getEdgeList(bfsArray);

			Map<Integer, V> numberMap = bfsCode.getNumberMap();
			boolean allCombsDone = false;

			graphMap = new HashMap<>();
			PropertyGraph g = new PropertyGraph();
			Collection<Integer> values;
			Set<Integer> valueSet;

			//tries all different possibilities of edges until the kk graph gets found
			while (!found && !allCombsDone) {
				//reset graphMap for next combination
				graphMap.clear();
				Set<Integer> keySet = numberMap.keySet();
				for (int i : keySet) {
					graphMap.put(numberMap.get(i), i);
				}

				//contract all edges from this combination
				for (int i = 0; i < actualComb.length; i++) {
					if (actualComb[i] == 1) {
						contractEdge(graphMap, numberMap.get(edges.get(i)[0]), numberMap.get(edges.get(i)[1]));
					}
				}

				values = graphMap.values();
				valueSet = new HashSet<>();
				//changes map into graph
				valueSet.addAll(values);

				for (int v : valueSet) {
					g.addVertex(v);
				}
				for (int i : valueSet) {
					for (Object v : graph.vertexSet()) {
						if (graphMap.get(v) == i) {
							for (Object w : graph.vertexSet()) {
								if (graph.containsEdge(v, w) && (graphMap.get(v) != graphMap.get(w))) {
									g.addEdge(graphMap.get(w), graphMap.get(v));
								}
							}
						}
					}
				}


				if (hasCliqueOfSize(g, numberOfColors)) {
					found = true;
				}
				if (sameComb(actualComb, endComb)) {
					allCombsDone = true;
				}
				if (!found) {
					actualComb = getNextEdgeCombination(actualComb);
				}

			}

			if (found) {
				Set<Object> vertieces = getCliqueOfSize(g, numberOfColors);
				Map<V, Integer> subgraphs = new HashMap<>();
				for (Object o : vertieces) {
					for (Object v : graph.vertexSet()) {
						if (graphMap.get(v) == o) {
							subgraphs.put((V) v, (Integer) o);
						}
					}
				}

				return new KkGraphImpl(subgraphs, vertieces.size());
			}
		}
		//kkgraph not found
		return new KkGraphImpl(new HashMap<V, Integer>(), 0);
	}

	/**
	 * contracts the edge from firstNode to secondNode
	 *
	 * @param graphMap input graph map
	 * @param firstNode the start vertex of the edge
	 * @param secondNode the end vertex of the edge
	 */
	private void contractEdge(Map<V, Integer> graphMap, V firstNode, V secondNode) {
		if (!(graphMap.containsKey(firstNode) && graphMap.containsKey(secondNode))) {
			throw new IllegalArgumentException("map does not contains the nodes");
		}
		int firstValue = graphMap.get(firstNode);
		int secondValue = graphMap.get(secondNode);
		int minValue = Math.min(firstValue, secondValue);

		for (V v : graphMap.keySet()) {
			if (graphMap.get(v) == firstValue || graphMap.get(v) == secondValue) {
				graphMap.replace(v, minValue);
			}
		}
	}

	/**
	 * calculates the next edge combination of a given combination
	 * @param prevComb the previous combination
	 * @return the next combination
	 */
	private int[] getNextEdgeCombination(int[] prevComb) {
		for (int i : prevComb) {
			if (!(i == 1 || i == 0)) {
				throw new IllegalArgumentException("no valid comb");
			}
		}
		int[] result = new int[prevComb.length];

		boolean found = false;
		int onesBeforeMoveableOne = 0;
		if (prevComb[prevComb.length - 1] == 1) {
			onesBeforeMoveableOne++;
		}
		int moveableOneIndex = 0;
		int i = prevComb.length - 2;
		while (!found && i > 0) {
			if (prevComb[i] == 1 && prevComb[i + 1] == 0) {
				found = true;
				moveableOneIndex = i;
			} else if (prevComb[i] == 1) {
				onesBeforeMoveableOne++;
			}
			i--;
		}
		for (int j = 0; j < moveableOneIndex; j++) {
			result[j] = prevComb[j];
		}
		result[moveableOneIndex] = 0;
		result[moveableOneIndex + 1] = 1;
		if (moveableOneIndex + 2 < result.length) {
			for (int j = moveableOneIndex + 2; j < result.length; j++) {
				if (onesBeforeMoveableOne > 0) {
					result[j] = 1;
				} else {
					result[j] = 0;
				}
				onesBeforeMoveableOne--;
			}
		}
		return result;
	}

	/**
	 * checks if first and second are the same
	 * @param first first comb
	 * @param second second comb
	 * @return if its the same comb
	 */
	private boolean sameComb(int[] first, int[] second) {
		for (int i = 0; i < Math.min(first.length, second.length); i++) {
			if (first[i] != second[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * get a list of edges
	 * @param bfsArray the bfsArray of the graph
	 * @return the list of edges represented as array of two vertices
	 */
	private ArrayList<int[]> getEdgeList(int[] bfsArray) {
		ArrayList<int[]> result = new ArrayList();
		for (int i = 0; i < graph.edgeSet().size(); i++) {
			int[] edge = new int[2];
			edge[0] = bfsArray[3 * i + 1];
			edge[1] = bfsArray[3 * i + 2];
			result.add(edge);
		}
		return result;
	}

	/**
	 * checks if the graph represented by the combMap is a clique
	 * @param g the graph
	 * @return if its a clique
	 */
	private boolean hasCliqueOfSize(PropertyGraph g, int size) {
		ArrayList<Set<Object>> cliques = new ArrayList<>();
		BronKerboschCliqueFinder alg = new BronKerboschCliqueFinder(g);
		Iterator<Set<Object>> it = alg.iterator();
		while (it.hasNext()) {
			Set<Object> clique = it.next();
			if (clique.size() == size) {
				return true;
			}
		}
		return false;
	}

	/**
	 * checks if the graph represented by the combMap is a clique
	 * @param g the graph
	 * @return if its a clique
	 */
	private Set<Object> getCliqueOfSize(PropertyGraph g, int size) {
		ArrayList<Set<Object>> cliques = new ArrayList<>();
		BronKerboschCliqueFinder alg = new BronKerboschCliqueFinder(g);
		Iterator<Set<Object>> it = alg.iterator();
		while (it.hasNext()) {
			Set<Object> clique = it.next();
			if (clique.size() == size) {
				return clique;
			}
		}
		return null;
	}
}

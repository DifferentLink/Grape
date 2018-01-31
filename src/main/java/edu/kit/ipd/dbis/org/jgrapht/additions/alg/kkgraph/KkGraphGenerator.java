package edu.kit.ipd.dbis.org.jgrapht.additions.alg.kkgraph;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.KkGraphAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.BfsCode;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.VertexColoring;
import org.jgrapht.VertexFactory;
import org.jgrapht.alg.clique.BronKerboschCliqueFinder;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;
import org.jgrapht.graph.ClassBasedVertexFactory;

import java.lang.reflect.Array;
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
				graph.getProperty(VertexColoring.class).getValue();
		System.out.println("numberColors" + vertexColoring.getNumberColors());
		//this is the number of edges that have to be contract to get the kk graph
		int numberOfContractEdges = graph.vertexSet().size() - vertexColoring.getNumberColors();

		//allocates the endComb and the actualComb f.e 5 Edges, numberOfContractEdges = 3 -> actualComb = (1,1,1,0,0)
		//endComb = (0,0,1,1,1)
		int[] actualComb = new int[graph.edgeSet().size()];
		int[] endComb = new int[graph.edgeSet().size()];
		for (int i = 0; i < actualComb.length; i++) {
			if (i < numberOfContractEdges) {
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

		BfsCodeAlgorithm.BfsCodeImpl bfsCode = (BfsCodeAlgorithm.BfsCodeImpl) graph.getProperty(BfsCode.class).getValue();
		int[] bfsArray = bfsCode.getCode();
		ArrayList<int[]> edges = this.getEdgeList(bfsArray);

		PropertyGraph graphCopy;
		Map<Integer, V> numberMap = bfsCode.getNumberMap();
		boolean found = false;
		boolean allCombsDone = false;

		/*
		a 1
		b 2
		c 3
		d 4
		e 5
		 */
		Map<V, Integer> graphMap = new HashMap<>();
		Set<Integer> keySet = numberMap.keySet();
		for (int i : keySet) {
			graphMap.put(numberMap.get(i), i);
		}

		//tries all different possibilities of edges until the kk graph gets found
		while (!found && !allCombsDone) {
			graphCopy = graph.clone();
			//contract all edges from this combination
			for (int i = 0; i < actualComb.length; i++) {
				if (actualComb[i] == 1) {
					contractEdge(graphMap, numberMap.get(edges.get(i)[0]), numberMap.get(edges.get(i)[1]));
				}
			}

			if (isClique(graphMap)) {
				found = true;
			}
			if (sameComb(actualComb, endComb)) {
				allCombsDone = true;
			}
			if (!found) {
				actualComb = getNextEdgeCombination(actualComb);
			}
		}
		//kk graph not found
		if (found = false) {
			return null;
		}
		Collection<Integer> values = graphMap.values();
		Set<Integer> v = new HashSet<>();
		for (int i : values) {
			v.add(i);
		}
		return new KkGraphImpl(graphMap, v.size());



		/**
		//tries all different possibilities of edges until the kk graph gets found
		while (!found && !allCombsDone) {
			graphCopy = graph.clone();
			//contract all edges from this combination
			for (int i = 0; i < actualComb.length; i++) {
				if (actualComb[i] == 1) {
					contractEdge(graphCopy, numberMap.get(edges.get(i)[0]), numberMap.get(edges.get(i)[1]));
				}
			}

			if (isClique(graphCopy)) {
				found = true;
			}
			if (sameComb(actualComb, endComb)) {
				allCombsDone = true;
			}
			if (!found) {
				actualComb = getNextEdgeCombination(actualComb);
			}
		}
		//kk graph not found
		if (found = false) {
			return null;
		}
		//getKkGraphMap
		int cnt = 0;
		Map<V, Integer> result = new HashMap<>();
		for (int i = 0; i < actualComb.length; i++) {
			if (actualComb[i] == 1) {
				V first = numberMap.get(edges.get(i)[0]);
				V second = numberMap.get(edges.get(i)[1]);
				if (result.keySet().contains(first) && result.keySet().contains(second)) {
					int valueFirst = result.get(first);
					int valueSecond = result.get(first);
					for (V v : result.keySet()) {
						if (result.get(v) == valueFirst) {
							result.replace(v, valueSecond);
						}
					}
				}else if (result.keySet().contains(first)) {
					int v = result.get(first);
					result.put(second, v);
				} else if (result.keySet().contains(second)) {
					int v = result.get(second);
					result.put(first, v);
				} else {
					result.put(first, cnt);
					result.put(second, cnt);
					cnt++;
				}
			}
		}
		Collection<Integer> values = result.values();
		Set<Integer> v = new HashSet<>();
		for (int i : values) {
			v.add(i);
		}
		return new KkGraphImpl(result, v.size());
		 */
	}


	/**
	 * contracs the edge
	 *
	 * @param graph input graph
	 * @param firstNode the start vertex of the edge
	 * @param secondNode the end vertex of the edge
	 */
	/*
	private void contractEdge(PropertyGraph graph, V firstNode, V secondNode) {
		VertexFactory<V> vertexFactory = new ClassBasedVertexFactory(firstNode.getClass());
		V newNode = vertexFactory.createVertex();
		graph.removeAllEdges(firstNode, secondNode);
		graph.addVertex(newNode);
		for (Object v : graph.vertexSet()) {
			if (graph.containsEdge(firstNode, v) || graph.containsEdge(secondNode, v)) {
				graph.addEdge(newNode, v);
			}
		}
		graph.removeVertex(firstNode);
		graph.removeVertex(secondNode);
		//TODO: implement me;
	}
	*/

	/**
	 * contracs the edge
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
	public int[] getNextEdgeCombination(int[] prevComb) {
		for (int i = 0; i < prevComb.length; i++) {
			if (!(prevComb[i] == 1 || prevComb[i] == 0)) {
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
		while(!found && i > 0) {
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
	 * checks if it is the same comb
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
	 * @param graphMap the graph
	 * @return if its a clique
	 */
	private boolean isClique(Map<V, Integer> graphMap) {

		PropertyGraph g = new PropertyGraph();
		Collection<Integer> values = graphMap.values();
		Set<Integer> valueSet = new HashSet<>();
		//changes map into graph
		for (int i : values) {
			valueSet.add(i);
		}
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

		ArrayList<Set<Object>> cliques = new ArrayList<>();
		BronKerboschCliqueFinder alg = new BronKerboschCliqueFinder(g);
		Iterator<Set<Object>> it = alg.iterator();
		while (it.hasNext()) {
			Set<Object> clique = it.next();
			cliques.add(clique);
		}
		if (cliques.size() == 1) {
			return true;
		}
		return false;
	}
}

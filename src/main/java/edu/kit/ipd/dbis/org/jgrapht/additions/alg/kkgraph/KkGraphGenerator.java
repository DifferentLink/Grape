package edu.kit.ipd.dbis.org.jgrapht.additions.alg.kkgraph;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.KkGraphAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.VertexColoring;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;

import java.util.Objects;

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
		int numberOfContractEdges = graph.vertexSet().size() - vertexColoring.getNumberColors();



		//TODO: implement me (empty implementation for test in PropertyGraphTest)
		return new KkGraphImpl(null, 2);
	}

	private void contractEdge(PropertyGraph graph, V firstNde, V secondNode) {
	}


	/**
	 * calculates the next combination of a combination
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
}

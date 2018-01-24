package edu.kit.ipd.dbis.org.jgrapht.additions.alg.color;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.NumberOfVertices;
import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;

import java.util.*;

/**
 * The minimal vertex coloring algorithm.
 *
 * <p>
 * Description of the algorithm
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 *
 */
public class MinimalVertexColoring<V, E> implements VertexColoringAlgorithm<V> {
	/**
	 * The input graph
	 */
	protected final PropertyGraph<V, E> graph;

	/**
	 * Construct a new coloring algorithm.
	 *
	 * @param graph the input graph
	 */
	public MinimalVertexColoring(PropertyGraph<V, E> graph) {
		this.graph = Objects.requireNonNull(graph, "Graph cannot be null");
	}

	public List<Coloring<V>> getColorings() {
		return null;
	}

	@Override
	public Coloring<V> getColoring() {
		int numberOfVertices = (int) this.graph.getProperty(NumberOfVertices.class).getValue();
		// give vertices an order
		ArrayList<V> sortedVertices = new ArrayList<>(new TreeSet<V>(this.graph.vertexSet()));
		Integer[] colors = new Integer[numberOfVertices];
		for (int i = 0; i < colors.length; i++) {
			colors[i] = 0;
		}

		if (numberOfVertices == 1) {
			return createColoringObject(new Integer[]{0}, sortedVertices);
		} else {
			int index = colors.length - 1;
			int numberOfColors = 2;
			for (int i = 0; numberOfColors < numberOfVertices; i++) {

				// this ensures that for every number of different colors,
				// all combinations are tried.
				if (index == 0 && colors[numberOfColors - 1] != numberOfColors - 1) {
					// this case:
					// 01123
					// what we need:
					// 01223
					// 01233
					// => we need to iterate over the other
					//    possible distributions of the colors.
					int tmp = numberOfColors;

					// increment starting from the smallest element (> 0) that
					// has the highest index (while ignoring first and last element).
					for (int j = 1; j < colors.length - 1; j++) {
						if (colors[j] <= colors[tmp] && colors[j] != j) {
							tmp = j;
						}
					}
					index = tmp;
				} else if (index == 0 || numberOfColors == colors.length - 1) {
					// this case:
					// 01233
					// what we need:
					// 01234
					// => we need to add a color and return colors to its
					// 	  beginning state.
					int tmp = numberOfColors;
					for (int j = colors.length - 1; j > 0; j--) {
						if (tmp > 0) {
							colors[j] = tmp;
							tmp--;
						} else {
							colors[j] = 0;
						}
					}
					numberOfColors++;
					index = colors.length - numberOfColors;
				} else if (colors[index] == index) {
					index = 0;
				} else {
					colors[index]++;
					index--;
				}

				Integer[] colorCopy2 = new Integer[colors.length];
				Integer[] colorCopy = new Integer[colors.length];
				for (int j = 0; j < colors.length; j++) {
					colorCopy[j] = colors[j];
					colorCopy2[j] = colors[j];
				}
				Arrays.sort(colorCopy, Collections.reverseOrder());

				// get all permutations
				while (!Arrays.equals(colorCopy, colors)) {
					colors = getNextPermutation(colors);
					Coloring coloring = createColoringObject(colors, sortedVertices);
					if (isValidVertexColoring(coloring, graph)) {
						return coloring;
					}
				}
				colors = colorCopy2;
			}
		}
		return null;
	}

	private Integer[] getNextPermutation(Integer[] ascendingArray) {
		for (int i = ascendingArray.length - 1; i > 0; i--) {
			if (ascendingArray[i - 1] < ascendingArray[i]) {
				// find last element which does not exceed ascendingArray[i-1]
				int s = ascendingArray.length - 1;
				while (ascendingArray[i - 1] >= ascendingArray[s]) {
					s--;
				}
				swap(ascendingArray, i - 1, s);
				// reverse order of elements
				for (int j = i, k = ascendingArray.length - 1; j < k; j++, k--) {
					swap(ascendingArray, j, k);
				}
				break;
			}
		}
		return ascendingArray;
	}

	private void swap(Integer[] array, int a, int b) {
		int tmp = array[a];
		array[a] = array[b];
		array[b] = tmp;
	}

	private Coloring createColoringObject(Integer[] coloring, List<V> sortedNodes) {
		Map<V, Integer> colors = new HashMap<>();
		Set<Integer> differentColors = new HashSet<>();
		for (int j = 0; j < coloring.length; j++) {
			colors.put(sortedNodes.get(j), coloring[j]);
			differentColors.add(coloring[j]);
		}
		return new ColoringImpl(colors, differentColors.size());
	}

	/**
	 * Checks if a coloring is valid
	 *
	 * @param coloring the coloring
	 * @param graph    the graph
	 * @return true if valid, false if invalid
	 */
	protected boolean isValidVertexColoring(Coloring<V> coloring, Graph<V, E> graph) {
		for (Set<V> colorClass : coloring.getColorClasses()) {
			for (V v : colorClass) {
				TreeSet<V> collect = new TreeSet<>();
				graph.outgoingEdgesOf(v).forEach(e -> {
					V edgeTarget = graph.getEdgeTarget(e);
					if (!edgeTarget.equals(v)) {
						collect.add(edgeTarget);
					} else {
						collect.add(graph.getEdgeSource(e));
					}
				});
				for (V v1 : collect) {
					if (colorClass.contains(v1)) {
						return false;
					}
				}
			}
		}
		return true;
	}
}

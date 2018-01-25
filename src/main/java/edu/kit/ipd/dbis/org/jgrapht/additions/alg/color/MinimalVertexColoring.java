package edu.kit.ipd.dbis.org.jgrapht.additions.alg.color;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.NumberOfVertices;
import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;

import java.util.*;

/**
 * A brute force implementation which works by calculating
 * all integer partitions for a number of colors, transforming
 * the result into a color distribution (e.g. for 2 colors:
 * vertex 1 has color 0, vertex 2 color 0,...,vertex n color 1),
 * and determining all possible permutations (while ignoring repeated
 * values) of this distribution. For each one, the algorithm checks
 * if it represents a valid vertex coloring.
 * This process guarantees that no isomorphic colorings are checked.
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
		// initialize colors
		for (int i = 0; i < colors.length; i++) {
			colors[i] = 0;
		}

		if (numberOfVertices == 1) {
			// trivial case
			return createColoringObject(new Integer[]{0}, sortedVertices);
		}

		// get integer partitions
		List<int[]> partitions = this.integerPartitioning(numberOfVertices);
		Iterator it = partitions.iterator();

		// iterate over partitions
		while (it.hasNext()) {
			// partitions have the following format:
			// (e.g. for 8 vertices and 2 numbers):
			// 7 1
			// the algorithm needs this format:
			// 0 0 0 0 0 0 0 1
			colors = this.parseIntegerPartitioning((int[]) it.next(), numberOfVertices);

			// create copy of array
			Integer[] colorCopy = new Integer[colors.length];
			System.arraycopy(colors, 0, colorCopy, 0, colors.length);

			// sort it backwards.
			// when all permutations are checked,
			// the color array is in ascending order.
			// using colorCopy, we can define an end
			// for the while loop below.
			Arrays.sort(colorCopy, Collections.reverseOrder());

			// get all permutations of partitioning
			while (!Arrays.equals(colorCopy, colors)) {
				colors = getNextPermutation(colors);
				Coloring coloring = createColoringObject(colors, sortedVertices);
				if (isValidVertexColoring(coloring, graph)) {
					return coloring;
				}
			}
		}
		return null;
	}

	private Integer[] parseIntegerPartitioning(int[] partitioning, int numberOfVertices) {
		Integer[] result = new Integer[numberOfVertices];
		int index = 0;
		for (int i = 0; i < partitioning.length; i++) {
			for (int j = index; j < result.length; j++) {
				result[j] = i;
			}
			index += partitioning[i];
		}
		return result;
	}

	//TODO: make me iterative
	private List<int[]> integerPartitioning(int numberOfVertices) {
		List<int[]> result = new LinkedList<>();

		// create first partitioning
		int numberOfColors = 2;
		int[] array = new int[numberOfColors];
		array[0] = numberOfVertices - 1;
		array[1] = 1;

		// iterate over all possible partitions
		// for numberOfVertices.
		while (numberOfColors < numberOfVertices) {
			int[] arrayCopy = new int[array.length];
			System.arraycopy(array, 0, arrayCopy, 0, array.length);
			result.add(arrayCopy);

			// check if first element equals last element
			// or if their difference is less than 2.
			// this means that there are no more possible
			// partitions for the numberOfVertices and
			// numberOfColors in this iteration.
			if (array[0] == array[array.length - 1] || array[0] - array[array.length - 1] < 2) {
				// initialize array with one more color.
				array = new int[++numberOfColors];
				int tmp = numberOfVertices;
				for (int j = array.length - 1; j > 0; j--) {
					array[j] = 1;
					tmp--;
				}
				array[0] = tmp;

				// TODO sometimes this creates duplicates.
				// add initial array to the results.
				int[] arrayCopy2 = new int[array.length];
				System.arraycopy(array, 0, arrayCopy2, 0, array.length);
				result.add(arrayCopy2);
			}

			// iterate over array to calculate
			// next distribution.
			for (int j = 1; j < array.length; j++) {
				// check if the difference between
				// the first and the j-th element
				// of the array is bigger than 2.
				// this means that there exists
				// a possible next distribution.
				if (array[0] - array[j] >= 2) {
					// the following section
					// launches increment and
					// decrement commands which
					// flow to the front like
					// air bubbles out of water.
					array[j]++;
					array[j - 1]--;
					j--;
					// if array is not in ascending order,
					// the distribution is not correct yet.
					while (!isInDescendingOrder(array)) {
						if (array[j] >= array[j - 1]) {
							break;
						}
						array[j]++;
						array[j - 1]--;
						j--;
					}
					break;
				}
			}
		}
		return result;
	}

	private boolean isInDescendingOrder(int[] array) {
		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] < array[i + 1]) {
				return false;
			}
		}
		return true;
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

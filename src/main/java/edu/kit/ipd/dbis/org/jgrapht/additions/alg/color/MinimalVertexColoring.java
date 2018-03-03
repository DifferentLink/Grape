package edu.kit.ipd.dbis.org.jgrapht.additions.alg.color;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.LargestCliqueSize;
import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;

import java.util.*;

/**
 * A brute force implementation which works by calculating
 * all integer partitionings for a number of colors, transforming
 * the result into a color distribution (e.g. for 2 colors:
 * vertex 1 has color 0, vertex 2 color 0,...,vertex n color 1),
 * and determining all possible permutations (while ignoring repeated
 * values) of this distribution. For each one, the algorithm checks
 * if it represents a valid vertex coloring.
 * This process guarantees that no equivalent colorings are checked.
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
	private List<Coloring<V>> colorings;
	private int[][] matrix;

	/**
	 * Construct a new coloring algorithm.
	 *
	 * @param graph the input graph
	 */
	public MinimalVertexColoring(PropertyGraph<V, E> graph) {
		this.graph = Objects.requireNonNull(graph, "Graph cannot be null");
		this.colorings = new ArrayList<>();
		this.matrix = graph.getAdjacencyMatrix();
	}

	/**
	 * Determines all minimal vertex colorings.
	 *
	 * @return List of minimal vertex colorings
	 */
	public List<Coloring<V>> getAllColorings() {
		int numberOfVertices = this.graph.vertexSet().size();
		int largestCliqueSize = (int) this.graph.getProperty(LargestCliqueSize.class).getValue();

		// give vertices an order
		ArrayList<V> sortedVertices = new ArrayList<>(new TreeSet<V>(this.graph.vertexSet()));
		int[] colors;

		if (numberOfVertices == 1) {
			//trivial case
			this.colorings.add(createColoringObject(new int[]{0}, sortedVertices));
			return this.colorings;
		}

		int numberOfColors = Integer.MAX_VALUE;
		int[] partitioning = getFirstPartitioning(numberOfVertices, largestCliqueSize);

		// iterate over partitions
		while (partitioning != null) {
			// because different partitionings can have the
			// same length (= number of colors), this is
			// needed in order to determine every isomorphic
			// coloring, and then break.
			if (partitioning.length > numberOfColors) {
				break;
			}

			// partitionings have the following format:
			// (e.g. for 8 vertices and 2 numbers):
			// 7 1
			// the algorithm needs this format:
			// 0 0 0 0 0 0 0 1
			colors = this.parseIntegerPartitioning(partitioning, numberOfVertices);

			// get all permutations of partitioning
			do {
				if (isValidVertexColoring(colors)) {
					// found one coloring of this partitioning.
					this.colorings.add(createColoringObject(colors, sortedVertices));
					numberOfColors = partitioning.length;
					break;
				}
			} while (getNextPermutation(colors));
			partitioning = nextPartitioning(partitioning, numberOfVertices);
		}
		return this.colorings;
	}

	@Override
	public Coloring<V> getColoring() {
		if (this.colorings.isEmpty()) {
			this.getAllColorings();
		}
		return this.colorings.get(0);
	}

	// determines permutations lexicographically,
	// input array must be in ascending order.
	private boolean getNextPermutation(int[] ascendingArray) {
		int i = ascendingArray.length - 1;
		while (i > 0 && ascendingArray[i - 1] >= ascendingArray[i]) {
			i--;
		}
		if (i <= 0) {
			// last permutation
			return false;
		}
		int j = ascendingArray.length - 1;
		while (ascendingArray[j] <= ascendingArray[i - 1]) {
			j--;
		}

		// swap
		int tmp = ascendingArray[i - 1];
		ascendingArray[i - 1] = ascendingArray[j];
		ascendingArray[j] = tmp;

		j = ascendingArray.length - 1;
		while (i < j) {
			tmp = ascendingArray[i];
			ascendingArray[i] = ascendingArray[j];
			ascendingArray[j] = tmp;
			i++;
			j--;
		}
		return true;
	}

	private int[] parseIntegerPartitioning(int[] partitioning, int numberOfVertices) {
		int[] result = new int[numberOfVertices];
		int index = 0;
		for (int i = 0; i < partitioning.length; i++) {
			for (int j = index; j < result.length; j++) {
				result[j] = i;
			}
			index += partitioning[i];
		}
		return result;
	}

	private int[] getFirstPartitioning(int numberOfVertices, int largestCliqueSize) {
		int[] result = new int[largestCliqueSize];
		result[0] = numberOfVertices - (largestCliqueSize - 1);
		for (int i = 1; i < result.length; i++) {
			result[i] = 1;
		}
		return result;
	}

	private int[] nextPartitioning(int[] array, int numberOfVertices) {
		if (array.length == numberOfVertices) {
			return null;
		}

		int[] arrayCopy = new int[array.length];
		System.arraycopy(array, 0, arrayCopy, 0, array.length);

		// check if first element equals last element
		// or if their difference is less than 2.
		// this means that there are no more possible
		// partitions for the numberOfVertices and
		// numberOfColors in this iteration.
		if (arrayCopy[0] == arrayCopy[arrayCopy.length - 1]
				|| arrayCopy[0] - arrayCopy[arrayCopy.length - 1] < 2) {
			// initialize arrayCopy with one more color.
			arrayCopy = new int[arrayCopy.length + 1];
			int tmp = numberOfVertices;
			for (int j = arrayCopy.length - 1; j > 0; j--) {
				arrayCopy[j] = 1;
				tmp--;
			}
			arrayCopy[0] = tmp;
			return arrayCopy;
		}

		// iterate over arrayCopy to calculate
		// next distribution.
		for (int j = 1; j < arrayCopy.length; j++) {
			// check if the difference between
			// the first and the j-th element
			// of the arrayCopy is bigger than 2.
			// this means that there exists
			// a possible next distribution.
			if (arrayCopy[0] - arrayCopy[j] >= 2) {
				arrayCopy[j]++;
				arrayCopy[j - 1]--;
				j--;
				// if arrayCopy is not in ascending order,
				// the distribution is not correct yet.
				while (!isInDescendingOrder(arrayCopy)) {
					if (arrayCopy[j] >= arrayCopy[j - 1]) {
						break;
					}
					arrayCopy[j]++;
					arrayCopy[j - 1]--;
					j--;
				}
				return arrayCopy;
			}
		}
		return null;
	}

	private boolean isInDescendingOrder(int[] array) {
		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] < array[i + 1]) {
				return false;
			}
		}
		return true;
	}

	private Coloring<V> createColoringObject(int[] coloring, List<V> sortedNodes) {
		Map<V, Integer> colors = new HashMap<>();
		Set<Integer> differentColors = new HashSet<>();
		for (int j = 0; j < coloring.length; j++) {
			colors.put(sortedNodes.get(j), coloring[j]);
			differentColors.add(coloring[j]);
		}
		return new ColoringImpl<>(colors, differentColors.size());
	}

	/**
	 * Checks if a coloring is valid
	 *
	 * @param coloring the coloring
	 * @param graph    the graph
	 * @return true if valid, false if invalid
	 */
	public static <V, E> boolean isValidVertexColoring(Coloring<V> coloring, Graph<V, E> graph) {
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

	/**
	 * Checks if a coloring is valid.
	 *
	 * @param colors the coloring
	 * @return true if valid, false if invalid
	 */
	public boolean isValidVertexColoring(int[] colors) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = i + 1; j < matrix.length; j++) {
				if (matrix[i][j] == 1 && colors[i] == colors[j]) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Checks if two colorings are isomorphic.
	 *
	 * @param c1 first coloring
	 * @param c2 second coloring
	 * @param <V> type
	 * @return true if they are equal, false if they are not.
	 */
	public static <V> boolean equivalentColoring(Coloring<V> c1, Coloring<V> c2) {
		if (c1.getNumberColors() != c2.getNumberColors()) {
			return false;
		}
		List<Integer> c1colors = new ArrayList<>(c1.getColors().values());
		List<Integer> c2colors = new ArrayList<>(c2.getColors().values());
		Collections.sort(c1colors);
		Collections.sort(c2colors);
		Iterator it1 = c1colors.iterator();
		Iterator it2 = c2colors.iterator();
		while (it1.hasNext()) {
			if (!it1.next().equals(it2.next())) {
				return false;
			}
		}
		return true;
	}
}

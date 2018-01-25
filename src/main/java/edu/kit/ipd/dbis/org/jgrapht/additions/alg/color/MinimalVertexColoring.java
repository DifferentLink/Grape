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
			List<int[]> partitions = this.integerPartitioning(numberOfVertices);
			Iterator it = partitions.iterator();
			while (it.hasNext()) {
				colors = this.parseIntegerPartitioning((int[]) it.next(), numberOfVertices);

				Integer[] colorCopy = new Integer[colors.length];
				for (int j = 0; j < colors.length; j++) {
					colorCopy[j] = colors[j];
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
			}
		}
		return null;
	}

	protected Integer[] parseIntegerPartitioning(int[] partitioning, int numberOfVertices) {
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

	protected List<int[]> integerPartitioning(int knoten) {
		List<int[]> result = new LinkedList<>();
		int anzahlFarben = 2;
		int[] array = new int[anzahlFarben];
		array[0] = knoten - 1;
		array[1] = 1;

		for (int i = 0; i < array.length && anzahlFarben <= knoten; ) {
			result.add(array);
			if (array[i] > array[i + 1] + 1) {
				array[i + 1]++;
				array[i]--;
			} else if (array[i] == array[i + 1] + 1 && array[i] != 2 && i + 2 < array.length) {
				array[i + 1]++;
				array[i]--;
				i++;
				array[i + 1]++;
				array[i]--;
			} else {
				array = new int[++anzahlFarben];
				int tmp = knoten;
				for (int j = array.length - 1; j > 0; j--) {
					array[j] = 1;
					tmp--;
				}
				array[0] = tmp;
				i = 0;
			}
		}
		return result;
	}

	private int[] nextIntegerPartitioning(int[] array, int numberOfVertices, int numberOfColors) {
		// TODO: implement me
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

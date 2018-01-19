package edu.kit.ipd.dbis.org.jgrapht.additions.alg.color;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
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
		// brute force algorithm that determines
		// a minimal vertex coloring.
		// needs to be made faster!
		int numberOfVertices = this.graph.getNumberOfVertices();
		ArrayList<V> sortedVertices = new ArrayList<>(new TreeSet<V>(this.graph.vertexSet()));
		int[] colors = new int[numberOfVertices];

		// give vertices an order
		for (int i = 0; i < sortedVertices.size(); i++) {
			colors[i] = 0;
		}

		if (numberOfVertices == 1) {
			return createColoringObject(new int[] {0}, sortedVertices);
		} else {
			Coloring coloring1 = createColoringObject(colors, sortedVertices);
			for (int maxColors = 1; maxColors < numberOfVertices; maxColors++) {
				// load maxColors - 1 different colors into array
				for (int i = 0; i < maxColors; i++) {
					colors[i] = i + 1;
				}
				// try every possible coloring for this number of colors
				List<int[]> permutations = getPermutations(maxColors, colors);
				for (int[] perm : permutations) {
					coloring1 = createColoringObject(perm, sortedVertices);
					if (isValidVertexColoring(coloring1, graph)) {
						return coloring1;
					}
				}
			}
		}
		return null;
	}

	protected List<int[]> getPermutations(int n, int[] a) {
		List<int[]> result = new ArrayList<>();
		int[] c = new int[n];
		for (int i = 0; i < c.length; i++) {
			c[i] = 0;
		}
		result.add(a);

		for (int i = 0; i < n;) {
			if (c[i] < i) {
				if (i % 2 == 0) {
					int tmp = a[0];
					a[0] = a[i];
					a[i] = tmp;
				} else {
					int tmp = a[c[i]];
					a[c[i]] = a[i];
					a[i] = tmp;
				}
				int[] aCopy = new int[a.length];
				System.arraycopy(a, 0, aCopy, 0, a.length);
				result.add(aCopy);
				c[i] += 1;
				i = 0;
			} else {
				c[i] = 0;
				i += 1;
			}
		}
		return result;
	}

	private Coloring createColoringObject(int[] coloring, List<V> sortedNodes) {
		Map<V, Integer> colors = new HashMap<>();
		Set<Integer> differentColors = new HashSet<>();
		for (int j = 0; j < coloring.length; j++) {
			colors.put(sortedNodes.get(j), coloring[j]);
			differentColors.add(coloring[j]);
		}
		return new ColoringImpl(colors, differentColors.size());
	}

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

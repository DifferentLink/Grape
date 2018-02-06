package edu.kit.ipd.dbis.org.jgrapht.additions.alg.color;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm.Coloring;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MinimalVertexColoringTest {
	@Test
	public void isValidVertexColoring() {
		PropertyGraph<String, Integer> graph = new PropertyGraph<>();
		graph.addVertex("a");
		graph.addVertex("b");
		graph.addVertex("c");
		graph.addVertex("d");
		graph.addEdge("a", "b");
		graph.addEdge("a", "c");
		graph.addEdge("b", "c");
		graph.addEdge("b", "d");

		Map<String, Integer> colors = new HashMap<>();
		colors.put("a", 0);
		colors.put("b", 1);
		colors.put("c", 1);
		colors.put("d", 0);
		Coloring coloring = new VertexColoringAlgorithm.ColoringImpl<>(colors, 2);
		MinimalVertexColoring alg = new MinimalVertexColoring(graph);

		assertEquals(false, alg.isValidVertexColoring(coloring, graph));
	}

	@Test
	public void isValidVertexColoring2() {
		PropertyGraph graph = new PropertyGraph();
		graph.addVertex("a");
		graph.addVertex("b");
		graph.addVertex("c");
		graph.addVertex("d");
		graph.addEdge("a", "b");
		graph.addEdge("a", "c");
		graph.addEdge("b", "d");

		Map<String, Integer> colors = new HashMap<>();
		colors.put("a", 0);
		colors.put("b", 1);
		colors.put("c", 1);
		colors.put("d", 0);
		Coloring coloring = new VertexColoringAlgorithm.ColoringImpl<String>(colors, 2);
		MinimalVertexColoring alg = new MinimalVertexColoring(graph);

		assertEquals(true, alg.isValidVertexColoring(coloring, graph));
	}

	@Test
	public void isValidVertexColoring3() {
		PropertyGraph graph = new PropertyGraph();
		graph.addVertex(0);
		graph.addVertex(1);
		graph.addEdge(0, 1);
		int[] colors = new int[]{0, 1};
		MinimalVertexColoring alg = new MinimalVertexColoring(graph);

		assertEquals(true, alg.isValidVertexColoring(colors));
	}

	@Test
	public void getColoring0() {
		PropertyGraph graph = new PropertyGraph();
		graph.addVertex("a");
		graph.addVertex("b");
		graph.addVertex("c");
		graph.addVertex("d");
		graph.addEdge("a", "b");
		graph.addEdge("a", "c");
		graph.addEdge("b", "d");

		MinimalVertexColoring alg = new MinimalVertexColoring(graph);

		Coloring<Object> coloring = alg.getColoring();

		assertEquals(true, alg.isValidVertexColoring(coloring, graph));
		assertEquals(2, coloring.getNumberColors());
	}

	@Test
	public void getColoring1() {
		PropertyGraph graph = new PropertyGraph();
		graph.addVertex("a");
		graph.addVertex("b");
		graph.addVertex("c");
		graph.addVertex("d");
		graph.addVertex("e");
		graph.addEdge("a", "b");
		graph.addEdge("a", "c");
		graph.addEdge("a", "d");
		graph.addEdge("a", "e");
		graph.addEdge("b", "c");
		graph.addEdge("b", "d");
		graph.addEdge("b", "e");
		graph.addEdge("c", "d");
		graph.addEdge("c", "e");
		graph.addEdge("d", "e");

		MinimalVertexColoring alg = new MinimalVertexColoring(graph);
		Coloring coloring = alg.getColoring();
		assertEquals(true, alg.isValidVertexColoring(coloring, graph));
		assertEquals(5, coloring.getNumberColors());
	}

	@Test
	public void getColoring2() {
		// based on this graph from wikipedia:
		// https://en.wikipedia.org/wiki/File:Hadwiger_conjecture.svg
		PropertyGraph graph = new PropertyGraph();
		graph.addVertex("a");
		graph.addVertex("b");
		graph.addVertex("c");
		graph.addVertex("d");
		graph.addVertex("e");
		graph.addVertex("f");
		graph.addVertex("g");
		graph.addVertex("h");
		graph.addVertex("i");
		graph.addVertex("j");
		graph.addVertex("k");
		graph.addVertex("l");
		graph.addEdge("a", "b");
		graph.addEdge("a", "c");
		graph.addEdge("b", "e");
		graph.addEdge("b", "f");
		graph.addEdge("c", "d");
		graph.addEdge("c", "g");
		graph.addEdge("c", "e");
		graph.addEdge("d", "e");
		graph.addEdge("d", "g");
		graph.addEdge("e", "f");
		graph.addEdge("e", "h");
		graph.addEdge("f", "h");
		graph.addEdge("f", "i");
		graph.addEdge("g", "j");
		graph.addEdge("h", "k");
		graph.addEdge("i", "k");
		graph.addEdge("i", "l");
		graph.addEdge("j", "k");
		graph.addEdge("j", "l");
		graph.addEdge("j", "h");
		graph.addEdge("k", "l");

		MinimalVertexColoring alg = new MinimalVertexColoring(graph);
		Coloring coloring = alg.getColoring();
		assertEquals(4, coloring.getNumberColors());
		assertEquals(true, alg.isValidVertexColoring(coloring, graph));
	}

	@Test
	public void nearlyCompleteBipartiteGraphColoring() {
		PropertyGraph graph = new PropertyGraph();
		graph.addVertex("a");
		graph.addVertex("b");
		graph.addVertex("c");
		graph.addVertex("d");
		graph.addVertex("e");
		graph.addVertex("f");
		graph.addVertex("g");
		graph.addVertex("h");

		graph.addEdge("a", "f");
		graph.addEdge("a", "g");
		graph.addEdge("a", "h");

		graph.addEdge("b", "e");
		graph.addEdge("b", "g");
		graph.addEdge("b", "h");

		graph.addEdge("c", "e");
		graph.addEdge("c", "f");
		graph.addEdge("c", "h");

		graph.addEdge("d", "e");
		graph.addEdge("d", "f");
		graph.addEdge("d", "g");

		MinimalVertexColoring alg = new MinimalVertexColoring(graph);
		Coloring coloring = alg.getColoring();

		assertEquals(2, coloring.getNumberColors());
		assertEquals(true, alg.isValidVertexColoring(coloring, graph));
	}

	@Test
	public void bigCompleteGraph() {
		PropertyGraph graph = createCompleteGraph(10);

		MinimalVertexColoring alg = new MinimalVertexColoring(graph);
		Coloring coloring = alg.getColoring();

		assertEquals(10, coloring.getNumberColors());
		assertEquals(true, alg.isValidVertexColoring(coloring, graph));
	}

	@Test
	public void getColorings() {
		PropertyGraph graph = new PropertyGraph();
		graph.addVertex(0);
		graph.addVertex(1);
		graph.addVertex(2);
		graph.addVertex(3);

		graph.addEdge(0, 1);
		graph.addEdge(0, 2);
		graph.addEdge(0, 3);
		graph.addEdge(1, 2);

		MinimalVertexColoring alg = new MinimalVertexColoring(graph);
		List<Coloring<Object>> colorings = alg.getAllColorings();
		assertEquals(true, colorings.size() == 1);
	}

	@Test
	public void getColorings2() {
		PropertyGraph<Integer, Integer> graph = new PropertyGraph<>();
		graph.addVertex(0);
		graph.addVertex(1);
		graph.addVertex(2);
		graph.addVertex(3);
		graph.addVertex(4);
		graph.addVertex(5);

		graph.addEdge(0, 1);
		graph.addEdge(0, 2);
		graph.addEdge(1, 2);
		graph.addEdge(2, 3);
		graph.addEdge(3, 4);
		graph.addEdge(4, 5);

		MinimalVertexColoring alg = new MinimalVertexColoring(graph);
		List<Coloring<Object>> colorings = alg.getAllColorings();
		assertEquals(true, colorings.size() > 1);
	}

	@Test
	public void isEquivalent() {
		Map<Integer, Integer> c1colors = new HashMap<>();
		Map<Integer, Integer> c2colors = new HashMap<>();
		c1colors.put(0, 0);
		c1colors.put(1, 1);

		c2colors.put(0, 1);
		c2colors.put(1, 0);

		Coloring c1 = new VertexColoringAlgorithm.ColoringImpl(c1colors, 2);
		Coloring c2 = new VertexColoringAlgorithm.ColoringImpl(c2colors, 2);

		assertEquals(true, MinimalVertexColoring.equivalentColoring(c1, c2));
	}

	@Test
	public void isEquivalent2() {
		Map<Integer, Integer> c1colors = new HashMap<>();
		Map<Integer, Integer> c2colors = new HashMap<>();
		c1colors.put(0, 0);
		c1colors.put(1, 1);
		c1colors.put(2, 2);
		c1colors.put(3, 1);
		c1colors.put(4, 0);
		c1colors.put(5, 1);
		c1colors.put(6, 2);
		c1colors.put(7, 1);

		c2colors.put(0, 1);
		c2colors.put(1, 1);
		c2colors.put(2, 0);
		c2colors.put(3, 1);
		c2colors.put(4, 0);
		c2colors.put(5, 2);
		c2colors.put(6, 2);
		c2colors.put(7, 2);

		Coloring<Integer> c1 = new VertexColoringAlgorithm.ColoringImpl<>(c1colors, 3);
		Coloring<Integer> c2 = new VertexColoringAlgorithm.ColoringImpl<>(c2colors, 3);

		assertEquals(false, MinimalVertexColoring.equivalentColoring(c1, c2));
	}

	private PropertyGraph createCompleteGraph(int numberOfVertices) {
		PropertyGraph graph = new PropertyGraph();
		for (int i = 0; i < numberOfVertices; i++) {
			graph.addVertex(i);
		}
		for (int i = 0; i < numberOfVertices; i++) {
			for (int j = 0; j < numberOfVertices; j++) {
				if (j != i && !graph.containsEdge(graph.getEdgeFactory().createEdge(j, i))) {
					graph.addEdge(i, j);

				}
			}
		}
		return graph;
	}
}
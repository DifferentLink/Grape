package edu.kit.ipd.dbis.org.jgrapht.additions.alg.color;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm.Coloring;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MinimalVertexColoringTest {
	@Test
	public void isValidVertexColoring() {
		PropertyGraph graph = new PropertyGraph();
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
		Coloring coloring = new VertexColoringAlgorithm.ColoringImpl<String>(colors, 2);
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
	public void getColorings() {
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
	public void getColorings1() {
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
	public void getColorings2() {
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
		System.out.println(coloring.getColors());
		assertEquals(4, coloring.getNumberColors());
		assertEquals(true, alg.isValidVertexColoring(coloring, graph));
	}
}
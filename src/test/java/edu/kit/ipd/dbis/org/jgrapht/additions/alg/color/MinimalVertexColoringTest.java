package edu.kit.ipd.dbis.org.jgrapht.additions.alg.color;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm.Coloring;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

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
	}
}
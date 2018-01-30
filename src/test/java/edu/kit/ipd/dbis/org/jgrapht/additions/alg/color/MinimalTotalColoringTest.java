package edu.kit.ipd.dbis.org.jgrapht.additions.alg.color;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.TotalColoringAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MinimalTotalColoringTest {

	@Test
	public void getColoring() {
		PropertyGraph graph = new PropertyGraph();
		graph.addVertex("a");
		graph.addVertex("b");
		graph.addVertex("c");
		graph.addVertex("d");
		graph.addEdge("a", "b");
		graph.addEdge("a", "c");
		graph.addEdge("b", "d");

		MinimalTotalColoring totalColoring = new MinimalTotalColoring(graph);
		TotalColoringAlgorithm.TotalColoring coloring = totalColoring.getColoring();
		assertEquals(3, coloring.getNumberColors());
	}

	@Test
	public void getColoring2() {

	}
}
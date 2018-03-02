package edu.kit.ipd.dbis.org.jgrapht.additions.alg.color;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.TotalColoringAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.jgrapht.graph.SimpleGraph;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class MinimalTotalColoringTest {

	@Test
	public void getColoring() {
		PropertyGraph<String, String> graph = new PropertyGraph<>();
		graph.addVertex("a");
		graph.addVertex("b");
		graph.addVertex("c");
		graph.addVertex("d");

		graph.addEdge("a", "b");
		graph.addEdge("a", "c");
		graph.addEdge("a", "d");
		graph.addEdge("b", "c");
		graph.addEdge("b", "d");
		graph.addEdge("c", "d");

		MinimalTotalColoring<String, String> totalColoring = new MinimalTotalColoring<>(graph);
		TotalColoringAlgorithm.TotalColoring coloring = totalColoring.getColoring();
		assertEquals(5, coloring.getNumberColors());
	}

	@Test
	public void getAllColorings() {
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

		MinimalTotalColoring<Integer, Integer> totalColoring = new MinimalTotalColoring<>(graph);
		List<TotalColoringAlgorithm.TotalColoring<Integer, Integer>> colorings = totalColoring.getAllColorings();
		assertEquals(true, colorings.size() > 1);
	}

	@Test
	public void cliqueOfFourTest() {
		PropertyGraph<Integer, Integer> graph = Util.createCompleteGraph(4);
		MinimalTotalColoring<Integer, Integer> totalColoring = new MinimalTotalColoring<>(graph);
		TotalColoringAlgorithm.TotalColoring coloring = totalColoring.getColoring();
		assertEquals(5, coloring.getNumberColors());
	}

	@Test
	public void cliqueOfFiveTest() {
		PropertyGraph graph = Util.createCompleteGraph(5);
		MinimalTotalColoring<Integer, Integer> totalColoring = new MinimalTotalColoring<>(graph);
		TotalColoringAlgorithm.TotalColoring coloring = totalColoring.getColoring();
		assertEquals(5, coloring.getNumberColors());
	}
}
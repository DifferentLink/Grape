package edu.kit.ipd.dbis.org.jgrapht.additions.alg.density;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.junit.Assert;
import org.junit.Test;

public class BinomialDensityAlgorithmTest {
	@Test
	public void binomialDensityTest1() {
		PropertyGraph graph = new PropertyGraph();
		graph.addVertex("a");
		graph.addVertex("b");
		graph.addVertex("c");
		graph.addVertex("d");
		graph.addVertex("e");
		graph.addEdge("a", "b");
		graph.addEdge("a", "c");
		graph.addEdge("b", "c");
		graph.addEdge("b", "d");
		graph.addEdge("d", "c");
		graph.addEdge("c", "e");
		BinomialDensityAlgorithm alg = new BinomialDensityAlgorithm(graph);
		double value = 6.0 / 10.0;

		System.out.println(alg.getDensity());
		System.out.println(value);
		Assert.assertTrue(alg.getDensity() - 0.01 < value && alg.getDensity() + 0.1 > value);
	}
}

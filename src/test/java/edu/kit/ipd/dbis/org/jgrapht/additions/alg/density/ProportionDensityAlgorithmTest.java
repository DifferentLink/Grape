package edu.kit.ipd.dbis.org.jgrapht.additions.alg.density;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.junit.Assert;
import org.junit.Test;

public class ProportionDensityAlgorithmTest {
	@Test
	public void proportionDensityTest1() {
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
		ProportionDensityAlgorithm alg = new ProportionDensityAlgorithm(graph);
		double value = 2.0 / 6.0;
		Assert.assertTrue(alg.getDensity() - 0.01 < value && alg.getDensity() + 0.1 > value);
	}
}

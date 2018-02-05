package edu.kit.ipd.dbis.org.jgrapht.additions.alg.density;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.junit.Assert;
import org.junit.Test;

public class StructureDensityAlgorithmTest {
	@Test
	public void structureDensityTest1() {
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
		StructureDensityAlgorithm alg = new StructureDensityAlgorithm(graph);
		double value = 9.0 / 26.0;
		Assert.assertTrue(alg.getDensity() - 0.0001 < value && alg.getDensity() + 0.0001 > value);
	}
}

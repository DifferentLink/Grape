package edu.kit.ipd.dbis.org.jgrapht.additions.alg.density;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Assert;
import org.junit.Test;

public class LocalBfsCodeAlgorithmTest {

	private PropertyGraph generateSimpleTestGraph() {
		PropertyGraph graph = new PropertyGraph();
		graph.addVertex("a");
		graph.addVertex("b");
		graph.addVertex("c");
		graph.addVertex("d");
		graph.addVertex("e");
		graph.addVertex("f");
		graph.addVertex("h");
		graph.addEdge("d", "a");
		graph.addEdge("d", "e");
		graph.addEdge("d", "b");
		graph.addEdge("a", "f");
		graph.addEdge("e", "f");
		graph.addEdge("e", "b");
		graph.addEdge("e", "h");
		graph.addEdge("e", "c");
		graph.addEdge("h", "c");
		graph.addEdge("a", "h");
		return graph;
	}


	@Test
	public void minimalBfsCodeTest() {
		PropertyGraph graph = generateSimpleTestGraph();
		LocalBfsCodeAlgorithm<String, DefaultEdge> alg = new LocalBfsCodeAlgorithm<>("e");
		int[] result = alg.getBfsCode(graph).getCode();
		int[] local = {1, 1, 2, 1, 1, 3, -1, 2, 3, 1, 1, 4, 1, 1, 5, -1, 4, 5, 1, 1, 6, 1, 2, 7, -1, 4, 7, -1, 6, 7};
		BfsCodeAlgorithm.BfsCodeImpl localCode = new BfsCodeAlgorithm.BfsCodeImpl(local);
		Assert.assertTrue(localCode.compareTo(new BfsCodeAlgorithm.BfsCodeImpl<>(result)) == 0);
	}
}

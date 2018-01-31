package edu.kit.ipd.dbis.org.jgrapht.additions.alg.kkgraph;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.KkGraphAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class KkGraphGeneratorTest {

	private PropertyGraph generateSimpleTestGraph() {
		PropertyGraph graph = new PropertyGraph();
		graph.addVertex("a");
		graph.addVertex("b");
		graph.addVertex("c");
		graph.addVertex("d");
		graph.addEdge("a", "b");
		graph.addEdge("a", "c");
		graph.addEdge("a", "d");
		graph.addEdge("b", "c");
		return graph;
	}

	private PropertyGraph generateSimpleTestCliqueGraph() {
		PropertyGraph graph = new PropertyGraph();
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
		return graph;
	}

	@Test
	public void Test() {
		PropertyGraph graph = new PropertyGraph();
		KkGraphGenerator g = new KkGraphGenerator(graph);
		int[] a = {1,1,1,0,0};
		for (int i = 0; i < 9; i++) {
			int[] b = g.getNextEdgeCombination(a);
			for (int j = 0; j < b.length; j++) {
				System.out.print(b[j] +  ", ");
			}
			System.out.println();
			a = b;
		}
	}

	@Test
	public void cliqueGraphTest() {
		PropertyGraph graph = this.generateSimpleTestCliqueGraph();
		KkGraphAlgorithm alg = new KkGraphGenerator(graph);
		KkGraphAlgorithm.KkGraph kkGraph = alg.getKkGraph();
		Assert.assertTrue(kkGraph.getNumberOfSubgraphs() == 4);
	}

	@Test
	public void simpleGraphTest() {
		PropertyGraph graph = this.generateSimpleTestGraph();
		KkGraphAlgorithm alg = new KkGraphGenerator(graph);
		KkGraphAlgorithm.KkGraph kkGraph = alg.getKkGraph();
		Map g = kkGraph.getKkGraph();
		System.out.println(g);
	}

	@Test
	public void simpleGraphTest2() {
		PropertyGraph graph = new PropertyGraph();
		graph.addVertex("a");
		graph.addVertex("b");
		graph.addVertex("c");
		graph.addVertex("d");
		graph.addVertex("e");
		graph.addEdge("a", "b");
		graph.addEdge("a", "c");
		graph.addEdge("a", "d");
		graph.addEdge("b", "c");
		graph.addEdge("c", "e");
		KkGraphAlgorithm alg = new KkGraphGenerator(graph);
		KkGraphAlgorithm.KkGraph kkGraph = alg.getKkGraph();
		Map g = kkGraph.getKkGraph();
		Assert.assertTrue(kkGraph.getNumberOfSubgraphs() == 3);
		System.out.println(g);
	}
}

package edu.kit.ipd.dbis.org.jgrapht.additions.alg.kkgraph;


import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.KkGraphAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.junit.Assert;
import org.junit.Test;


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
		Assert.assertTrue(kkGraph.getNumberOfSubgraphs() == 3);
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
		Assert.assertTrue(kkGraph.getNumberOfSubgraphs() == 3);
	}

	@Test
	public void simpleGraphTest3() {
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
		graph.addEdge("c", "e");
		graph.addEdge("e", "d");
		graph.addEdge("b", "e");;
		KkGraphAlgorithm alg = new KkGraphGenerator(graph);
		KkGraphAlgorithm.KkGraph kkGraph = alg.getKkGraph();
		Assert.assertTrue(kkGraph.getNumberOfSubgraphs() == 4);
	}

	@Test
	public void wikipediaGraphTest() {
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
		KkGraphAlgorithm alg = new KkGraphGenerator(graph);
		KkGraphAlgorithm.KkGraph kkGraph = alg.getKkGraph();
		Assert.assertTrue(kkGraph.getNumberOfSubgraphs() == 4);
	}

	@Test
	public void bfsPaperGraphTest() {
		PropertyGraph graph = new PropertyGraph();
		graph.addVertex("a");
		graph.addVertex("b");
		graph.addVertex("c");
		graph.addVertex("d");
		graph.addVertex("e");
		graph.addVertex("f");
		graph.addVertex("g");
		graph.addEdge("a", "b");
		graph.addEdge("a", "c");
		graph.addEdge("a", "d");
		graph.addEdge("a", "e");
		graph.addEdge("a", "f");
		graph.addEdge("b", "c");
		graph.addEdge("b", "g");
		graph.addEdge("d", "e");
		graph.addEdge("d", "g");
		graph.addEdge("f", "g");
		KkGraphAlgorithm alg = new KkGraphGenerator(graph);
		KkGraphAlgorithm.KkGraph kkGraph = alg.getKkGraph();
		Assert.assertTrue(kkGraph.getNumberOfSubgraphs() == 3);
	}

	@Test
	public void generatedGraphTest() {
		int[] code = {1,1,2,1,1,3,1,1,4,1,1,5,1,2,6,1,6,7};
		BfsCodeAlgorithm.BfsCodeImpl bfs = new BfsCodeAlgorithm.BfsCodeImpl(code);
		PropertyGraph graph = new PropertyGraph(bfs);

		KkGraphAlgorithm alg = new KkGraphGenerator(graph);
		KkGraphAlgorithm.KkGraph kkGraph = alg.getKkGraph();
		Assert.assertTrue(kkGraph.getNumberOfSubgraphs() == 2);
	}


	@Test
	public void bfsPaperGraphTest2() {
		PropertyGraph graph = new PropertyGraph();
		graph.addVertex("a");
		graph.addVertex("b");
		graph.addVertex("c");
		graph.addVertex("d");
		graph.addVertex("e");
		graph.addVertex("f");
		graph.addVertex("g");
		graph.addEdge("a", "b");
		graph.addEdge("a", "d");
		graph.addEdge("a", "e");
		graph.addEdge("a", "f");
		graph.addEdge("b", "c");
		graph.addEdge("b", "d");
		graph.addEdge("b", "e");
		graph.addEdge("b", "g");
		graph.addEdge("c", "d");
		graph.addEdge("c", "f");
		graph.addEdge("c", "g");
		graph.addEdge("d", "e");
		graph.addEdge("d", "f");
		graph.addEdge("e", "f");
		graph.addEdge("f", "g");
		KkGraphAlgorithm alg = new KkGraphGenerator(graph);
		KkGraphAlgorithm.KkGraph kkGraph = alg.getKkGraph();
		Assert.assertTrue(kkGraph.getNumberOfSubgraphs() == 4);
	}

	@Test
	public void graphCircleTest1() {
		PropertyGraph graph = new PropertyGraph();
		graph.addVertex("a");
		graph.addVertex("b");
		graph.addVertex("c");
		graph.addVertex("d");
		graph.addVertex("e");
		graph.addEdge("a", "b");
		graph.addEdge("b", "c");
		graph.addEdge("c", "d");
		graph.addEdge("d", "e");
		graph.addEdge("e", "a");
		KkGraphAlgorithm alg = new KkGraphGenerator(graph);
		KkGraphAlgorithm.KkGraph kkGraph = alg.getKkGraph();
		System.out.println(kkGraph.getSubgraphs());
		System.out.println(kkGraph.getNumberOfSubgraphs());
		Assert.assertTrue(kkGraph.getNumberOfSubgraphs() == 3);
	}
}

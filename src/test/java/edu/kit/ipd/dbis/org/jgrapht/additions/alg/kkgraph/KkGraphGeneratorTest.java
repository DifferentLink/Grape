package edu.kit.ipd.dbis.org.jgrapht.additions.alg.kkgraph;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.KkGraphAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.VertexColoring;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;
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
		graph.addEdge("b", "e");
		VertexColoringAlgorithm.ColoringImpl vertexColoring = (VertexColoringAlgorithm.ColoringImpl)
				graph.getProperty(VertexColoring.class).getValue();
		int numberOfColors = vertexColoring.getNumberColors();
		System.out.println("Number: " + numberOfColors);
		KkGraphAlgorithm alg = new KkGraphGenerator(graph);
		KkGraphAlgorithm.KkGraph kkGraph = alg.getKkGraph();
		Map g = kkGraph.getKkGraph();
		Assert.assertTrue(kkGraph.getNumberOfSubgraphs() == 4);
		System.out.println(g);
	}

	@Test
	public void wikipedieGraphTest() {
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
		VertexColoringAlgorithm.ColoringImpl vertexColoring = (VertexColoringAlgorithm.ColoringImpl)
				graph.getProperty(VertexColoring.class).getValue();
		int numberOfColors = vertexColoring.getNumberColors();
		System.out.println("Number: " + numberOfColors);
		KkGraphAlgorithm alg = new KkGraphGenerator(graph);
		KkGraphAlgorithm.KkGraph kkGraph = alg.getKkGraph();
		Map g = kkGraph.getKkGraph();
		Assert.assertTrue(kkGraph.getNumberOfSubgraphs() == 4);
		System.out.println(g);
	}
}

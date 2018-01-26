package edu.kit.ipd.dbis.org.jgrapht.additions.alg.density;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;


public class MinimalBfsCodeAlgorithmTest {

	private PropertyGraph generateSimpleTestGraph1() {
		PropertyGraph graph = new PropertyGraph();
		graph.addVertex("a");
		graph.addVertex("b");
		graph.addVertex("c");
		graph.addVertex("d");
		graph.addEdge("a", "b");
		graph.addEdge("a", "c");
		graph.addEdge("b", "c");
		graph.addEdge("b", "d");
		graph.addEdge("c", "d");
		return graph;
	}

	private PropertyGraph generateSimpleTestGraph2() {
		PropertyGraph graph = new PropertyGraph();
		graph.addVertex("a");
		graph.addVertex("b");
		graph.addVertex("c");
		graph.addVertex("d");
		graph.addVertex("e");
		graph.addVertex("f");
		graph.addVertex("g");
		graph.addEdge("d", "a");
		graph.addEdge("d", "e");
		graph.addEdge("d", "b");
		graph.addEdge("a", "f");
		graph.addEdge("e", "f");
		graph.addEdge("e", "b");
		graph.addEdge("e", "g");
		graph.addEdge("e", "c");
		graph.addEdge("g", "c");
		graph.addEdge("a", "g");
		return graph;
	}

	@Test
	public void minimalBfsCodeTest() {
		PropertyGraph graph = generateSimpleTestGraph2();
		MinimalBfsCodeAlgorithm<String, DefaultEdge> alg = new MinimalBfsCodeAlgorithm<>();
		int[] minimalBfsCode = alg.getBfsCode(graph).getCode();
		int[] minCode = {1, 1, 2, 1, 1, 3, -1, 2, 3, 1, 1, 4, 1, 1, 5, -1, 4, 5, 1, 1, 6, 1, 2, 7, -1, 4, 7, -1, 6, 7};
		BfsCodeAlgorithm.BfsCodeImpl result = new BfsCodeAlgorithm.BfsCodeImpl(minCode);
		Assert.assertTrue(result.compareTo(new BfsCodeAlgorithm.BfsCodeImpl<>(minimalBfsCode)) == 0);
	}

	@Test
	public void fiveNodeCliqueBfsCodeTest() {
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
		graph.addEdge("b", "d");
		graph.addEdge("b", "e");
		graph.addEdge("c", "d");
		graph.addEdge("c", "e");
		graph.addEdge("d", "e");
		MinimalBfsCodeAlgorithm<String, DefaultEdge> alg = new MinimalBfsCodeAlgorithm<>();
		int[] minimalBfsCode = alg.getBfsCode(graph).getCode();
		int[] minCode = {1,1,2,1,1,3,-1,2,3,1,1,4,-1,2,4,-1,3,4,1,1,5,-1,2,5,-1,3,5,-1,4,5};
		BfsCodeAlgorithm.BfsCodeImpl result = new BfsCodeAlgorithm.BfsCodeImpl(minCode);
		Assert.assertTrue(result.compareTo(new BfsCodeAlgorithm.BfsCodeImpl<>(minimalBfsCode)) == 0);
	}
	@Test
	@Ignore
	public void elvenNodeCliqueBfsCodeTest() {
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

		graph.addEdge("a", "b");
		graph.addEdge("a", "c");
		graph.addEdge("a", "d");
		graph.addEdge("a", "e");
		graph.addEdge("a", "f");
		graph.addEdge("a", "g");
		graph.addEdge("a", "h");
		graph.addEdge("a", "i");
		graph.addEdge("a", "j");
		graph.addEdge("a", "k");

		graph.addEdge("b", "c");
		graph.addEdge("b", "d");
		graph.addEdge("b", "e");
		graph.addEdge("b", "f");
		graph.addEdge("b", "g");
		graph.addEdge("b", "h");
		graph.addEdge("b", "i");
		graph.addEdge("b", "j");
		graph.addEdge("b", "k");

		graph.addEdge("c", "d");
		graph.addEdge("c", "e");
		graph.addEdge("c", "f");
		graph.addEdge("c", "g");
		graph.addEdge("c", "h");
		graph.addEdge("c", "i");
		graph.addEdge("c", "j");
		graph.addEdge("c", "k");

		graph.addEdge("d", "e");
		graph.addEdge("d", "f");
		graph.addEdge("d", "g");
		graph.addEdge("d", "h");
		graph.addEdge("d", "i");
		graph.addEdge("d", "j");
		graph.addEdge("d", "k");

		graph.addEdge("e", "f");
		graph.addEdge("e", "g");
		graph.addEdge("e", "h");
		graph.addEdge("e", "i");
		graph.addEdge("e", "j");
		graph.addEdge("e", "k");

		graph.addEdge("f", "g");
		graph.addEdge("f", "h");
		graph.addEdge("f", "i");
		graph.addEdge("f", "j");
		graph.addEdge("f", "k");

		graph.addEdge("g", "h");
		graph.addEdge("g", "i");
		graph.addEdge("g", "j");
		graph.addEdge("g", "k");

		graph.addEdge("h", "i");
		graph.addEdge("h", "j");
		graph.addEdge("h", "k");

		graph.addEdge("i", "j");
		graph.addEdge("i", "k");

		graph.addEdge("j", "k");


		MinimalBfsCodeAlgorithm<String, DefaultEdge> alg = new MinimalBfsCodeAlgorithm<>();
		int[] minimalBfsCode = alg.getBfsCode(graph).getCode();
		int[] minCode = {1,1,2,1,1,3,-1,2,3,1,1,4,-1,2,4,-1,3,4,1,1,5,-1,2,5,-1,3,5,-1,4,5,1,1,6,-1,2,6,-1,3,6,-1,4,6,-1,5,6,
				1,1,7,-1,2,7,-1,3,7,-1,4,7,-1,5,7,-1,6,7,1,1,8,-1,2,8,-1,3,8,-1,4,8,-1,5,8,-1,6,8,-1,7,8,
				1,1,9,-1,2,9,-1,3,9,-1,4,9,-1,5,9,-1,6,9,-1,7,9,-1,8,9,1,1,10,-1,2,10,-1,3,10,-1,4,10,-1,5,10,-1,6,10,-1,
				7,10,-1,8,10,-1,9,10,1,1,11,-1,2,11,-1,3,11,-1,4,11,-1,5,11,-1,6,11,-1,7,11,-1,8,11,-1,9,11,-1,10,11};
		BfsCodeAlgorithm.BfsCodeImpl result = new BfsCodeAlgorithm.BfsCodeImpl(minCode);
		Assert.assertTrue(result.compareTo(new BfsCodeAlgorithm.BfsCodeImpl<>(minimalBfsCode)) == 0);
	}
}
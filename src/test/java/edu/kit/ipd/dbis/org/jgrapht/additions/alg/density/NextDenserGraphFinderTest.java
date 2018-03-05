package edu.kit.ipd.dbis.org.jgrapht.additions.alg.density;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.generate.BulkGraphGenerator;
import edu.kit.ipd.dbis.org.jgrapht.additions.generate.BulkRandomConnectedGraphGenerator;
import edu.kit.ipd.dbis.org.jgrapht.additions.generate.NotEnoughGraphsException;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.BfsCode;
import org.jgrapht.alg.isomorphism.VF2GraphIsomorphismInspector;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;

public class NextDenserGraphFinderTest {

	private PropertyGraph generateNoDenserGraph() {
		PropertyGraph graph = new PropertyGraph();
		graph.addVertex("a");
		graph.addVertex("b");
		graph.addVertex("c");
		graph.addVertex("d");
		graph.addEdge("a", "b");
		graph.addEdge("c", "b");
		graph.addEdge("a", "c");
		graph.addEdge("a", "d");
		return graph;
	}

	private PropertyGraph generateSimpleGraph() {
		PropertyGraph graph = new PropertyGraph();
		graph.addVertex("a");
		graph.addVertex("b");
		graph.addVertex("c");
		graph.addVertex("d");
		graph.addEdge("a", "b");
		graph.addEdge("b", "c");
		graph.addEdge("c", "d");
		graph.addEdge("a", "d");
		return graph;
	}

	@Test (expected = NoDenserGraphException.class)
	public void noDenserGraphExistsTest1() {
		PropertyGraph graph = generateNoDenserGraph();
		NextDenserGraphFinder f = new NextDenserGraphFinder(graph);
		PropertyGraph nextDenserGraph = f.getNextDenserGraph();
	}

	@Test
	public void nextDenserGraphWithFourVertiecesTest1() {
		PropertyGraph graph = generateSimpleGraph();
		NextDenserGraphFinder f = new NextDenserGraphFinder(graph);
		PropertyGraph nextDenserGraph = f.getNextDenserGraph();
		int[] code = ((BfsCodeAlgorithm.BfsCodeImpl) nextDenserGraph.getProperty(BfsCode.class).getValue()).getCode();
		int[] nextDenserCode = {1,1,2,1,1,3,1,2,4};
		Assert.assertTrue(code.length == nextDenserCode.length);
		for (int i = 0; i < Math.min(code.length, nextDenserCode.length); i++) {
			Assert.assertTrue(code[i] == nextDenserCode[i]);
		}
	}

	@Test
	public void nextDenserGraphWithFourVertiecesTest2() {
		PropertyGraph graph = new PropertyGraph();
		graph.addVertex("a");
		graph.addVertex("b");
		graph.addVertex("c");
		graph.addVertex("d");
		graph.addEdge("a", "b");
		graph.addEdge("a", "c");
		graph.addEdge("b", "d");
		NextDenserGraphFinder f = new NextDenserGraphFinder(graph);
		PropertyGraph nextDenserGraph = f.getNextDenserGraph();
		int[] code = ((BfsCodeAlgorithm.BfsCodeImpl) nextDenserGraph.getProperty(BfsCode.class).getValue()).getCode();
		int[] nextDenserCode = {1,1,2,1,1,3,1,1,4};
		Assert.assertTrue(code.length == nextDenserCode.length);
		for (int i = 0; i < Math.min(code.length, nextDenserCode.length); i++) {
			Assert.assertTrue(code[i] == nextDenserCode[i]);
		}
	}

	@Test
	public void nextDenserGraphWithFourVertiecesTest3() {
		PropertyGraph graph = new PropertyGraph();
		graph.addVertex("a");
		graph.addVertex("b");
		graph.addVertex("c");
		graph.addVertex("d");
		graph.addEdge("a", "b");
		graph.addEdge("a", "c");
		graph.addEdge("a", "d");
		NextDenserGraphFinder f = new NextDenserGraphFinder(graph);
		PropertyGraph nextDenserGraph = f.getNextDenserGraph();
		int[] code = ((BfsCodeAlgorithm.BfsCodeImpl) nextDenserGraph.getProperty(BfsCode.class).getValue()).getCode();
		int[] nextDenserCode = {1,1,2,1,1,3,-1,2,3,1,1,4,-1,2,4,-1,3,4};
		Assert.assertTrue(code.length == nextDenserCode.length);
		for (int i = 0; i < Math.min(code.length, nextDenserCode.length); i++) {
			Assert.assertTrue(code[i] == nextDenserCode[i]);
		}
	}

	@Test
	public void nextDenserGraphWithFiveVertiecesTest1() {
		PropertyGraph graph = new PropertyGraph();
		graph.addVertex("a");
		graph.addVertex("b");
		graph.addVertex("c");
		graph.addVertex("d");
		graph.addVertex("e");
		graph.addEdge("a", "b");
		graph.addEdge("b", "d");
		graph.addEdge("d", "e");
		graph.addEdge("e", "c");
		graph.addEdge("a", "c");
		NextDenserGraphFinder f = new NextDenserGraphFinder(graph);
		PropertyGraph nextDenserGraph = f.getNextDenserGraph();
		int[] code = ((BfsCodeAlgorithm.BfsCodeImpl) nextDenserGraph.getProperty(BfsCode.class).getValue()).getCode();
		int[] nextDenserCode = {1,1,2,1,1,3,1,2,4,1,3,5};
		Assert.assertTrue(code.length == nextDenserCode.length);
		for (int i = 0; i < Math.min(code.length, nextDenserCode.length); i++) {
			Assert.assertTrue(code[i] == nextDenserCode[i]);
		}
	}

	@Test
	public void nextDenserGraphWithFiveVertiecesTest2() {
		PropertyGraph graph = new PropertyGraph();
		graph.addVertex("a");
		graph.addVertex("b");
		graph.addVertex("c");
		graph.addVertex("d");
		graph.addVertex("e");
		graph.addEdge("a", "b");
		graph.addEdge("b", "d");
		graph.addEdge("e", "c");
		graph.addEdge("a", "c");
		NextDenserGraphFinder f = new NextDenserGraphFinder(graph);
		PropertyGraph nextDenserGraph = f.getNextDenserGraph();
		int[] code = ((BfsCodeAlgorithm.BfsCodeImpl) nextDenserGraph.getProperty(BfsCode.class).getValue()).getCode();
		int[] nextDenserCode = {1,1,2,1,1,3,1,1,4,1,2,5,-1,3,5,-1,4,5};
		Assert.assertTrue(code.length == nextDenserCode.length);
		for (int i = 0; i < Math.min(code.length, nextDenserCode.length); i++) {
			Assert.assertTrue(code[i] == nextDenserCode[i]);
		}
	}

	@Test
	public void nextDenserGraphWithFiveVertiecesTest3() {
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
		NextDenserGraphFinder f = new NextDenserGraphFinder(graph);
		PropertyGraph nextDenserGraph = f.getNextDenserGraph();
		int[] code = ((BfsCodeAlgorithm.BfsCodeImpl) nextDenserGraph.getProperty(BfsCode.class).getValue()).getCode();
		int[] nextDenserCode = {1,1,2,1,1,3,-1,2,3,1,1,4,1,4,5};
		Assert.assertTrue(code.length == nextDenserCode.length);
		for (int i = 0; i < Math.min(code.length, nextDenserCode.length); i++) {
			Assert.assertTrue(code[i] == nextDenserCode[i]);
		}
	}

	@Test
	public void generatedGraphsTest() {
		BulkGraphGenerator bulkGen = new BulkRandomConnectedGraphGenerator();
		HashSet<PropertyGraph> target = new HashSet<>();
		try {
			bulkGen.generateBulk(target, 6, 4, 4, 1, 8);
		} catch (IllegalArgumentException | NotEnoughGraphsException e) {
			System.out.println("error by generating graphs");
			return;
		}
		for (PropertyGraph graph : target) {
			NextDenserGraphFinder g = new NextDenserGraphFinder(graph);
			try {
				PropertyGraph denserGraph = g.getNextDenserGraph();
			} catch (NoDenserGraphException e) {
				System.out.println(graph.edgeSet());
			}
		}
	}
}

package edu.kit.ipd.dbis.org.jgrapht.additions.generate;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.jgrapht.alg.util.IntegerVertexFactory;
import org.jgrapht.generate.GraphGenerator;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Assert;
import org.junit.Test;


public class RandomConnectedGraphGeneratorTest {

	@Test (expected = IllegalArgumentException.class)
	public void negativeParameterTest() {
		GraphGenerator gen = new RandomConnectedGraphGenerator(-1, 6, 3, 6);
	}

	@Test (expected = IllegalArgumentException.class)
	public void toLittleEdgesParameterTest() {
		GraphGenerator gen = new RandomConnectedGraphGenerator(5, 10, 0, 3);
	}

	@Test (expected = IllegalArgumentException.class)
	public void toManyEdgesParameterTest() {
		GraphGenerator gen = new RandomConnectedGraphGenerator(5, 10, 100, 120);
	}

	@Test
	public void zeroTest() {
		GraphGenerator gen = new RandomConnectedGraphGenerator(0, 0, 0, 0);
		PropertyGraph graph = new PropertyGraph();
		gen.generateGraph(graph, new IntegerVertexFactory(1), null);
		Assert.assertTrue(graph.vertexSet().size() == 0);
	}

	@Test
	public void graphGeneratorIntervalTest() {
		GraphGenerator gen = new RandomConnectedGraphGenerator(4, 6, 3, 6);
		ClassBasedEdgeFactory ef = new ClassBasedEdgeFactory(DefaultEdge.class);
		PropertyGraph graph = new PropertyGraph();
		gen.generateGraph(graph, new IntegerVertexFactory(1), null);
		Assert.assertTrue((graph.edgeSet().size() <= 6) && (graph.edgeSet().size() >= 3));
		Assert.assertTrue((graph.vertexSet().size() <= 6) && (graph.vertexSet().size() >= 4));
	}

	@Test
	public void graphGeneratorValueTest() {
		GraphGenerator gen = new RandomConnectedGraphGenerator(2, 2, 1, 1);
		PropertyGraph graph = new PropertyGraph();
		gen.generateGraph(graph, new IntegerVertexFactory(1), null);
		Assert.assertTrue((graph.edgeSet().size() == 1));
		Assert.assertTrue((graph.vertexSet().size() == 2));
	}

	@Test
	public void graphGeneratorEdgeValueTest1() {
		GraphGenerator gen = new RandomConnectedGraphGenerator(4, 4, 3, 3);
		PropertyGraph graph = new PropertyGraph();
		gen.generateGraph(graph, new IntegerVertexFactory(1), null);
		Assert.assertTrue((graph.edgeSet().size() == 3));
		Assert.assertTrue((graph.vertexSet().size() == 4));
	}

	@Test
	public void graphGeneratorEdgeValueTest2() {
		GraphGenerator gen = new RandomConnectedGraphGenerator(4, 4, 6, 6);
		PropertyGraph graph = new PropertyGraph();
		gen.generateGraph(graph, new IntegerVertexFactory(1), null);
		Assert.assertTrue((graph.edgeSet().size() == 6));
		Assert.assertTrue((graph.vertexSet().size() == 4));
	}

	@Test
	public void graphGeneratorEdgeValueTest3() {
		GraphGenerator gen = new RandomConnectedGraphGenerator(4, 4, 1, 6);
		PropertyGraph graph = new PropertyGraph();
		gen.generateGraph(graph, new IntegerVertexFactory(1), null);
		Assert.assertTrue((graph.edgeSet().size() >= 3 && graph.edgeSet().size() <= 6));
		Assert.assertTrue((graph.vertexSet().size() == 4));
	}

	@Test
	public void graphGeneratorEdgeValueTest4() {
		GraphGenerator gen = new RandomConnectedGraphGenerator(4, 4, 1, 8);
		PropertyGraph graph = new PropertyGraph();
		gen.generateGraph(graph, new IntegerVertexFactory(1), null);
		Assert.assertTrue((graph.edgeSet().size() >= 3 && graph.edgeSet().size() <= 6));
		Assert.assertTrue((graph.vertexSet().size() == 4));
	}

	@Test (expected = IllegalArgumentException.class)
	public void graphGeneratorEdgeValueTest5() {
		GraphGenerator gen = new RandomConnectedGraphGenerator(4, 4, 7, 8);
		PropertyGraph graph = new PropertyGraph();
		gen.generateGraph(graph, new IntegerVertexFactory(1), null);
	}

	@Test (expected = IllegalArgumentException.class)
	public void graphGeneratorVertexValueTest1() {
		GraphGenerator gen = new RandomConnectedGraphGenerator(1, 3, 5, 5);
		PropertyGraph graph = new PropertyGraph();
		gen.generateGraph(graph, new IntegerVertexFactory(1), null);
	}

	@Test
	public void graphGeneratorVertexValueTest2() {
		GraphGenerator gen = new RandomConnectedGraphGenerator(1, 5, 5, 5);
		PropertyGraph graph = new PropertyGraph();
		gen.generateGraph(graph, new IntegerVertexFactory(1), null);
		Assert.assertTrue((graph.vertexSet().size() >= 3 && graph.vertexSet().size() <= 6));
		Assert.assertTrue((graph.edgeSet().size() == 5));
	}

	@Test
	public void graphGeneratorHighValueTest() {
		GraphGenerator gen = new RandomConnectedGraphGenerator(20, 20, 0, 400);
		PropertyGraph graph = new PropertyGraph();
		gen.generateGraph(graph, new IntegerVertexFactory(1), null);
		Assert.assertTrue((graph.edgeSet().size() >= 0));
		Assert.assertTrue((graph.vertexSet().size() == 20));
	}

	@Test
	public void graphGeneratorHighIntervalTest() {
		GraphGenerator gen = new RandomConnectedGraphGenerator(1, 20, 0, 400);
		PropertyGraph graph = new PropertyGraph<>();
		gen.generateGraph(graph, new IntegerVertexFactory(1), null);
		Assert.assertTrue((graph.vertexSet().size() > 0) && (graph.vertexSet().size() <= 20));
		Assert.assertTrue((graph.edgeSet().size() >= 0) && (graph.edgeSet().size() <= 400));
	}

}

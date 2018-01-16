package edu.kit.ipd.dbis.org.jgrapht.additions.generate;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.jgrapht.alg.isomorphism.VF2GraphIsomorphismInspector;
import org.jgrapht.alg.util.IntegerVertexFactory;
import org.jgrapht.generate.GraphGenerator;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Assert;
import org.junit.Test;


public class RandomConnectedGraphGeneratorTest {




	@Test (expected = IllegalArgumentException.class)
	public void negativeParameterTest() {
		GraphGenerator<Integer, DefaultEdge, Integer> gen =
				new RandomConnectedGraphGenerator<>(-1, 6, 3, 6);
	}

	@Test (expected = IllegalArgumentException.class)
	public void toLittleEdgesParameterTest() {
		GraphGenerator<Integer, DefaultEdge, Integer> gen =
				new RandomConnectedGraphGenerator<>(5, 10, 0, 3);
	}

	@Test (expected = IllegalArgumentException.class)
	public void toManyEdgesParameterTest() {
		GraphGenerator<Integer, DefaultEdge, Integer> gen =
				new RandomConnectedGraphGenerator<>(5, 10, 100, 120);
	}

	@Test
	public void zeroTest() {
		GraphGenerator<Integer, DefaultEdge, Integer> gen =
				new RandomConnectedGraphGenerator<>(0, 0, 0, 0);
		ClassBasedEdgeFactory<Integer, DefaultEdge> ef = new ClassBasedEdgeFactory<>(DefaultEdge.class);
		PropertyGraph<Integer, DefaultEdge> graph = new PropertyGraph<>(ef, false);
		gen.generateGraph(graph, new IntegerVertexFactory(1), null);
		Assert.assertTrue(graph.vertexSet().size() == 0);
	}

	@Test
	public void graphGeneratorIntervalTest() {
		GraphGenerator<Integer, DefaultEdge, Integer> gen =
				new RandomConnectedGraphGenerator<>(4, 6, 3, 6);
		ClassBasedEdgeFactory<Integer, DefaultEdge> ef = new ClassBasedEdgeFactory<>(DefaultEdge.class);
		PropertyGraph<Integer, DefaultEdge> graph = new PropertyGraph<>(ef, false);
		gen.generateGraph(graph, new IntegerVertexFactory(1), null);
		Assert.assertTrue((graph.edgeSet().size() <= 6) && (graph.edgeSet().size() >= 3));
		Assert.assertTrue((graph.vertexSet().size() <= 6) && (graph.vertexSet().size() >= 4));
	}

	@Test
	public void graphGeneratorValueTest() {
		GraphGenerator<Integer, DefaultEdge, Integer> gen =
				new RandomConnectedGraphGenerator<>(2, 2, 1, 1);
		ClassBasedEdgeFactory<Integer, DefaultEdge> ef = new ClassBasedEdgeFactory<>(DefaultEdge.class);
		PropertyGraph<Integer, DefaultEdge> graph = new PropertyGraph<>(ef, false);
		gen.generateGraph(graph, new IntegerVertexFactory(1), null);
		Assert.assertTrue((graph.edgeSet().size() == 1));
		Assert.assertTrue((graph.vertexSet().size() == 2));
	}

	@Test
	public void graphGeneratorHighValueTest() {
		GraphGenerator<Integer, DefaultEdge, Integer> gen =
				new RandomConnectedGraphGenerator<>(20, 20, 0, 400);
		ClassBasedEdgeFactory<Integer, DefaultEdge> ef = new ClassBasedEdgeFactory<>(DefaultEdge.class);
		PropertyGraph<Integer, DefaultEdge> graph = new PropertyGraph<>(ef, false);
		gen.generateGraph(graph, new IntegerVertexFactory(1), null);
		Assert.assertTrue((graph.edgeSet().size() >= 0));
		Assert.assertTrue((graph.vertexSet().size() == 20));
	}

	@Test
	public void graphGeneratorHighIntervalTest() {
		GraphGenerator<Integer, DefaultEdge, Integer> gen =
				new RandomConnectedGraphGenerator<>(0, 20, 0, 400);
		ClassBasedEdgeFactory<Integer, DefaultEdge> ef = new ClassBasedEdgeFactory<>(DefaultEdge.class);
		PropertyGraph<Integer, DefaultEdge> graph = new PropertyGraph<>(ef, false);
		gen.generateGraph(graph, new IntegerVertexFactory(1), null);
		Assert.assertTrue((graph.vertexSet().size() >= 0) && (graph.vertexSet().size() <= 20));
		Assert.assertTrue((graph.edgeSet().size() >= 0) && (graph.edgeSet().size() <= 400));
	}

}

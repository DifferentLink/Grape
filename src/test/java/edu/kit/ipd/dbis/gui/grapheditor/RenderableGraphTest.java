package edu.kit.ipd.dbis.gui.grapheditor;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.color.MinimalTotalColoring;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.TotalColoringAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.VertexColoring;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class RenderableGraphTest {

	@Test
	public void constructorTest() {
		PropertyGraph graph = new PropertyGraph();
		graph.addVertex(1);
		graph.addVertex(2);
		graph.addVertex(3);
		graph.addVertex(4);
		graph.addEdge(1, 2);
		graph.addEdge(1, 3);
		graph.addEdge(2, 3);
		graph.addEdge(2, 4);
		RenderableGraph renderableGraph = new RenderableGraph(graph, new VertexFactory());
		assertEquals(graph.vertexSet().size(), renderableGraph.getVertices().size());
		assertEquals(graph.edgeSet().size(), renderableGraph.getEdges().size());
	}

	@Test
	public void constructorTestTotalColoring() {
		PropertyGraph<Integer, Integer> graph = new PropertyGraph();
		graph.addVertex(1);
		graph.addVertex(2);
		graph.addVertex(3);
		graph.addVertex(4);
		graph.addEdge(1, 2);
		graph.addEdge(1, 3);
		graph.addEdge(2, 3);
		graph.addEdge(2, 4);

		MinimalTotalColoring alg = new MinimalTotalColoring(graph);

		TotalColoringAlgorithm.TotalColoring coloring = alg.getColoring();
		RenderableGraph renderableGraph = new RenderableGraph(graph, coloring, new VertexFactory());

		assertEquals(graph.vertexSet().size(), renderableGraph.getVertices().size());
		assertEquals(graph.edgeSet().size(), renderableGraph.getEdges().size());
	}

	@Test
	public void asPropertyGraph() {
		PropertyGraph graph = new PropertyGraph();
		graph.addVertex(0);
		graph.addVertex(1);
		graph.addVertex(2);
		graph.addVertex(3);
		graph.addEdge(0, 1);
		graph.addEdge(0, 2);
		graph.addEdge(1, 2);
		graph.addEdge(1, 3);
		RenderableGraph renderableGraph = new RenderableGraph(graph, new VertexFactory());
		assertEquals(true, graph.equals(renderableGraph.asPropertyGraph()));
	}

	@Test
	public void asPropertyGraph2() {
		PropertyGraph<Integer, Integer> graph = new PropertyGraph<>();
		graph.addVertex(0);
		graph.addVertex(1);
		graph.addVertex(2);
		graph.addVertex(3);
		graph.addEdge(0, 1);
		graph.addEdge(0, 2);
		graph.addEdge(1, 2);
		graph.addEdge(1, 3);
		RenderableGraph renderableGraph = new RenderableGraph(graph, new VertexFactory());
		PropertyGraph<Integer, Integer> graph2 = renderableGraph.asPropertyGraph();
		assertEquals(true, graph.equals(graph2));
		renderableGraph = new RenderableGraph(graph2, new VertexFactory());
		graph2 = renderableGraph.asPropertyGraph();
		assertEquals(true, graph.equals(graph2));
	}

	@Test
	public void asPropertyGraph3() {
		PropertyGraph<Integer, Integer> graph = new PropertyGraph<>();
		graph.addVertex(0);
		graph.addVertex(1);
		graph.addVertex(2);
		graph.addVertex(3);
		graph.addEdge(0, 1);
		graph.addEdge(0, 2);
		graph.addEdge(1, 2);
		graph.addEdge(1, 3);
		VertexColoringAlgorithm.Coloring<Integer> coloring =
				((List<VertexColoringAlgorithm.Coloring<Integer>>) graph.getProperty(VertexColoring.class).getValue()).get(0);

		RenderableGraph renderableGraph = new RenderableGraph(graph, coloring, new VertexFactory());
		PropertyGraph<Integer, Integer> graph2 = renderableGraph.asPropertyGraph();
		assertEquals(true, graph.equals(graph2));
		renderableGraph = new RenderableGraph(graph2, coloring, new VertexFactory());
		graph2 = renderableGraph.asPropertyGraph();
		assertEquals(true, graph.equals(graph2));
		renderableGraph = new RenderableGraph(graph2, coloring, new VertexFactory());
		graph2 = renderableGraph.asPropertyGraph();
		assertEquals(true, graph.equals(graph2));
	}

	@Test
	public void construvtorVertexColoringTest() {
		PropertyGraph<Integer, Integer> graph = new PropertyGraph<>();
		graph.addVertex(0);
		graph.addVertex(1);
		graph.addVertex(2);
		graph.addVertex(3);
		graph.addEdge(0, 1);
		graph.addEdge(0, 2);
		graph.addEdge(2, 3);
		VertexColoringAlgorithm.Coloring coloring = ((List<VertexColoringAlgorithm.Coloring>) graph.getProperty(VertexColoring.class).getValue()).get(0);
		RenderableGraph renderableGraph = new RenderableGraph(graph, coloring);
		PropertyGraph graph2 = renderableGraph.asPropertyGraph();
		System.out.println(graph2.edgeSet());
	}
}
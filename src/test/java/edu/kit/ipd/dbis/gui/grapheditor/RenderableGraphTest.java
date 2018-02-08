package edu.kit.ipd.dbis.gui.grapheditor;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.color.MinimalTotalColoring;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.TotalColoringAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RenderableGraphTest {

	@Test
	public void constructorTest() {
		PropertyGraph graph = new PropertyGraph();
		graph.addVertex("a");
		graph.addVertex("b");
		graph.addVertex("c");
		graph.addVertex("d");
		graph.addEdge("a", "b");
		graph.addEdge("a", "c");
		graph.addEdge("b", "c");
		graph.addEdge("b", "d");
		RenderableGraph renderableGraph = new RenderableGraph(graph);
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
		RenderableGraph renderableGraph = new RenderableGraph(graph, coloring);

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
		RenderableGraph renderableGraph = new RenderableGraph(graph);
		assertEquals(true, graph.equals(renderableGraph.asPropertyGraph()));
	}
}
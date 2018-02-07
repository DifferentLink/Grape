package edu.kit.ipd.dbis.gui.grapheditor;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.TotalColoringAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

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
	public void constructorTestVertexColoring() {
		PropertyGraph graph = new PropertyGraph();
		graph.addVertex("a");
		graph.addVertex("b");
		graph.addVertex("c");
		graph.addVertex("d");
		graph.addEdge("a", "b");
		graph.addEdge("a", "c");
		graph.addEdge("b", "c");
		graph.addEdge("b", "d");

		Map<String, Integer> colors = new HashMap<>();
		colors.put("a", 0);
		colors.put("b", 1);
		colors.put("c", 2);
		colors.put("d", 0);
		VertexColoringAlgorithm.Coloring coloring = new VertexColoringAlgorithm.ColoringImpl(colors, 3);

		RenderableGraph renderableGraph = new RenderableGraph(graph, coloring);

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

		Map<Integer, Integer> vertexColors = new HashMap<>();
		vertexColors.put(1, 0);
		vertexColors.put(2, 1);
		vertexColors.put(3, 2);
		vertexColors.put(4, 0);
		Map<Integer, Integer> edgeColors = new HashMap<>();
		edgeColors.put(0, 0);
		edgeColors.put(1, 1);
		edgeColors.put(2, 2);
		edgeColors.put(3, 3);

		TotalColoringAlgorithm.TotalColoring coloring = new TotalColoringAlgorithm.TotalColoringImpl(vertexColors, edgeColors, 4);

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
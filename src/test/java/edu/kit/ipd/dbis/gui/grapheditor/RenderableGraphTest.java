package edu.kit.ipd.dbis.gui.grapheditor;

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
	public void asPropertyGraph() {
	}
}
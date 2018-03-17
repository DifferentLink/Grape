package edu.kit.ipd.dbis.org.jgrapht.additions;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

public class Util {
	public static PropertyGraph<Integer, Integer> createCompleteGraph(int numberOfVertices) {
		PropertyGraph<Integer, Integer> graph = new PropertyGraph<>();
		for (int i = 0; i < numberOfVertices; i++) {
			graph.addVertex(i);
		}
		for (int i = 0; i < numberOfVertices; i++) {
			for (int j = 0; j < numberOfVertices; j++) {
				if (j != i && !graph.containsEdge(graph.getEdgeFactory().createEdge(j, i))) {
					graph.addEdge(i, j);

				}
			}
		}
		return graph;
	}

	public static PropertyGraph generateSimpleTestGraph() {
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

	public static PropertyGraph generateSimpleTestGraph2() {
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
		graph.addEdge("d", "e");
		return graph;
	}

	public static PropertyGraph generateSimpleTestGraph3() {
		PropertyGraph graph = new PropertyGraph();
		graph.addVertex("a");
		graph.addVertex("b");
		graph.addVertex("c");
		graph.addEdge("a", "b");
		graph.addEdge("a", "c");
		return graph;
	}
}

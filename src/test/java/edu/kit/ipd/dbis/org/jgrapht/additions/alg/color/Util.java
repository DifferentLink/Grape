package edu.kit.ipd.dbis.org.jgrapht.additions.alg.color;

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
}

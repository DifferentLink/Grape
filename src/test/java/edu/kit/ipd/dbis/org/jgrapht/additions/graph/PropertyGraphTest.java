package edu.kit.ipd.dbis.org.jgrapht.additions.graph;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PropertyGraphTest {

	@Test
	public void getAdjacencyMatrix() {
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

		List<int[]> expected = new ArrayList<>();
		expected.add(new int[]{0, 1, 1, 0});
		expected.add(new int[]{1, 0, 1, 1});
		expected.add(new int[]{1, 1, 0, 1});
		expected.add(new int[]{0, 1, 1, 0});
		int[][] result = graph.getAdjacencyMatrix();
		for (int i = 0; i < expected.size(); i++) {
			int[] expectedLine = expected.get(i);
			int[] resultLine = result[i];
			for (int j = 0; j < expectedLine.length; j++) {
				assertEquals(expectedLine[j], resultLine[j]);
			}
		}
	}
}
package edu.kit.ipd.dbis.org.jgrapht.additions.alg.density;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class MinimalBfsCodeAlgorithmTest {

	private PropertyGraph generateSimpleTestGraph1() {
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
		return graph;
	}

	@Test
	public void getBfsCode() {
		PropertyGraph graph = this.generateSimpleTestGraph1();
		BfsCodeAlgorithm alg = new MinimalBfsCodeAlgorithm();
		assertArrayEquals(new int[] {1,1,2,1,1,3,-1,2,3,1,1,4,-1,2,4}, alg.getBfsCode(graph).getCode());
	}

	@Test
	public void getAdjacencyMatrix() {
		PropertyGraph graph = this.generateSimpleTestGraph1();
		MinimalBfsCodeAlgorithm alg = new MinimalBfsCodeAlgorithm();
		List<Object> sortedVertices = new ArrayList<>(new TreeSet<>(graph.vertexSet()));
		List<int[]> expected = new ArrayList<>();
		expected.add(new int[] {0,1,1,0});
		expected.add(new int[] {1,0,1,1});
		expected.add(new int[] {1,1,0,1});
		expected.add(new int[] {0,1,1,0});
		List<int[]> result = alg.getAdjacencyMatrix(graph);
		for (int i = 0; i < expected.size(); i++) {
			int[] expectedLine = expected.get(i);
			int[] resultLine = result.get(i);
 			for (int j = 0; j < expectedLine.length; j++) {
 				assertEquals(expectedLine[j], resultLine[j]);
			}
		}

	}
}
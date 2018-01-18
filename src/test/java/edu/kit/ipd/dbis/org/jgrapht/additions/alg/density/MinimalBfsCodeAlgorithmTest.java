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
		return graph;
	}

	@Test
	public void getlocalBfsCode() {
		PropertyGraph graph = generateSimpleTestGraph1();
		MinimalBfsCodeAlgorithm alg = new MinimalBfsCodeAlgorithm();
		assertArrayEquals(new int[] {1,1,2,1,1,3,-1,2,3,1,1,4,1,2,5,-1,4,5,1,2,6,-1,4,6,1,2,7,-1,5,7}, alg.getLocalBfsCode(graph, "d").getCode());
	}

	@Test
	public void bfsCodeTest() {
		PropertyGraph graph = generateSimpleTestGraph1();
		String[] perm = {"d", "e", "b", "a", "g", "f", "c"};
		MinimalBfsCodeAlgorithm<String, DefaultEdge> alg = new MinimalBfsCodeAlgorithm<>();
		int[] result = alg.calculateBFS(graph, perm, 5);
		System.out.print("[");
		for (int i = 0; i < result.length; i++) {
			System.out.print(result[i] + ", ");
		}
		System.out.println("]");
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
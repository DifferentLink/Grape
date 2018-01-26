package edu.kit.ipd.dbis.org.jgrapht.additions.alg.density;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.ProfileDensityAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Assert;
import org.junit.Test;

public class MinimalProfileAlgorithmTest {

	private PropertyGraph generateSimpleTestGraph1() {
		PropertyGraph graph = new PropertyGraph();
		graph.addVertex("a");
		graph.addVertex("b");
		graph.addVertex("c");
		graph.addVertex("d");
		graph.addVertex("e");
		graph.addVertex("f");
		graph.addVertex("i");
		graph.addEdge("d", "a");
		graph.addEdge("d", "e");
		graph.addEdge("d", "b");
		graph.addEdge("a", "f");
		graph.addEdge("e", "f");
		graph.addEdge("e", "b");
		graph.addEdge("e", "i");
		graph.addEdge("e", "c");
		graph.addEdge("i", "c");
		graph.addEdge("a", "i");
		return graph;
	}
	private PropertyGraph generateSimpleTestGraph2() {
		PropertyGraph graph = new PropertyGraph();
		graph.addVertex("a");
		graph.addVertex("b");
		graph.addVertex("c");
		graph.addVertex("d");
		graph.addVertex("e");
		graph.addVertex("f");
		graph.addVertex("i");
		graph.addEdge("d", "a");
		graph.addEdge("d", "e");
		graph.addEdge("d", "b");
		graph.addEdge("a", "f");
		graph.addEdge("e", "f");
		graph.addEdge("e", "b");
		graph.addEdge("e", "i");
		graph.addEdge("e", "c");
		graph.addEdge("i", "c");
		return graph;
	}

	@Test
	public void profileTest() {
		PropertyGraph graph = generateSimpleTestGraph1();
		MinimalProfileAlgorithm<String, DefaultEdge> alg = new MinimalProfileAlgorithm<>();
		int[][] result = alg.getProfile(graph).getMatrix();

		int[][] profile = {{1,1,2,1,1,3,-1,2,3,1,1,4,1,1,5,-1,4,5,1,1,6,1,2,7,-1,4,7,-1,6,7},
		 {1,1,2,1,1,3,-1,2,3,1,1,4,1,2,5,-1,4,5,1,2,6,-1,4,6,1,2,7,-1,5,7},
		 {1,1,2,1,1,3,-1,2,3,1,1,4,1,2,5,-1,4,5,1,2,6,-1,4,6,1,2,7,-1,5,7},
		 {1,1,2,1,1,3,-1,2,3,1,2,4,1,3,5,-1,4,5,1,3,6,-1,4,6,1,3,7,-1,5,7},
		 {1,1,2,1,1,3,-1,2,3,1,2,4,1,3,5,-1,4,5,1,3,6,-1,4,6,1,3,7,-1,5,7},
		 {1,1,2,1,1,3,1,1,4,1,2,5,-1,3,5,-1,4,5,1,3,6,-1,5,6,1,4,7,-1,5,7},
		 {1,1,2,1,1,3,1,2,4,-1,3,4,1,2,5,-1,3,5,1,3,6,-1,4,6,1,3,7,-1,5,7}};
		ProfileDensityAlgorithm.Profile p = new ProfileDensityAlgorithm.ProfileImpl(profile);
		Assert.assertTrue(p.compareTo(new ProfileDensityAlgorithm.ProfileImpl<>(result)) == 0);
	}

	@Test
	public void profileCompareTest() {
		PropertyGraph graph1 = generateSimpleTestGraph1();
		PropertyGraph graph2 = generateSimpleTestGraph2();
		MinimalProfileAlgorithm<String, DefaultEdge> alg = new MinimalProfileAlgorithm<>();

		int[][] result2 = alg.getProfile(graph2).getMatrix();
		int[][] result1 = alg.getProfile(graph1).getMatrix();

		ProfileDensityAlgorithm.Profile p1 = new ProfileDensityAlgorithm.ProfileImpl(result1);
		ProfileDensityAlgorithm.Profile p2 = new ProfileDensityAlgorithm.ProfileImpl(result2);

		Assert.assertTrue(p1.compareTo(p2) == -1);

	}
}

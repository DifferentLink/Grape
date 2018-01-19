package edu.kit.ipd.dbis.org.jgrapht.additions.alg.density;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.ProfileDensityAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Assert;
import org.junit.Test;

public class MinimalProfileAlgorithmTest {

	private PropertyGraph generateSimpleTestGraph() {
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


	@Test
	public void profileTest() {
		PropertyGraph graph = generateSimpleTestGraph();
		MinimalProfileAlgorithm<String, DefaultEdge> alg = new MinimalProfileAlgorithm<>();
		int[][] result = alg.getProfile(graph).getMatrix();
		/**
		int[][] profile = {{1},{2}};
		ProfileDensityAlgorithm.Profile p = new ProfileDensityAlgorithm.ProfileImpl(profile);
		Assert.assertTrue(p.compareTo(new ProfileDensityAlgorithm.ProfileImpl<>(result)) == 0);
		 */
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[0].length; j++) {
				System.out.print(result[i][j]);
			}
			System.out.println();
		}
	}
}

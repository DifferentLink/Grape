package edu.kit.ipd.dbis.org.jgrapht.additions.graph;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.ProfileDensityAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.BfsCode;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.Profile;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_.AverageDegree;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.GreatestDegree;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.NumberOfCliques;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.NumberOfEdges;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.SmallestDegree;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PropertyGraphTest {

	private PropertyGraph generateSimpleTestGraph() {
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

	@Test
	@Ignore
	public void calculateAllPropertyTest() {
		PropertyGraph graph = generateSimpleTestGraph();
		graph.calculateProperties();
		int[] code = {1,1,2,1,1,3,-1,2,3,1,1,4,1,1,5,-1,4,5,1,1,6,1,2,7,-1,4,7,-1,6,7};
		BfsCodeAlgorithm.BfsCodeImpl minBfsCode = new BfsCodeAlgorithm.BfsCodeImpl(code);

		int[][] profileCode = {{1,1,2,1,1,3,-1,2,3,1,1,4,1,1,5,-1,4,5,1,1,6,1,2,7,-1,4,7,-1,6,7},
				{1,1,2,1,1,3,-1,2,3,1,1,4,1,2,5,-1,4,5,1,2,6,-1,4,6,1,2,7,-1,5,7},
				{1,1,2,1,1,3,-1,2,3,1,1,4,1,2,5,-1,4,5,1,2,6,-1,4,6,1,2,7,-1,5,7},
				{1,1,2,1,1,3,-1,2,3,1,2,4,1,3,5,-1,4,5,1,3,6,-1,4,6,1,3,7,-1,5,7},
				{1,1,2,1,1,3,-1,2,3,1,2,4,1,3,5,-1,4,5,1,3,6,-1,4,6,1,3,7,-1,5,7},
				{1,1,2,1,1,3,1,1,4,1,2,5,-1,3,5,-1,4,5,1,3,6,-1,5,6,1,4,7,-1,5,7},
				{1,1,2,1,1,3,1,2,4,-1,3,4,1,2,5,-1,3,5,1,3,6,-1,4,6,1,3,7,-1,5,7}};
		ProfileDensityAlgorithm.ProfileImpl profile = new ProfileDensityAlgorithm.ProfileImpl(profileCode);
		Assert.assertTrue(((BfsCodeAlgorithm.BfsCodeImpl) graph.getProperty(BfsCode.class).getValue())
				.compareTo(minBfsCode) == 0);
		Assert.assertTrue(((ProfileDensityAlgorithm.ProfileImpl) graph.getProperty(Profile.class).getValue())
				.compareTo(profile) == 0);
		Assert.assertTrue(graph.getProperty(NumberOfCliques.class).getValue().equals(6));
		Assert.assertTrue(graph.getProperty(GreatestDegree.class).getValue().equals(5));
		Assert.assertTrue(graph.getProperty(SmallestDegree.class).getValue().equals(2));
		Assert.assertTrue(Math.abs((Double) graph.getProperty(AverageDegree.class).getValue() - 2.857142857) < 0.001);
		Assert.assertTrue(graph.getProperty(NumberOfEdges.class).getValue().equals(10));
	}

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
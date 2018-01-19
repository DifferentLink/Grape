package edu.kit.ipd.dbis.org.jgrapht.additions.generate;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashSet;

public class BulkRandomConnectedGraphGeneratorTest {


	@Test(expected = IllegalArgumentException.class)
	public void negativeParameterTest() {
		BulkGraphGenerator<Integer, DefaultEdge> bulkGen =
				new BulkRandomConnectedGraphGenerator<>();

		HashSet<PropertyGraph> target = new HashSet<>();
		bulkGen.generateBulk(target,-1, 2, 2, 1, 1);
	}

	@Test (expected = IllegalArgumentException.class)
	public void quantityTooBigTest() {
		BulkGraphGenerator<Integer, DefaultEdge> bulkGen =
				new BulkRandomConnectedGraphGenerator<>();

		HashSet<PropertyGraph> target = new HashSet<>();
		bulkGen.generateBulk(target,10, 2, 2, 1, 1);
		Assert.assertTrue(target.size() == 1000);
	}

	@Test
	public void completeParameterTest() {
		BulkGraphGenerator<Integer, DefaultEdge> bulkGen =
				new BulkRandomConnectedGraphGenerator<>();

		HashSet<PropertyGraph> target = new HashSet<>();
		try {
			bulkGen.generateBulk(target, 2000, 5, 5, 1, 1000);
		} catch (IllegalArgumentException e) {
			System.out.println(target.size());
			Assert.assertTrue(target.size() == 21);
		}
	}

	@Ignore
	@Test
	public void largeParameterTest() {
		BulkGraphGenerator<Integer, DefaultEdge> bulkGen =
				new BulkRandomConnectedGraphGenerator<>();

		HashSet<PropertyGraph> target = new HashSet<>();
		bulkGen.generateBulk(target,3000, 0, 20, 1, 3000);
		Assert.assertTrue(target.size() == 3000);
	}
}

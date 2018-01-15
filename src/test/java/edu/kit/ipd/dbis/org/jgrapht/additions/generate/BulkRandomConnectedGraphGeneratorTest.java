package edu.kit.ipd.dbis.org.jgrapht.additions.generate;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.jgrapht.Graph;
import org.jgrapht.generate.GraphGenerator;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class BulkRandomConnectedGraphGeneratorTest {


	@Test(expected = IllegalArgumentException.class)
	public void negativeParameterTest() {
		BulkGraphGenerator<Integer, DefaultEdge> bulkGen =
				new BulkRandomConnectedGraphGenerator<>();

		HashSet<Graph> target = new HashSet<>();
		bulkGen.generateBulk(target,-1, 2, 2, 1, 1);
	}

	@Test
	public void smallParameterTest() {
		BulkGraphGenerator<Integer, DefaultEdge> bulkGen =
				new BulkRandomConnectedGraphGenerator<>();

		HashSet<Graph> target = new HashSet<>();
		bulkGen.generateBulk(target,1, 2, 2, 1, 1);
	}
}

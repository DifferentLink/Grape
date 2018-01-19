package edu.kit.ipd.dbis.org.jgrapht.additions.generate;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.jgrapht.Graph;
import org.jgrapht.alg.isomorphism.VF2GraphIsomorphismInspector;
import org.jgrapht.alg.util.IntegerVertexFactory;
import org.jgrapht.generate.GraphGenerator;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

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
}

package edu.kit.ipd.dbis.org.jgrapht.additions;

import edu.kit.ipd.dbis.org.jgrapht.additions.generate.BulkGraphGenerator;
import edu.kit.ipd.dbis.org.jgrapht.additions.generate.BulkRandomConnectedGraphGenerator;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.junit.Test;

import java.util.HashSet;

public class JGraphAdditionsTest {

	@Test
	public void completeGeneratingTest() {
		BulkGraphGenerator bulkGen = new BulkRandomConnectedGraphGenerator();
		HashSet<PropertyGraph> target = new HashSet<>();
		try {
			bulkGen.generateBulk(target, 10, 1, 15, 1, 55);
		} catch (IllegalArgumentException e) {
			System.out.println("error by generating graphs");
			return;
		}
		for (PropertyGraph graph : target) {
			graph.calculateProperties();
		}
	}
}

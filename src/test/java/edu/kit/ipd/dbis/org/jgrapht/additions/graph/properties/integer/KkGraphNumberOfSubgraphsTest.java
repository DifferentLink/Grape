package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.junit.Assert;
import org.junit.Test;



public class KkGraphNumberOfSubgraphsTest {
	@Test
	public void generatedGraphTest() {
		int[] code = {1,1,2,1,1,3,1,1,4,1,1,5,1,2,6,1,6,7};
		BfsCodeAlgorithm.BfsCodeImpl bfs = new BfsCodeAlgorithm.BfsCodeImpl(code);
		PropertyGraph graph = new PropertyGraph(bfs);

		int numberOfSubgraphs = (Integer) graph.getProperty(KkGraphNumberOfSubgraphs.class).getValue();
		Assert.assertTrue(numberOfSubgraphs == 2);
	}
}

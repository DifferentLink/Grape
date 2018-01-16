package edu.kit.ipd.dbis.org.jgrapht.additions.alg.density;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Test;

import static org.junit.Assert.*;

public class MinimalBfsCodeAlgorithmTest {

	@Test
	public void getBfsCode() {
		EdgeFactory ef = new ClassBasedEdgeFactory(DefaultEdge.class);
		PropertyGraph graph = new PropertyGraph(ef, false);
		graph.addVertex("a");
		graph.addVertex("b");
		graph.addVertex("c");
		graph.addVertex("d");
		graph.addEdge("a", "b");
		graph.addEdge("a", "c");
		graph.addEdge("b", "c");
		graph.addEdge("b", "d");
		graph.addEdge("c", "d");

		BfsCodeAlgorithm alg = new MinimalBfsCodeAlgorithm();
		assertArrayEquals(new int[] {1,1,2,1,1,3,-1,2,3,1,1,4,-1,2,4}, alg.getBfsCode(graph).getCode());
	}
}
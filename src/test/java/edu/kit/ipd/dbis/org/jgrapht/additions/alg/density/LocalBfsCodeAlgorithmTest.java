package edu.kit.ipd.dbis.org.jgrapht.additions.alg.density;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.jgrapht.alg.clique.BronKerboschCliqueFinder;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class LocalBfsCodeAlgorithmTest {

	private PropertyGraph<String, String> generateSimpleTestGraph() {
		PropertyGraph<String, String> graph = new PropertyGraph<>();
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
	public void localBfsCodeTest() {
		PropertyGraph<String, String> graph = generateSimpleTestGraph();
		LocalBfsCodeAlgorithm<String> alg = new LocalBfsCodeAlgorithm<>("e");
		int[] result = alg.getBfsCode(graph).getCode();
		int[] local = {1, 1, 2, 1, 1, 3, -1, 2, 3, 1, 1, 4, 1, 1, 5, -1, 4, 5, 1, 1, 6, 1, 2, 7, -1, 4, 7, -1, 6, 7};
		BfsCodeAlgorithm.BfsCodeImpl localCode = new BfsCodeAlgorithm.BfsCodeImpl(local);
		Assert.assertTrue(localCode.compareTo(new BfsCodeAlgorithm.BfsCodeImpl<>(result)) == 0);
	}

	@Test
	public void test() {
		PropertyGraph<String, String> graph = generateSimpleTestGraph();
		ArrayList<Set<Object>> result = new ArrayList<>();
		BronKerboschCliqueFinder alg = new BronKerboschCliqueFinder(graph);
		Iterator<Set<Object>> it = alg.iterator();
		while (it.hasNext()) {
			Set<Object> clique = it.next();
			result.add(clique);
		}
	}

}

package edu.kit.ipd.dbis.org.jgrapht.additions.alg.density;

import edu.kit.ipd.dbis.org.jgrapht.additions.Util;
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
	@Test
	public void localBfsCodeTest() {
		PropertyGraph graph = Util.generateSimpleTestGraph();
		LocalBfsCodeAlgorithm<String> alg = new LocalBfsCodeAlgorithm<>("e");
		int[] result = alg.getBfsCode(graph).getCode();
		int[] local = {1, 1, 2, 1, 1, 3, -1, 2, 3, 1, 1, 4, 1, 1, 5, -1, 4, 5, 1, 1, 6, 1, 2, 7, -1, 4, 7, -1, 6, 7};
		BfsCodeAlgorithm.BfsCodeImpl localCode = new BfsCodeAlgorithm.BfsCodeImpl(local);
		Assert.assertTrue(localCode.compareTo(new BfsCodeAlgorithm.BfsCodeImpl<>(result)) == 0);
	}
}

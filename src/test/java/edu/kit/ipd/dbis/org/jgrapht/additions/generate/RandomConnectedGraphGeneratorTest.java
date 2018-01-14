package edu.kit.ipd.dbis.org.jgrapht.additions.generate;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.jgrapht.alg.util.IntegerVertexFactory;
import org.jgrapht.generate.GraphGenerator;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Test;


public class RandomConnectedGraphGeneratorTest {


	@Test
	public void graphTest() {
		GraphGenerator<Integer, DefaultEdge, Integer> gen =
				new RandomConnectedGraphGenerator<>(4, 6, 3, 6);

		ClassBasedEdgeFactory<Integer, DefaultEdge> ef = new ClassBasedEdgeFactory<Integer, DefaultEdge>(DefaultEdge.class);
		PropertyGraph<Integer, DefaultEdge> g = new PropertyGraph<Integer, DefaultEdge>(ef, false);
		gen.generateGraph(g, new IntegerVertexFactory(1), null);
	}
}

package edu.kit.ipd.dbis.org.jgrapht.additions.alg.kkgraph;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.junit.Test;

public class KkGraphGeneratorTest {

	@Test
	public void Test() {
		PropertyGraph graph = new PropertyGraph();
		KkGraphGenerator g = new KkGraphGenerator(graph);
		int[] a = {1,1,1,0,0};
		for (int i = 0; i < 9; i++) {
			int[] b = g.getNextEdgeCombination(a);
			for (int j = 0; j < b.length; j++) {
				System.out.print(b[j] +  ", ");
			}
			System.out.println();
			a = b;
		}
	}
	@Test
	public void Test1() {
		PropertyGraph graph = new PropertyGraph();
		KkGraphGenerator g = new KkGraphGenerator(graph);
		int[] a = {1,1,1,0,0,1,1,1};
		int[] b = g.getNextEdgeCombination(a);
		for (int j = 0; j < b.length; j++) {
			System.out.print(b[j] +  ", ");
		}
	}
}

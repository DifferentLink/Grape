package edu.kit.ipd.dbis.org.jgrapht.additions.alg.degree;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.DegreeAlgorithm;
import org.jgrapht.Graph;

import java.util.Iterator;

/**
 * Finds the smallest degree of all nodes
 * in a graph.
 * @param <V>
 */
public class MinDegreeFinder<V> implements DegreeAlgorithm {
	private Graph graph;

	/**
	 * Standard constructor
	 * @param graph target graph
	 */
	public MinDegreeFinder(Graph graph) {
		this.graph = graph;
	}

	@Override
	public Integer getDegree() {
		Iterator it = this.graph.vertexSet().iterator();
		int minDegree = Integer.MAX_VALUE;
		int tmp = 0;
		while (it.hasNext()) {
			tmp = this.graph.degreeOf(it.next());
			if (tmp < minDegree) {
				minDegree = tmp;
			}
		}
		return minDegree;
	}
}

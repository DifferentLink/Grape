package edu.kit.ipd.dbis.org.jgrapht.additions.alg.degree;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.DegreeAlgorithm;
import org.jgrapht.Graph;

import java.util.Iterator;

/**
 * Determines the average degree of all nodes
 * in a graph.
 * @param <V>
 */
public class AverageDegreeFinder<V> implements DegreeAlgorithm {
	private Graph graph;

	/**
	 * standard constructor
	 * @param graph target graph
	 */
	public AverageDegreeFinder(Graph graph) {
		this.graph = graph;
	}

	@Override
	public Double getDegree() {
		Iterator it = this.graph.vertexSet().iterator();
		double sum = 0;
		int number = 0;
		while (it.hasNext()) {
			sum += this.graph.degreeOf(it.next());
			number++;
		}
		if (number == 0) {
			return 0.0;
		}
		return sum / number;
	}
}

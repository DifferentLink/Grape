package edu.kit.ipd.dbis.org.jgrapht.additions.generate;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.util.Set;

/**
 * A generator the generates a bulk of graphs.
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 */
public interface BulkGraphGenerator<V, E> {
	/**
	 *Generates a set of graphs with the given restrictions. The target set does not contains any isomorphic graphs.
	 * That means that all graphs are not isomorph to each other.
	 *
	 * @param target the target set
	 * @param quantity the number of generating graphs
	 * @param minVertices the minimal number of vertices
	 * @param maxVertices the maximal number of vertices
	 * @param minEdges the minimal number of edges
	 * @param maxEdges the maximal number of edges
	 */
	void generateBulk(Set<PropertyGraph> target, int quantity, int minVertices, int maxVertices, int minEdges,
					  int maxEdges);
}

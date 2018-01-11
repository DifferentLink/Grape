package edu.kit.ipd.dbis.org.jgrapht.additions.generate;

import org.jgrapht.Graph;

import java.util.Set;

/**
 * A BulkGraphGenerator which creates a set of graphs that are strongly connected. The actual number of generated graphs
 * may be lower than the specied one, as the given restrictions may limit the number of possible graphs.
 * This class uses RandomConnectedGraphGenerator<V,E> to create each individual graph.
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 */
public class BulkRandomConnectedGraphGenerator<V, E> implements BulkGraphGenerator<V, E> {


	@Override
	public void generateBulk(Set<Graph> target, int quantity, int minVertices, int maxVertices, int minEdges,
									int maxEdges) {
		//Import algorithm
	}
}

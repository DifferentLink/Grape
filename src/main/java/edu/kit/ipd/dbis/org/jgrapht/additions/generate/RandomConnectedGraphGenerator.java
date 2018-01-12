package edu.kit.ipd.dbis.org.jgrapht.additions.generate;

import org.jgrapht.Graph;
import org.jgrapht.VertexFactory;
import org.jgrapht.generate.GraphGenerator;

import java.util.Map;

/**
 * A random graph generator. It generates a random graph which is strongly connected, but does not create self-loops or
 multiple edges between the same two vertices.

 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 */
public class RandomConnectedGraphGenerator<V, E> implements GraphGenerator {


	private final int minVertices;
	private final int maxVertices;
	private final int minEdges;
	private final int maxEdges;


	/**
	 * Construct a new radom connected graph generator.
	 *
	 * @param minVertices the minimal vertices of the graph
	 * @param maxVertices the maximal vertices of the graph
	 * @param minEdges the minimal edges of the graph
	 * @param maxEdges the maximal edges of the graph
	 */
	public RandomConnectedGraphGenerator(int minVertices, int maxVertices, int minEdges, int maxEdges) {
		this.minVertices = minVertices;
		this.maxVertices = maxVertices;
		this.minEdges = minEdges;
		this.maxEdges = maxEdges;
	}
	@Override
	public void generateGraph(Graph target, VertexFactory vertexFactory, Map resultMap) {
		//TODO: implement me
	}
}

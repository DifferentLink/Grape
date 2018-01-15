package edu.kit.ipd.dbis.org.jgrapht.additions.generate;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.jgrapht.Graph;
import org.jgrapht.alg.util.IntegerVertexFactory;
import org.jgrapht.generate.GraphGenerator;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DefaultEdge;

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

	/**
	 * Constructs a new bulk random connected graph generator
	 */
	public BulkRandomConnectedGraphGenerator() { }

	@Override
	public  void generateBulk(Set<Graph> target, int quantity, int minVertices, int maxVertices, int minEdges,
							 int maxEdges) {
		if (quantity < 0) {
			throw new IllegalArgumentException("Invalid quantity");
		}
		//TODO: Checken, wie viele Graphen es mit diesen Faktoren schon in der Datenbank gibt.
		//TODO: Ckecken, ob es noch "quanity" übrige Graphen mit diesen Parametern gibt die noch nicht existieren.


		GraphGenerator<Integer, DefaultEdge, Integer> gen =
				new RandomConnectedGraphGenerator<>(minVertices, maxVertices, minEdges, maxEdges);

		while (target.size() < quantity) {
			ClassBasedEdgeFactory<Integer, DefaultEdge> ef = new ClassBasedEdgeFactory<>(DefaultEdge.class);
			PropertyGraph<Integer, DefaultEdge> graph = new PropertyGraph<>(ef, false);
			gen.generateGraph(graph, new IntegerVertexFactory(1), null);

			target.add(graph); //set -> only added if not equals to any other graph in the set
		}
	}
}

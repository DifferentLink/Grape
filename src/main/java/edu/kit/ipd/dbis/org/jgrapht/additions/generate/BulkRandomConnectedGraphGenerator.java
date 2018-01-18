package edu.kit.ipd.dbis.org.jgrapht.additions.generate;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.jgrapht.alg.util.IntegerVertexFactory;
import org.jgrapht.generate.GraphGenerator;
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

	@Override
	public  void generateBulk(Set<PropertyGraph> target, int quantity, int minVertices, int maxVertices, int minEdges,
							 int maxEdges) {
		if (quantity < 0) {
			throw new IllegalArgumentException("negative quantity not valid");
		}

		//TODO: Ckecken, ob es noch "quanity" übrige Graphen mit diesen Parametern gibt die noch nicht existieren.


		GraphGenerator<Integer, DefaultEdge, Integer> gen =
				new RandomConnectedGraphGenerator<>(minVertices, maxVertices, minEdges, maxEdges);

		int cnt = 0;
		while (target.size() < quantity) {
			cnt++;
			PropertyGraph<Integer, DefaultEdge> graph = new PropertyGraph<>();
			gen.generateGraph(graph, new IntegerVertexFactory(1), null);

			boolean isDuplicat = false;
			for (PropertyGraph g : target) {
				if (g.equals(graph)) {
					isDuplicat = true;
				}
			}
			if (!isDuplicat) {
				target.add(graph);
			}
			//TODO: temporary solution (find a better one)
			if (cnt > 5 * quantity) {
				throw new IllegalArgumentException("Only " + target.size() + " graphs found. Quantity: " + quantity +
						" too big.");
			}
		}
	}
}

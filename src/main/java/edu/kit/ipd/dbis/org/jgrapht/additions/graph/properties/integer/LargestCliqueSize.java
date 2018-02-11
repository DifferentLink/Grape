package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.IntegerProperty;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.Cliques;

import java.util.List;
import java.util.Set;

/**
 * the largest clique size property
 */
public class LargestCliqueSize extends IntegerProperty {
	/**
	 * Standard constructor
	 *
	 * @param graph input graph
	 */
	public LargestCliqueSize(PropertyGraph graph) {
		super(graph);
	}

	@Override
	protected Integer calculateAlgorithm(PropertyGraph graph) {
		int largestCliqueSize = 0;
		for (Set<Object> s : ((List<Set<Object>>) graph.getProperty(Cliques.class).getValue())) {
			if (s.size() > largestCliqueSize) {
				largestCliqueSize = s.size();
			}
		}
		return largestCliqueSize;
	}
}

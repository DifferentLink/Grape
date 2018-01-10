package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;

import java.util.List;

/**
 * Represents the possible vertex colorings
 * of a graph.
 */
public class VertexColoringList extends Property {
	@Override
	protected List<VertexColoringAlgorithm.Coloring> calculationAlgorithm(PropertyGraph graph) {
		return null;
	}
}

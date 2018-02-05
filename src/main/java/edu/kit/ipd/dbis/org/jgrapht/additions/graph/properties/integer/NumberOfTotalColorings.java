package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.IntegerProperty;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.TotalColoring;
import org.kohsuke.MetaInfServices;

import java.util.List;

/**
 * Calculates the number of total colorings
 */
@MetaInfServices
public class NumberOfTotalColorings extends IntegerProperty {
	/**
	 * Standard constructor
	 *
	 * @param graph the input graph
	 */
	public NumberOfTotalColorings(PropertyGraph graph) {
		super(graph);
	}

	@Override
	protected Integer calculateAlgorithm(PropertyGraph graph) {
		return ((List<TotalColoring>) graph.getProperty(TotalColoring.class).getValue()).size();
	}
}

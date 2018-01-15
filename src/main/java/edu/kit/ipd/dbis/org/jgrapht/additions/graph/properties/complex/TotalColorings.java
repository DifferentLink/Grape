package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.TotalColoringAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.ComplexProperty;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class TotalColorings extends ComplexProperty {
	@Override
	protected TotalColoringAlgorithm.TotalColoring calculateAlgorithm(PropertyGraph graph) {
		// TODO: need method calculateAllColorings()
		return null;
	}
}

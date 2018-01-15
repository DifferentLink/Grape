package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.density.MinimalBfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.ComplexProperty;
import org.kohsuke.MetaInfServices;

@MetaInfServices(Property.class)
public class BfsCode extends ComplexProperty {
	@Override
	protected BfsCodeAlgorithm.BfsCode calculateAlgorithm(PropertyGraph graph) {
		MinimalBfsCodeAlgorithm alg = new MinimalBfsCodeAlgorithm(graph);
		return alg.getBfsCode();
	}
}

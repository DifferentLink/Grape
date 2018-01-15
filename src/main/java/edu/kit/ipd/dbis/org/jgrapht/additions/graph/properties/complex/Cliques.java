package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.ComplexProperty;
import org.kohsuke.MetaInfServices;

import java.util.List;
import java.util.Set;

@MetaInfServices
public class Cliques extends ComplexProperty {
	@Override
	protected List<Set<Object>> calculateAlgorithm(PropertyGraph graph) {
		return null;
	}
}

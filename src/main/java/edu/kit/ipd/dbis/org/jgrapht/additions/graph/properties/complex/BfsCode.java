package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.ProfileDensityAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.ComplexProperty;
import org.kohsuke.MetaInfServices;

/**
 * the bfs code property
 */
@MetaInfServices(Property.class)
public class BfsCode extends ComplexProperty {
	/**
	 * Standard constructor
	 *
	 * @param graph the input graph
	 */
	public BfsCode(PropertyGraph graph) {
		super(graph);
	}

	@Override
	protected BfsCodeAlgorithm.BfsCode calculateAlgorithm(PropertyGraph graph) {
		BfsCodeAlgorithm.BfsCodeImpl bfs =
				(BfsCodeAlgorithm.BfsCodeImpl) (((ProfileDensityAlgorithm.ProfileImpl) graph.getProperty(Profile.class).getValue()).getBfsList().get(0));
		return bfs;
	}
}

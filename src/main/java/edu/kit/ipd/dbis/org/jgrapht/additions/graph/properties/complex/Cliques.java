package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.ComplexProperty;
import org.jgrapht.alg.clique.BronKerboschCliqueFinder;
import org.kohsuke.MetaInfServices;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@MetaInfServices
public class Cliques extends ComplexProperty {
	/**
	 * Standard constructor
	 *
	 * @param graph
	 */
	public Cliques(PropertyGraph graph) {
		super(graph);
	}

	@Override
	protected List<Set<Object>> calculateAlgorithm(PropertyGraph graph) {
		ArrayList<Set<Object>> result = new ArrayList<>();
		BronKerboschCliqueFinder alg = new BronKerboschCliqueFinder(graph);
		//TODO: all cliques or only maximum cliques?
		Iterator<Set<Object>> it = alg.maximumIterator();
		while (it.hasNext()) {
			Set<Object> clique = it.next();
			result.add(clique);
		}
		return result;
	}
}

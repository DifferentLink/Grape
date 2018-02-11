package edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.ComplexProperty;
import org.jgrapht.alg.clique.BronKerboschCliqueFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * the clique property
 */
public class Cliques extends ComplexProperty {
	/**
	 * Standard constructor
	 *
	 * @param graph the input graph
	 */
	public Cliques(PropertyGraph graph) {
		super(graph);
	}

	@Override
	protected List<Set<Object>> calculateAlgorithm(PropertyGraph graph) {
		ArrayList<Set<Object>> result = new ArrayList<>();
		BronKerboschCliqueFinder alg = new BronKerboschCliqueFinder(graph);
		for (Set<Object> clique : (Iterable<Set<Object>>) alg) {
			result.add(clique);
		}
		return result;
	}

	/**
	 *
	 * @param size the size of cliques
	 * @return a list of all cliques with this size
	 */
	public List<Set<Object>> getCliquesOfSize(int size) {
		List<Set<Object>> result = new ArrayList<>();
		List<Set<Object>> allCliques = (List<Set<Object>>) this.getValue();
		for (Set<Object> clique : allCliques) {
			if (clique.size() == size) {
				result.add(clique);
			}
		}
		return result;
	}
}

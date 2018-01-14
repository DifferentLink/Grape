package edu.kit.ipd.dbis.org.jgrapht.additions.graph;
import org.jgrapht.EdgeFactory;
import org.jgrapht.alg.isomorphism.VF2GraphIsomorphismInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.Set;

/**
 * A SimpleGraph which contains Property-objects.
 * @param <V>
 * @param <E>
 */
public class PropertyGraph<V, E> extends SimpleGraph {
	private int id;
	private Set<Property> properties;

	/**
	 *
	 * @param ef
	 * @param weighted
	 */
	public PropertyGraph(EdgeFactory ef, boolean weighted) {
		super(ef, weighted);
	}



	/**
	 *
	 */
	public void calculateRemainingProperties() {
		for (Property p : this.properties) {
			if (p.getValue() == null) {
				p.calculate(this);
			}
		}
	}

	/**
	 *
	 */
	public void updateAll() {
		for (Property p : this.properties) {
				p.calculate(this);
		}
	}

	/**
	 *
	 * @param type
	 */
	public void update(PropertyType type) {
		for (Property p : properties) {
			if (p.type == type) {
				p.calculate(this);
			}
		}
	}

	/**
	 * Returns a property
	 * @param type the type of the desired property
	 * @return the property
	 */
	public Property getProperty(PropertyType type) {
		for (Property p : properties) {
			if (p.type == type) {
				return p;
			}
		}
		return null;
	}


	/**
	 * checks if two graphs are equals
	 *
	 * @param graph the input graph
	 * @return if this graph is equals to the input graph
	 */

	public boolean equals(PropertyGraph graph) {
		VF2GraphIsomorphismInspector<Integer, DefaultEdge> iI = new VF2GraphIsomorphismInspector<Integer, DefaultEdge>(graph, this);
		if (iI.isomorphismExists()) {
			return true;
		} else {
			return false;
		}
	}
}

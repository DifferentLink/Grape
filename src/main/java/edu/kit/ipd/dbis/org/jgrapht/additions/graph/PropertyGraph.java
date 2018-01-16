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
		this.properties = PropertyFactory.createAllProperties();
	}

	/**
	 * Setter method for id.
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}


	/**
	 * TODO: design change (name)
	 */
	public void calculateRemainingProperties() {
	}

	/**
	 * Getter method for id
	 * @return the graph's id
	 */
	public int getId() {
		return this.id;
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

	public Integer getNumberOfEdges() {
		return null;
	}
}

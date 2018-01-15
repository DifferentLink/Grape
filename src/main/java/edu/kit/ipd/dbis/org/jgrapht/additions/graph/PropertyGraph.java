package edu.kit.ipd.dbis.org.jgrapht.additions.graph;
import org.jgrapht.EdgeFactory;
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
	public PropertyGraph(EdgeFactory ef, boolean weighted, int id) {
		super(ef, weighted);
		this.id = id;
		this.properties = PropertyFactory.createAllProperties();
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
}

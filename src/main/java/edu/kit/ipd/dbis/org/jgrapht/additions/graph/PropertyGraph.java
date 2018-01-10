package edu.kit.ipd.dbis.org.jgrapht.additions.graph;
import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.SimpleGraph;

import java.util.Set;

/**
 * A SimpleGraph which contains Property-objects.
 * @param <V>
 * @param <E>
 */
public class PropertyGraph<V,E> extends SimpleGraph {
	private int id;
	Set<Property> properties;


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
	 * @param property
	 */
	public void update(Property property) {

	}
}

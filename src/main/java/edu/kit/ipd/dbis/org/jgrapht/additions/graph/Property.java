package edu.kit.ipd.dbis.org.jgrapht.additions.graph;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.IntegerProperty;
import org.kohsuke.MetaInfServices;

import java.io.Serializable;

/**
 * Represents a graph's property
 */
public abstract class Property implements Serializable{
	private Object value;

	/**
	 * Method which induces the calculation
	 * of the property
	 *
	 * @param graph target graph
	 */
	public void calculate(PropertyGraph graph) {
		this.value = this.calculateAlgorithm(graph);
	}

	/**
	 * The implementation of the calculation algorithm.
	 *
	 * @param graph target graph
	 * @return the result
	 */
	protected abstract Object calculateAlgorithm(PropertyGraph graph);

	/**
	 * Getter method for value
	 *
	 * @return attribute value
	 */
	public Object getValue() {
		return this.value;
	}
}

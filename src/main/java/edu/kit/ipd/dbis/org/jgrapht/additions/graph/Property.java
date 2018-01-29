package edu.kit.ipd.dbis.org.jgrapht.additions.graph;

import java.io.Serializable;

/**
 * Represents a graph's property
 */
public abstract class Property implements Serializable {
	private Object value;
	private PropertyGraph graph;

	/**
	 * Standard constructor
	 *
	 * @param graph the input graph
	 */
	public Property(PropertyGraph graph) {
		this.graph = graph;
	}

	/**
	 * Method which induces the calculation
	 * of the property
	 *
	 */
	protected void calculate() {
		if (this.value == null) {
			this.value = this.calculateAlgorithm(this.graph);
		}
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
		if (this.value == null) {
			this.calculate();
		}
		return this.value;
	}
}

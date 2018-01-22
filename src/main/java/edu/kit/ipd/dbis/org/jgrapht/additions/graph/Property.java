package edu.kit.ipd.dbis.org.jgrapht.additions.graph;

/**
 * Represents a graph's property
 */
public abstract class Property {
	private Object value;
	private PropertyGraph graph;

	/**
	 * Standard constructor
	 *
	 * @param graph
	 */
	public Property(PropertyGraph graph) {
		this.graph = graph;
	}

	/**
	 * Method which induces the calculation
	 * of the property
	 *
	 */
	public void calculate() {
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

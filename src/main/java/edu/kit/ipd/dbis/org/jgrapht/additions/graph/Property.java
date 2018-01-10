package edu.kit.ipd.dbis.org.jgrapht.additions.graph;

/**
 * Represents a graph's property
 *
 * @param <T> the type of the property's value
 */
public abstract class Property<T> {
	private T value;

	/**
	 * Template method which induces the calculation
	 * of the property
	 *
	 * @param graph graph for which the property is calculated
	 */
	public void calculate(PropertyGraph graph) {
		T result;
		result = this.calculationAlgorithm(graph);
		this.value = result;
	}

	/**
	 * Calculates the value of the property
	 * for the given graph
	 *
	 * @param graph graph for which the property is calculated
	 * @return result of calculation
	 */
	protected abstract T calculationAlgorithm(PropertyGraph graph);

	/**
	 * Getter method for value
	 *
	 * @return value
	 */
	public T getValue() {
		return this.value;
	}
}

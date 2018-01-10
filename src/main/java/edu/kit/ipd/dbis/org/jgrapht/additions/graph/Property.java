package edu.kit.ipd.dbis.org.jgrapht.additions.graph;

/**
 * Represents a graph's property
 */
public class Property {
	private Object value;
	protected PropertyType type;

	/**
	 * Standard constructor
	 * @param type the property's type
	 */
	public Property(PropertyType type) {
		this.type = type;
	}

	/**
	 * Method which induces the calculation
	 * of the property
	 *
	 * @param graph graph for which the property is calculated
	 */
	public void calculate(PropertyGraph graph) {
		this.value = this.type.calculate(graph);
	}

	/**
	 * Getter method for value
	 *
	 * @return value
	 */
	public Object getValue() {
		return this.value;
	}

}

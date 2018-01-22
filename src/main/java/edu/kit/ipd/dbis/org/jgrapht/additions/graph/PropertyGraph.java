package edu.kit.ipd.dbis.org.jgrapht.additions.graph;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.NumberOfVertices;
import org.jgrapht.alg.isomorphism.VF2GraphIsomorphismInspector;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.*;

/**
 * A SimpleGraph which contains Property-objects.
 *
 * @param <V>
 * @param <E>
 */
public class PropertyGraph<V, E> extends SimpleGraph {
	private int id;
	private Map<Class<?>, Property> properties;

	/**
	 * Standard constructor
	 */
	public PropertyGraph() {
		super(new ClassBasedEdgeFactory<>(DefaultEdge.class), false);
		this.properties = new HashMap<>();
		for (Property p : PropertyFactory.createAllProperties()) {
			this.properties.put(p.getClass(), p);
		}
	}

	/**
	 * Setter method for id.
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Getter method for id
	 * @return the graph's id
	 */
	public int getId() {
		return this.id;
	}


	/**
	 * Getter method for properties.
	 *
	 * @param propertyClass the desired property's class
	 * @return the property
	 */
	public Object getProperty(Class<? extends Property> propertyClass) {
		return this.properties.get(propertyClass);
	}

	/**
	 * Returns set of all properties.
	 *
	 * @return the properties
	 */
	public Collection<Property> getProperties() {
		return this.properties.values();
	}

	/**
	 * TODO: design change (name)
	 */
	public void calculateRemainingProperties() {
	}

	/**
	 * Updates a specific property.
	 *
	 * @param propertyClass the property's class
	 */
	public void updateProperty(Class<? extends Property> propertyClass) {
		this.properties.get(propertyClass).calculate(this);
	}

	/**
	 * checks if two graphs are equal
	 *
	 * @param graph the input graph
	 * @return if this graph is equal to the input graph
	 */
	public boolean equals(PropertyGraph graph) {
		VF2GraphIsomorphismInspector<Integer, DefaultEdge> iI = new VF2GraphIsomorphismInspector<Integer, DefaultEdge>(graph, this);
		if (iI.isomorphismExists()) {
			return true;
		} else {
			return false;
		}
	}

	public int getNumberOfVertices() {
		if (this.properties.get(NumberOfVertices.class).getValue() == null) {
			this.updateProperty(NumberOfVertices.class);
		}
		return (int) this.properties.get(NumberOfVertices.class).getValue();
	}

	/**
	 * Generates an adjacency matrix
	 *
	 * @return the adjacency matrix
	 */
	public int[][] getAdjacencyMatrix() {
		int[][] matrix;
		List<V> sortedVertices = new ArrayList<>(new TreeSet<>(this.vertexSet()));
		matrix = new int[sortedVertices.size()][sortedVertices.size()];
		for (int i = 0; i < sortedVertices.size(); i++) {
			V v = sortedVertices.get(i);
			Set<E> outgoingEdges = this.outgoingEdgesOf(v);
			for (Object e : outgoingEdges) {
				V edgeTarget = (V) this.getEdgeTarget(e);

				// problem with JGraphT implementation:
				// edges are treated as they should be in
				// an undirected graph. But depending on
				// how they were added, getEdgeTarget(e)
				// actually returns the sourceNode, as
				// this is how it is stored in JGraphT.
				if (edgeTarget.equals(v)) {
					edgeTarget = (V) this.getEdgeSource(e);
				}
				int index = sortedVertices.indexOf(edgeTarget);
				matrix[i][index] = 1;
			}
		}
		return matrix;
	}
}

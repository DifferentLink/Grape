package edu.kit.ipd.dbis.org.jgrapht.additions.graph;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import org.jgrapht.alg.isomorphism.VF2GraphIsomorphismInspector;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.Set;
import java.util.List;

/**
 * A SimpleGraph which contains Property-objects.
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
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
		for (Property p : PropertyFactory.createAllProperties(this)) {
			this.properties.put(p.getClass(), p);
		}
	}

	/**
	 * Constructs a property graph with an bfs code
	 *
	 * @param bfsCode the bfs code
	 */
	public PropertyGraph(BfsCodeAlgorithm.BfsCodeImpl bfsCode) {
		super(new ClassBasedEdgeFactory<>(DefaultEdge.class), false);
		int[] code = bfsCode.getCode();
		for (int i = 1; i <= (code[code.length - 1]); i++) {
			this.addVertex(i);
		}
		for (int i = 1; i < bfsCode.getLength(); i = i + 3) {
			this.addEdge(bfsCode.getCode()[i], bfsCode.getCode()[i + 1]);
		}
		this.properties = new HashMap<>();
		for (Property p : PropertyFactory.createAllProperties(this)) {
			this.properties.put(p.getClass(), p);
		}
	}

	/**
	 * Setter method for id.
	 * @param id the id of the graph
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
	public Property getProperty(Class<? extends Property> propertyClass) {
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
	 * Induces the calculation of every property.
	 */
	public void calculateProperties() {
		for (Property p : this.properties.values()) {
			p.calculate();
		}
	}

	/**
	 * checks if two graphs are equal
	 *
	 * @param graph the input graph
	 * @return if this graph is equal to the input graph
	 */
	public boolean equals(PropertyGraph graph) {
		VF2GraphIsomorphismInspector<Integer, DefaultEdge> iI =
				new VF2GraphIsomorphismInspector<Integer, DefaultEdge>(graph, this);
		return iI.isomorphismExists();
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

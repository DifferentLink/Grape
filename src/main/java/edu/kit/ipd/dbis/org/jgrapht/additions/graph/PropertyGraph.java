package edu.kit.ipd.dbis.org.jgrapht.additions.graph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.GreatestDegree;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.NumberOfEdges;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.NumberOfVertices;
import org.jgrapht.alg.isomorphism.VF2GraphIsomorphismInspector;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A SimpleGraph which contains Property-objects.
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
	 * checks if two graphs are equal
	 *
	 * @param graph the input graph
	 * @return if this graph is equal to the input graph
	 */
	public boolean equals(PropertyGraph graph) {
		// TODO: implement with BFS-Code
		VF2GraphIsomorphismInspector<Integer, DefaultEdge> iI = new VF2GraphIsomorphismInspector<Integer, DefaultEdge>(graph, this);
		if (iI.isomorphismExists()) {
			return true;
		} else {
			return false;
		}
	}

	public int getNumberOfVertices() {
		return (int) this.properties.get(NumberOfVertices.class).getValue();
	}

	public int getNumberOfEdges() {
		return (int) this.properties.get(NumberOfEdges.class).getValue();
	}

	public int getGreatestDegree() {
		return (int) this.properties.get(GreatestDegree.class).getValue();
	}

	public Collection<Property> getProperties() {return this.properties.values(); }
}

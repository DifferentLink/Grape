package edu.kit.ipd.dbis.org.jgrapht.additions.graph;
import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.SimpleGraph;

public class PropertyGraph<V,E> extends SimpleGraph {
	public PropertyGraph(EdgeFactory ef, boolean weighted) {
		super(ef, weighted);
	}
}

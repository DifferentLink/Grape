package edu.kit.ipd.dbis.org.jgrapht.additions.graph;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.*;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_.AverageDegree;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_.NodeSpecificDensity;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_.ProportionDensity;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_.StructureDensity;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.*;

import java.util.HashSet;
import java.util.Set;

public class PropertyFactory {
	/**
	 * Creates an instance of every property.
	 * @return a set of properties
	 */
	public static Set<Property> createAllProperties(PropertyGraph graph) {
		Set<Property> properties = new HashSet<>();
		properties.add(new Profile(graph));
		properties.add(new BfsCode(graph));
		properties.add(new Cliques(graph));
		properties.add(new KkGraph(graph));
		properties.add(new TotalColorings(graph));
		properties.add(new VertexColorings(graph));
		properties.add(new AverageDegree(graph));
		properties.add(new ProportionDensity(graph));
		properties.add(new StructureDensity(graph));
		properties.add(new NodeSpecificDensity(graph));
		properties.add(new GreatestDegree(graph));
		properties.add(new KkGraphNumberOfSubgraphs(graph));
		properties.add(new NumberOfCliques(graph));
		properties.add(new NumberOfEdges(graph));
		properties.add(new NumberOfTotalColorings(graph));
		properties.add(new NumberOfVertexColorings(graph));
		properties.add(new NumberOfVertices(graph));
		properties.add(new SmallestDegree(graph));
		return properties;
	}
}

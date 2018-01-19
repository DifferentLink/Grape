package edu.kit.ipd.dbis.org.jgrapht.additions.graph;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.*;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_.AverageDegree;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_.NodeSpecificDensity;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_.ProportionDensity;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_.StructureDensity;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.*;

import java.util.*;

public class PropertyFactory {
	/**
	 * Creates an instance of every property.
	 * @return a set of properties
	 */
	public static Set<Property> createAllProperties() {
		Set<Property> properties = new HashSet<>();
		properties.add(new BfsCode());
		properties.add(new Cliques());
		properties.add(new KkGraph());
		properties.add(new Profile());
		properties.add(new TotalColorings());
		properties.add(new VertexColorings());
		properties.add(new AverageDegree());
		properties.add(new NodeSpecificDensity());
		properties.add(new ProportionDensity());
		properties.add(new StructureDensity());
		properties.add(new GreatestDegree());
		properties.add(new KkGraphNumberOfSubgraphs());
		properties.add(new NumberOfCliques());
		properties.add(new NumberOfEdges());
		properties.add(new NumberOfTotalColorings());
		properties.add(new NumberOfVertexColorings());
		properties.add(new NumberOfVertices());
		properties.add(new SmallestDegree());
		return properties;
	}
}

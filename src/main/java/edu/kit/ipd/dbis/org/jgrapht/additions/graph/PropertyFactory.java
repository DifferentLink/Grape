package edu.kit.ipd.dbis.org.jgrapht.additions.graph;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.Profile;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.BfsCode;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.Cliques;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.KkGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.TotalColoring;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.VertexColoring;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_.*;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.GreatestDegree;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.SmallestDegree;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.KkGraphNumberOfSubgraphs;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.NumberOfEdges;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.NumberOfVertices;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.NumberOfCliques;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.NumberOfTotalColorings;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.NumberOfVertexColorings;
import java.util.HashSet;
import java.util.Set;

/**
 * a property factorie that creates all properties for the input graph
 */
public class PropertyFactory {
	/**
	 * an empty constructor
	 */
	protected PropertyFactory() { }
	/**
	 * Creates an instance of every property.
	 *
	 * @param graph the input graph
	 * @return a set of properties
	 */
	protected static Set<Property> createAllProperties(PropertyGraph graph) {
		Set<Property> properties = new HashSet<>();
		properties.add(new Profile(graph));
		properties.add(new BfsCode(graph));
		properties.add(new Cliques(graph));
		properties.add(new KkGraph(graph));
		properties.add(new TotalColoring(graph));
		properties.add(new VertexColoring(graph));
		properties.add(new AverageDegree(graph));
		properties.add(new ProportionDensity(graph));
		properties.add(new StructureDensity(graph));
		properties.add(new NodeSpecificDensity(graph));
		properties.add(new BinomialDensity(graph));
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

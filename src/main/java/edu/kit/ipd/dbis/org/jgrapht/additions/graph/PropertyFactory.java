package edu.kit.ipd.dbis.org.jgrapht.additions.graph;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.ComplexProperty;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.*;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_.AverageDegree;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_.BinomialDensity;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_.ProportionDensity;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_.StructureDensity;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.*;

import java.util.HashSet;
import java.util.Set;

/**
 * A factory that creates instances of all existing properties.
 */
public class PropertyFactory {
	/**
	 * Creates an instance of every property.
	 *
	 * @param graph the input graph
	 * @return a set of properties
	 */
	public static Set<Property> createAllProperties(PropertyGraph graph) {
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
		properties.add(new BinomialDensity(graph));
		properties.add(new NumberOfDisjointVerticesFromKkGraph(graph));
		properties.add(new NumberOfDisjointEdgesFromKkGraph(graph));
		properties.add(new GreatestDegree(graph));
		properties.add(new KkGraphNumberOfSubgraphs(graph));
		properties.add(new LargestCliqueSize(graph));
		properties.add(new LargestSubgraphSize(graph));
		properties.add(new NumberOfCliques(graph));
		properties.add(new NumberOfEdges(graph));
		properties.add(new NumberOfTotalColorings(graph));
		properties.add(new NumberOfVertexColorings(graph));
		properties.add(new NumberOfVertices(graph));
		properties.add(new SmallestDegree(graph));
		properties.add(new TotalColoringNumberOfColors(graph));
		properties.add(new VertexColoringNumberOfColors(graph));
		return properties;
	}

	/**
	 * Creates an instance of properties which are not a ComplexProperty.
	 *
	 * @param graph the input graph
	 * @return a set of properties
	 */
	public static Set<Property> createNumberProperties(PropertyGraph graph) {
		Set<Property> properties = new HashSet<>();
		for (Property p : PropertyFactory.createAllProperties(graph)) {
			if (!(p instanceof ComplexProperty)) {
				properties.add(p);
			}
		}
		return properties;
	}
}

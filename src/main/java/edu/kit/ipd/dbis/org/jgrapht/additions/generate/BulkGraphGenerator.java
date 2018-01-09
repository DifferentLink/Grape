package edu.kit.ipd.dbis.org.jgrapht.additions.generate;

import org.jgrapht.Graph;

import java.util.Set;

public interface BulkGraphGenerator {
	void generateBulk(Set<Graph> target, int quantity, int minVertices, int maxVertices, int minEdges, int maxEdges);
}

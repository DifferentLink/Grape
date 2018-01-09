/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.grapheditor;

import java.util.Set;

public class RenderableGraph {

	private Vertex[] vertices;
	private Edge[] edges;
	private int id;
	private Set<RenderableGraph> subgraphs;

	public RenderableGraph(Vertex[] vertices, Edge[] edges, int id) {
		this.vertices = vertices;
		this.edges = edges;
		this.id = id;
	}

	public RenderableGraph(PropertyGraph propertyGraph) {

	}


}

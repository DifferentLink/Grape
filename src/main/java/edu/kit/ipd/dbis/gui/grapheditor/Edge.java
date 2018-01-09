/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui.grapheditor;

public class Edge {
	public Vertex start;
	public Vertex end;

	public Edge(Vertex start, Vertex end) {
		this.start = start;
		this.end = end;
	}

	public boolean incident(Vertex vertex) {
		return start == vertex || end == vertex;
	}
}

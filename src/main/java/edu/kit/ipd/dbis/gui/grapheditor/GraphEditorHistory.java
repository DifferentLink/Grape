/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.grapheditor;

import java.util.LinkedList;
import java.util.ListIterator;

public class GraphEditorHistory {
	private LinkedList<RenderableGraph> history = new LinkedList<>();
	private ListIterator<RenderableGraph> activeState = history.listIterator();
	private int maxHistorySize;

	public GraphEditorHistory() {
	}

	public GraphEditorHistory(final int maxHistorySize) {
		this.maxHistorySize = maxHistorySize;
	}

	public void addToHistory(RenderableGraph graph) {
		history.add(graph);
		maintainHistorySize();
	}

	public RenderableGraph moveBack() {
		return activeState.previous();
	}

	public RenderableGraph moveForward() {
		return activeState.next();
	}

	private void maintainHistorySize() {
		final int historyOverflow = Math.abs(maxHistorySize - history.size());
		for (int i = 0; i < historyOverflow; i++) {
			history.removeFirst();
		}
	}

	private void cutTrailingHistory() { // todo implement cutTrailingHistory()
	}
}

/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.grapheditor;

import edu.kit.ipd.dbis.gui.grapheditor.util.ConcurrentLinkedList;
import edu.kit.ipd.dbis.gui.grapheditor.util.ConcurrentNode;

public class GraphEditorHistory {
	private ConcurrentLinkedList<RenderableGraph> history = new ConcurrentLinkedList<>();
	private ConcurrentNode<RenderableGraph> activeState;
	private int maxHistorySize;

	public GraphEditorHistory() {
	}

	public GraphEditorHistory(final int maxHistorySize) {
		this.maxHistorySize = maxHistorySize;
	}

	public void addToHistory(RenderableGraph graph) {
		history.pushBack(graph);
		activeState = history.last();
		// maintainHistorySize();
	}

	public RenderableGraph moveBack() {
		activeState = activeState.getPrevious();
		return activeState.get();
	}

	public RenderableGraph moveForward() {
		activeState = activeState.getNext();
		return activeState.get();
	}

	private void maintainHistorySize() {
		final int historyOverflow = Math.abs(maxHistorySize - history.size());
		for (int i = 0; i < historyOverflow; i++) {
			history.popFront();
		}
	}

	private void cutTrailingHistory() { // todo implement cutTrailingHistory()
	}
}

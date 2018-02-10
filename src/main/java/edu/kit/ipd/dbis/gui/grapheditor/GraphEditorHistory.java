package edu.kit.ipd.dbis.gui.grapheditor;

import edu.kit.ipd.dbis.gui.grapheditor.util.ConcurrentLinkedList;
import edu.kit.ipd.dbis.gui.grapheditor.util.ConcurrentNode;

/**
 * An undo history for the graph editor
 */
public class GraphEditorHistory {
	private ConcurrentLinkedList<RenderableGraph> history = new ConcurrentLinkedList<>();
	private ConcurrentNode<RenderableGraph> activeState;
	private int maxHistorySize;

	/**
	 * Don't specify the maximum number of recorded events to allow arbitrarily many
	 */
	public GraphEditorHistory() {

	}

	/**
	 * @param maxHistorySize the maximal amount of history states saved by the history
	 */
	public GraphEditorHistory(final int maxHistorySize) {
		this.maxHistorySize = maxHistorySize;
	}

	/**
	 * Records the state of a RenderableGraph.
	 * @param graph the graphs state
	 */
	public void addToHistory(RenderableGraph graph) {
		history.pushBack(graph);
		activeState = history.last();
	}

	/**
	 * Changes the active state to the previous state, if there is one.
	 * @return the content of the potentially updated active state
	 */
	public RenderableGraph moveBack() {
		if (activeState.getPrevious().get() != null) {
			activeState = activeState.getPrevious();
		}
		return activeState.get();
	}

	/**
	 * Changes the active state to the next state, if there is one.
	 * @return the content of the potentially updated active state
	 */
	public RenderableGraph moveForward() {
		if (activeState.getNext().get() != null) {
			activeState = activeState.getNext();
		}
		return activeState.get();
	}

	/**
	 * Removes events from the beginning of the history until it matches the maximum amount of elements allowed
	 */
	private void maintainHistorySize() {
		final int historyOverflow = Math.abs(maxHistorySize - history.size());
		for (int i = 0; i < historyOverflow; i++) {
			history.popLast();
		}
	}

	/**
	 * Removes all states after the active state
	 */
	private void cutTrailingHistory() { // todo implement cutTrailingHistory()
	}

	/**
	 * Deletes all recorded states
	 */
	public void clear() {
		this.history = new ConcurrentLinkedList<>();
		activeState = history.last();
	}
}

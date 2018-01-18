/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui.grapheditor;

public class ConcurrentNode<T> {
	private T element;
	private ConcurrentNode<T> previous;
	private ConcurrentNode<T> next;

	public ConcurrentNode(T element) {
		this.element = element;
	}

	public ConcurrentNode<T> getNext() {
		return next;
	}

	public T get() {
		return element;
	}

	public ConcurrentNode<T> getPrevious() {
		return previous;
	}

	public void setElement(T element) {
		this.element = element;
	}

	public void setPrevious(ConcurrentNode<T> previous) {
		this.previous = previous;
	}

	public void setNext(ConcurrentNode<T> next) {
		this.next = next;
	}
}

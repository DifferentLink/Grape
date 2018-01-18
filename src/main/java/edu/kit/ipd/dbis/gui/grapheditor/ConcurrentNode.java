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

	public synchronized ConcurrentNode<T> getNext() {
		return next;
	}

	public T get() {
		return element;
	}

	public ConcurrentNode<T> getPrevious() {
		return previous;
	}

	public synchronized void setElement(T element) {
		this.element = element;
	}

	public synchronized void setPrevious(ConcurrentNode<T> previous) {
		this.previous = previous;
	}

	public synchronized void setNext(ConcurrentNode<T> next) {
		this.next = next;
	}
}

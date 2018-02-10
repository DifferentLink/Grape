package edu.kit.ipd.dbis.gui.grapheditor.util;

/**
 * A thread safe node used in linked lists
 * @param <T> the type of the node's element
 */
public class ConcurrentNode<T> {
	private T element;
	private ConcurrentNode<T> previous;
	private ConcurrentNode<T> next;

	/**
	 * @param element the nodes element
	 */
	public ConcurrentNode(T element) {
		this.element = element;
	}

	/**
	 * @return the element coming after this node
	 */
	public synchronized ConcurrentNode<T> getNext() {
		return next;
	}

	/**
	 * @return the node's element
	 */
	public T get() {
		return element;
	}

	/**
	 * @return the element coming before this node
	 */
	public ConcurrentNode<T> getPrevious() {
		return previous;
	}

	/**
	 * @param element the new element of the node
	 */
	public synchronized void setElement(T element) {
		this.element = element;
	}

	/**
	 * @param previous the new node coming before the node
	 */
	public synchronized void setPrevious(ConcurrentNode<T> previous) {
		this.previous = previous;
	}

	/**
	 * @param next the new node coming after the node
	 */
	public synchronized void setNext(ConcurrentNode<T> next) {
		this.next = next;
	}
}

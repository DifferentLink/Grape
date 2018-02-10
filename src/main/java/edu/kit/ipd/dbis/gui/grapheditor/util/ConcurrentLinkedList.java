package edu.kit.ipd.dbis.gui.grapheditor.util;

import java.util.Iterator;

/**
 * A thread safe doubly linked list with a sentinel.
 * @param <T> a node's content type
 */
public class ConcurrentLinkedList<T> {
	private ConcurrentNode<T> head;

	/**
	 * Initialize an empty list.
	 */
	public ConcurrentLinkedList() {
		head = new ConcurrentNode<>(null);
		head.setNext(head);
		head.setPrevious(head);
	}

	/**
	 * @return the list's sentinel element.
	 */
	public synchronized ConcurrentNode<T> head() {
		return this.head;
	}

	/**
	 * @return the content of the first node in the list.
	 * If the list is empty this method returns null, which is the sentinel's element.
	 */
	public synchronized T getFirst() {
		return head.getNext().get();
	}

	/**
	 * @return the content of the last node in the list.
	 * If the list is empty this method returns null, which is the sentinel's element.
	 */
	public synchronized T getLast() {
		return head.getPrevious().get();
	}

	/**
	 * @return the list's first node.
	 * If the list is empty this method returns the list's sentinel.
	 */
	public synchronized ConcurrentNode<T> first() {
		return head.getNext();
	}

	/**
	 * @return the first list's last node.
	 * If the list is empty this method returns the list's sentinel.
	 */
	public synchronized ConcurrentNode<T> last() {
		return head.getPrevious();
	}

	/**
	 * @return true if the list is empty.
	 */
	public synchronized boolean isEmpty() {
		return head == head.getNext();
	}

	/**
	 * @return the element of the first node and remove it from the list.
	 * If the list is empty this method returns null.
	 */
	public synchronized T popFirst() {
		ConcurrentNode<T> out = head.getPrevious();
		head.getPrevious().setNext(null);
		head.getPrevious().setPrevious(null);
		head.setPrevious(out.getPrevious());
		return out.get();
	}

	/**
	 * @return the element of the last node and remove it from the list.
	 */
	public synchronized T popLast() {
		ConcurrentNode<T> out = head.getPrevious();
		head.getNext().setNext(null);
		head.getNext().setPrevious(null);
		head.setNext(out.getNext());
		return out.get();
	}

	/**
	 * @param element the element of a the new node to push at the first position of the list
	 */
	public synchronized void pushFront(T element) {
		ConcurrentNode<T> newNode = new ConcurrentNode<>(element);
		head.getNext().setPrevious(newNode);
		newNode.setNext(head.getNext());
		newNode.setPrevious(head);
		head.setNext(newNode);
	}

	/**
	 *
	 * @param element the element of a the new node to push at the last position of the list
	 */
	public synchronized void pushBack(T element) {
		ConcurrentNode<T> newNode = new ConcurrentNode<>(element);
		last().setNext(newNode);
		newNode.setNext(head);
		newNode.setPrevious(head.getPrevious());
		head.setPrevious(newNode);
	}

	/**
	 * @return the number of nodes in the list
	 */
	public synchronized int size() {
		int size = 0;

		Iterator<T> iterator = this.iterator();
		while (iterator.next() != null) {
			size++;
		}

		return size;
	}

	/**
	 * @return an iterator
	 */
	public synchronized Iterator<T> iterator() {
		return new ConcurrentIterator<>(this);
	}

	private final class ConcurrentIterator<E> implements Iterator<E> {

		private ConcurrentNode<E> pointer;

		private ConcurrentIterator(ConcurrentLinkedList<E> list) {
			this.pointer = list.head();
		}

		@Override
		public boolean hasNext() {
			return false;
		}

		@Override
		public E next() {
			return null;
		}
	}
}

/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui.grapheditor.util;

import java.util.Iterator;

public class ConcurrentLinkedList<T> {
	private ConcurrentNode<T> head;

	public ConcurrentLinkedList() {
		head = new ConcurrentNode<>(null);
		head.setNext(head);
		head.setPrevious(head);
	}

	public synchronized ConcurrentNode<T> head() {
		return this.head;
	}

	public synchronized T getFirst() {
		return head.getNext().get();
	}

	public synchronized T getLast() {
		return head.getPrevious().get();
	}

	public synchronized ConcurrentNode<T> first() {
		return head.getNext();
	}

	public synchronized ConcurrentNode<T> last() {
		return head.getPrevious();
	}

	public synchronized boolean isEmpty() {
		return head == head.getNext();
	}

	public synchronized T popFirst() {
		ConcurrentNode<T> out = head.getPrevious();
		head.getPrevious().setNext(null);
		head.getPrevious().setPrevious(null);
		head.setPrevious(out.getPrevious());
		return out.get();
	}

	public synchronized T popLast() {
		ConcurrentNode<T> out = head.getPrevious();
		head.getNext().setNext(null);
		head.getNext().setPrevious(null);
		head.setNext(out.getNext());
		return out.get();
	}

	public synchronized void pushFront(T element) {
		ConcurrentNode<T> newNode = new ConcurrentNode<>(element);
		head.getNext().setPrevious(newNode);
		newNode.setNext(head.getNext());
		newNode.setPrevious(head);
		head.setNext(newNode);
	}

	public synchronized void pushBack(T element) {
		ConcurrentNode<T> newNode = new ConcurrentNode<>(element);
		last().setNext(newNode);
		newNode.setNext(head);
		newNode.setPrevious(head.getPrevious());
		head.setPrevious(newNode);
	}

	public synchronized int size() {
		int size = 0;

		Iterator<T> iterator = this.iterator();
		while (iterator.next() != null) {
			size++;
		}

		return size;
	}

	public synchronized Iterator<T> iterator() {
		return new ConcurrentIterator<>(this);
	}

	private class ConcurrentIterator<E> implements Iterator<E> {

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

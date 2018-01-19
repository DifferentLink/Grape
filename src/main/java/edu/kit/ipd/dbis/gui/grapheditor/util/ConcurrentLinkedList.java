/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui.grapheditor.util;

public class ConcurrentLinkedList<T> {
	private ConcurrentNode<T> head = new ConcurrentNode<>(null);


	public synchronized T getFirst() {
		return head.getNext().get();
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

	public synchronized T popBack() {
		ConcurrentNode<T> out = head.getPrevious();
		head.getPrevious().setNext(null);
		head.getPrevious().setPrevious(null);
		head.setPrevious(out.getPrevious());
		return out.get();
	}

	public synchronized T popFront() {
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
		head.getPrevious().setNext(newNode);
		newNode.setNext(head);
		newNode.setPrevious(head.getPrevious());
		head.setPrevious(newNode);
	}
}

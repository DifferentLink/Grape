/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui.grapheditor;

public class ConcurrentLinkedList<T> {
	private ConcurrentNode<T> head;

	public ConcurrentLinkedList() {

	}

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

	public synchronized ConcurrentNode<T> popBack() {
		ConcurrentNode<T> out = head.getPrevious();
		head.getPrevious().setNext(null);
		head.getPrevious().setPrevious(null);
		head.setPrevious(out.getPrevious());
		return out;
	}

	public synchronized ConcurrentNode<T> popFront() {
		ConcurrentNode<T> out = head.getPrevious();
		head.getNext().setNext(null);
		head.getNext().setPrevious(null);
		head.setNext(out.getNext());
		return out;
	}
}

package edu.kit.ipd.dbis.log;

import java.util.LinkedList;
import java.util.List;

/**
 * The type History.
 */
public class History {

	private List<Event> events;
	// TODO: Messages and other EventTypes are now splitted
	private List<Event> messages;
	private Event activeState;
	private int maxHistorySize;

	/**
	 * Instantiates a new History.
	 *
	 * @param maxHistorySize the max history size
	 */
	public History(int maxHistorySize) {
		this.events = new LinkedList<>();
		this.messages = new LinkedList<>();
		this.maxHistorySize = maxHistorySize;
		this.activeState = null;
	}

	/**
	 * Gets events.
	 *
	 * @return the events
	 */
	public List<Event> getEvents() {
		return events;
	}

	/**
	 * Gets messages.
	 *
	 * @return the messages
	 */
	// TODO: New Getter
	public List<Event> getMessages() {
		return messages;
	}

	/**
	 * Gets active state.
	 *
	 * @return the active state
	 */
	// TODO: New Getter
	public Event getActiveState() {
		return activeState;
	}

	/**
	 * Move backward.
	 *
	 * @return the most previous Event for which the EventType is not MESSAGE.
	 */
	public void moveBackward() {
		int currentPosition = events.indexOf(activeState) - 1;
		activeState = events.get(currentPosition);
	}

	/**
	 * Move forward.
	 *
	 * @return the next Event for which the EventType is not MESSAGE.
	 */
	public void moveForward() {
		int currentPosition = events.indexOf(activeState) + 1;
		activeState = events.get(currentPosition);
	}

	/**
	 * Before this happens cutTrailing() is called. After the event is added maintainHistorySize() is called.
	 *
	 * @param event this event will be added to the end of the history.
	 */
	public void addEvent(Event event) {
		cutTrailing();
		events.add(event);
		activeState = event;
		maintainHistorySize();
	}

	/**
	 * Remove event.
	 *
	 * @param event the event
	 */
// TODO:
	public void removeEvent(Event event) {
		events.remove(event);
	}

	/**
	 * Remove message.
	 *
	 * @param event the event
	 */
// TODO:
	public void removeMessage(Event event) {
		messages.remove(event);
	}

	/**
	 * Add message.
	 *
	 * @param event the event
	 */
	public void addMessage(Event event) {
		messages.add(event);
	}

	/**
	 * Remove all Events after activeState.
	 */
	private void cutTrailing() {
		int indexOfActiveState = events.indexOf(activeState);
		for (int i = indexOfActiveState + 1; i < events.size(); i++) {
			events.remove(i);
		}
	}

	/**
	 * Remove oldest Events from the history until its size matches maxHistorySize.
	 */
	private void maintainHistorySize() {
		for (int i = 1; i <= (events.size() - maxHistorySize); i++) {
			events.remove(events.size() - i);
		}
	}

}

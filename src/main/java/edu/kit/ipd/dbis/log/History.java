package edu.kit.ipd.dbis.log;

import java.util.LinkedList;
import java.util.List;

/**
 * The History saves all events, that are added to the log.
 */
public class History {

	private List<Event> events;
	private Event activeState;
	private int maxHistorySize;

	/**
	 * Instantiates a new History.
	 *
	 * @param maxHistorySize the max history size
	 */
	public History(int maxHistorySize) {
		this.events = new LinkedList<>();
		this.maxHistorySize = maxHistorySize;
		this.activeState = null;
	}

	/**
	 * Gets all events.
	 *
	 * @return the events
	 */
	public List<Event> getEvents() {
		return events;
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
	 * Moves back one event in the history, which the EventType is not MESSAGE.
	 */
	public void moveBackward() {
		if (events.size() == 0) {
			return;
		}
		int currentPosition = events.indexOf(activeState);
		if (currentPosition == 0) {
			return;
		}
		currentPosition--;
		while (events.get(currentPosition).getType().equals(EventType.MESSAGE)) {
			if (currentPosition == 0) {
				return;
			}
			currentPosition--;
		}
		activeState = events.get(currentPosition);
	}

	/**
	 * Moves forward one event in the history, which the EventType is not MESSAGE.
	 */
	public void moveForward() {
		if (events.size() == 0) {
			return;
		}
		int currentPosition = events.indexOf(activeState);
		if (currentPosition == (events.size() - 1)) {
			return;
		}
		currentPosition++;
		while (events.get(currentPosition).getType().equals(EventType.MESSAGE)) {
			if (currentPosition == (events.size() - 1)) {
				return;
			}
			currentPosition++;
		}
		activeState = events.get(currentPosition);
	}

	/**
	 * Adds an event to the eventlist
	 *
	 * @param event this event will be added to the end of the history.
	 */
	public void addEvent(Event event) {
		cutTrailing();
		events.add(event);
		if (!event.getType().equals(EventType.MESSAGE)) {
			activeState = event;
		}
		maintainHistorySize();
	}

	/**
	 * Remove event from the list.
	 *
	 * @param event the event
	 */
	// TODO: method added
	public void removeEvent(Event event) {
		events.remove(event);
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
		int j = 0;
		for (int i = events.size(); i > maxHistorySize; i--) {
			events.remove(j);
			j++;
		}
	}

}

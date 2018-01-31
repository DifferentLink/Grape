package edu.kit.ipd.dbis.log;


import java.util.ArrayList;
import java.util.List;

public class History {

	private List<Event> events;
	private Event activeState;
	private int maxHistorySize;

	// TODO: Contructor?
	public History(int maxHistorySize) {
		this.events = new ArrayList<>();
		this.maxHistorySize = maxHistorySize;
		this.activeState = null;
	}

	public List<Event> getEvents() {
		return events;
	}

	// TODO: New Getter
	public Event getActiveState() {
		return activeState;
	}

	/**
	 * @return the most previous Event for which the EventType is not MESSAGE.
	 */
	public void moveBackward() {
		int currentPosition = events.indexOf(activeState) - 1; // todo skip messages
		while (events.get(currentPosition).getType().equals(EventType.MESSAGE)) {
			currentPosition--;
		}
		activeState = events.get(currentPosition);
	}

	/**
	 * @return the next Event for which the EventType is not MESSAGE.
	 */
	public void moveForward() {
		int currentPosition = events.indexOf(activeState) + 1; // todo skip messages
		while (events.get(currentPosition).getType().equals(EventType.MESSAGE)) {
			currentPosition++;
		}
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

	// TODO: method added
	public void removeEvent(Event event) {
		events.remove(event);
	}

	/**
	 * Remove all Events after activeState.
	 */
	public void cutTrailing() {
		int indexOfActiveState = events.indexOf(activeState);
		for (int i = indexOfActiveState + 1; i < events.size(); i++) {
			events.remove(i);
		}
	}

	/**
	 * Remove oldest Events from the history until its size matches maxHistorySize.
	 */
	public void maintainHistorySize() {
		int j = 0;
		for (int i = events.size(); i > maxHistorySize; i--) {
			events.remove(j);
			j++;
		}
	}

}

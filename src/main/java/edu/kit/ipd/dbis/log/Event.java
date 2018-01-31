package edu.kit.ipd.dbis.log;

import java.util.Set;

/**
 * The type Event.
 */
public class Event {

	private String message;
	private EventType type;
	private Set<Integer> changedGraphs;

	/**
	 * Instantiates a new Event.
	 *
	 * @param type          the type
	 * @param message       the message
	 * @param changedGraphs the changed graphs
	 */
	public Event(EventType type, String message, Set<Integer> changedGraphs) {
		this.message = message;
		this.type = type;
		this.changedGraphs = changedGraphs;
	}

	/**
	 * Gets message of the event.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Gets type of the event.
	 *
	 * @return the type
	 */
	public EventType getType() {
		return type;
	}

	/**
	 * Gets changed graphs of the event.
	 *
	 * @return the changed graphs
	 */
	public Set<Integer> getChangedGraphs() {
		return changedGraphs;
	}
}

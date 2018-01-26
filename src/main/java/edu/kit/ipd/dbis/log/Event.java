package edu.kit.ipd.dbis.log;

import java.util.Set;

public class Event {

	private String message;
	private EventType type;
	private Set<Integer> changedGraphs;

	public Event(EventType type, String message, Set<Integer> changedGraphs) {
		this.message = message;
		this.type = type;
		this.changedGraphs = changedGraphs;
	}

	public String getMessage() {
		return message;
	}

	public EventType getType() {
		return type;
	}

	public Set<Integer> getChangedGraphs() {
		return changedGraphs;
	}
}

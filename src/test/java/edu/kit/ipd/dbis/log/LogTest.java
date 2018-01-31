package edu.kit.ipd.dbis.log;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class LogTest {
	static Log log;
	static Set<Integer> graphs;
	static Event a1;
	static Event a2;
	static Event a3;
	static Event a4;
	static Event m1;
	static Event m2;
	static Event m3;
	static Event m4;
	static Event r1;
	static Event r2;
	static Event r3;
	static Event r4;


	@BeforeClass
	public static void initialize() {
		// random graphIDs
		graphs = new HashSet<>();
		graphs.add(1);
		graphs.add(2);
		graphs.add(3);
		graphs.add(4);

		// random events
		a1 = new Event(EventType.ADD, "a1", graphs);
		a2 = new Event(EventType.ADD, "a2", graphs);
		a3 = new Event(EventType.ADD, "a3", graphs);
		a4 = new Event(EventType.ADD, "a4", graphs);
		m1 = new Event(EventType.MESSAGE, "m1", graphs);
		m2 = new Event(EventType.MESSAGE, "m2", graphs);
		m3 = new Event(EventType.MESSAGE, "m3", graphs);
		m4 = new Event(EventType.MESSAGE, "m4", graphs);
		r1 = new Event(EventType.REMOVE, "r1", graphs);
		r2 = new Event(EventType.REMOVE, "r2", graphs);
		r3 = new Event(EventType.REMOVE, "r3", graphs);
		r4 = new Event(EventType.REMOVE, "r4", graphs);

		log = new Log(100);
	}

	@Test
	public void getAsString() {
		String expectedOutput = "";
		String output;
		log.addEvent(a1);
		log.addEvent(r2);
		log.addEvent(m3);
		output = log.getAsString();
		System.out.print(output);
	}

	@Test
	public void removeMessages() {
	}

	@Test
	public void undo() {
	}

	@Test
	public void redo() {
	}
}
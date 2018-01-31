package edu.kit.ipd.dbis.log;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class HistoryTest {

	@Before
	public void initialize() {
		History history = new History(100);

		// random graphIDs
		Set<Integer> graphs = new HashSet<>();
		graphs.add(1);
		graphs.add(2);
		graphs.add(3);
		graphs.add(4);

		// random events
		Event a1 = new Event(EventType.ADD, "", graphs);
		Event a2 = new Event(EventType.ADD, "", graphs);
		Event a3 = new Event(EventType.ADD, "", graphs);
		Event a4 = new Event(EventType.ADD, "", graphs);
		Event m1 = new Event(EventType.MESSAGE, "", graphs);
		Event m2 = new Event(EventType.MESSAGE, "", graphs);
		Event m3 = new Event(EventType.MESSAGE, "", graphs);
		Event m4 = new Event(EventType.MESSAGE, "", graphs);
		Event r1 = new Event(EventType.REMOVE, "", graphs);
		Event r2 = new Event(EventType.REMOVE, "", graphs);
		Event r3 = new Event(EventType.REMOVE, "", graphs);
		Event r4 = new Event(EventType.REMOVE, "", graphs);
	}

	@Test
	public void moveBackward() {

	}

	@Test
	public void moveForward() {
	}
}
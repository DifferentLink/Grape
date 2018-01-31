package edu.kit.ipd.dbis.log;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class LogTest {

	History history;
	Set<Integer> graphs;
	Event a1;
	Event a2;
	Event a3;
	Event a4;
	Event m1;
	Event m2;
	Event m3;
	Event m4;
	Event r1;
	Event r2;
	Event r3;
	Event r4;


	@BeforeClass
	public void initialize() {
		HistoryTest hTest = new HistoryTest();
		hTest.initialize();
	}


	@Test
	public void getAsString() {
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
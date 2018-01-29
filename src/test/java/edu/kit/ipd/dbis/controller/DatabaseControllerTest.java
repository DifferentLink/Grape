package edu.kit.ipd.dbis.controller;

import org.junit.Test;

import static org.junit.Assert.*;

public class DatabaseControllerTest {

	@Test
	public void newDatabaseTest() {
		DatabaseController data = new DatabaseController();
		data.newDatabase("", "", "", "");

	}
}
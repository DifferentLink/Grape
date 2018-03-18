package edu.kit.ipd.dbis.controller;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import org.junit.*;

import static org.junit.Assert.assertEquals;

public class GenerateControllerTest {

	private static GraphDatabase database;
	private static GenerateController g;

	@Test
	public void isValidBFSTest1() {
		GenerateController g = GenerateController.getInstance();
		String bfsCode = "r";
		assertEquals(false, g.isValidBFS(bfsCode));
	}

	@Test
	public void isValidBFSTest2() {
		GenerateController g = GenerateController.getInstance();
		String bfsCode = "[]";
		assertEquals(false, g.isValidBFS(bfsCode));
	}

	@Test
	public void isValidBFSTest3() {
		GenerateController g = GenerateController.getInstance();
		String bfsCode = "0,0,0";
		assertEquals(false, g.isValidBFS(bfsCode));
	}

	@Test
	public void isValidBFSTest4() {
		GenerateController g = GenerateController.getInstance();
		String bfsCode = "],1,2,1000";
		assertEquals(false, g.isValidBFS(bfsCode));
	}


	@Test
	public void isValidBFSTest5() {
		GenerateController g = GenerateController.getInstance();
		String bfsCode = "0,1,1,1";
		assertEquals(false, g.isValidBFS(bfsCode));
	}

	@Test
	public void isValidBFSTest6() {
		GenerateController g = GenerateController.getInstance();
		String bfsCode = "-3,-1,1,1";
		assertEquals(false, g.isValidBFS(bfsCode));
	}


	@Test
	public void isValidBFSTest7() {
		GenerateController g = GenerateController.getInstance();
		String bfsCode = "1,1,2,3,4,5,6";
		assertEquals(false, g.isValidBFS(bfsCode));
	}

	@Test
	public void isValidBFSTest8() {
		GenerateController g = GenerateController.getInstance();
		String bfsCode = "e,q,r,t,z,u,x";
		assertEquals(false, g.isValidBFS(bfsCode));
	}

	@Test
	public void isValidBFSTest9() {
		GenerateController g = GenerateController.getInstance();
		String bfsCode = "100,6761,45782,34789,5984,53,624";
		assertEquals(false, g.isValidBFS(bfsCode));
	}
}
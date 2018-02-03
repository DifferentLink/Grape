package edu.kit.ipd.dbis.controller;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.*;
import edu.kit.ipd.dbis.database.file.FileManager;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.SQLException;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

public class GenerateControllerTest {
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
		String bfsCode = "[0,0,0";
		assertEquals(false, g.isValidBFS(bfsCode));
	}

	@Test
	public void isValidBFSTest4() {
		GenerateController g = GenerateController.getInstance();
		String bfsCode = "],1,2,1000";
		assertEquals(false, g.isValidBFS(bfsCode));
	}


	@Test
	@Ignore
	public void isValidBFSTest5() {
		GenerateController g = GenerateController.getInstance();
		String bfsCode = "[0,1,1,1]";
		assertEquals(true, g.isValidBFS(bfsCode));
	}

	@Test
	public void isValidBFSTest6() {
		GenerateController g = GenerateController.getInstance();
		String bfsCode = "[-3,-1,1,1]";
		assertEquals(false, g.isValidBFS(bfsCode));
	}


	@Test
	@Ignore
	public void isValidBFSTest7() {
		GenerateController g = GenerateController.getInstance();
		String bfsCode = "[1,1,2,3,4,5,6]";
		assertEquals(true, g.isValidBFS(bfsCode));
	}

	@Test
	public void isValidBFSTest8() {
		GenerateController g = GenerateController.getInstance();
		String bfsCode = "[e,q,r,t,z,u,x]";
		assertEquals(false, g.isValidBFS(bfsCode));
	}

	@Test
	public void isValidBFSTest9() {
		GenerateController g = GenerateController.getInstance();
		String bfsCode = "[100,6761,45782,34789,5984,53,624]";
		assertEquals(false, g.isValidBFS(bfsCode));
	}


	@Test
	@Ignore
	public void generateBFSGraphTest() {
		GenerateController g = GenerateController.getInstance();
		GraphDatabase database = null;
		try {
			database = new FileManager().createGraphDatabase("", "", "", "");
		} catch (TableAlreadyExistsException | DatabaseDoesNotExistException | ConnectionFailedException |
				AccessDeniedForUserException | SQLException e) {
			e.printStackTrace();
		}
		g.setDatabase(database);
		String bfsCode = "[1,2,0,0,0,-1,3]";
		try {
			g.generateBFSGraph(bfsCode);
		} catch (InvalidBfsCodeInputException e) {
			e.printStackTrace();
		}
		LinkedList<PropertyGraph<Integer, Integer>> graphs = null;
		try {
			graphs = database.getUncalculatedGraph();
		} catch (AccessDeniedForUserException | DatabaseDoesNotExistException | TablesNotAsExpectedException |
				ConnectionFailedException e) {
			e.printStackTrace();
		}
		assert (graphs.isEmpty());
	}
}
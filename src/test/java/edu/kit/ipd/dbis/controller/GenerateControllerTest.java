package edu.kit.ipd.dbis.controller;

import edu.kit.ipd.dbis.controller.exceptions.InvalidBfsCodeInputException;
import edu.kit.ipd.dbis.controller.exceptions.InvalidGeneratorInputException;
import edu.kit.ipd.dbis.correlation.exceptions.InvalidCorrelationInputException;
import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.*;
import edu.kit.ipd.dbis.database.file.FileManager;
import edu.kit.ipd.dbis.filter.Filtermanagement;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.junit.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

public class GenerateControllerTest {

	private static GraphDatabase database;
	private static GenerateController g;

	@BeforeClass
	public static void setUp() throws Exception {
		try {
			String url = "jdbc:mysql://127.0.0.1/library";
			String user = "user";
			String password = "password";
			String name = "grape";

			FileManager fileManager = new FileManager();
			database = fileManager.createGraphDatabase(url, user, password, name);
			fileManager.deleteGraphDatabase(database);
			database = fileManager.createGraphDatabase(url, user, password, name);
		} catch (Exception e){
			Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/?user=travis&password=");
			connection.prepareStatement("CREATE DATABASE IF NOT EXISTS library").executeUpdate();
			String url = "jdbc:mysql://127.0.0.1/library";
			String user = "travis";
			String password = "";
			String name = "generatecontrollertest";

			FileManager fileManager = new FileManager();
			database = fileManager.createGraphDatabase(url, user, password, name);
		}
		g = GenerateController.getInstance();
		g.setDatabase(database);
	}

	@Before
	public void clear() throws SQLException, ConnectionFailedException {
		LinkedList<Integer> ids = database.getFilterTable().getIds();
		for (Integer id : ids) {
			if (id != 0) {
				database.deleteFilter(id);
			}
		}

		LinkedList<Integer> ids2 = database.getGraphTable().getIds();
		for (Integer id : ids) {
			if (id != 0) {
				database.deleteFilter(id);
			}
		}
	}

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


	@Test
	@Ignore
	public void generateBFSGraphTest() {
		String bfsCode = "1,1,2,1,1,3,-1,2,3";
		try {
			g.generateBFSGraph(bfsCode);
		} catch (InvalidBfsCodeInputException e) {
			e.printStackTrace();
		}
		int actual = 0;
		try {
			actual = database.getNumberOfGraphs();
		} catch (ConnectionFailedException e) {
			e.printStackTrace();
		}
		assertEquals(1, actual);
	}

	@Test(expected = InvalidCorrelationInputException.class)
	@Ignore
	public void generateBFSGraphFailureTest() throws InvalidBfsCodeInputException {
		String bfsCode = "fgh1,1,2,1,1,3,-1,2,3";
		g.generateBFSGraph(bfsCode);
	}

	@Test
	@Ignore
	public void generateGraphsTest() {

		g.generateGraphs(2, 5, 1, 5, 4);

		int actual = 0;
		try {
			actual = database.getNumberOfGraphs();
		} catch (ConnectionFailedException e) {
			e.printStackTrace();
		}
		assertEquals(4, actual);
	}

	@Test
	@Ignore
	public void generateGraphsTestNotEnoughGraphs() {

		g.generateGraphs(1, 2, 1, 3, 20);

		int actual = 0;
		try {
			actual = database.getNumberOfGraphs();
		} catch (ConnectionFailedException e) {
			e.printStackTrace();
		}
		assertEquals(1, actual);
	}

	@Test
	@Ignore
	public void deleteGraphTest() {
		String bfsCode = "1,1,2,1,1,3,-1,2,3";
		try {
			g.generateBFSGraph(bfsCode);
			g.deleteGraph(1);
		} catch (InvalidBfsCodeInputException e) {
			e.printStackTrace();
		}
		int actual = 0;
		try {
			actual = database.getNumberOfGraphs();
		} catch (ConnectionFailedException e) {
			e.printStackTrace();
		}
		assertEquals(0, actual);
	}

}
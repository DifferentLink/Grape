package edu.kit.ipd.dbis.correlation;

import edu.kit.ipd.dbis.correlation.exceptions.InvalidCorrelationInputException;
import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.connection.tables.FilterTable;
import edu.kit.ipd.dbis.database.connection.tables.GraphTable;
import edu.kit.ipd.dbis.database.exceptions.sql.*;
import edu.kit.ipd.dbis.database.file.FileManager;
import edu.kit.ipd.dbis.filter.Filtermanagement;
import org.junit.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class CorrelationRequestTest {

    private static GraphDatabase database;
	private static Filtermanagement manager;

    @Before
    public void delete() throws DatabaseDoesNotExistException, SQLException, AccessDeniedForUserException,
            ConnectionFailedException {

    	/*Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/?user=travis&password=");
		connection.prepareStatement("CREATE DATABASE IF NOT EXISTS library").executeUpdate();
		String url = "jdbc:mysql://127.0.0.1/library";
		String user = "travis";
		String password = "";
		String name = "grape";
		FileManager fileManager = new FileManager();
		database = fileManager.createGraphDatabase(url, user, password, name);
		manager = new Filtermanagement();
        manager.setDatabase(database);*/

        String url = "jdbc:mysql://127.0.0.1/library";
        String user = "user";
        String password = "password";
        String name = "grape2";
        GraphDatabase gdb = new GraphDatabase(new GraphTable(url, user, password, name),
                new FilterTable(url, user, password, "grape2filters"));
        FileManager fileManager = new FileManager();
        fileManager.deleteGraphDatabase(gdb);
        database = fileManager.createGraphDatabase(url, user, password, name);
        manager = new Filtermanagement();
        manager.setDatabase(database);
    }

    @Test
    public void testGetCorrelation() throws InvalidCorrelationInputException {
        CorrelationRequest myRequest = new CorrelationRequest("Max Pearson 3", database);
        assert myRequest.getCorrelation().getClass().equals(Pearson.class);
    }

    @Test
    public void testTestPropertyValidInput() {
        try {
            //only small letters are accepted as user input will be transformed to lower case
            CorrelationRequest.testProperty("proportiondensity");
        } catch (InvalidCorrelationInputException e) {
            throw new AssertionError();
        }
    }

    @Test(expected = InvalidCorrelationInputException.class)
    public void testTestPropertyComplexProperty() throws InvalidCorrelationInputException {
        CorrelationRequest.testProperty("Profile");
    }

    @Test(expected = InvalidCorrelationInputException.class)
    public void testTestPropertyInvalidInput() throws InvalidCorrelationInputException {
        CorrelationRequest.testProperty("THIS INPUT IS INVALID");
    }

    @Test
    public void testTestCorrelationStringValidInput() {
        try {
            CorrelationRequest.testCorrelationString("pearson");
        } catch (InvalidCorrelationInputException e) {
            throw new AssertionError();
        }
        try {
            CorrelationRequest.testCorrelationString("mutualcorrelation");
        } catch (InvalidCorrelationInputException e) {
            throw new AssertionError();
        }
    }

    @Test(expected = InvalidCorrelationInputException.class)
    public void testTestCorrelationStringInvalidInput() throws InvalidCorrelationInputException {
        CorrelationRequest.testCorrelationString("wrongInput");
    }

    @Test
    public void testTestFirstArgumentValidInput() {
        try {
            CorrelationRequest.testMaxOrMin("max");
        } catch (InvalidCorrelationInputException e) {
            throw new AssertionError();
        }
        try {
            CorrelationRequest.testMaxOrMin("min");
        } catch (InvalidCorrelationInputException e) {
            throw new AssertionError();
        }
        try {
            CorrelationRequest.testMaxOrMin("least");
        } catch (InvalidCorrelationInputException e) {
            throw new AssertionError();
        }
    }

    @Test(expected = InvalidCorrelationInputException.class)
    public void testTestFirstArgumentInvalidInput() throws InvalidCorrelationInputException {
        CorrelationRequest.testMaxOrMin("nonsence");
    }

    @Test(expected = InvalidCorrelationInputException.class)
    public void testParseCorrelationToStringWrongInput() throws InvalidCorrelationInputException {
        CorrelationRequest.parseCorrelationToString("thisisnotavalidinput");
    }

    @Test(expected = InvalidCorrelationInputException.class)
    public void testParseCorrelationToStringWrongNumber() throws InvalidCorrelationInputException {
        CorrelationRequest.parseCorrelationToString("Max Pearson 3.97");
    }

    @Test
    public void testParseCorrelationToStringValidInput1() {
        try {
            CorrelationRequest.parseCorrelationToString("Min Pearson 4");
        } catch (InvalidCorrelationInputException e) {
            throw new AssertionError();
        }
    }

    @Test
    public void testParseCorrelationToStringValidInput2() {
        try {
            CorrelationRequest.parseCorrelationToString("Max MutualCorrelation BinomialDensity 2");
        } catch (InvalidCorrelationInputException e) {
            throw new AssertionError();
        }
    }

    @Test (expected = InvalidCorrelationInputException.class)
    public void testParseCorrelationToStringIntegerTooBig() throws InvalidCorrelationInputException {
        CorrelationRequest.parseCorrelationToString("Max Pearson 500000000000000");
    }

    @Test (expected = InvalidCorrelationInputException.class)
    public void testParseCorrelationToStringIntegerIs0() throws InvalidCorrelationInputException {
        CorrelationRequest.parseCorrelationToString("Max Pearson 0");
    }

    @Test (expected = InvalidCorrelationInputException.class)
    public void testCheckCorrelationInputNull() throws InvalidCorrelationInputException {
        CorrelationRequest.parseCorrelationToString(null);
    }

    @Test
    public void testParseToList() {
        TreeSet<CorrelationOutput> testTreeSet = new TreeSet<>();
        testTreeSet.add(new CorrelationOutput("AverageDegree", "LargestCliqueSize", 5.32));
        testTreeSet.add(new CorrelationOutput("NumberOfVertices", "LargestCliqueSize", 1.94));
        testTreeSet.add(new CorrelationOutput("ProportionDensity", "LargestCliqueSize", 9.83));
        testTreeSet.add(new CorrelationOutput("KkGraphNumberOfSubgraphs", "LargestCliqueSize", 4.42));
        List<CorrelationOutput> resultList = CorrelationRequest.parseToList(testTreeSet);
        int counter = 0;
        for (CorrelationOutput current: resultList) {
            if (counter == 0) {
                assert current.getOutputNumber() == 9.83;
            } else if (counter == 1) {
                assert current.getOutputNumber() == 5.32;
            } else if (counter == 2) {
                assert current.getOutputNumber() == 4.42;
            } else {
                assert current.getOutputNumber() == 1.94;
            }
            counter++;
        }
    }

    @Test
    public void testGetValidCorrelations() {
        List<String> availableCorrelations = CorrelationRequest.getValidCorrelations();
        assert availableCorrelations.contains("pearson");
        assert availableCorrelations.contains("mutualcorrelation");
    }

    @Test
    public void testApplyCorrelation() throws ConnectionFailedException, InsertionFailedException,
            UnexpectedObjectException, InvalidCorrelationInputException {
		PearsonCorrelationTest.setDatabase(database);
        PearsonCorrelationTest.putGraphsIntoDatabase();

        CorrelationRequest testRequest1 = new CorrelationRequest("Min Pearson 4", database);
        testRequest1.applyCorrelation();
        CorrelationRequest testRequest2 = new CorrelationRequest("Max Pearson 3", database);
        testRequest2.applyCorrelation();
        CorrelationRequest testRequest3 = new CorrelationRequest("Min MutualCorrelation 5", database);
        testRequest3.applyCorrelation();
        CorrelationRequest testRequest4 = new CorrelationRequest("Max MutualCorrelation 6", database);
        testRequest4.applyCorrelation();
        CorrelationRequest testRequest5 = new CorrelationRequest("Min Pearson AverageDegree 2", database);
        testRequest5.applyCorrelation();
        CorrelationRequest testRequest6 = new CorrelationRequest("Max Pearson NumberOfEdges 2", database);
        testRequest6.applyCorrelation();
        CorrelationRequest testRequest7 = new CorrelationRequest("Min MutualCorrelation SmallestDegree 2", database);
        testRequest7.applyCorrelation();
        CorrelationRequest testRequest8 = new CorrelationRequest("Max MutualCorrelation BinomialDensity 2", database);
        testRequest8.applyCorrelation();
        CorrelationRequest testRequest9 = new CorrelationRequest("Min MutualCorrelation SmallestDegree 20000", database);
        testRequest9.applyCorrelation();
        CorrelationRequest testRequest10 = new CorrelationRequest("Max MutualCorrelation BinomialDensity 20000", database);
        testRequest10.applyCorrelation();
        CorrelationRequest testRequest11 = new CorrelationRequest("Min Pearson 4", database);
        testRequest11.applyCorrelation();
        CorrelationRequest testRequest12 = new CorrelationRequest("Max Pearson 3", database);
        testRequest12.applyCorrelation();
        CorrelationRequest testRequest13 = new CorrelationRequest("Least MutualCorrelation 5", database);
        testRequest13.applyCorrelation();
        CorrelationRequest testRequest14 = new CorrelationRequest("Least MutualCorrelation StructureDensity 3", database);
        testRequest14.applyCorrelation();
        CorrelationRequest testRequest15 = new CorrelationRequest("Least Pearson 4", database);
        testRequest15.applyCorrelation();
        CorrelationRequest testRequest16 = new CorrelationRequest("Least Pearson NumberOfEdges 2", database);
        testRequest16.applyCorrelation();
    }
}

package edu.kit.ipd.dbis.correlation;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.connection.tables.FilterTable;
import edu.kit.ipd.dbis.database.connection.tables.GraphTable;
import edu.kit.ipd.dbis.database.exceptions.sql.*;
import edu.kit.ipd.dbis.database.file.FileManager;
import edu.kit.ipd.dbis.filter.Filtermanagement;
import edu.kit.ipd.dbis.org.jgrapht.additions.generate.BulkRandomConnectedGraphGenerator;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

public class MutualCorrelationTest {

    private static GraphDatabase database;
    private static Filtermanagement manager;

    @Before
    public void delete() throws DatabaseDoesNotExistException, SQLException, AccessDeniedForUserException,
            ConnectionFailedException, UnexpectedObjectException, InsertionFailedException {

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

        Set<PropertyGraph> mySet = new HashSet<>();
        BulkRandomConnectedGraphGenerator<Integer, Integer> myGenerator = new BulkRandomConnectedGraphGenerator<>();
        myGenerator.generateBulk(mySet, 2,4,4,5,6);
        for (PropertyGraph<Integer, Integer> current: mySet) {
            current.calculateProperties();
        }
        for (PropertyGraph<Integer, Integer> current: mySet) {
            database.addGraph(current);
        }
    }

    @Test
    public void testGetMinimum() {
        LinkedList<Double> testList = new LinkedList<>();
        testList.add(4.76);
        testList.add(7.98);
        testList.add(1.45);
        testList.add(3.96);
        assert MutualCorrelation.getMinimum(testList) == 1.45;
    }

    @Test
    public void testCalculatePX() {
        LinkedList<Double> testList = new LinkedList<>();
        testList.add(3.78);
        testList.add(8.94);
        testList.add(2.97);
        testList.add(3.78);
        assert MutualCorrelation.calculatePX(testList, 3.78) == 0.5;
    }

    @Test
    public void testCalculatePXY() {
        LinkedList<Double> testList1 = new LinkedList<>();
        testList1.add(5.78);
        testList1.add(3.96);
        testList1.add(9.56);
        testList1.add(1.32);

        LinkedList<Double> testList2 = new LinkedList<>();
        testList2.add(8.56);
        testList2.add(4.78);
        testList2.add(6.32);
        testList2.add(2.90);

        assert MutualCorrelation.calculatePXY(testList1, 9.56, testList2, 6.32) == 0.25;
    }

    @Test
    public void testCalculateCorrelation() throws ConnectionFailedException {
        assert Math.abs(MutualCorrelation.calculateCorrelation("AverageDegree",
                "NumberOfEdges", database) - 0.693) < 0.01;
    }

    @Test
    public void testUseMinimumSpecificProperty() throws ConnectionFailedException {
        MutualCorrelation correlation = new MutualCorrelation();
        correlation.setAttributeCounter(3);
        TreeSet<CorrelationOutput> testCorrelation = correlation.useMinimum("AverageDegree", database);
        assert testCorrelation.size() == 3;
        for (CorrelationOutput current: testCorrelation) {
            assert Math.abs(current.getOutputNumber() - 0.693) < 0.01;
        }
    }

    @Test
    public void testUseMinimumEveryProperty() throws ConnectionFailedException {
        MutualCorrelation correlation = new MutualCorrelation();
        correlation.setAttributeCounter(3);
        TreeSet<CorrelationOutput> testCorrelation = correlation.useMinimum(database);
        assert testCorrelation.size() == 3;
        for (CorrelationOutput current: testCorrelation) {
            assert Math.abs(current.getOutputNumber() - 0.693) < 0.01;
        }
    }

    @Test
    public void testUseMaximumSpecificProperty() throws ConnectionFailedException {
        MutualCorrelation correlation = new MutualCorrelation();
        correlation.setAttributeCounter(4);
        TreeSet<CorrelationOutput> testCorrelation = correlation.useMaximum("NumberOfEdges", database);
        assert testCorrelation.size() == 4;
        for (CorrelationOutput current: testCorrelation) {
            assert Math.abs(current.getOutputNumber() - 0.693) < 0.01;
        }
    }

    @Test
    public void testUseMaximumEveryProperty() throws ConnectionFailedException {
        MutualCorrelation correlation = new MutualCorrelation();
        correlation.setAttributeCounter(4);
        TreeSet<CorrelationOutput> testCorrelation = correlation.useMaximum(database);
        assert testCorrelation.size() == 4;
        for (CorrelationOutput current: testCorrelation) {
            assert Math.abs(current.getOutputNumber() - 0.693) < 0.01;
        }
    }
}

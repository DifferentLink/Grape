package edu.kit.ipd.dbis.correlation;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.connection.tables.FilterTable;
import edu.kit.ipd.dbis.database.connection.tables.GraphTable;
import edu.kit.ipd.dbis.database.exceptions.sql.*;
import edu.kit.ipd.dbis.database.file.FileManager;
import edu.kit.ipd.dbis.filter.Filtermanagement;
import edu.kit.ipd.dbis.org.jgrapht.additions.generate.BulkRandomConnectedGraphGenerator;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.junit.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class PearsonCorrelationTest {

    private static GraphDatabase database;

    public static void setDatabase(GraphDatabase newDatabase) {
        database = newDatabase;
    }
    /**
     * generates two (of two possible) graphs and adds them to the database
     * @throws UnexpectedObjectException thrown if the object type is wrong
     * @throws InsertionFailedException thrown if the graphs could not be added to database
     * @throws ConnectionFailedException thrown if the connection to database failed
     */
    public static void putGraphsIntoDatabase() throws UnexpectedObjectException, InsertionFailedException,
            ConnectionFailedException {
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

    @BeforeClass
    public static void delete() throws DatabaseDoesNotExistException, SQLException, AccessDeniedForUserException,
            ConnectionFailedException {

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
            String name = "pearsoncorrelationtests";

            FileManager fileManager = new FileManager();
            database = fileManager.createGraphDatabase(url, user, password, name);
        }

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
		for (Integer id : ids2) {
			if (id != 0) {
				database.deleteGraph(id);
			}
		}

	}

    @Test
    public void testGetSampleVariationskoeffizient() {
        LinkedList<Double> sampleNumbers = new LinkedList<>();
        sampleNumbers.add(4.33);
        sampleNumbers.add(3.76);
        sampleNumbers.add(2.87);
        sampleNumbers.add(4.98);
        double result = Pearson.getSampleVariationskoeffizient(sampleNumbers, 3.52);
        assert Math.abs(result - 1.0892666666666) < 0.1;
    }

    @Test
    public void testGetSampleVariationskoeffizientOneElementList() {
        LinkedList<Double> sampleNumber = new LinkedList<>();
        sampleNumber.add(3.14);
        double result = Pearson.getSampleVariationskoeffizient(sampleNumber, 3.52);
        assert result == 0.0;
    }

    @Test
    public void testCreateRandomMedium() {
        LinkedList<Double> sampleNumbers = new LinkedList<>();
        sampleNumbers.add(3.98);
        sampleNumbers.add(4.67);
        sampleNumbers.add(2.14);
        sampleNumbers.add(6.73);
        double result = Pearson.createRandomMedium(sampleNumbers);
        assert Math.abs(result - 4.38) < 0.01;
    }

    @Test
    public void testCalculateCorrelation() throws Exception {
        PearsonCorrelationTest.putGraphsIntoDatabase();
        Pearson pearson = new Pearson();
        double result = pearson.calculateCorrelation("numberofedges", "VertexColoringNumberOfColors", database);
        assert (result - 1) < 0.01;
    }

    @Test
    public void testCalculateCorrelationDivisionByZero() throws Exception {
        PearsonCorrelationTest.putGraphsIntoDatabase();
        //The number of vertex colorings is 1 in both cases --> SampleVariationskoeffizient is 0 --> Division by zero
        Pearson pearson = new Pearson();
        double result = pearson.calculateCorrelation("numberofvertexcolorings", "VertexColoringNumberOfColors", database);
        assert result == Double.MAX_VALUE;
    }

    @Test
    public void testUseMinimumWithProperty() throws UnexpectedObjectException, InsertionFailedException,
            ConnectionFailedException {
        PearsonCorrelationTest.putGraphsIntoDatabase();
        Pearson pearsonObject = new Pearson();
        pearsonObject.setAttributeCounter(3);
        TreeSet<CorrelationOutput> resultSet = pearsonObject.useMinimum("AverageDegree", database);

        assert resultSet.size() == 3;
        for (CorrelationOutput current: resultSet) {
            assert Math.abs(current.getOutputNumber() + 1) < 0.01;
        }
    }

    @Test
    public void testUseMinimum() throws ConnectionFailedException, InsertionFailedException, UnexpectedObjectException {
        PearsonCorrelationTest.putGraphsIntoDatabase();
        Pearson pearsonObject = new Pearson();
        pearsonObject.setAttributeCounter(4);
        TreeSet<CorrelationOutput> resultSet = pearsonObject.useMinimum(database);

        assert resultSet.size() == 4;
        for (CorrelationOutput current: resultSet) {
            assert Math.abs(current.getOutputNumber() + 1) < 0.01;
        }
    }

    @Test
    public void testUseMaximumWithProperty() throws UnexpectedObjectException, InsertionFailedException,
            ConnectionFailedException {
        PearsonCorrelationTest.putGraphsIntoDatabase();
        Pearson pearsonObject = new Pearson();
        pearsonObject.setAttributeCounter(3);
        TreeSet<CorrelationOutput> resultSet = pearsonObject.useMaximum("VertexColoringNumberOfColors", database);

        assert resultSet.size() == 3;
        for (CorrelationOutput current: resultSet) {
            assert Math.abs(current.getOutputNumber() - 1) < 0.01;
        }
    }

    @Test
    public void testUseMaximum() throws ConnectionFailedException, InsertionFailedException, UnexpectedObjectException {
        PearsonCorrelationTest.putGraphsIntoDatabase();
        Pearson pearsonObject = new Pearson();
        pearsonObject.setAttributeCounter(4);
        TreeSet<CorrelationOutput> resultSet = pearsonObject.useMaximum(database);

        assert resultSet.size() == 4;
        for (CorrelationOutput current: resultSet) {
            assert Math.abs(current.getOutputNumber() - 1) < 0.01;
        }
    }
}

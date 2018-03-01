package edu.kit.ipd.dbis.correlation;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.connection.tables.FilterTable;
import edu.kit.ipd.dbis.database.connection.tables.GraphTable;
import edu.kit.ipd.dbis.database.file.FileManager;
import edu.kit.ipd.dbis.filter.Filtermanagement;
import edu.kit.ipd.dbis.org.jgrapht.additions.generate.BulkRandomConnectedGraphGenerator;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.junit.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class PearsonCorrelationTest {

    private static GraphDatabase database;

    @Before
    public void delete() throws Exception {

    	/*Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/?user=travis&password=");
		connection.prepareStatement("CREATE DATABASE IF NOT EXISTS library").executeUpdate();
		String url = "jdbc:mysql://127.0.0.1/library";
		String user = "travis";
		String password = "";
		String name = "grape";
		FileManager fileManager = new FileManager();
		database = fileManager.createGraphDatabase(url, user, password, name);*/

	    String url = "jdbc:mysql://127.0.0.1/library";
	    String user = "user";
	    String password = "password";
	    String name = "grape2";
	    GraphDatabase gdb = new GraphDatabase(new GraphTable(url, user, password, name),
			    new FilterTable(url, user, password, "grape2filters"));
	    FileManager fileManager = new FileManager();
	    fileManager.deleteGraphDatabase(gdb);
	    database = fileManager.createGraphDatabase(url, user, password, name);
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
    public void testGetSampleVariationskoeffizientWithSmallList() {
        LinkedList<Double> sampleNumbers = new LinkedList<>();
        sampleNumbers.add(9.34);
        double result = Pearson.getSampleVariationskoeffizient(sampleNumbers, 6.78);
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
        PropertyGraph<Integer, Integer> testGraph1 = new PropertyGraph<>();
        Set<PropertyGraph> mySet = new HashSet<>();
        BulkRandomConnectedGraphGenerator<Integer, Integer> myGenerator = new BulkRandomConnectedGraphGenerator<>();
        myGenerator.generateBulk(mySet, 2,4,4,5,6);
        for (PropertyGraph<Integer, Integer> current: mySet) {
            database.addGraph(current);
        }
    }
}

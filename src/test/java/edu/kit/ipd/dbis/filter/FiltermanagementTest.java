package edu.kit.ipd.dbis.filter;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.connection.tables.FilterTable;
import edu.kit.ipd.dbis.database.connection.tables.GraphTable;
import edu.kit.ipd.dbis.database.exceptions.sql.*;
import edu.kit.ipd.dbis.database.file.FileManager;
import edu.kit.ipd.dbis.filter.exceptions.InvalidInputException;
import org.junit.*;

import java.sql.Connection;
import java.sql.DriverManager;

public class FiltermanagementTest {

    private static Filtermanagement manager;
    private static GraphDatabase database;

    @Ignore
    @Before
    public void setUp() throws Exception {
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/?user=travis&password=");
        connection.prepareStatement("CREATE DATABASE IF NOT EXISTS library").executeUpdate();
        String url = "jdbc:mysql://127.0.0.1/library";
        String user = "travis";
        String password = "";
        String name = "grape";

	    GraphTable graphs = new GraphTable(url, user, password, name);
	    FilterTable filters = new FilterTable(url, user, password, name);
	    GraphDatabase graphDatabase = new GraphDatabase(graphs, filters);

	    FileManager fileManager = new FileManager();
	    fileManager.deleteGraphDatabase(graphDatabase);
	    database = fileManager.createGraphDatabase(url, user, password, name);

        manager = new Filtermanagement();
        manager.setDatabase(database);

    }

    @Ignore
    @Test
    public void testSetDatabase() throws InvalidInputException, ConnectionFailedException, InsertionFailedException, UnexpectedObjectException {
        manager.updateFilter("Averagedegree = 10", 1);
        manager.updateFiltergroup("asf", 2);
        manager.updateFilter("greatestDegree < 5", 3, 2);
        manager.availableFilter.clear();
        manager.availableFilterGroups.clear();
        manager.setDatabase(database);
        System.out.println(manager.availableFilter.get(0));
        System.out.println(manager.availableFilterGroups.get(0));
        System.out.println(manager.availableFilterGroups.get(0).getAvailableFilter().get(0));

    }

    @Ignore
    @Test
    public void testSettingOfDatabase() {
        assert manager.getDatabase() != null;
    }

    @Ignore
    @Test
    public void testStringParserSimpleFilter() throws InvalidInputException, ConnectionFailedException,
            InsertionFailedException, UnexpectedObjectException {
        manager.updateFilter("averagedegree = 10", 7);
        manager.activate(7);
        manager.updateFilter("LargestCliqueSize + 12 >= smallestdegree - 8", 2);
        manager.activate(2);
        manager.updateFilter("ProportionDensity / 3.24 > StructureDensity * 2", 4);
        manager.activate(4);
        String[][] output = manager.parseFilterList();

        assert output[0][0].equals("averagedegree");
        assert output[0][1].equals("+");
        assert output[0][2].equals("0");
        assert output[0][3].equals("=");
        assert output[0][4].equals("0");
        assert output[0][5].equals("+");
        assert output[0][6].equals("10.0");

        assert output[1][0].equals("largestcliquesize");
        assert output[1][1].equals("+");
        assert output[1][2].equals("12.0");
        assert output[1][3].equals(">=");
        assert output[1][4].equals("smallestdegree");
        assert output[1][5].equals("-");
        assert output[1][6].equals("8.0");

        assert output[2][0].equals("proportiondensity");
        assert output[2][1].equals("/");
        assert output[2][2].equals("3.24");
        assert output[2][3].equals(">");
        assert output[2][4].equals("structuredensity");
        assert output[2][5].equals("*");
        assert output[2][6].equals("2.0");
    }

    @Ignore
    @Test
    public void testStringParserFiltergroup() throws InvalidInputException, ConnectionFailedException,
            InsertionFailedException, UnexpectedObjectException {
        manager.updateFiltergroup("This is a filtergroup", 44);
        manager.updateFilter("StructureDensity < 39", 7, 44);
        manager.activate(44);
        manager.activate(7);
        manager.updateFilter("TotalColoringNumberOfColors / 3 <= VertexColoringNumberOfColors * 4", 2, 44);
        manager.activate(2);
        String[][] output = manager.parseFilterList();

        assert output[0][0].equals("structuredensity");
        assert output[0][1].equals("+");
        assert output[0][2].equals("0");
        assert output[0][3].equals("<");
        assert output[0][4].equals("0");
        assert output[0][5].equals("+");
        assert output[0][6].equals("39.0");

        assert output[1][0].equals("totalcoloringnumberofcolors");
        assert output[1][1].equals("/");
        assert output[1][2].equals("3.0");
        assert output[1][3].equals("<=");
        assert output[1][4].equals("vertexcoloringnumberofcolors");
        assert output[1][5].equals("*");
        assert output[1][6].equals("4.0");
    }

    @Ignore
    @Test
    public void replaceFilter() throws ConnectionFailedException, InvalidInputException, InsertionFailedException,
            UnexpectedObjectException {
        manager.updateFilter("averagedegree > 22", 8);
        manager.updateFilter("averagedegree >= 12", 8);
        assert manager.availableFilter.size() == 1;
        assert manager.availableFilter.get(0).getName().equals("averagedegree >= 12");
    }

    @Ignore
    @Test
    public void replaceFilterGroup() throws ConnectionFailedException, UnexpectedObjectException,
            InsertionFailedException {
        manager.updateFiltergroup("This is a filtergroup.", 9);
        manager.updateFiltergroup("This is an updated filtergroup", 9);
        assert manager.availableFilterGroups.size() == 1;
        assert manager.availableFilterGroups.get(0).getName().equals("This is an updated filtergroup");
    }

    @Ignore
    @Test
    public void replaceFilterInFiltergroup() throws ConnectionFailedException, UnexpectedObjectException,
            InsertionFailedException, InvalidInputException {
        manager.updateFiltergroup("This is a filtergroup", 8);
        manager.updateFilter("averagedegree <= 41", 4, 8);
        manager.updateFilter("averagedegree < 41", 4);
        assert manager.availableFilterGroups.size() == 1;
        assert manager.availableFilterGroups.get(0).getAvailableFilter().get(0).getName().equals("averagedegree < 41");
    }

    @Ignore
    @Test
    public void replaceFiltersegments() throws ConnectionFailedException, InvalidInputException,
            InsertionFailedException, UnexpectedObjectException {
        manager.updateFilter("LargestSubgraphSize < 21.87", 1);
        manager.updateFilter("NumberOfCliques > 12.43", 2);
        manager.updateFilter("NumberOfEdges = 15", 3);
        manager.updateFiltergroup("This is the first sample group", 4);
        manager.updateFilter("NumberOfTotalColorings + 23 = averagedegree", 5, 4);
        manager.updateFilter("NumberOfVertexColorings > 32", 6, 4);
        manager.updateFiltergroup("This is the second filtergroup", 7);
        manager.updateFilter("NumberOfVertices <= 2",8,7);
        manager.updateFilter("SmallestDegree >= 6.76", 9, 7);

        assert manager.availableFilter.size() == 3;
        assert manager.availableFilterGroups.size() == 2;
        assert manager.availableFilterGroups.get(0).getAvailableFilter().size() == 2;
        assert manager.availableFilterGroups.get(1).getAvailableFilter().size() == 2;

        manager.updateFilter("averagedegree = 23", 3);
        manager.updateFiltergroup("The second group has been modified", 4);
        manager.updateFilter("smallestdegree < 12.65", 5, 4);

        assert manager.availableFilter.size() == 3;
        assert manager.availableFilterGroups.size() == 2;
        assert manager.availableFilterGroups.get(0).getAvailableFilter().size() == 2;
        assert manager.availableFilterGroups.get(1).getAvailableFilter().size() == 2;
    }

    @Ignore
    @Test
    public void removeFilter() throws ConnectionFailedException, InvalidInputException, InsertionFailedException,
            UnexpectedObjectException {
        manager.updateFilter("smallestdegree > 32", 16);
        assert manager.availableFilter.get(0).getName().equals("smallestdegree > 32");
        manager.removeFiltersegment(16);
        assert manager.availableFilter.size() == 0;

        //removes a filter which is not in the list of available filter / available filtergroups --> nothing should happen
        manager.removeFiltersegment(81);
    }

    @Ignore
    @Test
    public void removeFiltergroup() throws ConnectionFailedException, UnexpectedObjectException,
            InsertionFailedException {
        manager.updateFiltergroup("This is a filter group", 9);
        assert manager.availableFilterGroups.get(0).getName().equals("This is a filter group");
        manager.removeFiltersegment(9);
        assert manager.availableFilterGroups.size() == 0;
    }

    @Ignore
    @Test
    public void removeFilterFromGroup() throws ConnectionFailedException, UnexpectedObjectException,
            InsertionFailedException, InvalidInputException {
        manager.updateFiltergroup("This is a sample group", 76);
        manager.updateFilter("largestcliquesize < 21", 12, 76);
        assert  manager.availableFilterGroups.get(0).getAvailableFilter().get(0).getName().equals("largestcliquesize < 21");
        manager.removeFiltersegment(12);
        assert manager.availableFilterGroups.get(0).getAvailableFilter().size() == 0;
    }

    @Ignore
    @Test
    public void deactivateFilter() throws ConnectionFailedException, InvalidInputException, InsertionFailedException,
            UnexpectedObjectException {
        manager.updateFilter("smallestdegree = 3", 2);
        manager.activate(2);
        assert manager.availableFilter.get(0).isActivated;
        manager.deactivate(2);
        assert !manager.availableFilter.get(0).isActivated;
    }

    @Ignore
    @Test
    public void deactivateFiltergroup() throws ConnectionFailedException, UnexpectedObjectException,
            InsertionFailedException {
        manager.updateFiltergroup("This is a sample group", 33);
        manager.activate(33);
        assert manager.availableFilterGroups.get(0).isActivated;
        manager.deactivate(33);
        assert !manager.availableFilterGroups.get(0).isActivated;
    }

    @Ignore
    @Test
    public void deactivateFilterInGroup() throws ConnectionFailedException, UnexpectedObjectException,
            InsertionFailedException, InvalidInputException {
        manager.updateFiltergroup("This is a sample group", 22);
        manager.updateFilter("numberofvertices >= 32", 21, 22);
        manager.activate(22);
        manager.activate(21);
        assert manager.availableFilterGroups.get(0).getAvailableFilter().get(0).isActivated;
        manager.deactivate(21);
        assert !manager.availableFilterGroups.get(0).getAvailableFilter().get(0).isActivated;
    }

    @Ignore
    @Test
    public void deactivateFiltersegments() throws ConnectionFailedException, InvalidInputException,
            InsertionFailedException, UnexpectedObjectException {
        manager.updateFilter("LargestSubgraphSize < 21.87", 1);
        manager.updateFilter("NumberOfCliques > 12.43", 2);
        manager.updateFilter("NumberOfEdges = 15", 3);
        manager.updateFiltergroup("This is the first sample group", 4);
        manager.updateFilter("NumberOfTotalColorings + 23 = averagedegree", 5, 4);
        manager.updateFilter("NumberOfVertexColorings > 32", 6, 4);
        manager.updateFiltergroup("This is the second filtergroup", 7);
        manager.updateFilter("NumberOfVertices <= 2",8,7);
        manager.updateFilter("SmallestDegree >= 6.76", 9, 7);

        manager.activate(1);
        manager.activate(2);
        manager.activate(3);
        manager.activate(4);
        manager.activate(5);
        manager.activate(6);
        manager.activate(7);
        manager.activate(8);
        manager.activate(9);

        //activate a filter which does not exist --> nothing should happen
        manager.activate(10);

        assert manager.availableFilter.get(0).isActivated;
        assert manager.availableFilter.get(1).isActivated;
        assert manager.availableFilter.get(2).isActivated;
        assert manager.availableFilterGroups.get(0).isActivated;
        assert manager.availableFilterGroups.get(0).getAvailableFilter().get(0).isActivated;
        assert manager.availableFilterGroups.get(0).getAvailableFilter().get(1).isActivated;
        assert manager.availableFilterGroups.get(1).isActivated;
        assert manager.availableFilterGroups.get(1).getAvailableFilter().get(0).isActivated;
        assert manager.availableFilterGroups.get(1).getAvailableFilter().get(1).isActivated;

        manager.deactivate(1);
        manager.deactivate(2);
        manager.deactivate(3);
        manager.deactivate(4);
        manager.deactivate(8);
        manager.deactivate(9);

        //deactivate a filter which does not exist --> nothing should happen
        manager.deactivate(10);

        assert !manager.availableFilter.get(0).isActivated;
        assert !manager.availableFilter.get(1).isActivated;
        assert !manager.availableFilter.get(2).isActivated;
        assert !manager.availableFilterGroups.get(0).isActivated;
        assert manager.availableFilterGroups.get(0).getAvailableFilter().get(0).isActivated;
        assert manager.availableFilterGroups.get(0).getAvailableFilter().get(1).isActivated;
        assert manager.availableFilterGroups.get(1).isActivated;
        assert !manager.availableFilterGroups.get(1).getAvailableFilter().get(0).isActivated;
        assert !manager.availableFilterGroups.get(1).getAvailableFilter().get(1).isActivated;
    }

    @Ignore
    @Test
    public void testFilterInputParserBasicFilter() throws ConnectionFailedException, InvalidInputException,
            InsertionFailedException, UnexpectedObjectException {
        manager.updateFilter("averagedegree = 10.87", 7);
        assert manager.availableFilter.get(0).getName().equals("averagedegree = 10.87");
    }

    @Ignore
    @Test
    public void testFilterInputParserConnectedFilter() throws  ConnectionFailedException, InvalidInputException,
            InsertionFailedException,
            UnexpectedObjectException {
        manager.updateFilter("NumberOfDisjointEdgesFromKkGraph + 34 < averagedegree - 4.76", 65);
        assert manager.availableFilter.get(0).property1.equals("numberofdisjointedgesfromkkgraph");
        assert manager.availableFilter.get(0).operator1.equals(Operator.ADD);
        assert manager.availableFilter.get(0).value1 == 34.0;
        assert manager.availableFilter.get(0).relation.equals(Relation.LESSTHAN);
        assert manager.availableFilter.get(0).property2.equals("averagedegree");
        assert manager.availableFilter.get(0).operator2.equals(Operator.SUB);
        assert manager.availableFilter.get(0).value2 == 4.76;
        manager.removeFiltersegment(65);

        manager.updateFilter("NumberOfDisjointVerticesFromKkGraph / 7 > LargestSubgraphSize", 4);
        assert manager.availableFilter.get(0).property1.equals("numberofdisjointverticesfromkkgraph");
        assert manager.availableFilter.get(0).operator1.equals(Operator.DIV);
        assert manager.availableFilter.get(0).value1 == 7.0;
        assert manager.availableFilter.get(0).relation.equals(Relation.GREATHERTHAN);
        assert manager.availableFilter.get(0).property2.equals("largestsubgraphsize");
        assert manager.availableFilter.get(0).operator2.equals(Operator.ADD);
        assert manager.availableFilter.get(0).value2 == 0.0;
        manager.removeFiltersegment(4);

        manager.updateFilter("ProportionDensity >= StructureDensity", 5);
        assert manager.availableFilter.get(0).property1.equals("proportiondensity");
        assert manager.availableFilter.get(0).operator1.equals(Operator.ADD);
        assert manager.availableFilter.get(0).value1 == 0.0;
        assert manager.availableFilter.get(0).relation.equals(Relation.GREATHEROREQUAL);
        assert manager.availableFilter.get(0).property2.equals("structuredensity");
        assert manager.availableFilter.get(0).operator2.equals(Operator.ADD);
        assert manager.availableFilter.get(0).value2 == 0.0;
        manager.removeFiltersegment(5);

        manager.updateFilter("BinomialDensity <= LargestCliqueSize * 3", 6);
        assert manager.availableFilter.get(0).property1.equals("binomialdensity");
        assert manager.availableFilter.get(0).operator1.equals(Operator.ADD);
        assert manager.availableFilter.get(0).value1 == 0.0;
        assert manager.availableFilter.get(0).relation.equals(Relation.LESSOREQUAL);
        assert manager.availableFilter.get(0).property2.equals("largestcliquesize");
        assert manager.availableFilter.get(0).operator2.equals(Operator.MULT);
        assert manager.availableFilter.get(0).value2 == 3.0;
        manager.removeFiltersegment(6);

        manager.updateFilter("hadwigerconjecture", 7);
        assert manager.availableFilter.get(0).property1.equals("kkgraphnumberofsubgraphs");
        assert manager.availableFilter.get(0).operator1.equals(Operator.ADD);
        assert manager.availableFilter.get(0).value1 == 0.0;
        assert manager.availableFilter.get(0).relation.equals(Relation.GREATHEROREQUAL);
        assert manager.availableFilter.get(0).property2.equals("vertexcoloringnumberofcolors");
        assert manager.availableFilter.get(0).operator2.equals(Operator.ADD);
        assert manager.availableFilter.get(0).value2 == 0.0;
        manager.removeFiltersegment(7);

        manager.updateFilter("totalcoloringconjecture", 9);
        assert manager.availableFilter.get(0).property1.equals("totalcoloringnumberofcolors");
        assert manager.availableFilter.get(0).operator1.equals(Operator.ADD);
        assert manager.availableFilter.get(0).value1 == 0.0;
        assert manager.availableFilter.get(0).relation.equals(Relation.LESSOREQUAL);
        assert manager.availableFilter.get(0).property2.equals("greatestdegree");
        assert manager.availableFilter.get(0).operator2.equals(Operator.ADD);
        assert manager.availableFilter.get(0).value2 == 2.0;
        manager.removeFiltersegment(9);
    }

    @Ignore
    @Test
    public void testFilterInputParserInvalidInputs() throws ConnectionFailedException,
            InsertionFailedException, UnexpectedObjectException {
        int inputExceptionCounter = 0;
        try {
            manager.updateFilter("", 23);
        } catch (InvalidInputException e) {
            inputExceptionCounter++;
        }
        try {
            manager.updateFilter("averagedegree ", 23);
        } catch (InvalidInputException e) {
            inputExceptionCounter++;
        }
        try {
            manager.updateFilter(null, 23);
        } catch (InvalidInputException e) {
            inputExceptionCounter++;
        }
        assert inputExceptionCounter == 3;
    }

    @Ignore
    @Test
    public void testFilterInputParserInvalidBasicInput() throws UnexpectedObjectException, InsertionFailedException,
            ConnectionFailedException {
        int inputExceptionCounter = 0;
        try {
            manager.updateFilter("WRONGPROPERTY = 12", 23);
        } catch (InvalidInputException e) {
            inputExceptionCounter++;
        }
        try {
            manager.updateFilter("averagedegree WRNONGRELATION 12", 23);
        } catch (InvalidInputException e) {
            inputExceptionCounter++;
        }
        try {
            manager.updateFilter("averagedegree = NOTANUMBER", 23);
        } catch (InvalidInputException e) {
            inputExceptionCounter++;
        }
        assert inputExceptionCounter == 3;
    }

    @Ignore
    @Test
    public void testFilterInputParserInvalidConnectedInput() throws UnexpectedObjectException, InsertionFailedException,
            ConnectionFailedException {
        int inputExceptionCounter = 0;
        try {
            manager.updateFilter("WRONGFIRSTPROPERTY * 10 < TotalColoringNumberOfColors / 5", 23);
        } catch (InvalidInputException e) {
            inputExceptionCounter++;
        }
        try {
            manager.updateFilter("averagedegree WRONGFIRSTOPERATOR 10 < TotalColoringNumberOfColors / 5", 23);
        } catch (InvalidInputException e) {
            inputExceptionCounter++;
        }
        try {
            manager.updateFilter("averagedegree * NOTANUMBER < TotalColoringNumberOfColors / 5", 23);
        } catch (InvalidInputException e) {
            inputExceptionCounter++;
        }
        try {
            manager.updateFilter("averagedegree * 4 WRONGRELATION TotalColoringNumberOfColors / 5", 23);

        } catch (InvalidInputException e) {
            inputExceptionCounter++;
        }
        try {
            manager.updateFilter("averagedegree * 4 < WRONGSECONDPROPERTY / 5", 23);
        } catch (InvalidInputException e) {
            inputExceptionCounter++;
        }
        try {
            manager.updateFilter("averagedegree * 4 < TotalColoringNumberOfColors WRONGSECONDOPERATOR 5", 23);
        } catch (InvalidInputException e) {
            inputExceptionCounter++;
        }
        try {
            manager.updateFilter("averagedegree * 4 < TotalColoringNumberOfColors / NOTANUMBER", 23);
        } catch (InvalidInputException e) {
            inputExceptionCounter++;
        }
        assert inputExceptionCounter == 7;
    }
}

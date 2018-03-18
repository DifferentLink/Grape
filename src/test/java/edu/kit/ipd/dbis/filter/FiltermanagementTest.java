package edu.kit.ipd.dbis.filter;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.InsertionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.UnexpectedObjectException;
import edu.kit.ipd.dbis.database.file.FileManager;
import edu.kit.ipd.dbis.filter.exceptions.InvalidInputException;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_.AverageDegree;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class FiltermanagementTest {

    private static Filtermanagement manager;
    private static GraphDatabase database;

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
            String name = "filtermanagementtests";

            FileManager fileManager = new FileManager();
            database = fileManager.createGraphDatabase(url, user, password, name);
        }
        manager = new Filtermanagement();
        manager.setDatabase(database);


    }

    @Before
    public void clear() throws SQLException, ConnectionFailedException {
    	LinkedList<Integer> ids = database.getFilterTable().getIds();
    	for (Integer id : ids) {
    		if (id != 0) {
    			database.deleteFilter(id);
		    }
	    }

	    manager.availableFilterGroups.clear();
    	manager.availableFilters.clear();
    }

    @Test
    public void testSetDatabase() throws InvalidInputException, ConnectionFailedException, InsertionFailedException, UnexpectedObjectException {
        manager.updateFilter("Averagedegree = 10", 1);
        manager.updateFiltergroup("asf", 2);
        manager.updateFilter("greatestDegree < 5", 3, 2);
        manager.availableFilters.clear();
        manager.availableFilterGroups.clear();
        manager.setDatabase(database);
    }

    @Test
    public void testSettingOfDatabase() {
        assert manager.getDatabase() != null;
    }

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
        int averageDegreeId = 0;
        int largestCliqueSizeId = 0;
        int proportionDensityId = 0;
        for (int i = 0; i < 3; i++) {
			switch (output[i][0]) {
				case "averagedegree": averageDegreeId = i; break;
				case "largestcliquesize" : largestCliqueSizeId = i; break;
				case "proportiondensity" : proportionDensityId = i; break;
			}
		}

        assert output[averageDegreeId][0].equals("averagedegree");
        assert output[averageDegreeId][1].equals("+");
        assert output[averageDegreeId][2].equals("0");
        assert output[averageDegreeId][3].equals("=");
        assert output[averageDegreeId][4].equals("0");
        assert output[averageDegreeId][5].equals("+");
        assert output[averageDegreeId][6].equals("10.0");

        assert output[largestCliqueSizeId][0].equals("largestcliquesize");
        assert output[largestCliqueSizeId][1].equals("+");
        assert output[largestCliqueSizeId][2].equals("12.0");
        assert output[largestCliqueSizeId][3].equals(">=");
        assert output[largestCliqueSizeId][4].equals("smallestdegree");
        assert output[largestCliqueSizeId][5].equals("-");
        assert output[largestCliqueSizeId][6].equals("8.0");

        assert output[proportionDensityId][0].equals("proportiondensity");
        assert output[proportionDensityId][1].equals("/");
        assert output[proportionDensityId][2].equals("3.24");
        assert output[proportionDensityId][3].equals(">");
        assert output[proportionDensityId][4].equals("structuredensity");
        assert output[proportionDensityId][5].equals("*");
        assert output[proportionDensityId][6].equals("2.0");
    }

    @Test
    public void testStringParserFiltergroup() throws InvalidInputException, ConnectionFailedException,
            InsertionFailedException, UnexpectedObjectException {
        manager.updateFiltergroup("This is a filtergroup", 44);
        manager.updateFilter("ProportionDensity < 39", 7, 44);
        manager.activate(44);
        manager.activate(7);
        manager.updateFilter("TotalColoringNumberOfColors / 3 <= VertexColoringNumberOfColors * 4", 2, 44);
        manager.activate(2);
        String[][] output = manager.parseFilterList();
		int totalcoloringId = 0;
		int proportionDensityId = 0;
		for (int i = 0; i < 2; i++) {
			switch (output[i][0]) {
				case "totalcoloringnumberofcolors": totalcoloringId = i; break;
				case "proportiondensity" : proportionDensityId = i; break;
			}
		}

        assert output[proportionDensityId][0].equals("proportiondensity");
        assert output[proportionDensityId][1].equals("+");
        assert output[proportionDensityId][2].equals("0");
        assert output[proportionDensityId][3].equals("<");
        assert output[proportionDensityId][4].equals("0");
        assert output[proportionDensityId][5].equals("+");
        assert output[proportionDensityId][6].equals("39.0");

        assert output[totalcoloringId][0].equals("totalcoloringnumberofcolors");
        assert output[totalcoloringId][1].equals("/");
        assert output[totalcoloringId][2].equals("3.0");
        assert output[totalcoloringId][3].equals("<=");
        assert output[totalcoloringId][4].equals("vertexcoloringnumberofcolors");
        assert output[totalcoloringId][5].equals("*");
        assert output[totalcoloringId][6].equals("4.0");
    }

    @Test
    public void replaceFilter() throws ConnectionFailedException, InvalidInputException, InsertionFailedException,
            UnexpectedObjectException {
        manager.updateFilter("averagedegree > 22", 8);
        manager.updateFilter("averagedegree >= 12", 8);
        assert manager.availableFilters.size() == 1;
        assert manager.availableFilters.get(8).getName().equals("averagedegree >= 12");
    }

    @Test
    public void replaceFilterGroup() throws ConnectionFailedException, UnexpectedObjectException,
            InsertionFailedException {
        manager.updateFiltergroup("This is a filtergroup.", 9);
        manager.updateFiltergroup("This is an updated filtergroup", 9);
        assert manager.availableFilterGroups.values().size() == 1;
        assert manager.availableFilterGroups.get(9).getName().equals("This is an updated filtergroup");
    }

    @Test
    public void replaceFilterInFiltergroup() throws ConnectionFailedException, UnexpectedObjectException,
            InsertionFailedException, InvalidInputException {
        manager.updateFiltergroup("This is a filtergroup", 8);
        manager.updateFilter("averagedegree <= 41", 4, 8);
        manager.updateFilter("averagedegree < 41", 4);
        assert manager.availableFilterGroups.values().size() == 1;
        assert manager.availableFilterGroups.get(8).getFilter(4).getName().equals("averagedegree < 41");
    }

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

        assert manager.availableFilters.size() == 3;
        assert manager.availableFilterGroups.size() == 2;
        assert manager.availableFilterGroups.get(4).getAvailableFilters().size() == 2;
        assert manager.availableFilterGroups.get(7).getAvailableFilters().size() == 2;

        manager.updateFilter("averagedegree = 23", 3);
        manager.updateFiltergroup("The second group has been modified", 4);
        manager.updateFilter("smallestdegree < 12.65", 5, 4);

        assert manager.availableFilters.size() == 3;
        assert manager.availableFilterGroups.size() == 2;
        assert manager.availableFilterGroups.get(4).getAvailableFilters().size() == 2;
        assert manager.availableFilterGroups.get(7).getAvailableFilters().size() == 2;
    }

    @Test
    public void removeFilter() throws ConnectionFailedException, InvalidInputException, InsertionFailedException,
            UnexpectedObjectException {
        manager.updateFilter("smallestdegree > 32", 16);
        assert manager.availableFilters.get(16).getName().equals("smallestdegree > 32");
        manager.removeFiltersegment(16);
        assert !manager.availableFilters.containsKey(16);

        //removes a filter which is not in the list of available filter / available filtergroups --> nothing should happen
        manager.removeFiltersegment(81);
    }

    @Test
    public void removeFiltergroup() throws ConnectionFailedException, UnexpectedObjectException,
            InsertionFailedException {
        manager.updateFiltergroup("This is a filter group", 9);
        assert manager.availableFilterGroups.get(9).getName().equals("This is a filter group");
        manager.removeFiltersegment(9);
        assert manager.availableFilterGroups.values().size() == 0;
    }

    @Test
    public void removeFilterFromGroup() throws ConnectionFailedException, UnexpectedObjectException,
            InsertionFailedException, InvalidInputException {
        manager.updateFiltergroup("This is a sample group", 76);
        manager.updateFilter("largestcliquesize < 21", 12, 76);
        assert  manager.availableFilterGroups.get(76).getFilter(12).getName().equals("largestcliquesize < 21");
        manager.removeFiltersegment(12);
        assert manager.availableFilterGroups.get(76).getAvailableFilters().size() == 0;
    }

    @Test
    public void deactivateFilter() throws ConnectionFailedException, InvalidInputException, InsertionFailedException,
            UnexpectedObjectException {
        manager.updateFilter("smallestdegree = 3", 2);
        manager.activate(2);
        assert manager.availableFilters.get(2).isActivated;
        manager.deactivate(2);
        assert !manager.availableFilters.get(2).isActivated;
    }

    @Test
    public void deactivateFiltergroup() throws ConnectionFailedException, UnexpectedObjectException,
            InsertionFailedException {
        manager.updateFiltergroup("This is a sample group", 33);
        manager.activate(33);
        assert manager.availableFilterGroups.get(33).isActivated;
        manager.deactivate(33);
        assert !manager.availableFilterGroups.get(33).isActivated;
    }

    @Test
    public void deactivateFilterInGroup() throws ConnectionFailedException, UnexpectedObjectException,
            InsertionFailedException, InvalidInputException {
        manager.updateFiltergroup("This is a sample group", 22);
        manager.updateFilter("numberofvertices >= 32", 21, 22);
        manager.activate(22);
        manager.activate(21);
        assert manager.availableFilterGroups.get(22).getFilter(21).isActivated;
        manager.deactivate(21);
        assert !manager.availableFilterGroups.get(22).getFilter(21).isActivated;
    }

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

        assert manager.availableFilters.get(1).isActivated;
        assert manager.availableFilters.get(2).isActivated;
        assert manager.availableFilters.get(3).isActivated;
        assert manager.availableFilterGroups.get(4).isActivated;
        assert manager.availableFilterGroups.get(4).getFilter(5).isActivated;
        assert manager.availableFilterGroups.get(4).getFilter(6).isActivated;
        assert manager.availableFilterGroups.get(7).isActivated;
        assert manager.availableFilterGroups.get(7).getFilter(8).isActivated;
        assert manager.availableFilterGroups.get(7).getFilter(9).isActivated;

        manager.deactivate(1);
        manager.deactivate(2);
        manager.deactivate(3);
        manager.deactivate(4);
        manager.deactivate(8);
        manager.deactivate(9);

        //deactivate a filter which does not exist --> nothing should happen
        manager.deactivate(10);

        assert !manager.availableFilters.get(1).isActivated;
        assert !manager.availableFilters.get(2).isActivated;
        assert !manager.availableFilters.get(3).isActivated;
        assert !manager.availableFilterGroups.get(4).isActivated;
        assert !manager.availableFilterGroups.get(4).getFilter(5).isActivated;
        assert !manager.availableFilterGroups.get(4).getFilter(6).isActivated;
        assert manager.availableFilterGroups.get(7).isActivated;
        assert !manager.availableFilterGroups.get(7).getFilter(8).isActivated;
        assert !manager.availableFilterGroups.get(7).getFilter(9).isActivated;
    }

    @Test
    public void testFilterInputParserBasicFilter() throws ConnectionFailedException, InvalidInputException,
            InsertionFailedException, UnexpectedObjectException {
        manager.updateFilter("averagedegree = 10.87", 7);
        assert manager.availableFilters.get(7).getName().equals("averagedegree = 10.87");
    }

    @Test
    public void testFilterInputParserConnectedFilter() throws  ConnectionFailedException, InvalidInputException,
            InsertionFailedException,
            UnexpectedObjectException {
        manager.updateFilter("NumberOfDisjointEdgesFromKkGraph + 34 < averagedegree - 4.76", 65);
        assert manager.availableFilters.get(65).property1.equals("numberofdisjointedgesfromkkgraph");
        assert manager.availableFilters.get(65).operator1.equals(Operator.ADD);
        assert manager.availableFilters.get(65).value1 == 34.0;
        assert manager.availableFilters.get(65).relation.equals(Relation.LESSTHAN);
        assert manager.availableFilters.get(65).property2.equals("averagedegree");
        assert manager.availableFilters.get(65).operator2.equals(Operator.SUB);
        assert manager.availableFilters.get(65).value2 == 4.76;
        manager.removeFiltersegment(65);

        manager.updateFilter("NumberOfDisjointVerticesFromKkGraph / 7 > LargestSubgraphSize", 4);
        assert manager.availableFilters.get(4).property1.equals("numberofdisjointverticesfromkkgraph");
        assert manager.availableFilters.get(4).operator1.equals(Operator.DIV);
        assert manager.availableFilters.get(4).value1 == 7.0;
        assert manager.availableFilters.get(4).relation.equals(Relation.GREATHERTHAN);
        assert manager.availableFilters.get(4).property2.equals("largestsubgraphsize");
        assert manager.availableFilters.get(4).operator2.equals(Operator.ADD);
        assert manager.availableFilters.get(4).value2 == 0.0;
        manager.removeFiltersegment(4);

        manager.updateFilter("ProportionDensity >= StructureDensity", 5);
        assert manager.availableFilters.get(5).property1.equals("proportiondensity");
        assert manager.availableFilters.get(5).operator1.equals(Operator.ADD);
        assert manager.availableFilters.get(5).value1 == 0.0;
        assert manager.availableFilters.get(5).relation.equals(Relation.GREATHEROREQUAL);
        assert manager.availableFilters.get(5).property2.equals("structuredensity");
        assert manager.availableFilters.get(5).operator2.equals(Operator.ADD);
        assert manager.availableFilters.get(5).value2 == 0.0;
        manager.removeFiltersegment(5);

        manager.updateFilter("BinomialDensity <= LargestCliqueSize * 3", 6);
        assert manager.availableFilters.get(6).property1.equals("binomialdensity");
        assert manager.availableFilters.get(6).operator1.equals(Operator.ADD);
        assert manager.availableFilters.get(6).value1 == 0.0;
        assert manager.availableFilters.get(6).relation.equals(Relation.LESSOREQUAL);
        assert manager.availableFilters.get(6).property2.equals("largestcliquesize");
        assert manager.availableFilters.get(6).operator2.equals(Operator.MULT);
        assert manager.availableFilters.get(6).value2 == 3.0;
        manager.removeFiltersegment(6);

        manager.updateFilter("hadwigerconjecture", 7);
        assert manager.availableFilters.get(7).property1.equals("kkgraphnumberofsubgraphs");
        assert manager.availableFilters.get(7).operator1.equals(Operator.ADD);
        assert manager.availableFilters.get(7).value1 == 0.0;
        assert manager.availableFilters.get(7).relation.equals(Relation.GREATHEROREQUAL);
        assert manager.availableFilters.get(7).property2.equals("vertexcoloringnumberofcolors");
        assert manager.availableFilters.get(7).operator2.equals(Operator.ADD);
        assert manager.availableFilters.get(7).value2 == 0.0;
        manager.removeFiltersegment(7);

        manager.updateFilter("totalcoloringconjecture", 9);
        assert manager.availableFilters.get(9).property1.equals("totalcoloringnumberofcolors");
        assert manager.availableFilters.get(9).operator1.equals(Operator.ADD);
        assert manager.availableFilters.get(9).value1 == 0.0;
        assert manager.availableFilters.get(9).relation.equals(Relation.LESSOREQUAL);
        assert manager.availableFilters.get(9).property2.equals("greatestdegree");
        assert manager.availableFilters.get(9).operator2.equals(Operator.ADD);
        assert manager.availableFilters.get(9).value2 == 2.0;
        manager.removeFiltersegment(9);
    }


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


    @Test
    public void testGetAvailableFilterGroups() throws InvalidInputException, ConnectionFailedException,
            InsertionFailedException, UnexpectedObjectException {
        manager.updateFilter("VertexColoringNumberOfColors < 45.8", 1);
        manager.updateFilter("LargestCliqueSize > 22.1", 2);
        manager.updateFiltergroup("This is the first sample group", 3);
        manager.updateFiltergroup("This is the second sample group", 4);
        assert manager.availableFilters.get(1).getName().equals("VertexColoringNumberOfColors < 45.8");
        assert manager.availableFilters.get(2).getName().equals("LargestCliqueSize > 22.1");
        assert manager.availableFilterGroups.get(3).getName().equals("This is the first sample group");
        assert manager.availableFilterGroups.get(4).getName().equals("This is the second sample group");
    }


    @Test
    public void testGetFilteredAndAscendingSortedGraphs() throws ConnectionFailedException {
        manager.getFilteredAndAscendingSortedGraphs(new AverageDegree(new PropertyGraph<Integer, Integer>()));
    }


    @Test
    public void testGetFilteredAndDescendingSortedGraphs() throws ConnectionFailedException {
        manager.getFilteredAndDescendingSortedGraphs(new AverageDegree(new PropertyGraph<Integer, Integer>()));
    }


    @Test
    public void testGetFilteredAndSortedGraphs() throws ConnectionFailedException {
        manager.getFilteredAndSortedGraphs();
    }
}

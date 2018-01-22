package edu.kit.ipd.dbis.Filter;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.GreatestDegree;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.NumberOfTotalColorings;
import org.junit.Test;

import java.sql.SQLException;

public class FiltermanagementTest {

    @Test
    public void insertGroupToTable() throws Exception {
        GraphDatabase database = new GraphDatabase(null, null);
        Filtermanagement manager = new Filtermanagement();
        Filtergroup group = new Filtergroup("Testgroup", true, 1);
        try {
            manager.addFilterGroup(group);
            if (!manager.availableFilterGroups.contains(group)) throw new AssertionError();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insertFilterToTable() throws Exception {
        GraphDatabase database = new GraphDatabase(null, null);
        Filtermanagement manager = new Filtermanagement();
        Filter filter = new BasicFilter("Testfilter", true, 1, Relation.GREATHEROREQUAL, null, 2);
        try {
            manager.addFilter(filter);
            if (!manager.availableFilter.contains(filter)) throw new AssertionError();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insertFilterToGroupTable() throws Exception {
        GraphDatabase database = new GraphDatabase(null, null);
        Filtermanagement manager = new Filtermanagement();
        Filter filter = new BasicFilter("Testfilter", true, 1, Relation.EQUAL, null, 3);
        Filtergroup group = new Filtergroup("Testgroup", true, 4);
        try {
            manager.addFilterGroup(group);
            manager.addFilterToFiltergroup(filter, 4);
            if (!manager.availableFilterGroups.contains(group)) throw new AssertionError();
            if (!group.availableFilter.contains(filter)) throw new AssertionError();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void removeFiltersegmentTest() throws Exception {
        GraphDatabase database = new GraphDatabase(null, null);
        Filtermanagement manager = new Filtermanagement();
        Filter standartFilter = new BasicFilter("Testfilter", true, 1, Relation.LESSOREQUAL, null,5);
        Filtergroup group = new Filtergroup("Testgroup", true, 6);
        Filter groupFilter = new BasicFilter("Testfilter", true, 1, Relation.LESSTHAN, null, 7);
        try {
            manager.addFilterGroup(group);
            manager.addFilter(standartFilter);
            manager.addFilterToFiltergroup(groupFilter, 6);
            manager.removeFiltersegment(5);
            manager.removeFiltersegment(7);
            manager.removeFiltersegment(6);
            if (group.availableFilter.size() != 0) throw new AssertionError();
            if (manager.availableFilterGroups.size() != 0) throw new AssertionError();
            if (manager.availableFilter.size() != 0) throw new AssertionError();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void activationTest() throws Exception {
        GraphDatabase database = new GraphDatabase(null, null);
        Filtermanagement manager = new Filtermanagement();
        Filter standartFilter = new BasicFilter("Testfilter", false, 1, Relation.LESSOREQUAL, null, 1);
        Filtergroup group = new Filtergroup("Testgroup", false, 2);
        Filter groupFilter = new BasicFilter("Testfilter", false, 1, Relation.EQUAL, null, 3);
        try {
            manager.addFilterGroup(group);
            manager.addFilter(standartFilter);
            manager.addFilterToFiltergroup(groupFilter, 2);
            manager.activate(2);
            manager.activate(1);
            manager.activate(3);
            if (!groupFilter.isActivated) throw new AssertionError();
            if (!standartFilter.isActivated) throw new AssertionError();
            if (!group.isActivated) throw new AssertionError();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deactivationTest() throws Exception {
        GraphDatabase database = new GraphDatabase(null, null);
        Filtermanagement manager = new Filtermanagement();
        Filter standartFilter = new BasicFilter("Testfilter", true, 1, Relation.EQUAL, null, 1);
        Filtergroup group = new Filtergroup("Testgroup", true, 2);
        Filter groupFilter = new BasicFilter("Testfilter", true, 1, Relation.LESSTHAN, null, 3);
        try {
            manager.addFilterGroup(group);
            manager.addFilter(standartFilter);
            manager.addFilterToFiltergroup(groupFilter, 2);
            manager.deactivate(2);
            manager.deactivate(1);
            manager.deactivate(3);
            if (groupFilter.isActivated) throw new AssertionError();
            if (standartFilter.isActivated) throw new AssertionError();
            if (group.isActivated) throw new AssertionError();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDBParserBasicFilter() throws Exception {
        GraphDatabase database = new GraphDatabase(null, null);
        Filtermanagement manager = new Filtermanagement();
        Filtergroup activeGroup = new Filtergroup("Test", true, 1);
        Filtergroup inactiveGroup = new Filtergroup("InactiveFilter", false, 2);
        Property myProperty = new GreatestDegree();
        Filter activeFilter = new BasicFilter("Filter", true, 1, Relation.EQUAL, myProperty, 3);
        Filter inactiveFilter = new BasicFilter("InactiveFilter", false, 1, Relation.EQUAL, myProperty, 4);
        Filter activeFilterInactiveGroup = new BasicFilter("ActiveFilterOfInactiveGroup", true, 5, Relation.EQUAL, myProperty, 5);
        Filter activeFilterActiveGroup = new BasicFilter("ActiveFilterInactiveGroup", true, 6, Relation.LESSTHAN, myProperty, 6);
        manager.addFilterGroup(activeGroup);
        manager.addFilterGroup(inactiveGroup);
        manager.addFilter(activeFilter);
        manager.addFilter(inactiveFilter);
        manager.addFilterToFiltergroup(activeFilterInactiveGroup, 2);
        manager.addFilterToFiltergroup(activeFilterActiveGroup, 1);
        String[][] resultString = manager.parseFilterList();

        assert (resultString[0][0].equals(myProperty.toString()));
        assert (resultString[0][1].equals("+"));
        assert (resultString[0][2].equals("0"));
        assert (resultString[0][3].equals("="));
        assert (resultString[0][4].equals("nothing"));
        assert (resultString[0][5].equals("+"));
        assert (resultString[0][6].equals("1"));

        assert (resultString[1][0].equals(myProperty.toString()));
        assert (resultString[1][1].equals("+"));
        assert (resultString[1][2].equals("0"));
        assert (resultString[1][3].equals("<"));
        assert (resultString[1][4].equals("nothing"));
        assert (resultString[1][5].equals("+"));
        assert (resultString[1][6].equals("6"));
    }

    @Test
    public void testDBParserConnectedFilter() throws Exception {
        GraphDatabase database = new GraphDatabase(null, null);
        Filtermanagement manager = new Filtermanagement();
        Filtergroup activeGroup = new Filtergroup("Test", true, 1);
        Filtergroup inactiveGroup = new Filtergroup("InactiveFilter", false, 2);
        Property myProperty = new GreatestDegree();
        Filter activeFilter = new ConnectedFilter("First", true, myProperty, myProperty, Operator.MULT, Operator.DIV, 1, 2, Relation.LESSOREQUAL,3);
        Filter inactiveFilter = new ConnectedFilter("Second", false, myProperty, myProperty, Operator.MULT, Operator.DIV, 1, 2, Relation.LESSOREQUAL,4);
        Filter activeFilterInactiveGroup = new ConnectedFilter("Third", true, myProperty, myProperty, Operator.MULT, Operator.SUB, 1, 2, Relation.GREATHEROREQUAL,5);
        Filter activeFilterActiveGroup = new ConnectedFilter("Third", true, myProperty, myProperty, Operator.MULT, Operator.SUB, 1, 2, Relation.GREATHEROREQUAL,6);
        manager.addFilterGroup(activeGroup);
        manager.addFilterGroup(inactiveGroup);
        manager.addFilter(activeFilter);
        manager.addFilter(inactiveFilter);
        manager.addFilterToFiltergroup(activeFilterInactiveGroup, 2);
        manager.addFilterToFiltergroup(activeFilterActiveGroup, 1);
        String[][] resultString = manager.parseFilterList();

        assert (resultString[0][0].equals(myProperty.toString()));
        assert (resultString[0][1].equals("*"));
        assert (resultString[0][2].equals("1"));
        assert (resultString[0][3].equals("<="));
        assert (resultString[0][4].equals(myProperty.toString()));
        assert (resultString[0][5].equals("/"));
        assert (resultString[0][6].equals("2"));

        assert (resultString[1][0].equals(myProperty.toString()));
        assert (resultString[1][1].equals("*"));
        assert (resultString[1][2].equals("1"));
        assert (resultString[1][3].equals(">="));
        assert (resultString[1][4].equals(myProperty.toString()));
        assert (resultString[1][5].equals("-"));
        assert (resultString[1][6].equals("2"));
    }

    @Test
    public void testRemoveCapitalLetters() {
        GraphDatabase database = new GraphDatabase(null, null);
        Filtermanagement manager = new Filtermanagement();
        String testString = "thisISaTeSTstRing#42";
        String resultString = manager.removeCapitalLetters(testString);
        assert resultString.equals("thisisateststring#42");
    }

    @Test
    public void testPropertyParserInCheckFilterInput() throws Exception, InvalidInputException {
        Property testProperty = Filtermanagement.testProperty("numberoftotalcolorings");
        assert testProperty.getClass() == NumberOfTotalColorings.class;
    }

    @Test
    public void testConnectedFilterInputParser() throws InvalidInputException, Exception {
        Filtermanagement manager = new Filtermanagement();
        manager.addFilter("NumberOfCliques + 23 = NumberOfCliques - 33", 1);
        Filter connectedFilter = manager.availableFilter.get(0);
        assert connectedFilter.name.equals("NumberOfCliques + 23 = NumberOfCliques - 33");
        assert connectedFilter.property1.getClass().equals(edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.NumberOfCliques.class);
        assert connectedFilter.operator1.equals(Operator.ADD);
        assert connectedFilter.value1 == 23;
        assert connectedFilter.relation.equals(Relation.EQUAL);
        assert connectedFilter.property2.getClass().equals(edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.NumberOfCliques.class);
        assert connectedFilter.operator2.equals(Operator.SUB);
        assert connectedFilter.value2 == 33;
        assert connectedFilter.id == 1;
    }

    @Test
    public void testBasicFilterIputParser() throws InvalidInputException, Exception {
        Filtermanagement manager = new Filtermanagement();
        manager.addFilter("NumberOfEdges < 69", 2);
        Filter basicFilter = manager.availableFilter.get(0);
        assert basicFilter.name.equals("NumberOfEdges < 69");
        assert basicFilter.property1.getClass().equals(edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.NumberOfEdges.class);
        assert basicFilter.relation.equals(Relation.LESSTHAN);
        assert basicFilter.value1 == 69;
    }
}
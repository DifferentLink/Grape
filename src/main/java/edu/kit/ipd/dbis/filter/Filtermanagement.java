package edu.kit.ipd.dbis.filter;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.*;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.Profile;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_.AverageDegree;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_.ProportionDensity;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_.StructureDensity;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * class which communicates with other packages of Grpape
 */
public class Filtermanagement {

    private List<Filtergroup> availableFilterGroups;
    private List<Filter> availableFilter;
    private GraphDatabase database;

    /**
     * Constructor of class Filtermanagement. The constructor creates two empty lists: One list for the
     * filter and another list for the filtergroups.
     */
    public Filtermanagement() {
        availableFilterGroups = new ArrayList<>();
        availableFilter = new ArrayList<>();
    }

    private void addFilterGroup(Filtergroup filtergroup) throws TablesNotAsExpectedException, ConnectionFailedException,
            InsertionFailedException, AccessDeniedForUserException, UnexpectedObjectException,
            DatabaseDoesNotExistException {
        database.addFilter(filtergroup);
        availableFilterGroups.add(filtergroup);
    }

    private void addFilter(Filter filter) throws TablesNotAsExpectedException, ConnectionFailedException,
            InsertionFailedException, AccessDeniedForUserException, UnexpectedObjectException,
            DatabaseDoesNotExistException {
        database.addFilter(filter);
        availableFilter.add(filter);
    }

    private void addFilterToFiltergroup(Filter filter, int groupID) throws TablesNotAsExpectedException,
            ConnectionFailedException, InsertionFailedException, AccessDeniedForUserException,
            UnexpectedObjectException, DatabaseDoesNotExistException {
        for (Filtergroup element: availableFilterGroups) {
            if (element.id == groupID) {
                element.availableFilter.add(filter);
                database.replaceFilter(groupID, element);
            }
        }
    }

    private int removeFiltersegmentAngGetID(int id) throws DatabaseDoesNotExistException, AccessDeniedForUserException,
            ConnectionFailedException, TablesNotAsExpectedException, UnexpectedObjectException,
            InsertionFailedException {
        for (Filtersegment element : availableFilter) {
            if (element.id == id) {
                availableFilter.remove(element);
                database.deleteFilter(id);
                return 0;
            }
        }
        for (Filtergroup element : availableFilterGroups) {
            if (element.id == id) {
                availableFilterGroups.remove(element);
                database.deleteFilter(id);
                return 0;
            }
            for (Filter filterInGroup : element.availableFilter) {
                if (filterInGroup.id == id) {
                    element.availableFilter.remove(filterInGroup);
                    database.replaceFilter(element.id, element);
                    return element.id;
                }
            }
        }
        return 0;
    }

    /**
     * removes a filtersegment out of the list of class Filtermanagement
     * @param id unique identifier of the filtersegment which should be removed
     * @throws DatabaseDoesNotExistException thrown if there is no database
     * @throws AccessDeniedForUserException thrown if there is no access to the database
     * @throws ConnectionFailedException thrown if the connection to database failed
     * @throws TablesNotAsExpectedException thrown if the tables in database are bad
     * @throws UnexpectedObjectException thrown if the database gets in conflict with unknown objects
     * @throws InsertionFailedException thrown if the database operation failed
     */
    public void removeFiltersegment(int id) throws DatabaseDoesNotExistException, AccessDeniedForUserException,
            ConnectionFailedException, TablesNotAsExpectedException, UnexpectedObjectException,
            InsertionFailedException {
        for (Filtersegment element: availableFilter) {
            if (element.id == id) {
                availableFilter.remove(element);
                database.deleteFilter(id);
                return;
            }
        }
        for (Filtergroup element: availableFilterGroups) {
            if (element.id == id) {
                availableFilterGroups.remove(element);
                database.deleteFilter(id);
                return;
            }
            for (Filter filterInGroup: element.availableFilter) {
                if (filterInGroup.id == id) {
                    element.availableFilter.remove(filterInGroup);
                    database.replaceFilter(element.id, element);
                    return;
                }
            }
        }
    }

    /**
     * enables a filtersegment which means that the criteria of the fitersegment are now
     * used to filter graphs
     * @param id unique identifier of the filtersegment which should be enabled
     * @throws DatabaseDoesNotExistException thrown if there is no database
     * @throws AccessDeniedForUserException thrown if there is no access to the database
     * @throws ConnectionFailedException thrown if the connection to database failed
     * @throws TablesNotAsExpectedException thrown if the tables in database are bad
     * @throws UnexpectedObjectException thrown if the database gets in conflict with unknown objects
     * @throws InsertionFailedException thrown if the database operation failed
     */
    public void activate(int id) throws TablesNotAsExpectedException, ConnectionFailedException,
            InsertionFailedException, AccessDeniedForUserException, UnexpectedObjectException,
            DatabaseDoesNotExistException {
        for (Filtersegment element: availableFilter) {
            if (element.id == id) {
                element.activate();
                database.replaceFilter(id, element);
                return;
            }
        }
        for (Filtergroup element: availableFilterGroups) {
            if (element.id == id) {
                element.activate();
                database.replaceFilter(id, element);
                return;
            }
            for (Filter currentFilter: element.availableFilter) {
                if (currentFilter.id == id) {
                    currentFilter.activate();
                    database.replaceFilter(element.id, element);
                    return;
                }
            }
        }
    }

    /**
     * disables a filtersegment which means that the criteria of the fitersegment are now
     * used to filter graphs
     * @param id unique identifier of the filtersegment which should be enabled
     * @throws DatabaseDoesNotExistException thrown if there is no database
     * @throws AccessDeniedForUserException thrown if there is no access to the database
     * @throws ConnectionFailedException thrown if the connection to database failed
     * @throws TablesNotAsExpectedException thrown if the tables in database are bad
     * @throws UnexpectedObjectException thrown if the database gets in conflict with unknown objects
     * @throws InsertionFailedException thrown if the database operation failed
     */
    public void deactivate(int id) throws TablesNotAsExpectedException, ConnectionFailedException,
            InsertionFailedException, AccessDeniedForUserException, UnexpectedObjectException,
            DatabaseDoesNotExistException {
        for (Filter element: availableFilter) {
            if (element.id == id) {
                element.deactivate();
                database.replaceFilter(id, element);
                return;
            }
        }
        for (Filtergroup currentElement: availableFilterGroups) {
            if (currentElement.id == id) {
                currentElement.deactivate();
                database.replaceFilter(id, currentElement);
                return;
            }
            for (Filter currentFilter : currentElement.availableFilter) {
                if (currentFilter.id == id) {
                    currentFilter.deactivate();
                    database.replaceFilter(currentElement.id, currentElement);
                    return;
                }
            }
        }

    }

    /**
     * method which offers the oppotunity to modify a specific filter
     * @param input code of the modified filter
     * @param id id of the filter to modify
     * @throws TablesNotAsExpectedException thrown if the table in database is not as expected
     * @throws ConnectionFailedException thrown if the connection to database failed
     * @throws InsertionFailedException thrown if filter could not be added to database
     * @throws AccessDeniedForUserException thrown if there is no access to database
     * @throws UnexpectedObjectException thrown if there is an unknown object
     * @throws DatabaseDoesNotExistException thrown if there is no database
     * @throws InvalidInputException thrown if no valid filter is coded in iput string
     */
    public void updateFilter(String input, int id) throws TablesNotAsExpectedException, ConnectionFailedException,
            InsertionFailedException, AccessDeniedForUserException, UnexpectedObjectException,
            DatabaseDoesNotExistException, InvalidInputException {
        int groupID = this.removeFiltersegmentAngGetID(id);
        if (groupID != 0) {
            this.addFilterToGroup(input, id, groupID);
        } else {
            this.addFilter(input, id);
        }
    }

    /**
     * method which offers the opportunity to modify a specific filtergroup
     * @param input new name of the filtergroup
     * @param id id of the filtergroup which should be modified
     */
    public void updateFiltergroup(String input, int id) {
        for (Filtergroup element: availableFilterGroups) {
            if (element.id == id) {
                element.name = input;
            }
        }
    }

    /**
     * method which returns all graphs sorted by a specific property ascending
     * @param property property to sort after
     * @return returns all graphs sorted by a specific property
     * @throws DatabaseDoesNotExistException thrown if there is no database
     * @throws AccessDeniedForUserException thrown if there is no access to database
     * @throws ConnectionFailedException thrown if the connection to database failed
     * @throws TablesNotAsExpectedException thrown if the table in database is not as expected
     */
    public LinkedList<PropertyGraph> getFilteredAndAscendingSortedGraphs(Property property) throws
            DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException,
            TablesNotAsExpectedException {
        return database.getGraphs(this.parseFilterList(), property.toString(), true);
    }

    /**
     * method which returns all graphs sorted by a specific property descending
     * @param property property to sort after
     * @return returns all graphs sorted by a specific property
     * @throws DatabaseDoesNotExistException thrown if there is no database
     * @throws AccessDeniedForUserException thrown if there is no access to database
     * @throws ConnectionFailedException thrown if the connection to database failed
     * @throws TablesNotAsExpectedException thrown if the table in database is not as expected
     */
    public LinkedList<PropertyGraph> getFilteredAndDescendingSortedGraphs(Property property) throws
            DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException,
            TablesNotAsExpectedException {
        return database.getGraphs(this.parseFilterList(), property.toString(), false);
    }

    /**
     * method which returns all graphs sorted by a specific property
     * @return returns all graphs sorted by a specific property
     * @throws DatabaseDoesNotExistException thrown if there is no database
     * @throws AccessDeniedForUserException thrown if there is no access to database
     * @throws ConnectionFailedException thrown if the connection to database failed
     * @throws TablesNotAsExpectedException thrown if the table in database is not as expected
     */
    public LinkedList<PropertyGraph> getFilteredAndSortedGraphs() throws DatabaseDoesNotExistException,
            AccessDeniedForUserException, ConnectionFailedException, TablesNotAsExpectedException {
        return database.getGraphs(this.parseFilterList(), "id", true);
    }

    /**
     * checks whether the input string codes a valid filter. In case of success the method
     * addFiltersegment(filtersegment: Filtersegment): void is called and a new
     * filter is added to the list of class Filtermanagement
     * @param input string which might code a filter
     * @param id unique identifier of the new filterobject
     * @param groupID unique identifier of the filtergroup on which the filter should be add to
     * @throws InvalidInputException this exception is thrown if the input string does not code a valid filter
     * @throws TablesNotAsExpectedException thrown if the table in database is not as expected
     * @throws ConnectionFailedException thrown if the connection to database failed
     * @throws InsertionFailedException thrown if filter could not be added to database
     * @throws AccessDeniedForUserException thrown if there is no access to database
     * @throws UnexpectedObjectException thrown if there is an unknown object
     * @throws DatabaseDoesNotExistException thrown if there is no database
     */
    public void addFilterToGroup(String input, int id, int groupID) throws InvalidInputException,
            TablesNotAsExpectedException, ConnectionFailedException, InsertionFailedException,
            AccessDeniedForUserException, UnexpectedObjectException, DatabaseDoesNotExistException {
        this.addFilterToFiltergroup(Filtermanagement.parseToFilter(input, id), groupID);
    }

    /**
     * checks whether the input string codes a valid filter. In case of success the method
     * addFiltersegment(filtersegment: Filtersegment): void is called and a new
     * filter is added to the list of class Filtermanagement
     * @param input string which might code a filter
     * @param id unique identifier of the new filterobject
     * @throws InvalidInputException this exception is thrown if the input string does not code a valid filter
     * @throws TablesNotAsExpectedException thrown if the table in database is not as expected
     * @throws ConnectionFailedException thrown if the connection to database failed
     * @throws InsertionFailedException thrown if filter could not be added to database
     * @throws AccessDeniedForUserException thrown if there is no access to database
     * @throws UnexpectedObjectException thrown if there is an unknown object
     * @throws DatabaseDoesNotExistException thrown if there is no database
     */
    public void addFilter(String input, int id) throws InvalidInputException, TablesNotAsExpectedException,
            ConnectionFailedException, InsertionFailedException, AccessDeniedForUserException,
            UnexpectedObjectException, DatabaseDoesNotExistException {
        this.addFilter(Filtermanagement.parseToFilter(input, id));
    }

    private static Filter parseToFilter(String input, int id) throws InvalidInputException {
        int i = 0;
        String property1String = "";
        while (input.charAt(i) != ' ') {
            property1String = property1String + input.charAt(i);
            i++;
        }
        property1String = property1String.toLowerCase();
        Property property1 = Filtermanagement.testProperty(property1String);

        String firstOperator = "";
        i++;
        while (i < input.length() && input.charAt(i) != ' ') {
            firstOperator = firstOperator + input.charAt(i);
            i++;
        }
        try {
            Operator operator1 = Filtermanagement.testOperator(firstOperator);

            String firstValueString = "";
            i++;
            while (i < input.length() && input.charAt(i) != ' ') {
                firstValueString = firstValueString + input.charAt(i);
                i++;
            }
            int firstValue = Integer.parseInt(firstValueString);

            String relationString = "";
            i++;
            while (i < input.length() && input.charAt(i) != ' ') {
                relationString = relationString + input.charAt(i);
                i++;
            }
            Relation relation = Filtermanagement.testRelation(relationString);

            String property2String = "";
            i++;
            while (i < input.length() && input.charAt(i) != ' ') {
                property2String = property2String + input.charAt(i);
                i++;
            }
            property2String = property2String.toLowerCase();
            Property property2 = Filtermanagement.testProperty(property2String);

            String operator2String = "";
            i++;
            while (i < input.length() && input.charAt(i) != ' ') {
                operator2String = operator2String + input.charAt(i);
                i++;
            }
            Operator operator2 = Filtermanagement.testOperator(operator2String);

            String secondValueString = "";
            i++;
            while (i < input.length() && input.charAt(i) != ' ') {
                secondValueString = secondValueString + input.charAt(i);
                i++;
            }
            int secondValue = Integer.parseInt(secondValueString);

            return new ConnectedFilter(input, true, property1, property2, operator1,
                    operator2, firstValue, secondValue, relation, id);
        } catch (InvalidInputException e) {
            Relation basicRelation = Filtermanagement.testRelation(firstOperator);

            String valueString = "";
            i++;
            while (i < input.length() && input.charAt(i) != ' ') {
                valueString = valueString + input.charAt(i);
                i++;
            }
            int value = Integer.parseInt(valueString);
            return new BasicFilter(input, true, value, basicRelation, property1, id);
        }
    }

    private static Operator testOperator(String input) throws  InvalidInputException {
        Operator operator;
        switch (input) {
            case "+": operator = Operator.ADD; return operator;
            case "-": operator = Operator.SUB; return operator;
            case "/": operator = Operator.DIV; return operator;
            case "*": operator = Operator.MULT; return operator;
            default: throw new InvalidInputException();
        }
    }

    private static Relation testRelation(String input) throws InvalidInputException {
        Relation relation;
        switch (input) {
            case "=": relation = Relation.EQUAL; return relation;
            case "<": relation = Relation.LESSTHAN; return relation;
            case ">": relation = Relation.GREATHERTHAN; return relation;
            case "<=": relation = Relation.LESSOREQUAL; return relation;
            case ">=": relation = Relation.GREATHEROREQUAL; return relation;
            default: throw new InvalidInputException();
        }
    }

    private static Property testProperty(String input) throws InvalidInputException {
		PropertyGraph graph = new PropertyGraph();
        Property property;
        switch (input) {
			case "profile":
				property = new Profile(graph);
				return property;
			case "averagedegree":
				property = new AverageDegree(graph);
				return property;
			case "proportiondensity":
				property = new ProportionDensity(graph);
				return property;
			case "structuredensity":
				property = new StructureDensity(graph);
				return property;
			case "greatestDegree":
				property = new GreatestDegree(graph);
				return property;
			case "kkgraphnumberofsubgraphs":
				property = new KkGraphNumberOfSubgraphs(graph);
				return property;
			case "numberofcliques":
				property = new NumberOfCliques(graph);
				return property;
			case "numberofedges":
				property = new NumberOfEdges(graph);
				return property;
			case "numberoftotalcolorings":
				property = new NumberOfTotalColorings(graph);
				return property;
			case "numberofvertexcolorings":
				property = new NumberOfVertexColorings(graph);
				return property;
			case "numberofvertices":
				property = new NumberOfVertices(graph);
				return property;
			case "smallestdegree":
				property = new SmallestDegree(graph);
				return property;
            default: throw new InvalidInputException();
        }
    }

    /**
     * checks whether the input string codes a valid Filter. In case of success the method
     * addFiltersegment(filtersegment: Filtersegment): void is called and a new
     * Filter is added to the list of class Filtermanagement
     * @param input string which might code a Filter
     * @param id unique identifier of the new Filterobject
     * @throws TablesNotAsExpectedException thrown if the table in database is not as expected
     * @throws ConnectionFailedException thrown if the connection to database failed
     * @throws InsertionFailedException thrown if filter could not be added to database
     * @throws AccessDeniedForUserException thrown if there is no access to database
     * @throws UnexpectedObjectException thrown if there is an unknown object
     * @throws DatabaseDoesNotExistException thrown if there is no database
     */
    public void addFiltergroup(String input, int id) throws TablesNotAsExpectedException, ConnectionFailedException,
            InsertionFailedException, AccessDeniedForUserException, UnexpectedObjectException,
            DatabaseDoesNotExistException {
        Filtergroup myFiltergroup = new Filtergroup(input, true, id);
        this.addFilterGroup(myFiltergroup);
    }

    /**
     * used when initializing Grape or switching a database. The methode clears the current
     * list of Filtersegments and calls the methode addFiltersegment(filtersegment:
     * Filtersegment): void for every Filter element of the new database
     * @param database new database which should be used in future
     * @throws DatabaseDoesNotExistException thrown if there is no database
     * @throws AccessDeniedForUserException thrown if there is no access to database
     * @throws ConnectionFailedException thrown if the connection to database failed
     * @throws TablesNotAsExpectedException thrown if the table in database is not as expected
     */
    public void setDatabase(GraphDatabase database) throws DatabaseDoesNotExistException,
            AccessDeniedForUserException, ConnectionFailedException, TablesNotAsExpectedException {
        availableFilterGroups.clear();
        availableFilter.clear();
        this.database = database;
        LinkedList<Filtersegment> activatedFilter = database.getFilters();
        for (Filtersegment element: activatedFilter) {
            if (element.getClass() == Filtergroup.class) {
                if (element.getClass() == Filtergroup.class) {
                    availableFilterGroups.add((Filtergroup) element);
                } else {
                    availableFilter.add((Filter) element);
                }
            }
        }
    }

    /**
     * parses all available filter to a string array. The method should only be used by database
     * @return returns a two-dimensional string array which codes all filter which are currently activ
     */
    public String[][] parseFilterList() {
        int arrayLenght = availableFilter.size() + availableFilterGroups.size();
        String[][] stringArray = new String[arrayLenght][7];
        int currentColumn = 0;
        for (Filter element: availableFilter) {
            stringArray = Filtermanagement.fillColumn(stringArray, currentColumn, element);
            currentColumn++;
        }
        for (Filtergroup element: availableFilterGroups) {
            if (element.isActivated) {
                for (Filter groupElement : element.availableFilter) {
                    stringArray = Filtermanagement.fillColumn(stringArray, currentColumn, groupElement);
                    currentColumn++;
                }
            }
        }
        return stringArray;
    }

    private static String[][] fillColumn(String[][] stringArray, int currentColumn, Filter element) {
        if (element.getClass() == BasicFilter.class && element.isActivated) {
            stringArray[currentColumn][0] = String.valueOf(element.getProperty1());
            stringArray[currentColumn][1] = "+";
            stringArray[currentColumn][2] = "0";
            stringArray[currentColumn][3] = Filtermanagement.transformRelationToString(element);
            stringArray[currentColumn][4] = "nothing";
            stringArray[currentColumn][5] = "+";
            stringArray[currentColumn][6] = String.valueOf(element.getValue1());
        } else if (element.getClass() == ConnectedFilter.class && element.isActivated) {
            stringArray[currentColumn][0] = String.valueOf(element.getProperty1());
            stringArray[currentColumn][1] = Filtermanagement.transformFirstOperatorToString(element);
            stringArray[currentColumn][2] = String.valueOf(element.getValue1());
            stringArray[currentColumn][3] = Filtermanagement.transformRelationToString(element);
            stringArray[currentColumn][4] = String.valueOf(element.getProperty2());
            stringArray[currentColumn][5] = Filtermanagement.transformSecondOperatorToString(element);
            stringArray[currentColumn][6] = String.valueOf(element.getValue2());
        }
        return  stringArray;
    }

    private static String transformFirstOperatorToString(Filter filter) {
        String returnString = String.valueOf(filter.getOperator1());
        if (String.valueOf(filter.getOperator1()).equals("ADD")) {
            returnString = "+";
        } else if (String.valueOf(filter.getOperator1()).equals("SUB")) {
            returnString = "-";
        } else if (String.valueOf(filter.getOperator1()).equals("MULT")) {
            returnString = "*";
        } else if (String.valueOf(filter.getOperator1()).equals("DIV")) {
            returnString = "/";
        }
        return returnString;
    }

    private static String transformSecondOperatorToString(Filter filter) {
        String operatorString = String.valueOf(filter.getOperator2());
        String returnString;
        switch (operatorString) {
            case "ADD": returnString = "+"; return returnString;
            case "SUB": returnString = "-"; return returnString;
            case "MULT": returnString = "*"; return returnString;
            default: returnString = "/"; return returnString;
        }
    }

    private static String transformRelationToString(Filter filter) {
        String relationString = String.valueOf(String.valueOf(filter.getRelation()));
        String returnString;
        switch (relationString) {
            case "EQUAL": returnString = "="; return returnString;
            case "GREATHERTHAN": returnString = ">"; return returnString;
            case "LESSTHAN": returnString = "<"; return returnString;
            case "GREATHEROREQUAL": returnString = ">="; return returnString;
            default: returnString = "<="; return returnString;
        }
    }
}

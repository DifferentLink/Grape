package edu.kit.ipd.dbis.filter;

import com.mysql.jdbc.StringUtils;
import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.UnexpectedObjectException;
import edu.kit.ipd.dbis.database.exceptions.sql.InsertionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.filter.exceptions.InvalidInputException;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyFactory;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.sql.ResultSet;
import java.util.*;

/**
 * class which communicates with other packages of Grape
 */
public class Filtermanagement {

    Map<Integer, Filtergroup> availableFilterGroups;
    Map<Integer, Filter> availableFilters;
    private GraphDatabase database;

    /**
     * Constructor of class Filtermanagement. The constructor creates two empty lists: One list for the
     * filter and another list for the filtergroups.
     */
    public Filtermanagement() {
        availableFilterGroups = new HashMap<>();
        availableFilters = new HashMap<>();
    }

    /**
     * getter-method for database
     * @return current database of the filtermanager
     */
    public GraphDatabase getDatabase() {
        return database;
    }

    /**
     * getter-method for filter groups
     * @return returns all filtergroups the current filtermanager inherits
     */
    public Set<Filtergroup> getAvailableFilterGroups() {
        return new HashSet<>(availableFilterGroups.values());
    }

    /**
     * getter-method for filter
     * @return returns all filter the current filtermanager inherits
     */
    public Set<Filter> getAvailableFilters() {
        return new HashSet<>(availableFilters.values());
    }

    private void addFilterGroup(Filtergroup filtergroup) throws ConnectionFailedException,

            UnexpectedObjectException, InsertionFailedException {
        database.addFilter(filtergroup);
        availableFilterGroups.put(filtergroup.id, filtergroup);
    }

    private void addFilter(Filter filter) throws ConnectionFailedException,
            InsertionFailedException, UnexpectedObjectException {
        database.addFilter(filter);
        availableFilters.put(filter.id, filter);
    }

    private void addFilterToFiltergroup(Filter filter, int groupID) throws ConnectionFailedException,
            InsertionFailedException, UnexpectedObjectException {
        Filtergroup group = availableFilterGroups.get(groupID);
        group.addFilter(filter);
        database.replaceFilter(groupID, group);
    }

    private int removeFiltersegmentAndGetID(int id) throws ConnectionFailedException, UnexpectedObjectException,
            InsertionFailedException {
        if (availableFilters.containsKey(id)) {
            availableFilters.remove(availableFilters.get(id));
            database.deleteFilter(id);
            return 0;
        } else if (availableFilterGroups.containsKey(id)) {
            availableFilterGroups.remove(availableFilterGroups.get(id));
            database.deleteFilter(id);
            return 0;
        } else {
            // filter is inside of filtergroup
            Filtergroup f = this.getFilterGroup(id);
            if (f != null) {
                f.removeFilter(id);
                database.replaceFilter(f.id, f);
                return f.id;
            }
        }
        return 0;
    }

    /**
     * removes a filtersegment out of the list of class Filtermanagement
     * @param id unique identifier of the filtersegment which should be removed
     * @throws ConnectionFailedException thrown if the connection to database failed
     * @throws ConnectionFailedException thrown if the tables in database are bad
     * @throws UnexpectedObjectException thrown if the database gets in conflict with unknown objects
     * @throws InsertionFailedException thrown if the database operation failed
     */
    public void removeFiltersegment(int id) throws ConnectionFailedException, UnexpectedObjectException,
            InsertionFailedException {
        if (availableFilters.containsKey(id)) {
            availableFilters.remove(id);
            database.deleteFilter(id);
        } else if (availableFilterGroups.containsKey(id)) {
            availableFilterGroups.remove(id);
            database.deleteFilter(id);
        } else {
            // filter is inside of filtergroup
            Filtergroup f = this.getFilterGroup(id);
            if (f != null) {
                f.removeFilter(id);
                database.replaceFilter(f.id, f);
            }
        }
    }

    /**
     * enables a filtersegment which means that the criteria of the fitersegment are now
     * used to filter graphs
     * @param id unique identifier of the filtersegment which should be enabled
     * @throws ConnectionFailedException thrown if the connection to database failed
     * @throws ConnectionFailedException thrown if the tables in database are bad
     * @throws UnexpectedObjectException thrown if the database gets in conflict with unknown objects
     * @throws InsertionFailedException thrown if the database operation failed
     */
    public void activate(int id) throws InsertionFailedException, UnexpectedObjectException, ConnectionFailedException {
        if (availableFilters.containsKey(id)) {
            Filter f = availableFilters.get(id);
            f.activate();
            database.replaceFilter(f.id, f);
        } else if (availableFilterGroups.containsKey(id)) {
            Filtergroup f = availableFilterGroups.get(id);
            f.activate();
            database.replaceFilter(f.id, f);
        } else {
            // filter is inside of filtergroup
            Filtergroup f = this.getFilterGroup(id);
            if (f != null) {
                f.getFilter(id).activate();
                database.replaceFilter(f.id, f);
            }
        }
    }

    /**
     * disables a filtersegment which means that the criteria of the fitersegment are now
     * used to filter graphs
     * @param id unique identifier of the filtersegment which should be enabled
     * @throws ConnectionFailedException thrown if the connection to database failed
     * @throws ConnectionFailedException thrown if the tables in database are bad
     * @throws UnexpectedObjectException thrown if the database gets in conflict with unknown objects
     * @throws InsertionFailedException thrown if the database operation failed
     */
    public void deactivate(int id) throws ConnectionFailedException,
            InsertionFailedException, UnexpectedObjectException {
        if (availableFilters.containsKey(id)) {
            Filter f = availableFilters.get(id);
            f.deactivate();
            database.replaceFilter(f.id, f);
        } else if (availableFilterGroups.containsKey(id)) {
            Filtergroup f = availableFilterGroups.get(id);
            f.deactivate();
            database.replaceFilter(f.id, f);
        } else {
            // filter is inside of filtergroup
            Filtergroup f = this.getFilterGroup(id);
            if (f != null) {
                f.getFilter(id).deactivate();
                database.replaceFilter(f.id, f);
            }
        }

    }

    /**
     * method which offers the opportunity to modify a specific filter
     * @param input code of the modified filter
     * @param id id of the filter to modify
     * @throws ConnectionFailedException thrown if the table in database is not as expected
     * @throws ConnectionFailedException thrown if the connection to database failed
     * @throws InsertionFailedException thrown if filter could not be added to database
     * @throws UnexpectedObjectException thrown if there is an unknown object
     * @throws InvalidInputException thrown if no valid filter is coded in iput string
     */
    public void updateFilter(String input, int id) throws ConnectionFailedException,
            InsertionFailedException, UnexpectedObjectException, InvalidInputException {
        boolean activated = false;
        if (availableFilters.containsKey(id)) {
            activated = availableFilters.get(id).isActivated;
        } else {
            Filtergroup f = this.getFilterGroup(id);
            if (f != null) {
                activated = f.getFilter(id).isActivated;
            }
        }
        int groupID = this.removeFiltersegmentAndGetID(id);
        if (groupID != 0) {
            this.removeFiltersegment(id);
            this.updateFilter(input, id, groupID);
        } else {
            this.removeFiltersegment(id);
            this.addFilter(input, id);
        }

        if (activated && availableFilters.containsKey(id)) {
            availableFilters.get(id).activate();
        } else if (activated) {
            Filtergroup f = this.getFilterGroup(id);
            if (f != null) {
                f.getFilter(id).activate();
            }
        }
    }

    /**
     * checks whether the input string codes a valid filter. In case of success the method
     * addFiltersegment(filtersegment: Filtersegment): void is called and a new
     * filter is added to the list of class Filtermanagement
     * @param input string which might code a filter
     * @param id unique identifier of the new filterobject
     * @param groupID unique identifier of the filtergroup on which the filter should be add to
     * @throws InvalidInputException this exception is thrown if the input string does not code a valid filter
     * @throws ConnectionFailedException thrown if the table in database is not as expected
     * @throws ConnectionFailedException thrown if the connection to database failed
     * @throws InsertionFailedException thrown if filter could not be added to database
     * @throws UnexpectedObjectException thrown if there is an unknown object
     */
    public void updateFilter(String input, int id, int groupID) throws InvalidInputException,
            ConnectionFailedException, InsertionFailedException, UnexpectedObjectException {
        boolean activated = false;
        if (availableFilterGroups.containsKey(groupID)
                && availableFilterGroups.get(groupID).containsFilter(id)) {
            activated = availableFilterGroups.get(groupID).getFilter(id).isActivated;
        }
        this.removeFiltersegment(id);
        this.addFilterToFiltergroup(Filtermanagement.parseToFilter(input, id), groupID);
        if (activated) {
            availableFilterGroups.get(groupID).getFilter(id).activate();
        }
    }

    /**
     * method which offers the opportunity to modify a specific filtergroup
     * @param input new name of the filtergroup
     * @param id id of the filtergroup which should be modified
     * @throws ConnectionFailedException thrown if the connection to database failed
     * @throws InsertionFailedException thrown if filter could not be added to database
     * @throws UnexpectedObjectException thrown if there is an unknown object
     */
    public void updateFiltergroup(String input, int id) throws ConnectionFailedException, InsertionFailedException,
            UnexpectedObjectException {
        if (availableFilterGroups.containsKey(id)) {
            availableFilterGroups.get(id).name = input;
        } else {
            Filtergroup myGroup = new Filtergroup(input, false, id);
            this.addFilterGroup(myGroup);
        }
    }



    /**
     * method which returns all graphs sorted by a specific property ascending
     * @param property property to sort after
     * @return returns all graphs sorted by a specific property
     * @throws ConnectionFailedException thrown if the connection to database failed
     * @throws ConnectionFailedException thrown if the table in database is not as expected
     */
    public ResultSet getFilteredAndAscendingSortedGraphs(Property property) throws ConnectionFailedException {
        return database.getGraphs(this.parseFilterList(), property.getClass().getSimpleName(), true);
    }

    /**
     * method which returns all graphs sorted by a specific property descending
     * @param property property to sort after
     * @return returns all graphs sorted by a specific property
     * @throws ConnectionFailedException thrown if the connection to database failed
     * @throws ConnectionFailedException thrown if the table in database is not as expected
     */
    public ResultSet getFilteredAndDescendingSortedGraphs(Property property) throws ConnectionFailedException {
        return database.getGraphs(this.parseFilterList(), property.getClass().getSimpleName(), false);
    }

    /**
     * method which returns all graphs sorted by a specific property
     * @return returns all graphs sorted by a specific property
     * @throws ConnectionFailedException thrown if the connection to database failed
     * @throws ConnectionFailedException thrown if the table in database is not as expected
     */
    public ResultSet getFilteredAndSortedGraphs() throws ConnectionFailedException {
        return database.getGraphs(this.parseFilterList(), "id", true);
    }

    /**
     * checks whether the input string codes a valid filter. In case of success the method
     * addFiltersegment(filtersegment: Filtersegment): void is called and a new
     * filter is added to the list of class Filtermanagement
     * @param input string which might code a filter
     * @param id unique identifier of the new filterobject
     * @throws InvalidInputException this exception is thrown if the input string does not code a valid filter
     * @throws ConnectionFailedException thrown if the table in database is not as expected
     * @throws ConnectionFailedException thrown if the connection to database failed
     * @throws InsertionFailedException thrown if filter could not be added to database
     * @throws UnexpectedObjectException thrown if there is an unknown object
     */
    private void addFilter(String input, int id) throws InvalidInputException,
            ConnectionFailedException, InsertionFailedException, UnexpectedObjectException {
        this.addFilter(Filtermanagement.parseToFilter(input, id));
    }

    private static Filter parseToFilter(String input, int id) throws InvalidInputException {
        Filtermanagement.checkFilterInputNull(input);
        String inputCopy = input.toLowerCase();
        String[] parameters = inputCopy.split(" ", 7);
        if (parameters.length == 1) {
            switch (parameters[0]) {
                case "hadwigerconjecture":
                    inputCopy = "kkgraphnumberofsubgraphs + 0 >= vertexcoloringnumberofcolors + 0";
                    parameters = inputCopy.split(" ", 7);
                    break;
                case "totalcoloringconjecture":
                    inputCopy = "totalcoloringnumberofcolors + 0 <= greatestdegree + 2";
                    parameters = inputCopy.split(" ", 7);
                    break;
                default: throw new InvalidInputException();
            }
        }
        if (parameters.length < 3 || parameters.length == 4 || parameters.length == 6) {
            throw new InvalidInputException();
        }

        String property1String = parameters[0];
        checkFilterInputNull(parameters[0]);
        property1String = property1String.toLowerCase();
        String property1 = Filtermanagement.testProperty(property1String);

        if (parameters.length == 7) {
            String firstOperator = parameters[1];
            checkFilterInputNull(parameters[1]);
            Operator operator1 = Filtermanagement.testOperator(firstOperator);

            String firstValueString = parameters[2];
            if (!Filtermanagement.isIntegerOrDouble(firstValueString) || firstValueString.length() == 0) {
                throw new InvalidInputException();
            }
            checkFilterInputNull(parameters[2]);
            double firstValue = Filtermanagement.parseToIntegerOrDouble(firstValueString);

            String relationString = parameters[3];
            checkFilterInputNull(parameters[3]);
            Relation relation = Filtermanagement.testRelation(relationString);

            String property2String = parameters[4];
            checkFilterInputNull(parameters[4]);
            property2String = property2String.toLowerCase();
            String property2 = Filtermanagement.testProperty(property2String);

            String operator2String = parameters[5];
            checkFilterInputNull(parameters[5]);
            Operator operator2 = Filtermanagement.testOperator(operator2String);

            String secondValueString = parameters[6];
            if (!Filtermanagement.isIntegerOrDouble(secondValueString) || secondValueString.length() == 0) {
                throw new InvalidInputException();
            }
            double secondValue = Filtermanagement.parseToIntegerOrDouble(secondValueString);

            if (parameters[5].equals("+") && parameters[6].equals("0")) {
                input = parameters[0] + " "
                        + parameters[1] + " "
                        + parameters[2] + " "
                        + parameters[3] + " "
                        + parameters[4];
                if (parameters[1].equals("+") && parameters[2].equals("0")) {
                    input = parameters[0] + " "
                            + parameters[3] + " "
                            + parameters[4];
                }
            }
            else if (parameters[1].equals("+") && parameters[2].equals("0")) {
                input = parameters[0] + " "
                        + parameters[3] + " "
                        + parameters[4] + " "
                        + parameters[5] + " "
                        + parameters[6];
            }
            return new ConnectedFilter(input, false, property1, property2, operator1,
                    operator2, firstValue, secondValue, relation, id);
        } else if (parameters.length == 3 && Filtermanagement.isIntegerOrDouble(parameters[2])) {
            String relationString = parameters[1];
            checkFilterInputNull(parameters[1]);
            Relation relation = Filtermanagement.testRelation(relationString);

            String valueString = parameters[2];
            checkFilterInputNull(parameters[2]);
            double value = Filtermanagement.parseToIntegerOrDouble(valueString);
            return new BasicFilter(input, false, value, relation, property1, id);
        } else if (parameters.length == 3) {
            return Filtermanagement.parseToFilter(parameters[0] + " + 0 " + parameters[1] + " "
                    + parameters[2] + " + 0", id);
        } else if (parameters.length == 5 && Filtermanagement.isIntegerOrDouble(parameters[2])) {
            return Filtermanagement.parseToFilter(parameters[0] + " " + parameters[1] + " " + parameters[2]
                    + " " + parameters[3] + " " + parameters[4] + " + 0", id);
        } else {
            return Filtermanagement.parseToFilter(parameters[0] + " + 0 " + parameters[1] + " " + parameters[2]
                    + " " + parameters[3] + " " + parameters[4], id);
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

    private static String testProperty(String input) throws InvalidInputException {
        PropertyGraph<Integer, Integer> graph = new PropertyGraph<>();
        Set<Property> propertySet = PropertyFactory.createNumberProperties(graph);
        String[] propertyStrings = new String[propertySet.size()];
        int i = 0;
        for (Property currentProperty: propertySet) {
            propertyStrings[i] = currentProperty.getClass().getSimpleName();
            i++;
        }
        for (String currentString: propertyStrings) {
            currentString = currentString.toLowerCase();
            if (currentString.equals(input)) {
                return input;
            }
        }
        throw new InvalidInputException();
    }

    /**
     * used when initializing Grape or switching a database. The method clears the current
     * list of Filtersegments and calls the method addFiltersegment(filtersegment:
     * Filtersegment): void for every Filter element of the new database
     * @param database new database which should be used in future
     * @throws ConnectionFailedException thrown if the connection to database failed
     * @throws ConnectionFailedException thrown if the table in database is not as expected
     */
    public void setDatabase(GraphDatabase database) throws ConnectionFailedException {
        availableFilterGroups.clear();
        availableFilters.clear();
        this.database = database;
        LinkedList<Filtersegment> activatedFilter = database.getFilters();
        for (Filtersegment element: activatedFilter) {
            if (element.getClass() == Filtergroup.class) {
                availableFilterGroups.put(element.getID(), (Filtergroup) element);
            } else {
                availableFilters.put(element.getID(), (Filter) element);
            }
        }
    }

    /**
     * parses all available filter to a string array. The method should only be used by database
     * @return returns a two-dimensional string array which codes all filter which are currently activ
     */
    public String[][] parseFilterList() {
        ArrayList<Filter> activatedFilter = new ArrayList<>();
        for (Filter current: availableFilters.values()) {
            if (current.isActivated) {
                activatedFilter.add(current);
            }
        }
        for (Filtergroup current: availableFilterGroups.values()) {
        	for (Filter filter : current.getAvailableFilters()) {
                if (filter.isActivated) {
                    activatedFilter.add(filter);
                }
            }
        }
        int arrayLength = activatedFilter.size();
        String[][] stringArray = new String[arrayLength][7];
        int currentColumn = 0;
        for (Filter element: activatedFilter) {
            stringArray = Filtermanagement.fillColumn(stringArray, currentColumn, element);
            currentColumn++;
        }
        return stringArray;
    }

    private static String[][] fillColumn(String[][] stringArray, int currentColumn, Filter element) {
        if (element.getClass() == BasicFilter.class) {
            stringArray[currentColumn][0] = element.getProperty1();
            stringArray[currentColumn][1] = "+";
            stringArray[currentColumn][2] = "0";
            stringArray[currentColumn][3] = Filtermanagement.transformRelationToString(element);
            stringArray[currentColumn][4] = "0";
            stringArray[currentColumn][5] = "+";
            stringArray[currentColumn][6] = String.valueOf(element.getValue1());
        } else if (element.getClass() == ConnectedFilter.class) {
            stringArray[currentColumn][0] = element.getProperty1();
            stringArray[currentColumn][1] = Filtermanagement.transformFirstOperatorToString(element);
            stringArray[currentColumn][2] = String.valueOf(element.getValue1());
            stringArray[currentColumn][3] = Filtermanagement.transformRelationToString(element);
            stringArray[currentColumn][4] = element.getProperty2();
            stringArray[currentColumn][5] = Filtermanagement.transformSecondOperatorToString(element);
            stringArray[currentColumn][6] = String.valueOf(element.getValue2());
        }
        return  stringArray;
    }

    private static String transformFirstOperatorToString(Filter filter) {
        String operatorString = String.valueOf(filter.getOperator1());
        return Filtermanagement.transformOperatorToString(operatorString);
    }

    private static String transformSecondOperatorToString(Filter filter) {
        String operatorString = String.valueOf(filter.getOperator2());
        return Filtermanagement.transformOperatorToString(operatorString);
    }

    private static String transformOperatorToString(String operatorString) {
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

    private static void checkFilterInputNull(String input) throws InvalidInputException {
        if (input == null) {
            throw new InvalidInputException();
        }
    }

    private static boolean isIntegerOrDouble(String input) throws InvalidInputException {
        Filtermanagement.checkFilterInputNull(input);
        if (StringUtils.isStrictlyNumeric(input)) {
            return true;
        } else {
            String[] parameters = input.split("\\.", 2);
            return (StringUtils.isStrictlyNumeric(parameters[0]) && StringUtils.isStrictlyNumeric(parameters[1]));
        }
    }

    private static double parseToIntegerOrDouble(String input) {
        if (StringUtils.isStrictlyNumeric(input)) {
            return (double) Integer.parseInt(input);
        } else {
            return Double.parseDouble(input);
        }
    }

    private Filtergroup getFilterGroup(int filterId) {
        for (Filtergroup f : availableFilterGroups.values()) {
            if (f.containsFilter(filterId)) {
                return f;
            }
        }
        return null;
    }
}

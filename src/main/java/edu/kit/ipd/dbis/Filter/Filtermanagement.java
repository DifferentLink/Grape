package edu.kit.ipd.dbis.Filter;

import edu.kit.ipd.dbis.database.GraphDatabase;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.Profile;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_.AverageDegree;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_.ProportionDensity;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_.StructureDensity;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * class which communicates with other packages of Grpape
 */
public class Filtermanagement {

    public List<Filtergroup> availableFilterGroups;
    public List<Filter> availableFilter;
    public GraphDatabase database;

    public Filtermanagement() {
        availableFilterGroups = new ArrayList<>();
        availableFilter = new ArrayList<>();
    }

    /**
     * adds a filtergroup to the list availableFilterGroups of class Filtermanagement
     * @param filtergroup filtersegment which should be added
     */
    void addFilterGroup(Filtergroup filtergroup) throws Exception {
        //database.addFilter(filtergroup);
        availableFilterGroups.add(filtergroup);
    }

    /**
     * adds a filter to the list availableFilter of class Filtermanagement
     * @param filter filtersegment which should be added
     */
    void addFilter(Filter filter) throws Exception {
        //database.addFilter(filter);
        availableFilter.add(filter);
    }

    /**
     * adds a filter to a specific filtergroup
     * @param filter filter which should be added to a specific filtergroup
     * @param groupID ID of the filtergroup
     */
    void addFilterToFiltergroup(Filter filter, int groupID) throws Exception {
        for (Filtergroup element: availableFilterGroups) {
            if (element.id == groupID) {
                element.availableFilter.add(filter);
                //database.repleaceFilter(groupID, element);
            }
        }
    }

    /**
     * removes a filtersegment out of the list of class Filtermanagement
     * @param id unique identifier of the filtersegment which should be removed
     */
    public void removeFiltersegment(int id) throws Exception {
        for (Filtersegment element: availableFilter) {
            if (element.id == id) {
                availableFilter.remove(element);
                //database.deleteFilter(id);
                return;
            }
        }
        for (Filtergroup element: availableFilterGroups) {
            if (element.id == id) {
                availableFilterGroups.remove(element);
                //database.deleteFilter(id);
                return;
            }
            for (Filter filterInGroup: element.availableFilter) {
                if (filterInGroup.id == id) {
                    element.availableFilter.remove(filterInGroup);
                    //database.repleaceFilter(element.id, element);
                    return;
                }
            }
        }
    }

    /**
     * enables a filtersegment which means that the criteria of the fitersegment are now
     * used to filter graphs
     * @param id unique identifier of the filtersegment which should be enabled

     */
    public void activate(int id) throws Exception {
        for (Filtersegment element: availableFilter) {
            if (element.id == id) {
                element.activate();
                //database.repleaceFilter(id, element);
                return;
            }
        }
        for (Filtergroup element: availableFilterGroups) {
            if (element.id == id) {
                element.activate();
                //database.repleaceFilter(id, element);
                return;
            }
            for (Filter currentFilter: element.availableFilter) {
                if (currentFilter.id == id) {
                    currentFilter.activate();
                    //database.repleaceFilter(element.id, element);
                    return;
                }
            }
        }
    }

    /**
     * disables a filtersegment which means that the criteria of the filtersegment are
     * ignored while filtering graphs
     * @param id unique identifier of the filtersegment which should be disabled
     */
    public void deactivate(int id) throws Exception {
            for (Filter element: availableFilter) {
                if (element.id == id) {
                    element.deactivate();
                    //database.repleaceFilter(id, element);
                    return;
                }
            }
            for (Filtergroup currentElement: availableFilterGroups) {
                if (currentElement.id == id) {
                    currentElement.deactivate();
                    //database.repleaceFilter(id, currentElement);
                    return;
                }
                for (Filter currentFilter : currentElement.availableFilter) {
                    if (currentFilter.id == id) {
                        currentFilter.deactivate();
                        //database.repleaceFilter(currentElement.id, currentElement);
                        return;
                    }
                }
            }

    }

    /**
     * method which returns all graphs sorted by a specific property
     * @param property property to sort after
     * @param upwards true if you want to sort graphs upwards
     * @return returns all graphs sorted by a specific property
     * @throws Exception thrown if an exception in database occured
     */
    public Set<PropertyGraph> getGraphs(Property property, boolean upwards) throws Exception {
        return database.getGraphs();
    }

    /**
     * checks whether the input string codes a valid filter. In case of success the method
     * addFiltersegment(filtersegment: Filtersegment): void is called and a new
     * filter is added to the list of class Filtermanagement
     * @param input string which might code a filter
     * @param id unique identifier of the new filterobject
     * @throws InvalidInputException this exception is thrown if the input string
     * does not code a valid filter
     * a filter with same identifier
     */
    public String checkFilterInput(String input, int id) throws InvalidInputException {
        int i = 0;
        String filterName = input;
        String propertyString = "";
        while (input.charAt(i) != ' ') {
            propertyString = propertyString + (char) input.charAt(i);
            i++;
        }
        Property filterProperty = Filtermanagement.testProperty(propertyString);
        return propertyString;
    }

    static Operator testOperator(String input) throws  InvalidInputException {
        Operator operator;
        switch (input) {
            case "+": operator = Operator.ADD; return operator;
            case "-": operator = Operator.SUB; return operator;
            case "/": operator = Operator.DIV; return operator;
            case "*": operator = Operator.MULT; return operator;
            default: throw new InvalidInputException();
        }
    }

    static Relation testRelation(String input) throws InvalidInputException {
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

    static Property testProperty(String input) throws InvalidInputException {
        Property property;
        switch (input) {
            case "profile": property = new Profile(); return property;
            case "averagedegree": property = new AverageDegree(); return property;
            case "proportiondensity": property = new ProportionDensity(); return property;
            case "structuredensity": property = new StructureDensity(); return property;
            case "greatestDegree": property = new GreatestDegree(); return property;
            case "kkgraphnumberofsubgraphs": property = new KkGraphNumberOfSubgraphs(); return property;
            case "numberofcliques": property = new NumberOfCliques(); return property;
            case "numberofedges": property = new NumberOfEdges(); return property;
            case "numberoftotalcolorings": property = new NumberOfTotalColorings(); return property;
            case "numberofvertexcolorings": property = new NumberOfVertexColorings(); return property;
            case "numberofvertices": property = new NumberOfVertices(); return property;
            case "smallestdegree": property = new SmallestDegree(); return property;
            default: throw new InvalidInputException();
        }
    }

    /**
     * checks whether the input string codes a valid Filter. In case of success the method
     * addFiltersegment(filtersegment: Filtersegment): void is called and a new
     * Filter is added to the list of class Filtermanagement
     * @param input string which might code a Filter
     * @param id unique identifier of the new Filterobject
     */
    public void checkGroupInput(String input, int id) {

    }

    static String removeCapitalLetters(String input) {
        int i = 0;
        String output = "";
        while (i < input.length()) {
            if (input.charAt(i) > 64 && input.charAt(i) < 91) {
                output = output + (char) (input.charAt(i) + 32);
            } else {
                output = output + input.charAt(i);
            }
            i++;
        }
        return output;
    }

    /**
     * used when initializing Grape or switching a database. The methode clears the current
     * list of Filtersegments and calls the methode addFiltersegment(filtersegment:
     * Filtersegment): void for every Filter element of the new database
     */
    public void setDatabase(GraphDatabase newDatabase) throws Exception {
        availableFilterGroups.clear();
        availableFilter.clear();
        Set<Filtersegment> activatedFilter = newDatabase.getFilters();
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

    public String[][] parseFilterList() {
        int arrayLenght = availableFilter.size() + availableFilterGroups.size();
        String[][] stringArray = new String[arrayLenght][7];
        int currentColumn = 0;
        for (Filter element: availableFilter) {
            if (element.getClass() == BasicFilter.class && element.isActivated) {
                stringArray[currentColumn][0] = String.valueOf(element.getProperty1());
                stringArray[currentColumn][1] = "+";
                stringArray[currentColumn][2] = "0";
                stringArray[currentColumn][3] = Filtermanagement.transformRelationToString(element);
                stringArray[currentColumn][4] = "nothing";
                stringArray[currentColumn][5] = "+";
                stringArray[currentColumn][6] = String.valueOf(element.getValue1());
                currentColumn++;
            } else if (element.getClass() == ConnectedFilter.class && element.isActivated) {
                stringArray[currentColumn][0] = String.valueOf(element.getProperty1());
                stringArray[currentColumn][1] = Filtermanagement.transformFirstOperatorToString(element);
                stringArray[currentColumn][2] = String.valueOf(element.getValue1());
                stringArray[currentColumn][3] = Filtermanagement.transformRelationToString(element);
                stringArray[currentColumn][4] = String.valueOf(element.getProperty2());
                stringArray[currentColumn][5] = Filtermanagement.transformSecondOperatorToString(element);
                stringArray[currentColumn][6] = String.valueOf(element.getValue2());
                currentColumn++;
            }
        }
        for (Filtergroup element: availableFilterGroups) {
            if (element.isActivated) {
                for (Filter groupElement : element.availableFilter) {
                    if (groupElement.getClass() == BasicFilter.class && groupElement.isActivated) {
                        stringArray[currentColumn][0] = String.valueOf(groupElement.getProperty1());
                        stringArray[currentColumn][1] = "+";
                        stringArray[currentColumn][2] = "0";
                        stringArray[currentColumn][3] = Filtermanagement.transformRelationToString(groupElement);
                        stringArray[currentColumn][4] = "nothing";
                        stringArray[currentColumn][5] = "+";
                        stringArray[currentColumn][6] = String.valueOf(groupElement.getValue1());
                        currentColumn++;
                    } else if (groupElement.getClass() == ConnectedFilter.class && element.isActivated) {
                        stringArray[currentColumn][0] = String.valueOf(groupElement.getProperty1());
                        stringArray[currentColumn][1] = Filtermanagement.transformFirstOperatorToString(groupElement);
                        stringArray[currentColumn][2] = String.valueOf(groupElement.getValue1());
                        stringArray[currentColumn][3] = Filtermanagement.transformRelationToString(groupElement);
                        stringArray[currentColumn][4] = String.valueOf(groupElement.getProperty2());
                        stringArray[currentColumn][5] = Filtermanagement.transformSecondOperatorToString(groupElement);
                        stringArray[currentColumn][6] = String.valueOf(groupElement.getValue2());
                        currentColumn++;
                    }
                }
            }
        }
        return stringArray;
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
        String returnString = String.valueOf(filter.getOperator2());
        if (String.valueOf(filter.getOperator2()).equals("ADD")) {
            returnString = "+";
        } else if (String.valueOf(filter.getOperator2()).equals("SUB")) {
            returnString = "-";
        } else if (String.valueOf(filter.getOperator2()).equals("MULT")) {
            returnString = "*";
        } else if (String.valueOf(filter.getOperator2()).equals("DIV")) {
            returnString = "/";
        }
        return returnString;
    }

    private static String transformRelationToString(Filter filter) {
        String returnString = String.valueOf(filter.getRelation());
        if (String.valueOf(filter.getRelation()).equals("EQUAL")) {
            returnString = "=";
        } else if (String.valueOf(filter.getRelation()).equals("GREATHERTHAN")) {
            returnString = ">";
        } else if (String.valueOf(filter.getRelation()).equals("LESSTHAN")) {
            returnString = "<";
        } else if (String.valueOf(filter.getRelation()).equals("GREATHEROREQUAL")) {
            returnString = ">=";
        } else if (String.valueOf(filter.getRelation()).equals("LESSOREQUAL")) {
            returnString = "<=";
        }
        return returnString;
    }
}
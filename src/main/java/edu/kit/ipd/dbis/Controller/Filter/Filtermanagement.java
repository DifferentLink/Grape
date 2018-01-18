package edu.kit.ipd.dbis.Controller.Filter;

import edu.kit.ipd.dbis.database.GraphDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * class which communicates with other packages of Grpape
 */
public class Filtermanagement {

    private List<Filtergroup> availableFilterGroups;
    private List<Filter> availableFilter;
    private GraphDatabase database;

    public Filtermanagement() {
        availableFilterGroups = new ArrayList<>();
        availableFilter = new ArrayList<>();
    }

    /**
     * adds a filtergroup to the list availableFilterGroups of class Filtermanagement
     * @param filtergroup filtersegment which should be added
     */
    int addFilterGroup(Filtergroup filtergroup) throws Exception {
        database.addFilter(filtergroup);
        availableFilterGroups.add(filtergroup);
        return filtergroup.getID();
    }

    /**
     * adds a filter to the list availableFilter of class Filtermanagement
     * @param filter filtersegment which should be added
     */
    int addFilter(Filter filter) throws Exception {
        database.addFilter(filter);
        availableFilter.add(filter);
        return filter.getID();
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
                database.repleaceFilter(groupID, element);
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
            element.removeFilter(id);
            database.repleaceFilter(id, element);
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
                database.repleaceFilter(id, element);
                return;
            }
        }
        for (Filtergroup element: availableFilterGroups) {
            if (element.id == id) {
                element.activate();
                database.repleaceFilter(id, element);
                return;
            }
            for (Filter currentFilter: element.availableFilter) {
                if (currentFilter.id == id) {
                    currentFilter.activate();
                    int groupID = element.id;
                    database.repleaceFilter(groupID, element);
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
                    database.repleaceFilter(id, element);
                    return;
                }
            }
            for (Filtergroup currentElement: availableFilterGroups) {
                if (currentElement.id == id) {
                    currentElement.deactivate();
                    database.repleaceFilter(id, currentElement);
                    return;
                }
                for (Filter currentFilter : currentElement.availableFilter) {
                    if (currentFilter.id == id) {
                        currentFilter.deactivate();
                        //Aktualisieren in der Datenbank
                        return;
                    }
                }
            }

    }

    /**
     * allows to prevent a NullPointerException by asking if there is a graph to return
     * @return returns true if there is a graph which meets the current filter criteria
     * but was not returned yet
     */
    public boolean hasNextValidGraph() {
        return false;
    }

    /**
     * allows to get only graphs from database which meet the current filter criteria
     * @return graph which meets all current filter criteria
     * @throws NullPointerException this exception is thrown if all graphs of database
     * which meet the current criteria were already returned
     */
    public PropertyGraph getNextValidGraph() throws NullPointerException {
        return null;
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
    public void checkFilterInput(String input, int id) throws InvalidInputException {

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

    /**
     * used when initializing Grape or switching a database. The methode clears the current
     * list of Filtersegments and calls the methode addFiltersegment(filtersegment:
     * Filtersegment): void for every Filter element of the new database
     */
    public void switchDB() throws Exception {
        availableFilterGroups.clear();
        availableFilter.clear();
        Set<Filtersegment> activatedFilter = database.getActivatedFilters();
        for (Filtersegment element: activatedFilter) {
            if (element.getClass() == Filtergroup.class) {
            }
        }
    }

    public void parseFilterList() {
        int arrayLenght = availableFilter.size() + availableFilterGroups.size();
        String[][] stringArray = new String[7][arrayLenght];
        int currentColumn = 0;
        for (Filter element: availableFilter) {
            if (element.getClass() == BasicFilter.class) {
                stringArray[0][currentColumn] = String.valueOf(element.getProperty1());
                stringArray[1][currentColumn] = "+";
                stringArray[2][currentColumn] = "0";
                stringArray[3][currentColumn] = String.valueOf(element.getRelation());
                stringArray[4][currentColumn] = "0";
                stringArray[5][currentColumn] = "+";
                stringArray[6][currentColumn] = String.valueOf(element.getValue1());
            } else if (element.getClass() == ConnectedFilter.class) {
                stringArray[0][currentColumn] = String.valueOf(element.getProperty1());
                stringArray[1][currentColumn] = String.valueOf(element.getOperator1());
                stringArray[2][currentColumn] = String.valueOf(element.getValue1());
                stringArray[3][currentColumn] = String.valueOf(element.getRelation());
                stringArray[4][currentColumn] = String.valueOf(element.getProperty2());
                stringArray[5][currentColumn] = String.valueOf(element.getOperator2());
                stringArray[6][currentColumn] = String.valueOf(element.getValue2());
            }
        }
    }
}

package edu.kit.ipd.dbis.Controller.Filter;

import java.util.List;

/**
 * this class allows to put multiply filter to one set
 */
public class Filtergroup extends Filtersegment {

    List<Filter> availableFilter;

    /**
     * Constructor of class Filtergroup
     * @param name name of the filtergroup (name of filtergroup should be equal to user
     * input)
     * @param isActivated true if the filter of this group are currently used to filter
     * graphs
     * @param id unique identifier of the filter group
     */
    public Filtergroup(String name, boolean isActivated, int id) {
        this.name = name;
        this.isActivated = isActivated;
        this.id = id;
    }

    /**
     * adds a filter to a specific filtergroup
     * @param filter filter which should be added to a specific filtergroup
     */
    void addFilter(Filter filter) {
        availableFilter.add(filter);
    }

    /**
     * removes a filter from a specific filtergroup
     * @param id unique identifier of the filtersegment which should be enabled
     */
    void removeFilter(int id) {

    }

    /**
     * helps to prevent a NullPointerException by checking if there is any filter left
     * @return returns true if there is a filter which was not returned yet
     */
    boolean hasNextFilter() {
        return false;
    }

    /**
     * used to get all filter of a specific filtergroup
     * @return returns a filter which was not returned yet
     * @throws NullPointerException this exception is thrown if all filter of the specific
     * group were already returned
     */
    Filter getNextFilter() throws NullPointerException {
        throw new NullPointerException();
    }
}

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
     */
    Filtergroup(String name, boolean isActivated) {
        this.name = name;
        this.isActivated = isActivated;
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

}

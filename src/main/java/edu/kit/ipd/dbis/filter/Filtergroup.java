package edu.kit.ipd.dbis.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * this class allows to put multiply filter to one set
 */
public class Filtergroup extends Filtersegment {

    List<Filter> availableFilter;

    /**
     * getter-method for availableFilter
     * @return availableFilter
     */
    public List<Filter> getAvailableFilter() {
        return availableFilter;
    }

    /**
     * Constructor of class Filtergroup
     * @param name name of the filtergroup (name of filtergroup should be equal to user
     * input)
     * @param isActivated true if the filter of this group are currently used to filter
     * graphs
     * @param id unique identifier the filtergroup should have
     */
    public Filtergroup(String name, boolean isActivated, int id) {
        this.name = name;
        this.isActivated = isActivated;
        this.id = id;
        availableFilter = new ArrayList<>();
    }
}

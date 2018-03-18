package edu.kit.ipd.dbis.filter;

import java.util.*;

/**
 * this class allows to put multiply filter to one set
 */
public class Filtergroup extends Filtersegment {

    private Map<Integer, Filter> availableFilters;

    /**
     * getter-method for availableFilters
     * @return availableFilters
     */
    public Set<Filter> getAvailableFilters() {
        return new HashSet<>(availableFilters.values());
    }

    public void addFilter(Filter filter) {
        this.availableFilters.put(filter.id, filter);
    }

    public boolean containsFilter(int filterId) {
        return this.availableFilters.containsKey(filterId);
    }

    public Filter getFilter(int filterId) {
        return this.availableFilters.get(filterId);
    }

    public void removeFilter(int filterId) {
        this.availableFilters.remove(filterId);
    }

    @Override
    public void activate() {
        super.activate();
        this.availableFilters.values().forEach(f -> f.activate());
    }

    @Override
    public void deactivate() {
        super.deactivate();
        this.availableFilters.values().forEach(f -> f.deactivate());
    }

    /**
     * Constructor of class Filtergroup
     * @param name name of the filtergroup (name of filtergroup should be equal to user
     * input)
     * @param isActivated true if the filter of this group are currently used to filter
     * graphs
     * @param id unique identifier the filtergroup should have
     */
    Filtergroup(String name, boolean isActivated, int id) {
        this.name = name;
        this.isActivated = isActivated;
        this.id = id;
        this.availableFilters = new HashMap<>();
    }
}

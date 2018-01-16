package edu.kit.ipd.dbis.Controller.Filter;

/**
 * a specific filter might be a BasicFilter or a ConnectedFilter
 */
public abstract class Filter extends Filtersegment {

    /**
     * checks if a specific graph meets every criteria of the current filter
     * @param graph graph which should be checked for the criteria of the filter
     * @return returns true if the graph meets every criteria of the filter
     */
    abstract boolean fulfillFilter(PropertyGraph graph);
}

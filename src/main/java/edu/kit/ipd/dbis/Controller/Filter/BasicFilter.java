package edu.kit.ipd.dbis.Controller.Filter;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;

/**
 * class which checks a specific attribute of a graph
 */
public class BasicFilter extends Filter {
    private int value;
    private Relation relation;
    private Property property;

    /**
     * Constructor of class BasicFilter
     * @param name name of the BasicFilter (the name should be identical to the user
     * input)
     * @param isActivated boolean which shows if the specific filter is activated
     * @param value largest or smallest value which an attribute of a graph can have to
     * meet the criteria of the filter
     * @param relation describes the relation between the property and the value
     * @param property property the current filter inspects
     */
    BasicFilter(String name, boolean isActivated, int value,
                Relation relation, Property property) {

    }

    @Override
    boolean fulfillFilter(PropertyGraph graph) {
        return false;
    }
}

package edu.kit.ipd.dbis.Controller.Filter;

/**
 * class which checks a specific attribute of a graph
 */
public class BasicFilter extends Filter {
    private int minimalValue;
    private int maximalValue;
    private Attribute attribute;

    /**
     * Constructor of class BasicFilter
     * @param name name of the BasicFilter (the name should be identical to the user
     * input)
     * @param isActivated boolean which shows if the specific filter is activated
     * @param maximalValue argest value which an attribute of a graph can have to
     * meet the criteria of the filter
     * @param minimalValue smallest value which an attribute of a graph can have to
     * meet the criteria of the filter
     * @param attribute attribute which should be checked by the filter
     */
    BasicFilter(String name, boolean isActivated, int maximalValue,
    int minimalValue, Attribute attribute) {

    }

    @Override
    boolean fulfillFilter(PropertyGraph graph) {
        return false;
    }
}

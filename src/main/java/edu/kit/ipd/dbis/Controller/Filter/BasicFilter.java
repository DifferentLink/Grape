package edu.kit.ipd.dbis.Controller.Filter;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;

/**
 * class which checks a specific attribute of a graph
 */
public class BasicFilter extends Filter {

    /**
     * Constructor of class BasicFilter
     * @param name name of the BasicFilter (the name should be identical to the user
     * input)
     * @param isActivated boolean which shows if the specific filter is activated
     * @param value1 largest or smallest value which an attribute of a graph can have to
     * meet the criteria of the filter
     * @param relation1 describes the relation between the property and the value
     * @param property1 property the current filter inspects
     */
    BasicFilter(String name, boolean isActivated, int value1,
                Relation relation1, Property property1) {
        this.name = name;
        this.property1 = property1;
        this.isActivated = isActivated;
        this.value1 = value1;
        this.relation = relation;

    }

    @Override
    boolean fulfillFilter(PropertyGraph graph) {
        return false;
    }
}

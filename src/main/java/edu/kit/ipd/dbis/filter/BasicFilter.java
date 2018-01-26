package edu.kit.ipd.dbis.filter;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;

/**
 * class which checks a specific attribute of a graph
 */
class BasicFilter extends Filter {

    /**
     * Constructor of class BasicFilter
     * @param name name of the BasicFilter (the name should be identical to the user
     * input)
     * @param isActivated boolean which shows if the specific filter is activated
     * @param value1 largest or smallest value which an attribute of a graph can have to
     * meet the criteria of the filter
     * @param relation describes the relation between the property and the value
     * @param property1 property the current filter inspects
     * @param id unique identifier the current filter has
     */
    BasicFilter(String name, boolean isActivated, int value1,
                Relation relation, Property property1, int id) {
        this.name = name;
        this.property1 = property1;
        this.isActivated = isActivated;
        this.value1 = value1;
        this.relation = relation;
        this.id = id;

    }
}

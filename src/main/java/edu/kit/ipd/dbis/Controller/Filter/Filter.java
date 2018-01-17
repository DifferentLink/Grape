package edu.kit.ipd.dbis.Controller.Filter;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;

/**
 * a specific filter might be a BasicFilter or a ConnectedFilter
 */
public abstract class Filter extends Filtersegment {

    protected int value1;
    protected Relation relation1;
    protected Property property1;

    /**
     * getter of attribute value1
     * @return integer value of attribute value1
     */
    public int getValue() {
        return value1;
    }

    /**
     * getter of attribute relation1
     * @return value of attribute relation1
     */
    public Relation getRelation1() {
        return relation1;
    }

    /**
     * getter of attribute property1
     * @return value of attribute property1
     */
    public Property getProperty() {
        return property1;
    }

    /**
     * checks if a specific graph meets every criteria of the current filter
     * @param graph graph which should be checked for the criteria of the filter
     * @return returns true if the graph meets every criteria of the filter
     */
    abstract boolean fulfillFilter(PropertyGraph graph);
}

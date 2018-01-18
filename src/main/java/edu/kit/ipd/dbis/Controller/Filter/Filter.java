package edu.kit.ipd.dbis.Controller.Filter;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;

/**
 * a specific filter might be a BasicFilter or a ConnectedFilter
 */
public abstract class Filter extends Filtersegment {

    protected Property property1;
    protected Operator operator1;
    protected int value1;
    protected Relation relation;
    protected Property property2;
    protected Operator operator2;
    protected int value2;

    /**
     * getter of attribute value2
     * @return value of attribute value2
     */
    public int getValue2() {
        return value2;
    }

    /**
     * getter of attribute operator2
     * @return value of attribute operator2
     */
    public Operator getOperator2() {
        return operator2;
    }

    /**
     * getter of attribute property2
     * @return value of attribute property1
     */
    public Property getProperty2() {
        return property2;
    }

    /**
     * getter of attribute operator1
     * @return value of attribute operator1;
     */
    public Operator getOperator1() {
        return operator1;
    }

    /**
     * getter of attribute value1
     * @return integer value of attribute value1
     */
    public int getValue1() {
        return value1;
    }

    /**
     * getter of attribute relation
     * @return value of attribute relation
     */
    public Relation getRelation() {
        return relation;
    }

    /**
     * getter of attribute property1
     * @return value of attribute property1
     */
    public Property getProperty1() {
        return property1;
    }

    /**
     * checks if a specific graph meets every criteria of the current filter
     * @param graph graph which should be checked for the criteria of the filter
     * @return returns true if the graph meets every criteria of the filter
     */
    abstract boolean fulfillFilter(PropertyGraph graph);
}

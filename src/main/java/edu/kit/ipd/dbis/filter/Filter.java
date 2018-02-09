package edu.kit.ipd.dbis.filter;

/**
 * a specific filter might be a BasicFilter or a ConnectedFilter
 */
public abstract class Filter extends Filtersegment {

    String property1;
    Operator operator1;
    double value1;
    Relation relation;
    String property2;
    Operator operator2;
    double value2;

    /**
     * getter of attribute value2
     * @return value of attribute value2
     */
    public double getValue2() {
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
    public String getProperty2() {
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
    public double getValue1() {
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
    public String getProperty1() {
        return property1;
    }

}

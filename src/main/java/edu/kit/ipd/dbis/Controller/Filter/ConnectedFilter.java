package edu.kit.ipd.dbis.Controller.Filter;

/**
 * class which connectes two objects of class BasicFilter to a new filter
 */
public class ConnectedFilter {
    Attribute attribute1;
    Attribute attribute2;
    Operator operator1;
    Operator operator2;
    int value1;
    int value2;
    Relation realation;

    /**
     * Constructor of class ConnectedFilter
     * @param name name of the connected filter
     * @param isActivated boolean which shows if the specific filter is activated
     * @param id unique identifier for this specific connected filter
     * @param attribute1 first attribute who is part of the relation
     * @param attribute2 second attribute who is part of the relation
     * @param operator1 first operator which modifies an attribute value of f1
     * @param operator2 second operator which modifies an attribute value of f2
     * @param value1 integer value which modifies an attribute value of f1
     * @param value2 integer value which modifies an attribute value of f2
     * @param relation relation which connects the two filter
     */
    public ConnectedFilter(String name, boolean isActivated, int id,
                           Attribute attribute1, Attribute attribute2,
                           Operator operator1, Operator operator2,
                           int value1, int value2, Relation relation) {

    }
}
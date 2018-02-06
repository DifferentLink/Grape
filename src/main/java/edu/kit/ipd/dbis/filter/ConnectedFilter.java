package edu.kit.ipd.dbis.filter;

/**
 * class which connectes two objects of class BasicFilter to a new filter
 */
class ConnectedFilter extends Filter {

    /**
     * Constructor of class ConnectedFilter
     * @param name name of the connected filter
     * @param isActivated boolean which shows if the specific filter is activated
     * @param property1 first attribute who is part of the relation
     * @param property2 second attribute who is part of the relation
     * @param operator1 first operator which modifies an attribute value of f1
     * @param operator2 second operator which modifies an attribute value of f2
     * @param value1 integer value which modifies an attribute value of f1
     * @param value2 integer value which modifies an attribute value of f2
     * @param relation relation which connects the two filter
     * @param id unique identifier the connected filter should have
     */
    ConnectedFilter(String name, boolean isActivated,
                           String property1, String property2,
                           Operator operator1, Operator operator2,
                           int value1, int value2, Relation relation, int id) {
        this.name = name;
        this.isActivated = isActivated;
        this.property1 = property1;
        this.property2 = property2;
        this.operator1 = operator1;
        this.operator2 = operator2;
        this.value1 = value1;
        this.value2 = value2;
        this.relation = relation;
        this.id = id;

    }
}
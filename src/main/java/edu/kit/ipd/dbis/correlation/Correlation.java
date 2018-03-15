package edu.kit.ipd.dbis.correlation;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;

import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyFactory;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.util.Set;
import java.util.TreeSet;

/**
 * abstract class which inherits all attributes and methods every correlation
 * should implement
 */
abstract class Correlation {
    private int attributeCounter;
    private String property;
    private String firstArgument;

    /**
     * method which is used to check filters for a specific correlation
     * @param database database which inherits graphs for the correlation
     * @return returns a list of objects of class CorrelationOutput which code the results of the
     * correlation calculation
     * @throws ConnectionFailedException thrown if the connection to database failed
     */
    public TreeSet<CorrelationOutput> useMaximum(GraphDatabase database) throws ConnectionFailedException {
        TreeSet<CorrelationOutput> resultSet = this.createCorrelationList(database);
        return MutualCorrelation.cutListMaximum(resultSet, this.getAttributeCounter());
    }

    /**
     * method which is used to check filters for a specific correlation monitoring only one property
     * @param property2 property to focus on
     * @param database database which inherits graphs for the correlation
     * @return returns a list of objects of class CorrelationOutput which code the results of the
     * correlation calculation
     * @throws ConnectionFailedException thrown if the connection to database failed
     */
    public TreeSet<CorrelationOutput> useMaximum(String property2, GraphDatabase database) throws
            ConnectionFailedException {
        TreeSet<CorrelationOutput> resultSet = this.createCorrelationList(property2, database);
        return MutualCorrelation.cutListMaximum(resultSet, this.getAttributeCounter());
    }

    /**
     * method which is used to check filters for a specific correlation
     * @param database database which inherits graphs for the correlation
     * @return returns a list of objects of class CorrelationOutput which code the results of the
     * correlation calculation
     * @throws ConnectionFailedException thrown if the connection to database failed
     */
    public TreeSet<CorrelationOutput> useMinimum(GraphDatabase database) throws ConnectionFailedException {
        TreeSet<CorrelationOutput> resultSet = this.createCorrelationList(database);
        return MutualCorrelation.cutListMinimum(resultSet, this.getAttributeCounter());
    }

    /**
     * method which is used to check filters for a specific correlation monitoring only one property
     * @param property2 property to focus on
     * @param database database which inherits graphs for the correlation
     * @return returns a list of objects of class CorrelationOutput which code the results of the
     * correlation calculation
     * @throws ConnectionFailedException thrown if the connection to database failed
     */
    public TreeSet<CorrelationOutput> useMinimum(String property2, GraphDatabase database) throws
            ConnectionFailedException {
        TreeSet<CorrelationOutput> resultSet = this.createCorrelationList(property2, database);
        return MutualCorrelation.cutListMinimum(resultSet, this.getAttributeCounter());
    }

    private TreeSet<CorrelationOutput> createCorrelationList(GraphDatabase database) throws ConnectionFailedException {
        String[] firstPropertyList = Pearson.getValidProperties();
        String[] secondPropertyList = Pearson.getValidProperties();
        TreeSet<CorrelationOutput> resultSet = new TreeSet<>();
        for (int i = 0; i < firstPropertyList.length; i++) {
            for (int j = (i + 1); j < secondPropertyList.length; j++) {
                CorrelationOutput outputObject = new CorrelationOutput(firstPropertyList[i], secondPropertyList[j],
                        this.calculateCorrelation(firstPropertyList[i], secondPropertyList[j], database));
                if (outputObject.getOutputNumber() != Double.MAX_VALUE) {
                    resultSet.add(outputObject);
                }
            }
        }
        return resultSet;
    }

    private TreeSet<CorrelationOutput> createCorrelationList(String property2, GraphDatabase database)
            throws ConnectionFailedException {
        TreeSet<CorrelationOutput> resultSet = new TreeSet<>();
        String[] firstPropertyList = Correlation.getValidProperties();
        for (String property1: firstPropertyList) {
            if (!property1.toLowerCase().equals(property2.toLowerCase())) {
                CorrelationOutput outputObject = new CorrelationOutput(property1, property2,
                        this.calculateCorrelation(property1, property2, database));
                if (outputObject.getOutputNumber() != Double.MAX_VALUE) {
                    resultSet.add(outputObject);
                }
            }
        }
        return resultSet;
    }

    /**
     * method which calculates the specific correlation
     * @param firstProperty first property to focus on
     * @param secondProperty second property to focus on
     * @param database database which inherits the graphs with calculated properties
     * @return returns the value of the mutual correlation of two properties (firstProperty and secondProperty)
     * @throws ConnectionFailedException thrown if there was no connection to the database possible
     */
    public abstract double calculateCorrelation(String firstProperty, String secondProperty,
                                       GraphDatabase database) throws ConnectionFailedException;

    /**
     * setter of attribute attributeCounter
     * @param attributeCounter integer value which inherits the information
     * how many pairs of properties are wanted
     */
    void setAttributeCounter(int attributeCounter) {
        this.attributeCounter = attributeCounter;
    }

    /**
     * setter of attribute property
     * @param property specific property the correlation should focus on
     */
    void setProperty(String property) {
        this.property = property;
    }

    /**
     * setter of attribute maximum
     * @param firstArgument shows if it is searched for the weakest, for
     * the strongest or for the least correlations
     */
    void setFirstArgument(String firstArgument) {
        this.firstArgument = firstArgument;
    }

    /**
     * getter of attribute attributeCounter
     * @return integer value which inherits the information
     * how many pairs of properties are wanted
     */
    int getAttributeCounter() {
        return attributeCounter;
    }

    /**
     * getter of attribute property
     * @return returns the property the specific correlation should focus on
     */
    String getProperty() {
        return property;
    }

    /**
     * getter of attribute maximum
     * @return return the first argument
     * if it is searched for a weak correlation
     */
    String getFirstArgument() {
        return firstArgument;
    }

    /**
     * method to get all properties which inherit an integer or double
     * @return string array which inherits all integer or double properties
     */
    protected static String[] getValidProperties() {
        PropertyGraph<Integer, Integer> graph = new PropertyGraph<>();
        Set<Property> propertySet = PropertyFactory.createNumberProperties(graph);
        String[] resultPropertyList = new String[propertySet.size()];
        int h = 0;
        for (Property currentProperty: propertySet) {
            resultPropertyList[h] = currentProperty.getClass().getSimpleName();
            h++;
        }
        return resultPropertyList;
    }

    /**
     * used to take only the last elements of a specific list
     * @param resultSet list which inherits too much elements
     * @param attributeCounter size of new list
     * @return short list which inherits only the last elements of the input list
     */
    protected static TreeSet<CorrelationOutput> cutListMinimum(TreeSet<CorrelationOutput> resultSet,
                                                               int attributeCounter) {
        TreeSet<CorrelationOutput> outputSet = new TreeSet<>();
        CorrelationOutput[] outputArray = new CorrelationOutput[resultSet.size()];
        int z = 0;
        for (CorrelationOutput current: resultSet) {
            outputArray[z] = current;
            z++;
        }
        int k = 0;
        int l = 0;
        while (k < attributeCounter) {
            if ((outputArray.length - l - 1) < 0) {
                return outputSet;
            }
            if (outputArray[outputArray.length - 1 - l].getFirstProperty().
                    equals(outputArray[outputArray.length - 1 - l].getSecondProperty())) {
                l++;
            } else {
                outputSet.add(outputArray[outputArray.length - 1 - l]);
                l++;
                k++;
            }
        }
        return outputSet;
    }

    /**
     * used to take only the first elements of a specific list
     * @param resultSet list which inherits too much elements
     * @param attributeCounter size of new list
     * @return short list which inherits only the first elements of the input list
     */
    protected static TreeSet<CorrelationOutput> cutListMaximum(TreeSet<CorrelationOutput> resultSet,
                                                               int attributeCounter) {
        TreeSet<CorrelationOutput> outputSet = new TreeSet<>();
        CorrelationOutput[] outputArray = new CorrelationOutput[resultSet.size()];
        int z = 0;
        for (CorrelationOutput current: resultSet) {
            outputArray[z] = current;
            z++;
        }
        int k = 0;
        int l = 0;
        while (k < attributeCounter) {
            if (l >= outputArray.length) {
                return outputSet;
            }
            if (outputArray[l].getFirstProperty().
                    equals(outputArray[l].getSecondProperty())) {
                l++;
            } else {
                outputSet.add(outputArray[l]);
                l++;
                k++;
            }
        }
        return outputSet;
    }
}

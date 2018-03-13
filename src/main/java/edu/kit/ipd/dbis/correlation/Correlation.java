package edu.kit.ipd.dbis.correlation;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;

import edu.kit.ipd.dbis.database.exceptions.sql.AccessDeniedForUserException;
import edu.kit.ipd.dbis.database.exceptions.sql.DatabaseDoesNotExistException;
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
    private boolean maximum;

    /**
     * method which is used to check filters for a specific correlation
     * @param database database which inherits graphs for the correlation
     * @return returns a list of objects of class CorrelationOutput which code the results of the
     * correlation calculation
     * @throws ConnectionFailedException thrown if the connection to database failed
     * @throws AccessDeniedForUserException thrown if there is no access to database
     * @throws DatabaseDoesNotExistException thrown if there is no database
     */
    public abstract TreeSet<CorrelationOutput> useMaximum(GraphDatabase database) throws DatabaseDoesNotExistException,
            AccessDeniedForUserException, ConnectionFailedException;

    /**
     * method which is used to check filters for a specific correlation monitoring only one property
     * @param property1 property to focus on
     * @param database database which inherits graphs for the correlation
     * @return returns a list of objects of class CorrelationOutput which code the results of the
     * correlation calculation
     * @throws ConnectionFailedException thrown if the connection to database failed
     * @throws AccessDeniedForUserException thrown if there is no access to database
     * @throws DatabaseDoesNotExistException thrown if there is no database
     */
    public abstract TreeSet<CorrelationOutput> useMaximum(String property1, GraphDatabase database) throws
            DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException;

    /**
     * method which is used to check filters for a specific correlation
     * @param database database which inherits graphs for the correlation
     * @return returns a list of objects of class CorrelationOutput which code the results of the
     * correlation calculation
     * @throws ConnectionFailedException thrown if the connection to database failed
     * @throws AccessDeniedForUserException thrown if there is no access to database
     * @throws DatabaseDoesNotExistException thrown if there is no database
     */
    public abstract TreeSet<CorrelationOutput> useMinimum(GraphDatabase database) throws
            DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException;

    /**
     * method which is used to check filters for a specific correlation monitoring only one property
     * @param property1 property to focus on
     * @param database database which inherits graphs for the correlation
     * @return returns a list of objects of class CorrelationOutput which code the results of the
     * correlation calculation
     * @throws ConnectionFailedException thrown if the connection to database failed
     * @throws AccessDeniedForUserException thrown if there is no access to database
     * @throws DatabaseDoesNotExistException thrown if there is no database
     */
    public abstract TreeSet<CorrelationOutput> useMinimum(String property1, GraphDatabase database)
            throws DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException;

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
     * @param maximum shows if it is searched for the weakest or for
     * the strongest correlations
     */
    void setMaximum(boolean maximum) {
        this.maximum = maximum;
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
     * @return return true if it is search for a strong correlation and false
     * if it is searched for a weak correlation
     */
    boolean getMaximum() {
        return maximum;
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

package edu.kit.ipd.dbis.correlation;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;

import edu.kit.ipd.dbis.database.exceptions.sql.AccessDeniedForUserException;
import edu.kit.ipd.dbis.database.exceptions.sql.DatabaseDoesNotExistException;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;

import java.util.TreeSet;

/**
 * abstract class which inherits all attributes and methods every correlation
 * should implement
 */
abstract class Correlation {
    private int attributeCounter;
    private Property property;
    private boolean maximum;

    /**
     * method which is used to check filters for a specific correlation
     * @param database database which inherits graphs for the correlation
     * @return returns a list of objects of class CorrelationOutput which code the results of the
     * correlation calculation
     * @throws ConnectionFailedException thrown if the table in database is not as expected
     * @throws ConnectionFailedException thrown if the connection to database failed
     * @throws AccessDeniedForUserException thrown if there is no access to database
     * @throws DatabaseDoesNotExistException thrown if there is no database
     */
    public abstract TreeSet<CorrelationOutput> useMaximum(GraphDatabase database) throws DatabaseDoesNotExistException,
            AccessDeniedForUserException, ConnectionFailedException, ConnectionFailedException;

    /**
     * method which is used to check filters for a specific correlation monitoring only one property
     * @param property1 property to focus on
     * @param database database which inherits graphs for the correlation
     * @return returns a list of objects of class CorrelationOutput which code the results of the
     * correlation calculation
     * @throws ConnectionFailedException thrown if the table in database is not as expected
     * @throws ConnectionFailedException thrown if the connection to database failed
     * @throws AccessDeniedForUserException thrown if there is no access to database
     * @throws DatabaseDoesNotExistException thrown if there is no database
     */
    public abstract TreeSet<CorrelationOutput> useMaximum(Property property1, GraphDatabase database) throws
            DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException,
            ConnectionFailedException;

    /**
     * method which is used to check filters for a specific correlation
     * @param database database which inherits graphs for the correlation
     * @return returns a list of objects of class CorrelationOutput which code the results of the
     * correlation calculation
     * @throws ConnectionFailedException thrown if the table in database is not as expected
     * @throws ConnectionFailedException thrown if the connection to database failed
     * @throws AccessDeniedForUserException thrown if there is no access to database
     * @throws DatabaseDoesNotExistException thrown if there is no database
     */
    public abstract TreeSet<CorrelationOutput> useMinimum(GraphDatabase database) throws
            DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException,
            ConnectionFailedException;

    /**
     * method which is used to check filters for a specific correlation monitoring only one property
     * @param property1 property to focus on
     * @param database database which inherits graphs for the correlation
     * @return returns a list of objects of class CorrelationOutput which code the results of the
     * correlation calculation
     * @throws ConnectionFailedException thrown if the table in database is not as expected
     * @throws ConnectionFailedException thrown if the connection to database failed
     * @throws AccessDeniedForUserException thrown if there is no access to database
     * @throws DatabaseDoesNotExistException thrown if there is no database
     */
    public abstract TreeSet<CorrelationOutput> useMinimum(Property property1, GraphDatabase database)
            throws DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException,
            ConnectionFailedException;

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
    void setProperty(Property property) {
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
    Property getProperty() {
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
}

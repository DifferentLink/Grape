package edu.kit.ipd.dbis.correlation;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.AccessDeniedForUserException;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.DatabaseDoesNotExistException;
import edu.kit.ipd.dbis.database.exceptions.sql.TablesNotAsExpectedException;
import edu.kit.ipd.dbis.filter.Filtermanagement;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyFactory;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

/**
 * class which calculates the MutualCorrelation for a list of graphs
 */
public class MutualCorrelation extends Correlation {

    @Override
    public TreeSet<CorrelationOutput> useMaximum(GraphDatabase database) throws DatabaseDoesNotExistException,
            AccessDeniedForUserException, ConnectionFailedException, TablesNotAsExpectedException {
        Set<Property> firstPropertyList = PropertyFactory.createAllProperties(new PropertyGraph());
        Set<Property> secondPropertyList = PropertyFactory.createAllProperties(new PropertyGraph());
        TreeSet<CorrelationOutput> resultSet = new TreeSet<>();
        for (Property property1: firstPropertyList) {
            for (Property property2: secondPropertyList) {
                CorrelationOutput outputObject = new CorrelationOutput(property1, property2,
                        MutualCorrelation.calculateCorrelation(property1, property2, database));
                resultSet.add(outputObject);
            }
        }
        TreeSet<CorrelationOutput> outputSet = new TreeSet<>();
        for (int i = 0; i < this.getAttributeCounter(); i++) {
            outputSet.add(resultSet.last());
            resultSet.remove(resultSet.last());
        }
        return outputSet;
    }

    @Override
    public TreeSet<CorrelationOutput> useMaximum(Property property1, GraphDatabase database) throws
            DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException,
            TablesNotAsExpectedException {
        Set<Property> secondPropertyList = PropertyFactory.createAllProperties(new PropertyGraph());
        TreeSet<CorrelationOutput> resultSet = new TreeSet<>();
        for (Property property2: secondPropertyList) {
            CorrelationOutput outputObject = new CorrelationOutput(property1, property2,
                    MutualCorrelation.calculateCorrelation(property1, property2, database));
            resultSet.add(outputObject);
        }
        TreeSet<CorrelationOutput> outputSet = new TreeSet<>();
        for (int i = 0; i < this.getAttributeCounter(); i++) {
            outputSet.add(resultSet.last());
            resultSet.remove(resultSet.last());
        }
        return outputSet;
    }

    @Override
    public TreeSet<CorrelationOutput> useMinimum(GraphDatabase database) throws DatabaseDoesNotExistException,
            AccessDeniedForUserException, ConnectionFailedException, TablesNotAsExpectedException {
        Set<Property> firstPropertyList = PropertyFactory.createAllProperties(new PropertyGraph());
        Set<Property> secondPropertyList = PropertyFactory.createAllProperties(new PropertyGraph());
        TreeSet<CorrelationOutput> resultSet = new TreeSet<>();
        for (Property property1: firstPropertyList) {
            for (Property property2: secondPropertyList) {
                CorrelationOutput outputObject = new CorrelationOutput(property1, property2,
                        MutualCorrelation.calculateCorrelation(property1, property2, database));
                resultSet.add(outputObject);
            }
        }
        TreeSet<CorrelationOutput> outputSet = new TreeSet<>();
        for (int i = 0; i < this.getAttributeCounter(); i++) {
            outputSet.add(resultSet.first());
            resultSet.remove(resultSet.first());
        }
        return outputSet;
    }

    @Override
    public TreeSet<CorrelationOutput> useMinimum(Property property1, GraphDatabase database) throws
            DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException,
            TablesNotAsExpectedException {
        Set<Property> secondPropertyList = PropertyFactory.createAllProperties(new PropertyGraph());
        TreeSet<CorrelationOutput> resultSet = new TreeSet<>();
        for (Property property2: secondPropertyList) {
            CorrelationOutput outputObject = new CorrelationOutput(property1, property2,
                    MutualCorrelation.calculateCorrelation(property1, property2, database));
            resultSet.add(outputObject);
        }
        TreeSet<CorrelationOutput> outputSet = new TreeSet<>();
        for (int i = 0; i < this.getAttributeCounter(); i++) {
            outputSet.add(resultSet.first());
            resultSet.remove(resultSet.first());
        }
        return outputSet;
    }

    private static double calculateCorrelation(Property firstProperty, Property secondProperty,
                                               GraphDatabase database) throws DatabaseDoesNotExistException,
            AccessDeniedForUserException, ConnectionFailedException, TablesNotAsExpectedException {
        Filtermanagement manager = new Filtermanagement();
        manager.setDatabase(database);
        LinkedList<Double> firstPropertyValues = database.getValues(manager.parseFilterList(),
                firstProperty.toString());
        double returnValue = 0;
        while (!firstPropertyValues.isEmpty()) {
            LinkedList<Double> secondPropertyValues = database.getValues(manager.parseFilterList(),
                    secondProperty.toString());
            double i = MutualCorrelation.getMinimum(firstPropertyValues);
            while (!secondPropertyValues.isEmpty()) {
                double j = MutualCorrelation.getMinimum(secondPropertyValues);
                returnValue = returnValue + (MutualCorrelation.calculatePXY(firstPropertyValues, i,
                        secondPropertyValues, j) * Math.log((MutualCorrelation.calculatePXY(firstPropertyValues, i,
                        secondPropertyValues, j) / (MutualCorrelation.calculatePX(firstPropertyValues, i)
                        * MutualCorrelation.calculatePX(secondPropertyValues, j)))));
                secondPropertyValues.remove(j);
            }
            firstPropertyValues.remove(i);
        }
        return returnValue;
    }

    private static double calculatePXY(LinkedList<Double> inputList1, double value1, LinkedList<Double> inputList2,
                                double value2) {
        double counter = 0.0;
        try {
            for (int i = 0; i < inputList1.size(); i++) {
                if (inputList1.get(i) == value1 && inputList2.get(i) == value2) {
                    counter++;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            return (counter / inputList1.size());
        }
        return (counter / inputList1.size());
    }

    private static double calculatePX(LinkedList<Double> inputList, double value) {
        double counter = 0.0;
        for (double currentElement: inputList) {
            if (currentElement == value) {
                counter++;
            }
        }
        return (counter / inputList.size());
    }

    private static double getMinimum(LinkedList<Double> inputList) {
        double minimum = Integer.MAX_VALUE;
        for (double currentElement: inputList) {
            if (currentElement < minimum) {
                minimum = currentElement;
            }
        }
        return minimum;
    }
}

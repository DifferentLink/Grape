package edu.kit.ipd.dbis.correlation;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.filter.Filtermanagement;

import java.util.LinkedList;
import java.util.TreeSet;

/**
 * class which calculates the MutualCorrelation for a list of graphs
 */
public class MutualCorrelation extends Correlation {

    @Override
    public TreeSet<CorrelationOutput> useMaximum(GraphDatabase database) throws ConnectionFailedException {
        String[] firstPropertyList = MutualCorrelation.getValidProperties();
        String[] secondPropertyList = MutualCorrelation.getValidProperties();
        TreeSet<CorrelationOutput> resultSet = new TreeSet<>();
        int i = 0;
        for (String property1: firstPropertyList) {
            i++;
            for (int j = i; j < secondPropertyList.length; j++) {
                CorrelationOutput outputObject = new CorrelationOutput(property1, secondPropertyList[j],
                        MutualCorrelation.calculateCorrelation(property1, secondPropertyList[j], database));
                resultSet.add(outputObject);
            }
        }
        return MutualCorrelation.cutListMaximum(resultSet, this.getAttributeCounter());
    }

    @Override
    public TreeSet<CorrelationOutput> useMaximum(String property2, GraphDatabase database) throws
            ConnectionFailedException {
        String[] firstPropertyList = Pearson.getValidProperties();
        TreeSet<CorrelationOutput> resultSet = new TreeSet<>();
        for (String property1: firstPropertyList) {
            if (!property1.toLowerCase().equals(property2.toLowerCase())) {
                CorrelationOutput outputObject = new CorrelationOutput(property1, property2,
                        MutualCorrelation.calculateCorrelation(property1, property2, database));
                resultSet.add(outputObject);
            }
        }
        return MutualCorrelation.cutListMaximum(resultSet, this.getAttributeCounter());
    }

    @Override
    public TreeSet<CorrelationOutput> useMinimum(GraphDatabase database) throws ConnectionFailedException {
        String[] firstPropertyList = Pearson.getValidProperties();
        String[] secondPropertyList = Pearson.getValidProperties();
        TreeSet<CorrelationOutput> resultSet = new TreeSet<>();
        for (String property1: firstPropertyList) {
            for (String property2: secondPropertyList) {
                CorrelationOutput outputObject = new CorrelationOutput(property1, property2,
                        MutualCorrelation.calculateCorrelation(property1, property2, database));
                resultSet.add(outputObject);
            }
        }
        return MutualCorrelation.cutListMinimum(resultSet, this.getAttributeCounter());
    }

    @Override
    public TreeSet<CorrelationOutput> useMinimum(String property2, GraphDatabase database) throws
            ConnectionFailedException {
        String[] firstPropertyList = Pearson.getValidProperties();
        TreeSet<CorrelationOutput> resultSet = new TreeSet<>();
        for (String property1: firstPropertyList) {
            if (!property1.toLowerCase().equals(property2.toLowerCase())) {
                CorrelationOutput outputObject = new CorrelationOutput(property1, property2,
                        MutualCorrelation.calculateCorrelation(property1, property2, database));
                resultSet.add(outputObject);
            }
        }
        return MutualCorrelation.cutListMinimum(resultSet, this.getAttributeCounter());
    }

    private static double calculateCorrelation(String firstProperty, String secondProperty,
                                               GraphDatabase database) throws ConnectionFailedException {
        Filtermanagement manager = new Filtermanagement();
        manager.setDatabase(database);
        LinkedList<Double> firstPropertyValues = database.getValues(manager.parseFilterList(),
                firstProperty);
        double returnValue = 0;
        while (!firstPropertyValues.isEmpty()) {
            LinkedList<Double> secondPropertyValues = database.getValues(manager.parseFilterList(),
                    secondProperty);
            double i = MutualCorrelation.getMinimum(firstPropertyValues);
            while (!secondPropertyValues.isEmpty()) {
                double j = MutualCorrelation.getMinimum(secondPropertyValues);
                double log = Math.log(MutualCorrelation.calculatePXY(firstPropertyValues, i,
                        secondPropertyValues, j) / (MutualCorrelation.calculatePX(firstPropertyValues, i)));
                if (log > -2000.0) {
                    returnValue = returnValue + (MutualCorrelation.calculatePXY(firstPropertyValues, i,
                            secondPropertyValues, j) * Math.log((MutualCorrelation.calculatePXY(firstPropertyValues, i,
                            secondPropertyValues, j) / (MutualCorrelation.calculatePX(firstPropertyValues, i)
                            * MutualCorrelation.calculatePX(secondPropertyValues, j)))));
                }
                secondPropertyValues.remove(j);
            }
            firstPropertyValues.remove(i);
        }
        if (!(returnValue > -2000 && returnValue < 2000)) {
            return 0.0;
        }
        return returnValue;
    }

    private static double calculatePXY(LinkedList<Double> inputList1, double value1, LinkedList<Double> inputList2,
                                       double value2) {
        double counter = 0.0;
        try {
            for (int i = 0; i < inputList1.size(); i++) {
                if ((inputList1.get(i) - value1) < 0.01 && (inputList2.get(i) - value2) < 0.01) {
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
            if ((currentElement - value) < 0.01) {
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

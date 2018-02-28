package edu.kit.ipd.dbis.correlation;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.filter.Filtermanagement;
import java.util.*;

/**
 * class which calculates the Pearson-Correlation for a list of graphs
 */
public class Pearson extends Correlation {

    @Override
    public TreeSet<CorrelationOutput> useMaximum(GraphDatabase database) throws ConnectionFailedException {
        String[] firstPropertyList = Pearson.getValidProperties();
        String[] secondPropertyList = Pearson.getValidProperties();
        TreeSet<CorrelationOutput> resultSet = new TreeSet<>();
        int i = 0;
        for (String property1: firstPropertyList) {
            i++;
            for (int j = i; j < secondPropertyList.length; j++) {
                CorrelationOutput outputObject = new CorrelationOutput(property1, secondPropertyList[j],
                        Pearson.calculateCorrelation(property1, secondPropertyList[j], database));
                resultSet.add(outputObject);
            }
        }
        return Pearson.cutListMaximum(resultSet, this.getAttributeCounter());
    }

    @Override
    public TreeSet<CorrelationOutput> useMaximum(String property2, GraphDatabase database) throws
            ConnectionFailedException {
        String[] firstPropertyList = Pearson.getValidProperties();
        TreeSet<CorrelationOutput> resultSet = new TreeSet<>();
        for (String property1: firstPropertyList) {
            if (!property1.toLowerCase().equals(property2.toLowerCase())) {
                CorrelationOutput outputObject = new CorrelationOutput(property1, property2,
                        Pearson.calculateCorrelation(property1, property2, database));
                resultSet.add(outputObject);
            }
        }
        return Pearson.cutListMaximum(resultSet, this.getAttributeCounter());
    }

    @Override
    public TreeSet<CorrelationOutput> useMinimum(GraphDatabase database) throws ConnectionFailedException {
        String[] firstPropertyList = Pearson.getValidProperties();
        String[] secondPropertyList = Pearson.getValidProperties();
        TreeSet<CorrelationOutput> resultSet = new TreeSet<>();
        for (String property1: firstPropertyList) {
            for (String property2: secondPropertyList) {
                CorrelationOutput outputObject = new CorrelationOutput(property1, property2,
                        Pearson.calculateCorrelation(property1, property2, database));
                resultSet.add(outputObject);
            }
        }
        return Pearson.cutListMinimum(resultSet, this.getAttributeCounter());
    }

    @Override
    public TreeSet<CorrelationOutput> useMinimum(String property2, GraphDatabase database) throws
            ConnectionFailedException {
        String[] firstPropertyList = Pearson.getValidProperties();
        TreeSet<CorrelationOutput> resultSet = new TreeSet<>();
        for (String property1: firstPropertyList) {
            if (!property1.toLowerCase().equals(property2.toLowerCase())) {
                CorrelationOutput outputObject = new CorrelationOutput(property1, property2,
                        Pearson.calculateCorrelation(property1, property2, database));
                resultSet.add(outputObject);
            }
        }
        return Pearson.cutListMinimum(resultSet, this.getAttributeCounter());
    }

    private static double calculateCorrelation(String firstProperty, String secondProperty,
                                               GraphDatabase database) throws ConnectionFailedException {
        Filtermanagement manager = new Filtermanagement();
        manager.setDatabase(database);
        LinkedList<Double> firstPropertyValues = database.getValues(manager.parseFilterList(),
                firstProperty);
        LinkedList<Double> secondPropertyValues = database.getValues(manager.parseFilterList(),
                secondProperty);
        double firstRandomMedium = Pearson.createRandomMedium(firstPropertyValues);
        double secondRandomMedium = Pearson.createRandomMedium(secondPropertyValues);

        double sum = 0;
        Double[] firstPropertyArrayValues = new Double[firstPropertyValues.size()];
        int a = 0;
        for (double current: firstPropertyValues) {
            firstPropertyArrayValues[a] = current;
            a++;
        }
        Double[] secondPropertyArrayValues = new Double[firstPropertyValues.size()];
        a = 0;
        for (double current: firstPropertyValues) {
            secondPropertyArrayValues[a] = current;
            a++;
        }
        for (int b = 0; b < firstPropertyArrayValues.length; b++) {
            sum = sum + ((firstPropertyArrayValues[b] - firstRandomMedium)
                    * (secondPropertyArrayValues[b] - secondRandomMedium));
        }

        double result = (sum / (firstPropertyValues.size() - 1));
        double coefficient = result / (Pearson.getSampleVariationskoeffizient(firstPropertyValues, firstRandomMedium)
                * Pearson.getSampleVariationskoeffizient(secondPropertyValues, secondRandomMedium));
        if (!(coefficient > -1 && coefficient < 1)) {
            return 0;
        }
        return coefficient;
    }

    /**
     * method which calculates the random medium
     * @param inputList list of double values
     * @return returns the random medium of all values of the input list
     */
    static double createRandomMedium(LinkedList<Double> inputList) {
        double sum = 0;
        for (double currentValue: inputList) {
            sum = (sum + currentValue);
        }
        return (sum / inputList.size());
    }

    /**
     * method which calculates the sample variation coefficient
     * @param inputList list of double values
     * @param randomMedium random medium to compare with
     * @return returns the calculated variation coefficient
     */
    static double getSampleVariationskoeffizient(LinkedList<Double> inputList, double randomMedium) {
        if (inputList.size() == 1) {
            return 0;
        }
        double sum = 0;
        for (double currentValue: inputList) {
            sum = (sum + Math.pow(currentValue - randomMedium, 2));
        }
        return Math.sqrt(sum / (inputList.size() - 1));
    }
}

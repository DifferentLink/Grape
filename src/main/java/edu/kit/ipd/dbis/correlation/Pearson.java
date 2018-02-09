package edu.kit.ipd.dbis.correlation;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.filter.Filtermanagement;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyFactory;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

/**
 * class which calculates the Pearson-Correlation for a list of graphs
 */
public class Pearson extends Correlation {

    @Override
    public TreeSet<CorrelationOutput> useMaximum(GraphDatabase database) throws ConnectionFailedException {
        PropertyGraph<Integer, Integer> graph = new PropertyGraph<>();
        Set<Property> firstPropertySet = PropertyFactory.createAllProperties(graph);
        String[] firstPropertyList = new String[firstPropertySet.size()];
        int h = 0;
        for (Property currentProperty: firstPropertySet) {
            firstPropertyList[h] = currentProperty.getClass().getSimpleName();
            h++;
        }
        Set<Property> secondPropertySet = PropertyFactory.createAllProperties(graph);
        String[] secondPropertyList = new String[secondPropertySet.size()];
        int j = 0;
        for (Property currentProperty: secondPropertySet) {
            secondPropertyList[j] = currentProperty.getClass().getSimpleName();
            j++;
        }
        TreeSet<CorrelationOutput> resultSet = new TreeSet<>();
        for (String property1: firstPropertyList) {
            for (String property2: secondPropertyList) {
                CorrelationOutput outputObject = new CorrelationOutput(property1, property2,
                        Pearson.calculateCorrelation(property1, property2, database));
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
    public TreeSet<CorrelationOutput> useMaximum(String property2, GraphDatabase database) throws
            ConnectionFailedException {
        PropertyGraph<Integer, Integer> graph = new PropertyGraph<>();
        Set<Property> firstPropertySet = PropertyFactory.createAllProperties(graph);
        String[] firstPropertyList = new String[firstPropertySet.size()];
        int h = 0;
        for (Property currentProperty: firstPropertySet) {
            firstPropertyList[h] = currentProperty.getClass().getSimpleName();
            h++;
        }
        TreeSet<CorrelationOutput> resultSet = new TreeSet<>();
        for (String property1: firstPropertyList) {
            CorrelationOutput outputObject = new CorrelationOutput(property1, property2,
                    Pearson.calculateCorrelation(property1, property2, database));
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
    public TreeSet<CorrelationOutput> useMinimum(GraphDatabase database) throws ConnectionFailedException {
        PropertyGraph<Integer, Integer> graph = new PropertyGraph<>();
        Set<Property> firstPropertySet = PropertyFactory.createAllProperties(graph);
        String[] firstPropertyList = new String[firstPropertySet.size()];
        int h = 0;
        for (Property currentProperty: firstPropertySet) {
            firstPropertyList[h] = currentProperty.getClass().getSimpleName();
            h++;
        }
        Set<Property> secondPropertySet = PropertyFactory.createAllProperties(graph);
        String[] secondPropertyList = new String[secondPropertySet.size()];
        int j = 0;
        for (Property currentProperty: secondPropertySet) {
            secondPropertyList[j] = currentProperty.getClass().getSimpleName();
            j++;
        }
        TreeSet<CorrelationOutput> resultSet = new TreeSet<>();
        for (String property1: firstPropertyList) {
            for (String property2: secondPropertyList) {
                CorrelationOutput outputObject = new CorrelationOutput(property1, property2,
                        Pearson.calculateCorrelation(property1, property2, database));
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
    public TreeSet<CorrelationOutput> useMinimum(String property2, GraphDatabase database) throws
            ConnectionFailedException {
        PropertyGraph<Integer, Integer> graph = new PropertyGraph<>();
        Set<Property> firstPropertySet = PropertyFactory.createAllProperties(graph);
        String[] firstPropertyList = new String[firstPropertySet.size()];
        int h = 0;
        for (Property currentProperty: firstPropertySet) {
            firstPropertyList[h] = currentProperty.getClass().getSimpleName();
            h++;
        }
        TreeSet<CorrelationOutput> resultSet = new TreeSet<>();
        for (String property1: firstPropertyList) {
            CorrelationOutput outputObject = new CorrelationOutput(property1, property2,
                    Pearson.calculateCorrelation(property1, property2, database));
            resultSet.add(outputObject);
        }
        TreeSet<CorrelationOutput> outputSet = new TreeSet<>();
        for (int i = 0; i < this.getAttributeCounter(); i++) {
            outputSet.add(resultSet.first());
            resultSet.remove(resultSet.first());
        }
        return outputSet;
    }

    private static double calculateCorrelation(String firstProperty, String secondProperty,
                                               GraphDatabase database) throws ConnectionFailedException {
        Filtermanagement manager = new Filtermanagement();
        manager.setDatabase(database);
        LinkedList<Double> firstPropertyValues = database.getValues(manager.parseFilterList(),
                firstProperty.getClass().getSimpleName());
        LinkedList<Double> secondPropertyValues = database.getValues(manager.parseFilterList(),
                secondProperty.getClass().getSimpleName());
        double firstRandomMedium = Pearson.createRandomMedium(firstPropertyValues);
        double secondRandomMedium = Pearson.createRandomMedium(secondPropertyValues);
        double sum = 0;
        for (double currentFirstPropertyValue: firstPropertyValues) {
            for (double currentSecondPropertyValue: secondPropertyValues) {
                sum = sum + ((currentFirstPropertyValue - firstRandomMedium)
                        * (currentSecondPropertyValue - secondRandomMedium));
            }
        }
        double result = (sum / (firstPropertyValues.size() - 1));
        return (result / (Pearson.getSampleVariationskoeffizient(firstPropertyValues, firstRandomMedium)
            * Pearson.getSampleVariationskoeffizient(secondPropertyValues, secondRandomMedium)));
    }

    private static double createRandomMedium(LinkedList<Double> inputList) {
        double sum = 0;
        for (double currentValue: inputList) {
            sum = (sum + currentValue);
        }
        return (sum / inputList.size());
    }

    private static double getSampleVariationskoeffizient(LinkedList<Double> inputList, double randomMedium) {
        double sum = 0;
        for (double currentValue: inputList) {
            sum = (sum + Math.pow(currentValue - randomMedium, 2));
        }
        return Math.sqrt(sum / (inputList.size() - 1));
    }
}

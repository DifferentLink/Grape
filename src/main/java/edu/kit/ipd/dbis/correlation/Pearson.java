package edu.kit.ipd.dbis.correlation;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyFactory;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.*;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_.*;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.*;
import java.util.*;

/**
 * class which calculates the Pearson-Correlation for a list of graphs
 */
public class Pearson extends Correlation {

    @Override
    public TreeSet<CorrelationOutput> useMaximum() {
        Set<Property> firstPropertyList = PropertyFactory.createAllProperties(new PropertyGraph());
        Set<Property> secondPropertyList = PropertyFactory.createAllProperties(new PropertyGraph());
        TreeSet<CorrelationOutput> resultSet = new TreeSet<>();
        for (Property property1: firstPropertyList) {
            for (Property property2: secondPropertyList) {
                CorrelationOutput outputObject = new CorrelationOutput(property1, property2,
                        Pearson.calculateCorrelation(property1, property2));
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
    public TreeSet<CorrelationOutput> useMaximum(Property property2) {
        Set<Property> firstPropertyList = PropertyFactory.createAllProperties(new PropertyGraph());
        TreeSet<CorrelationOutput> resultSet = new TreeSet<>();
        for (Property property1: firstPropertyList) {
            CorrelationOutput outputObject = new CorrelationOutput(property1, property2,
                    Pearson.calculateCorrelation(property1, property2));
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
    public TreeSet<CorrelationOutput> useMinimum() {
        Set<Property> firstPropertyList = PropertyFactory.createAllProperties(new PropertyGraph());
        Set<Property> secondPropertyList = PropertyFactory.createAllProperties(new PropertyGraph());
        TreeSet<CorrelationOutput> resultSet = new TreeSet<>();
        for (Property property1: firstPropertyList) {
            for (Property property2: secondPropertyList) {
                CorrelationOutput outputObject = new CorrelationOutput(property1, property2,
                        Pearson.calculateCorrelation(property1, property2));
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
    public TreeSet<CorrelationOutput> useMinimum(Property property2) {
        Set<Property> firstPropertyList = PropertyFactory.createAllProperties(new PropertyGraph());
        TreeSet<CorrelationOutput> resultSet = new TreeSet<>();
        for (Property property1: firstPropertyList) {
            CorrelationOutput outputObject = new CorrelationOutput(property1, property2,
                    Pearson.calculateCorrelation(property1, property2));
            resultSet.add(outputObject);
        }
        TreeSet<CorrelationOutput> outputSet = new TreeSet<>();
        for (int i = 0; i < this.getAttributeCounter(); i++) {
            outputSet.add(resultSet.first());
            resultSet.remove(resultSet.first());
        }
        return outputSet;
    }

    private static double calculateCorrelation(Property firstProperty, Property secondProperty) {
        //TODO: Hier muss der Teil nach dem = durch eine Methode aus der Datenbank ersetzt werden
        LinkedList<Double> firstPropertyValues = Pearson.generateRandomLinkedList();
        //TODO: Hir muss der Teil nach dem = durch eine Methode aus der Datenbank ersetzt werden
        LinkedList<Double> secondPropertyValues = Pearson.generateRandomLinkedList();
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
        return (result / (Pearson.getSampleVariationskeffizient(firstPropertyValues, firstRandomMedium)
            * Pearson.getSampleVariationskeffizient(secondPropertyValues, secondRandomMedium)));
    }

    private static double createRandomMedium(LinkedList<Double> inputList) {
        double sum = 0;
        for (double currentValue: inputList) {
            sum = (sum + currentValue);
        }
        return (sum / inputList.size());
    }

    private static double getSampleVariationskeffizient(LinkedList<Double> inputList, double randomMedium) {
        double sum = 0;
        for (double currentValue: inputList) {
            sum = (sum + Math.pow(currentValue - randomMedium, 2));
        }
        return Math.sqrt(sum / (inputList.size() - 1));
    }

    //TODO: Diese Methode muss nach DB-Zugriff entfernt werden
    private static LinkedList<Double> generateRandomLinkedList() {
        LinkedList<Double> outputList = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            outputList.add(Math.random());
        }
        return outputList;
    }
}

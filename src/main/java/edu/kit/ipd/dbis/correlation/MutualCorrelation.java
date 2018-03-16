package edu.kit.ipd.dbis.correlation;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.filter.Filtermanagement;

import java.util.LinkedList;

/**
 * class which calculates the MutualCorrelation for a list of graphs
 */
public class MutualCorrelation extends Correlation {

    /**
     * calculates the mutual correlation / information for two specific properties
     * @param firstProperty first property for the specific correlation
     * @param secondProperty second property for the specific correlation
     * @param database database which inherits the graphs with calculated properties
     * @return returns the value of the mutual correlation of two properties (firstProperty and secondProperty)
     * @throws ConnectionFailedException thrown if there was no connection to the database possible
     */
    @Override
    public double calculateCorrelation(String firstProperty, String secondProperty,
                                       GraphDatabase database) throws ConnectionFailedException {
        Filtermanagement manager = new Filtermanagement();
        manager.setDatabase(database);
        LinkedList<Double> firstPropertyValues = database.getValues(manager.parseFilterList(), firstProperty);
        LinkedList<Double> firstPropertyValuesCopy = database.getValues(manager.parseFilterList(), firstProperty);
        LinkedList<Double> secondPropertyValuesCopy = database.getValues(manager.parseFilterList(), secondProperty);
        double returnValue = 0;
        while (!firstPropertyValues.isEmpty()) {
            LinkedList<Double> secondPropertyValues = database.getValues(manager.parseFilterList(), secondProperty);
            double i = firstPropertyValues.pollFirst();
            while (!secondPropertyValues.isEmpty()) {
                double j = secondPropertyValues.pollFirst();
                double log = Math.log(MutualCorrelation.calculatePXY(firstPropertyValuesCopy, i,
                        secondPropertyValuesCopy, j) / (MutualCorrelation.calculatePX(firstPropertyValuesCopy, i)
                        * MutualCorrelation.calculatePX(secondPropertyValuesCopy, j)));
                if (log >= 0.0 && log < 2000.0) {
                    returnValue = returnValue + (MutualCorrelation.calculatePXY(firstPropertyValuesCopy, i,
                            secondPropertyValuesCopy, j) * log);
                }
            }
        }
        if (Math.abs(returnValue) < 0.01) {
            return Double.MAX_VALUE;
        }
        return returnValue;
    }

    /**
     *  calculates the probability that a randomly selected tuple from two lists equals the input tuple
     *  (Note: Tuple are represented by the same position in two lists)
     * @param inputList1 first list which inherits all first values of all tuple
     * @param value1 first value of the tuple which is searched for
     * @param inputList2 second list which inherits all second values of all tuple
     * @param value2 second value of the tuple which is searched for
     * @return returns the probability that a randomly selected tuple equals the input tuple
     */
    static double calculatePXY(LinkedList<Double> inputList1, double value1, LinkedList<Double> inputList2,
                               double value2) {
        double counter = 0.0;
        for (int i = 0; i < inputList1.size(); i++) {
            if (Math.abs(inputList1.get(i) - value1) < 0.01 && Math.abs(inputList2.get(i) - value2) < 0.01) {
                counter = counter + 1;
            }
        }
        return (counter / inputList1.size());
    }

    /**
     * calculates the probability that a randomly selected element from a list equals the input double value
     * @param inputList input list where the specific value might be (more than once possibly)
     * @param value value where is searched for
     * @return returns the probability that a randomly selected element equals the input value
     */
    static double calculatePX(LinkedList<Double> inputList, double value) {
        double counter = 0.0;
        for (double currentElement: inputList) {
            if (Math.abs(currentElement - value) < 0.01) {
                counter = counter + 1;
            }
        }
        return (counter / inputList.size());
    }

}

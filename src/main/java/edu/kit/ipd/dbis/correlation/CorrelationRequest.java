package edu.kit.ipd.dbis.correlation;

import com.mysql.jdbc.StringUtils;
import edu.kit.ipd.dbis.correlation.exceptions.InvalidCorrelationInputException;
import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyFactory;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.util.*;

/**
 * class which is used to handle correlation requests
 */
public class CorrelationRequest {

    private Correlation correlation;
    private GraphDatabase database;

    /**
     * constructor of class CorrelationRequest
     * @param input string which should code a valid correlation
     * @param database database which inherits the current graphs
     * @throws InvalidCorrelationInputException thrown if the input string does not
     * code a valid correlation
     */
    public CorrelationRequest(String input, GraphDatabase database) throws InvalidCorrelationInputException {
        correlation = CorrelationRequest.parseCorrelationToString(input);
        this.database = database;
    }

    /**
     * getter of attribute correlation
     * @return returns the correlation object
     */
    public Correlation getCorrelation() {
        return correlation;
    }

    /**
     * used to perform a specific correlation calculation
     * @return returns an array list which codes the results of the correlation calculation
     * @throws ConnectionFailedException thrown if the connection to database failed
     */
    public List<CorrelationOutput> applyCorrelation() throws ConnectionFailedException {
        if (correlation.getFirstArgument().equals("maximum") && correlation.getProperty() == null) {
            return CorrelationRequest.parseToList(correlation.useMaximum(database));
        } else if (correlation.getFirstArgument().equals("minimum") && correlation.getProperty() == null) {
            return CorrelationRequest.parseToList(correlation.useMinimum(database));
        } else if (correlation.getFirstArgument().equals("maximum") && correlation.getProperty() != null) {
            return CorrelationRequest.parseToList(correlation.useMaximum(correlation.getProperty(), database));
        } else if (correlation.getFirstArgument().equals("minimum") && correlation.getProperty() != null) {
            return CorrelationRequest.parseToList(correlation.useMinimum(correlation.getProperty(), database));
        } else if (correlation.getFirstArgument().equals("least") && correlation.getProperty() == null) {
            return CorrelationRequest.parseToList(correlation.useMinimum(database));
        } else {
            return CorrelationRequest.parseToList(correlation.useMinimum(correlation.getProperty(), database));
        }
    }

    /**
     * parser which parses a tree set into al list
     * @param input tree set which should be transformed int a list
     * @return list which inherits all elements the tree set inherits
     */
    static List<CorrelationOutput> parseToList(TreeSet<CorrelationOutput> input) {
        return new ArrayList<>(input);
    }

    private static void checkCorrelationInputNull(String input) throws InvalidCorrelationInputException {
        if (input == null) {
            throw new InvalidCorrelationInputException();
        }
    }

    /**
     * parses the user input into a valid correlation request
     * @param correlationInput string which is entered by user
     * @return returns a correlation object which collects all information about the correlation in this object
     * @throws InvalidCorrelationInputException thrown if the user input was not valid
     */
    public static Correlation parseCorrelationToString(String correlationInput) throws
            InvalidCorrelationInputException {
        CorrelationRequest.checkCorrelationInputNull(correlationInput);
        String[] potentialPropertyArray = correlationInput.split(" ", 4);
        if (potentialPropertyArray.length < 3) {
            throw new InvalidCorrelationInputException();
        }
        String potentialProperty = potentialPropertyArray[2];
        String inputCopy = correlationInput.toLowerCase();
        String[] parameters = inputCopy.split(" ", 4);

        String maxOrMin = parameters[0];
        checkCorrelationInputNull(maxOrMin);
        String firstArgument = CorrelationRequest.testMaxOrMin(maxOrMin);

        String correlationString = parameters[1];
        checkCorrelationInputNull(correlationString);
        Correlation correlation = CorrelationRequest.testCorrelationString(correlationString);

        int paramStart = 2;
        if (parameters.length == 4) {
            String propertyString = parameters[paramStart];
            checkCorrelationInputNull(propertyString);
            CorrelationRequest.testProperty(propertyString);
            paramStart++;
            correlation.setProperty(potentialProperty);
        }

        String propertyCounterString = parameters[paramStart];
        checkCorrelationInputNull(propertyCounterString);
        if (!StringUtils.isStrictlyNumeric(propertyCounterString) || propertyCounterString.length() == 0) {
            throw new InvalidCorrelationInputException();
        }

        try {
            int attributeCounter = Integer.parseInt(propertyCounterString);

            correlation.setFirstArgument(firstArgument);
            correlation.setAttributeCounter(attributeCounter);
            return correlation;
        } catch (NumberFormatException e) {
            throw new InvalidCorrelationInputException();
        }
    }

    /**
     * checks if the user entered min or max
     * @param input string which should be "min" or "max"
     * @return returns true if the user entered "max" and false if the user entered "min"
     * @throws InvalidCorrelationInputException thrown if the user input was not valid
     */
    static String testMaxOrMin(String input) throws InvalidCorrelationInputException {
        for (String current: CorrelationRequest.getValidFirstArguments()) {
            if (current.equals(input)) {
                return input;
            }
        }
        throw new InvalidCorrelationInputException();
    }

    /**
     * method which allows other packages (e.g. gui) to find out all supported first expressions
     * @return returns a list which inherits all supported first expressions
     */
    public static List<String> getValidFirstArguments() {
        ArrayList<String> validFirstArguments = new ArrayList<>();
        validFirstArguments.add("min");
        validFirstArguments.add("max");
        validFirstArguments.add("least");
        return validFirstArguments;
    }

    /**
     * checks if the user entered a valid correlation
     * @param input string which might code a valid correlation
     * @return correlation object of the specific correlation
     * @throws InvalidCorrelationInputException thrown if the user input was not valid
     */
    static Correlation testCorrelationString(String input) throws InvalidCorrelationInputException {
        if (input.equals("pearson")) {
            return new Pearson();
        } else if (input.equals("mutualcorrelation")) {
            return new MutualCorrelation();
        }
        throw new InvalidCorrelationInputException();
    }

    /**
     * method which allows other packages (e.g. gui) to find out all supported correlation algorithms
     * @return returns a list of strings which inherits all names of subclasses of abstract class Correlation
     */
    public static List<String> getValidCorrelations() {
        ArrayList<String> validCorrelations = new ArrayList<>();
        validCorrelations.add("pearson");
        validCorrelations.add("mutualcorrelation");
        return validCorrelations;
    }

    /**
     * checks if the input string codes a valid property. If this is not true, an InvalidCorrelationInputException
     * is thrown
     * @param input string which might code a valid property
     * @throws InvalidCorrelationInputException thrown if the input string does not code a valid property
     */
    static void testProperty(String input) throws InvalidCorrelationInputException {
        PropertyGraph<Integer, Integer> graph = new PropertyGraph<>();
        Set<Property> propertySet = PropertyFactory.createNumberProperties(graph);
        String[] propertyStrings = new String[propertySet.size()];
        int i = 0;
        for (Property currentProperty: propertySet) {
            propertyStrings[i] = currentProperty.getClass().getSimpleName();
            i++;
        }
        for (String currentString: propertyStrings) {
            currentString = currentString.toLowerCase();
            if (currentString.equals(input)) {
                return;
            }
        }
        throw new InvalidCorrelationInputException();
    }
}

package edu.kit.ipd.dbis.correlation;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.Profile;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_.AverageDegree;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_.ProportionDensity;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_.StructureDensity;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.SmallestDegree;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.NumberOfVertices;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.NumberOfVertexColorings;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.NumberOfEdges;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.NumberOfCliques;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.GreatestDegree;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.KkGraphNumberOfSubgraphs;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.NumberOfTotalColorings;

import java.util.ArrayList;

/**
 * class which is used to handle correlation requests
 */
public class CorrelationRequest {

    private Correlation correlation;

    /**
     * constructor of class CorrelationRequest
     * @param input string which should code a valid correlation
     * @throws InvalidCorrelationInputException thrown if the input string does not
     * code a valid correlation
     */
    public CorrelationRequest(String input) throws InvalidCorrelationInputException {
        correlation = CorrelationRequest.parseCorrelationToString(input);
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
     */
    public ArrayList<CorrelationOutput> use() {
        return correlation.use();
    }

    private static Correlation parseCorrelationToString(String correlationInput) throws InvalidCorrelationInputException {
        int i = 0;
        String input = correlationInput.toLowerCase();
        String maxOrMin = "";
        while (i < input.length() && input.charAt(i) != ' ') {
            maxOrMin = maxOrMin + (char) input.charAt(i);
            i++;
        }
        boolean maximum = CorrelationRequest.testMaxOrMin(maxOrMin);

        String correlationString = "";
        i++;
        while (i < input.length() && input.charAt(i) != ' ') {
            correlationString = correlationString + (char) input.charAt(i);
            i++;
        }
        Correlation correlation = CorrelationRequest.testCorrelationString(correlationString);

        try {
            String propertyString = "";
            int j = i;
            j++;
            while (j < input.length() && input.charAt(j) != ' ') {
                propertyString = propertyString + (char) input.charAt(j);
                j++;
            }
            Property property = CorrelationRequest.testProperty(propertyString);

            String propertyCounterString = "";
            j++;
            while (j < input.length() && input.charAt(j) != ' ') {
                propertyCounterString = propertyCounterString + (char) input.charAt(j);
                j++;
            }
            int attributeCounter = Integer.parseInt(propertyCounterString);

            correlation.setAttributeCounter(attributeCounter);
            correlation.setProperty(property);
            correlation.setMaximum(maximum);
            return correlation;
        } catch (InvalidCorrelationInputException e) {
            String propertyCounterString = "";
            i++;
            while (i < input.length() && input.charAt(i) != ' ') {
                propertyCounterString = propertyCounterString + (char) input.charAt(i);
                i++;
            }
            int attributeCounter = Integer.parseInt(propertyCounterString);
            correlation.setAttributeCounter(attributeCounter);
            correlation.setMaximum(maximum);
            return correlation;
        }
    }

    private static boolean testMaxOrMin(String input) throws InvalidCorrelationInputException {
        if (input.equals("min")) {
            return false;
        } else if (input.equals("max")) {
            return true;
        }
        throw new InvalidCorrelationInputException();
    }

    private static Correlation testCorrelationString(String input) throws InvalidCorrelationInputException {
        if (input.equals("pearson")) {
            return new Pearson();
        } else if (input.equals("mutualcorrelation")) {
            return new MutualCorrelation();
        }
        throw new InvalidCorrelationInputException();
    }

    private static Property testProperty(String input) throws InvalidCorrelationInputException {
        Property property;
        switch (input) {
            case "profile": property = new Profile(); return property;
            case "averagedegree": property = new AverageDegree(); return property;
            case "proportiondensity": property = new ProportionDensity(); return property;
            case "structuredensity": property = new StructureDensity(); return property;
            case "greatestDegree": property = new GreatestDegree(); return property;
            case "kkgraphnumberofsubgraphs": property = new KkGraphNumberOfSubgraphs(); return property;
            case "numberofcliques": property = new NumberOfCliques(); return property;
            case "numberofedges": property = new NumberOfEdges(); return property;
            case "numberoftotalcolorings": property = new NumberOfTotalColorings(); return property;
            case "numberofvertexcolorings": property = new NumberOfVertexColorings(); return property;
            case "numberofvertices": property = new NumberOfVertices(); return property;
            case "smallestdegree": property = new SmallestDegree(); return property;
            default: throw new InvalidCorrelationInputException();
        }
    }
}

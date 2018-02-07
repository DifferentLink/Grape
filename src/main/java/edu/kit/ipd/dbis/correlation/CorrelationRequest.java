package edu.kit.ipd.dbis.correlation;

import com.mysql.jdbc.StringUtils;
import edu.kit.ipd.dbis.correlation.exceptions.InvalidCorrelationInputException;
import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.AccessDeniedForUserException;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.DatabaseDoesNotExistException;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.Profile;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_.AverageDegree;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_.BinomialDensity;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_.ProportionDensity;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.double_.StructureDensity;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

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
	 * @throws ConnectionFailedException thrown if the table in database is not as expected
     * @throws ConnectionFailedException thrown if the connection to database failed
     * @throws AccessDeniedForUserException thrown if there is no access to database
     * @throws DatabaseDoesNotExistException thrown if there is no database
     */
    public List<CorrelationOutput> applyCorrelation() throws DatabaseDoesNotExistException,
			AccessDeniedForUserException, ConnectionFailedException, ConnectionFailedException {
        if (correlation.getMaximum() && correlation.getProperty() == null) {
            return CorrelationRequest.parseToList(correlation.useMaximum(database));
        } else if (!correlation.getMaximum() && correlation.getProperty() == null) {
            return CorrelationRequest.parseToList(correlation.useMinimum(database));
        } else if (correlation.getMaximum() && correlation.getProperty() != null) {
            return CorrelationRequest.parseToList(correlation.useMaximum(correlation.getProperty(), database));
        } else {
            return CorrelationRequest.parseToList(correlation.useMinimum(correlation.getProperty(), database));
        }
    }

    private static List<CorrelationOutput> parseToList(TreeSet<CorrelationOutput> input) {
        return new ArrayList<>(input);
    }

    private static void checkCorrelationInputNull(String input) throws InvalidCorrelationInputException {
        if (input == null) {
            throw new InvalidCorrelationInputException();
        }
    }

    private static Correlation parseCorrelationToString(String correlationInput) throws
            InvalidCorrelationInputException {

        String inputCopy = correlationInput.toLowerCase();
        String[] parameters = inputCopy.split(" ", 4);
        if (parameters.length < 3) {
            throw new InvalidCorrelationInputException();
        }

        String maxOrMin = parameters[0];
        checkCorrelationInputNull(maxOrMin);
        boolean maximum = CorrelationRequest.testMaxOrMin(maxOrMin);

        String correlationString = parameters[1];
        checkCorrelationInputNull(correlationString);
        Correlation correlation = CorrelationRequest.testCorrelationString(correlationString);

        int paramStart = 2;
        if (parameters.length == 4) {
            String propertyString = parameters[paramStart];
            checkCorrelationInputNull(propertyString);
            Property property = CorrelationRequest.testProperty(propertyString);
            paramStart++;
            correlation.setProperty(property);
        }

        String propertyCounterString = parameters[paramStart];
        checkCorrelationInputNull(propertyCounterString);
        if (!StringUtils.isStrictlyNumeric(propertyCounterString) || propertyCounterString.length() == 0) {
            throw new InvalidCorrelationInputException();
        }
        int attributeCounter = Integer.parseInt(propertyCounterString);


        correlation.setMaximum(maximum);
        correlation.setAttributeCounter(attributeCounter);
        return correlation;
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
		PropertyGraph<Integer, Integer> graph = new PropertyGraph<>();
        Property property;
        switch (input) {
			case "profile":
				property = new Profile(graph);
				return property;
			case "averagedegree":
				property = new AverageDegree(graph);
				return property;
			case "proportiondensity":
				property = new ProportionDensity(graph);
				return property;
			case "structuredensity":
				property = new StructureDensity(graph);
				return property;
			case "greatestDegree":
				property = new GreatestDegree(graph);
				return property;
			case "kkgraphnumberofsubgraphs":
				property = new KkGraphNumberOfSubgraphs(graph);
				return property;
			case "numberofcliques":
				property = new NumberOfCliques(graph);
				return property;
			case "numberofedges":
				property = new NumberOfEdges(graph);
				return property;
			case "numberoftotalcolorings":
				property = new NumberOfTotalColorings(graph);
				return property;
			case "numberofvertexcolorings":
				property = new NumberOfVertexColorings(graph);
				return property;
			case "numberofvertices":
				property = new NumberOfVertices(graph);
				return property;
			case "smallestdegree":
				property = new SmallestDegree(graph);
				return property;
			case "totalcoloringnumberofcolors":
				property = new TotalColoringNumberOfColors(graph);
				return property;
			case "vertexcoloringnumberofcolors":
				property = new VertexColoringNumberOfColors(graph);
				return property;
			case "largestsubgraphsize":
				property = new LargestSubgraphSize(graph);
				return property;
			case "binomialdensity":
				property = new BinomialDensity(graph);
				return property;
			case "largestcliquesize":
				property = new LargestCliqueSize(graph);
				return property;
			case "disjointfromsubgraph":
				property = new DisjointFromSubgraphs(graph);
				return property;
            default: throw new InvalidCorrelationInputException();
        }
    }
}

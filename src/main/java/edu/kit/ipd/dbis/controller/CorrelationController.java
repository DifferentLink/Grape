package edu.kit.ipd.dbis.controller;

import edu.kit.ipd.dbis.correlation.CorrelationOutput;
import edu.kit.ipd.dbis.correlation.CorrelationRequest;
import edu.kit.ipd.dbis.correlation.exceptions.InvalidCorrelationInputException;

import java.util.List;

/**
 * The type Correlation controller.
 */
public class CorrelationController {
	/**
	 * gets all filtered and sorted graphs, checks the input for the correlation, creates a new instance
	 * of CorrelationRequest and finally executes use() on the CorrelationRequest instance with the graph list.
	 *
	 * @param input the input for the correlation.
	 * @return a list of CorrelationOutput.
	 * @throws InvalidCorrelationInputException the invalid correlation input exception
	 */
	public List<CorrelationOutput> addNewCorrelation(String input) throws InvalidCorrelationInputException {
		try {
			List<CorrelationOutput> output = null;
			CorrelationRequest request = new CorrelationRequest(input, null);
			output = request.use();
			return output;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

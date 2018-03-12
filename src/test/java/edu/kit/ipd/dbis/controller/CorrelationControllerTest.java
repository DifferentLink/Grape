package edu.kit.ipd.dbis.controller;

import edu.kit.ipd.dbis.correlation.CorrelationOutput;
import edu.kit.ipd.dbis.correlation.Pearson;
import edu.kit.ipd.dbis.correlation.exceptions.InvalidCorrelationInputException;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CorrelationControllerTest {

	@Ignore
	@Test
	public void newCorrelationTestRightInput() {
		String input = "Max Pearson 3";

		CorrelationController c = CorrelationController.getInstance();
		List<CorrelationOutput> output = null;
		try {
			output = c.addNewCorrelation(input);
		} catch (InvalidCorrelationInputException e) {
			e.printStackTrace();
		}
		assert output.getClass().equals(Pearson.class);
	}

	@Ignore
	@Test
	public void newCorrelationTestWrongInput() {
		String input = "wrong input";
		CorrelationController c = CorrelationController.getInstance();
		List<CorrelationOutput> output = null;
		try {
			output = c.addNewCorrelation(input);
		} catch (InvalidCorrelationInputException e) {
			e.printStackTrace();
		}
		assertNotEquals("", output.get(0).getFirstProperty());
		assertNotEquals("", output.get(0).getSecondProperty());
		assertNotEquals("", output.get(0).getOutputNumber());
	}
}
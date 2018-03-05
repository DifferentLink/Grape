package edu.kit.ipd.dbis.correlation.exceptions;

import org.junit.Test;

public class InvalidCorrelationInputExceptionTest {

    @Test(expected = InvalidCorrelationInputException.class)
    public void testToStringMethod() throws InvalidCorrelationInputException{
        InvalidCorrelationInputException testException = new InvalidCorrelationInputException();
        assert testException.getMessage().equals("The input does not code a valid correlation");
        throw testException;
    }
}

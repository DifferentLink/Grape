package edu.kit.ipd.dbis.filter.exceptions;

import org.junit.*;

public class InvalidInputExceptionTest {

    @Test(expected = InvalidInputException.class)
    public void testToStringMethod() throws InvalidInputException{
        InvalidInputException testException = new InvalidInputException();
        assert testException.getMessage().equals("The input does not code a valid filter");
        throw testException;
    }

}

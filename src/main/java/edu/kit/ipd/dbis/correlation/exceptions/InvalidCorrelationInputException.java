package edu.kit.ipd.dbis.correlation.exceptions;

/**
 * exception class which is used if the user tries to calculate a correlation but uses wrong syntax
 */
public class InvalidCorrelationInputException extends Throwable {

    @Override
    public String getMessage() {
        return "The input does not code a valid correlation";
    }

}

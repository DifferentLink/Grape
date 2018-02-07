package edu.kit.ipd.dbis.filter.exceptions;

/**
 * an InvalidInputException is thrown when the user tries to add a new filter or a new filtergroup but uses wrong
 * syntax
 */
public class InvalidInputException extends Throwable {

    @Override
    public String getMessage() {
        return "The input does not code a valid filter";
    }

}

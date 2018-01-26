package edu.kit.ipd.dbis.database.exceptions.sql;

public class DatabaseDoesNotExistException extends Exception {
	public DatabaseDoesNotExistException(String message) {
		super(message);
	}

	public DatabaseDoesNotExistException() {

	}
}

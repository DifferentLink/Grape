package edu.kit.ipd.dbis.database.exceptions.sql;

public class TableAlreadyExistsException extends Exception {

	public TableAlreadyExistsException(String message) {
		super(message);
	}

	public TableAlreadyExistsException() {

	}
}

package edu.kit.ipd.dbis.database.exceptions.sql;

public class ConnectionFailedException extends Exception {
	public ConnectionFailedException(String message) {
		super(message);
	}

	public ConnectionFailedException() {

	}
}

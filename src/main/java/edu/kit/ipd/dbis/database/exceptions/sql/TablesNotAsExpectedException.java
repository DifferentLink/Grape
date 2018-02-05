package edu.kit.ipd.dbis.database.exceptions.sql;

public class TablesNotAsExpectedException extends Exception {
	public TablesNotAsExpectedException(String message) {
		super(message);
	}

	public TablesNotAsExpectedException() {
		super("Selected table does not exist or does not contain the required columns.");
	}
}

package edu.kit.ipd.dbis.database.file.parsers;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.connection.tables.FilterTable;
import edu.kit.ipd.dbis.database.connection.tables.GraphTable;
import edu.kit.ipd.dbis.database.exceptions.files.FileContentNotAsExpectedException;
import edu.kit.ipd.dbis.database.exceptions.sql.AccessDeniedForUserException;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.DatabaseDoesNotExistException;

import java.sql.SQLException;

/**
 * Creates a GraphDatabase-Object by setting its Table-Objects and the directory where its text le is saved.
 */
public class LoadParser extends Parser {

	/**
	 * and sets the given information as attribute.
	 * @param information contains all the information that should be saved in the text file
	 */
	public LoadParser(String information) {
		this.information = information;
	}

	@Override
	public void parse() throws FileContentNotAsExpectedException {
		try {
			String[] content = this.information.split(";");
			if (content[3].equals("database")) {
				throw new FileContentNotAsExpectedException("Invalid name for a table.");
			}
			GraphTable graphTable = new GraphTable(content[0], content[1], content[2], content[3]);
			FilterTable filterTable = new FilterTable(content[4], content[5],content[6],content[7]);
			this.database = new GraphDatabase(graphTable, filterTable);
		} catch (AccessDeniedForUserException
				| ConnectionFailedException
				| SQLException
				| DatabaseDoesNotExistException e) {
			throw new FileContentNotAsExpectedException("Connection invalid.");
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new FileContentNotAsExpectedException("This file does not contain the connection to a database.");
		}

	}

}
